/** $Id: MedlineImport.java,v 1.12 2013/03/20 06:40:42 lan Exp $ */
package ru.biosoft.biblio;

import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import ru.biosoft.util.TextUtil;

/**
 * Provides import of Medline records into the Publications table.
 */
public class MedlineImport
{
    protected static String medlineQuery ="http://www.ncbi.nlm.nih.gov/pubmed/$pmid$?report=MEDLINE&mode=text";
    protected static String proxySet  = "true";
    protected static String proxyHost = "proxy";
    protected static String proxyPort = "8080";
//
//    /**
//     * Imports corresponding record from PubMed/Medline into Publications table.
//     *
//     * @param out - output stream for logging
//     * @param connector - database connector
//     * @param id - record id in ublications table.
//     */
//    public static void fill(Writer out, DatabaseConnector connector, String table, String id) throws Exception
//    {
//        out.write( "<br>Record id: " + id + ". ");
//        try
//        {
//            ResultSet rs = connector.executeQuery(
//                                "SELECT p.PMID " +
//                                "FROM publications p WHERE p.ID=" + id);
//            String pmid = null;
//            while (rs.next())
//            {
//                pmid = rs.getString(1);
//            }
//            connector.close( rs );
//
//            if( pmid == null)
//            {
//                out.write("Medline PMID is not specified, can not retrieve the data.");
//                return;
//            }
//
//            boolean success = false;
//            if( pmid != null )
//            {
//                out.write("<br>  Retrieve data for PMID=" + pmid + ": ");
//                success = fill(out, connector, table, id, pmid);
//            }
//
//            if( success )
//                out.write("Ok.");
//
//        }
//        catch(Throwable t)
//        {
//            out.write("Error: " + t);
//        }
//    }
//
//    /**
//     * Imports record with the specified PubMed/Medline identifier into Publications table.
//     *
//     * @param out - output stream for logging
//     * @param connector - database connector
//     * @param id - record id in ublications table.
//     * @param pmid - PubMed or Medline publications identifier.
//     */
//    public static boolean fill(Writer out, DatabaseConnector connector, String table, String id, String pmid) throws Exception
//    {
//        try
//        {
//            System.setProperty("proxySet",  proxySet );
//            System.setProperty("proxyHost", proxyHost );
//            System.setProperty("proxyPort", proxyPort );
//
//            URL url = new URL(medlineQuery.replace("$pmid$", pmid));
//            if( url == null )
//            {
//                out.write("Can not get entry with pmid '" + pmid + "' from PubMed. URL is null.");
//                out.write("\nProxy: " + proxyHost + ": " + proxyPort + ", enabled=" + proxySet + ".");
//                return false;
//            }
//
//
//            Object content = url.getContent();
//           if( content == null )
//            {
//                out.write("Can not get entry with pmid '" + pmid + "' from PubMed. Content is null.");
//                return false;
//            }
//
//            InputStream is = (InputStream)content;
//            StringBuffer result = new StringBuffer();
//            byte[]  bytes = new byte[1024];
//            int len=0;
//            while( (len = is.read(bytes)) > 0 )
//            {
//                result.append( new String(bytes, 0, len) );
//            }
//            is.close();
//
//            String entry = result.toString();
//
//            // parsing result
//            String dp    = TextUtil.getField(entry, "DP  -");
//            String year  = dp.substring(0, 4);
//            String month = dp.length() > 5 ? dp.substring(5) : "";
//
//            String pages = TextUtil.getField(entry, "PG  -");
//            String pageFrom = pages;
//            String pageTo   = pages;
//            if( pages != null )
//            {
//                int delim = pages.indexOf('-');
//                if( delim == - 1 )
//                    delim = pages.indexOf(',');
//                if( delim != -1 )
//                {
//                    pageFrom = pages.substring(0, delim).trim();
//                    pageTo   = pages.substring(delim+1).trim();
//                }
//            }
//
//            // read authors
//            String prefix = "\nFAU";
//            //if( entry.indexOf(prefix) < 1)
//                prefix = "\nAU";
//
//            int from = 0;
//            int to;
//            result = new StringBuffer();
//            while( (from = entry.indexOf(prefix, from)) > 0 )
//            {
//                if( result.length() > 1 )
//                    result.append(", ");
//
//                to = entry.indexOf('\n', from+2);
//                if(to < 0)
//                    to = entry.length()-1;
//
//                result.append(entry.substring(from+7, to));
//
//                from=to;
//            }
//            String authors = result.toString();
//
//            String ref = generateReferenceId(out, connector,authors, year, TextUtil.getField(entry, "TI  -"));
//
//
//            // generate query
//            String query = "UPDATE IGNORE " + table + " SET " +
//                            "\n PMID="             + safeValue(entry, "PMID-") + ", " +
//                            "\n source="           + safeValue(entry, "SO  -") + "," +
//                            "\n journalTitle="     + safeValue(entry, "TA  -") + "," +
//                            "\n title="            + safeValue(entry, "TI  -") + "," +
//                            "\n volume="           + safeValue(entry, "VI  -") + "," +
//                            "\n issue="            + safeValue(entry, "IP  -") + "," +
//                            "\n language="         + safeValue(entry, "LA  -") + "," +
//                            "\n publicationType="  + safeValue(entry, "PT  -") + "," +
//                            "\n abstract="         + safeValue(entry, "AB  -") + "," +
//                            "\n affiliation="      + safeValue(entry, "AD  -") + "," +
//
//                            "\n ref='"        + safestr(ref) + "'," +
//                            "\n authors='"    + safestr(authors) + "'," +
//                            "\n year=" + year + ", month='" + month + "'," +
//                            "\n pageFrom='" + pageFrom + "', pageTo='" + pageTo + "'" +
//
//                            "\nWHERE ID=" + id;
//out.write("Query: <pre>" + query + "</pre>");
//
//            connector.executeUpdate(query);
//            return true;
//
//        }
//        catch(Exception e)
//        {
//            out.write("!Can not get entry with pmid '" + pmid + "' from PubMed." +
//                      "Error: <pre>");
//            e.printStackTrace(new java.io.PrintWriter(out));
//            out.write("</pre>");
//        }
//
//        return false;
//    }
//
//    ///////////////////////////////////////////////////////////////////////////
//    // Utility functions
//    //
//
//    public static String getAuthorLastName(String author)
//    {
//        author = author.trim();
//        int offset = author.indexOf(' ');
//        if( offset > 0 )
//            author = author.substring(0, offset);
//
//        return author;
//    }
//
//    public static String generateReferenceId(Writer out, DatabaseConnector connector,
//                                             String authors, String year, String title) throws Exception
//    {
//        StringTokenizer tokens = new StringTokenizer(authors, ",");
//        int num = tokens.countTokens();
//
//        String id;
//        if( num == 0 )
//        {
//            id = title;
//        }
//        else if( num == 1)
//        {
//            id = getAuthorLastName(tokens.nextToken()) + ", " + year;
//        }
//        else if( num == 2)
//        {
//            id = getAuthorLastName(tokens.nextToken()) + " and " +
//                 getAuthorLastName(tokens.nextToken()) + ", " + year;
//        }
//        else
//        {
//            id = getAuthorLastName(tokens.nextToken()) + " et al., " + year;
//        }
//
//        // check that reference is unique
//        ResultSet rs = null;
//        char letter = 'a';
//        String ref = id;
//
//        while( true )
//        {
//            try
//            {
//                rs = connector.executeQuery("SELECT p.ref " +
//                                            "FROM publications p WHERE p.ref='" + ref + "'");
//                if( !rs.next() )
//                    return ref;
//
//                ref = id + letter;
//                letter++;
//            }
//            catch(Throwable t)
//            {
//                out.write("Can not generate reference id, error: " + t);
//                return "";
//            }
//            finally
//            {
//                connector.close(rs);
//            }
//        }
//    }
//
//    public static String safeValue(String entry, String field)
//    {
//        String value = TextUtil.getField(entry, field);
//
//        if( value == null || value.length() == 0 )
//            return null;
//
//        value = safestr(value);
//        value = value.replaceAll("--", "-");
//
//        return "'" + value + "'";
//    }
//
//    public static String safestr( String text )
//    {
//        StringBuffer to = new StringBuffer();
//        char from[] = text.toCharArray();
//
//        for( int i = 0; i < from.length; i++ )
//        {
//            if( from[ i ] == '\'' )
//            {
//                to.append( "''" );
//            }
//            else if( from[ i ] == '\\' )
//            {
//                to.append( "\\\\" );
//            }
//            else
//            {
//                to.append( from[ i ] );
//            }
//        }
//
//        return to.toString();
//    }

}
