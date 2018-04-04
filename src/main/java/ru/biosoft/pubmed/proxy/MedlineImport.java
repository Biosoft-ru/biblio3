// $Id: MedlineImport.java,v 1.2 2012/11/06 11:21:12 lan Exp $
package ru.biosoft.pubmed.proxy;

import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.sql.ResultSet;
import java.util.StringTokenizer;

/**
 * Provides import of Medline records into the Publications table.
 */
public class MedlineImport
{
//    protected static String medlineQuery = "http://www.ncbi.nlm.nih.gov/pubmed/$id$?report=MEDLINE&format=text";
//
//    public static String getEntryPromPubMed(Writer out, String pmid) throws Exception
//    {
//        try
//        {
//            ProxyServlet.setProxy();
//
//            URL url = new URL(medlineQuery.replace("$id$", pmid));
//            Object content = url.getContent();
//            if( content == null )
//            {
//                out.write("Can not get entry with pmid '" + pmid + "' from PubMed. Content is null.");
//                return null;
//            }
//
//            InputStream is = (InputStream)content;
//            StringBuffer result = new StringBuffer();
//            byte[] bytes = new byte[1024];
//            int len = 0;
//            while( ( len = is.read(bytes) ) > 0 )
//            {
//                result.append(new String(bytes, 0, len));
//            }
//            is.close();
//
//            String entry = result.toString();
//            return entry;
//        }
//        catch( Exception e )
//        {
//            out.write("!Can not get entry with pmid '" + pmid + "' from PubMed." + "Error: <pre>");
//            e.printStackTrace(new java.io.PrintWriter(out));
//            out.write("</pre>");
//        }
//
//        return null;
//    }
//
//    public static boolean insertIntoDatabase(Writer out, DatabaseConnector connector, String table, String entry) throws Exception
//    {
//        try
//        {
//            // parsing result
//            String dp = getField(entry, "DP  -");
//            String year = dp.substring(0, 4);
//            String month = dp.length() > 5 ? dp.substring(5) : "";
//
//            String pages = getField(entry, "PG  -");
//            String pageFrom = pages;
//            String pageTo = pages;
//            if( pages != null )
//            {
//                int delim = pages.indexOf('-');
//                if( delim == -1 )
//                    delim = pages.indexOf(',');
//                if( delim != -1 )
//                {
//                    pageFrom = pages.substring(0, delim).trim();
//                    pageTo = pages.substring(delim + 1).trim();
//                }
//            }
//
//            // read authors
//            String prefix = "\nFAU";
//            //if( entry.indexOf(prefix) < 1)
//            prefix = "\nAU";
//
//            int from = 0;
//            int to;
//            StringBuffer result = new StringBuffer();
//            while( ( from = entry.indexOf(prefix, from) ) > 0 )
//            {
//                if( result.length() > 1 )
//                    result.append(", ");
//
//                to = entry.indexOf('\n', from + 2);
//                if( to < 0 )
//                    to = entry.length() - 1;
//
//                result.append(entry.substring(from + 7, to));
//
//                from = to;
//            }
//            String authors = result.toString();
//
//            String ref = generateReferenceId(out, connector, authors, year, getField(entry, "TI  -"));
//
//
//            // generate query
//            String query = "INSERT INTO "
//                    + table
//                    + "(PMID, source, journalTitle, title, volume, issue, language, publicationType, abstract, affiliation, ref, authors, year, month, pageFrom, pageTo) VALUES("
//                    + safeValue(entry, "PMID-") + ", " + safeValue(entry, "SO  -") + ", " + safeValue(entry, "TA  -") + ", "
//                    + safeValue(entry, "TI  -") + ", " + safeValue(entry, "VI  -") + ", " + safeValue(entry, "IP  -") + ", "
//                    + safeValue(entry, "LA  -") + ", " + safeValue(entry, "PT  -") + ", " + safeValue(entry, "AB  -") + ", "
//                    + safeValue(entry, "AD  -") + ", '" + safestr(ref) + "', '" + safestr(authors) + "', " + year + ", '" + month + "', '"
//                    + pageFrom + "', '" + pageTo + "')";
//
//            connector.executeUpdate(query);
//
//            String pmcid = MedlineImport.getField(entry, "PMC");
//            if( pmcid != null )
//            {
//                pmcid = pmcid.substring(pmcid.indexOf("PMC") + 3);
//                query = "UPDATE " + table + " SET PMCID=" + safestr(pmcid) + " WHERE PMID=" + safeValue(entry, "PMID-");
//                connector.executeUpdate(query);
//            }
//
//            return true;
//
//        }
//        catch( Exception e )
//        {
//            out.write("!Can not save entry" + "Error: <pre>");
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
//    public static String generateReferenceId(Writer out, DatabaseConnector connector, String authors, String year, String title)
//            throws Exception
//    {
//        StringTokenizer tokens = new StringTokenizer(authors, ",");
//        int num = tokens.countTokens();
//
//        String id;
//        if( num == 0 )
//        {
//            id = title;
//        }
//        else if( num == 1 )
//        {
//            id = getAuthorLastName(tokens.nextToken()) + ", " + year;
//        }
//        else if( num == 2 )
//        {
//            id = getAuthorLastName(tokens.nextToken()) + " and " + getAuthorLastName(tokens.nextToken()) + ", " + year;
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
//                rs = connector.executeQuery("SELECT p.ref " + "FROM publications p WHERE p.ref='" + ref + "'");
//                if( !rs.next() )
//                    return ref;
//
//                ref = id + letter;
//                letter++;
//            }
//            catch( Throwable t )
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
//        String value = getField(entry, field);
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
//    public static String safestr(String text)
//    {
//        StringBuffer to = new StringBuffer();
//        char from[] = text.toCharArray();
//
//        for( int i = 0; i < from.length; i++ )
//        {
//            if( from[i] == '\'' )
//            {
//                to.append("''");
//            }
//            else if( from[i] == '\\' )
//            {
//                to.append("\\\\");
//            }
//            else
//            {
//                to.append(from[i]);
//            }
//        }
//
//        return to.toString();
//    }
//
//    public static String getField(String entry, String field)
//    {
//        int start = entry.indexOf(field);
//        if( start == -1 )
//            return null;
//        if( start != 0 && entry.charAt(start - 1) != '\n' )
//        {
//            start = entry.indexOf("\n" + field, start);
//            if( start == -1 )
//                return null;
//            start++;
//        }
//
//        int end;
//        StringBuffer answer = new StringBuffer();
//        String s;
//        boolean isFirst = true;
//        while( entry.startsWith(field, start) || entry.startsWith(" ", start) )
//        {
//            end = entry.indexOf('\n', start + field.length());
//            s = ( entry.substring(start + field.length(), end) ).trim();
//
//            // append delimiter (space) between lines
//            if( isFirst )
//                isFirst = false;
//            else
//                answer.append(' ');
//
//            answer.append(s);
//            start = end + 1;
//        }
//
//        return answer.toString();
//    }
}
