/** $Id: MedlineImport.java,v 1.12 2013/03/20 06:40:42 lan Exp $ */
package ru.biosoft.biblio.services;

import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.StringTokenizer;

import com.developmentontheedge.be5.api.services.SqlService;
import ru.biosoft.biblio.util.TextUtil;

/**
 * Provides import of Medline records into the Publications table.
 */
public class MedlineImport
{
    protected static String medlineQuery ="https://www.ncbi.nlm.nih.gov/pubmed/$pmid$?report=MEDLINE&mode=text";

    private final SqlService db;

    public MedlineImport(SqlService db)
    {
        this.db = db;
    }

    /**
     * Imports corresponding record from PubMed/Medline into Publications table.
     *
     * @param id - record id in ublications table.
     */
    public void fill(String table, Long id) throws Exception
    {
        Objects.requireNonNull(table);
        Objects.requireNonNull(id);

        Long pmid = db.oneLong("SELECT p.PMID FROM publications p WHERE p.ID= ?", id);

        if( pmid == null)
        {
            throw new RuntimeException("Medline PMID is not specified, can not retrieve the data.");
        }

        fill(table, id, pmid);
    }

    /**
     * Imports record with the specified PubMed/Medline identifier into Publications table.
     *
     * @param id - record id in ublications table.
     * @param pmid - PubMed or Medline publications identifier.
     */
    public void fill(String table, Long id, Long pmid) throws Exception
    {
        URL url = new URL(medlineQuery.replace("$pmid$", "" + pmid));

        Object content = url.getContent();
        if( content == null )
        {
            throw new RuntimeException("Can not get entry with pmid '" + pmid + "' from PubMed. Content is null.");
        }

        InputStream is = (InputStream)content;
        StringBuffer result = new StringBuffer();
        byte[]  bytes = new byte[1024];
        int len=0;
        while( (len = is.read(bytes)) > 0 )
        {
            result.append( new String(bytes, 0, len) );
        }
        is.close();

        String entry = result.toString();

        String returnPMID = TextUtil.getField(entry, "PMID-");
        if(returnPMID == null)
        {
            throw new RuntimeException(TextUtil.getField(entry, "id: " + pmid));
        }

        // parsing result
        String dp    = TextUtil.getField(entry, "DP  -");
        String[] DP = dp.split(" ");
        String year  = DP[0];
        String month = DP.length > 1 ? DP[1] : "";
        String day   = DP.length > 2 ? DP[2] : "";

        String pages = TextUtil.getField(entry, "PG  -");
        String pageFrom = pages;
        String pageTo   = pages;
        if( pages != null )
        {
            int delim = pages.indexOf('-');
            if( delim == - 1 )
                delim = pages.indexOf(',');
            if( delim != -1 )
            {
                pageFrom = pages.substring(0, delim).trim();
                pageTo   = pages.substring(delim+1).trim();
            }
        }

        // read authors
        String prefix = "\nFAU";
        //if( entry.indexOf(prefix) < 1)
            prefix = "\nAU";

        int from = 0;
        int to;
        result = new StringBuffer();
        while( (from = entry.indexOf(prefix, from)) > 0 )
        {
            if( result.length() > 1 )
                result.append(", ");

            to = entry.indexOf('\n', from+2);
            if(to < 0)
                to = entry.length()-1;

            result.append(entry.substring(from+7, to));

            from=to;
        }
        String authors = result.toString();

        String ref = generateReferenceId(authors, year, TextUtil.getField(entry, "TI  -"));


        // generate query //IGNORE
        String query = "UPDATE " + table + " SET " +
                        "\n PMID="             + safeValue(entry, "PMID-") + ", " +
                        "\n source="           + safeValue(entry, "SO  -") + "," +
                        "\n journalTitle="     + safeValue(entry, "TA  -") + "," +
                        "\n title="            + safeValue(entry, "TI  -") + "," +
                        "\n volume="           + safeValue(entry, "VI  -") + "," +
                        "\n issue="            + safeValue(entry, "IP  -") + "," +
                        "\n language="         + safeValue(entry, "LA  -") + "," +
                        "\n publicationType="  + safeValue(entry, "PT  -") + "," +
                        "\n abstract="         + safeValue(entry, "AB  -") + "," +
                        "\n affiliation="      + safeValue(entry, "AD  -") + "," +

                        "\n ref='"        + safestr(ref) + "'," +
                        "\n authors='"    + safestr(authors) + "'," +
                        "\n year=" + year + ", month='" + month + "'," +
                        "\n pageFrom='" + pageFrom + "', pageTo='" + pageTo + "'" +

                        "\nWHERE ID=" + id;

        //out.write("Query: <pre>" + query + "</pre>");

        db.update(query);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Utility functions
    //

    public static String getAuthorLastName(String author)
    {
        author = author.trim();
        int offset = author.indexOf(' ');
        if( offset > 0 )
            author = author.substring(0, offset);

        return author;
    }

    public String generateReferenceId(String authors, String year, String title) throws Exception
    {
        StringTokenizer tokens = new StringTokenizer(authors, ",");
        int num = tokens.countTokens();

        String id;
        if( num == 0 )
        {
            id = title;
        }
        else if( num == 1)
        {
            id = getAuthorLastName(tokens.nextToken()) + ", " + year;
        }
        else if( num == 2)
        {
            id = getAuthorLastName(tokens.nextToken()) + " and " +
                 getAuthorLastName(tokens.nextToken()) + ", " + year;
        }
        else
        {
            id = getAuthorLastName(tokens.nextToken()) + " et al., " + year;
        }

        char letter = 'a';
        String ref = id;

        while( true )
        {
            try
            {
                if( db.oneLong("SELECT COUNT(1) FROM publications p WHERE p.ref= ?", ref) == 0 )
                {
                    return ref;
                }

                ref = id + letter;
                letter++;
            }
            catch(Throwable t)
            {
                throw new RuntimeException("Can not generate reference id", t);
            }
        }
    }

    public static String safeValue(String entry, String field)
    {
        String value = TextUtil.getField(entry, field);

        if( value == null || value.length() == 0 )
            return null;

        value = safestr(value);
        value = value.replaceAll("--", "-");

        return "'" + value + "'";
    }

    public static String safestr( String text )
    {
        StringBuffer to = new StringBuffer();
        char from[] = text.toCharArray();

        for( int i = 0; i < from.length; i++ )
        {
            if( from[ i ] == '\'' )
            {
                to.append( "''" );
            }
            else if( from[ i ] == '\\' )
            {
                to.append( "\\\\" );
            }
            else
            {
                to.append( from[ i ] );
            }
        }

        return to.toString();
    }

}
