// $Id: SearchResultPageGenerator.java,v 1.12 2012/12/03 10:50:34 lan Exp $
package ru.biosoft.pubmed.proxy;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class SearchResultPageGenerator
{
    public static boolean isResultData(String page)
    {
        int start = page.indexOf("<div><div class=\"rprt\"");
        if( start != -1 )
        {
            return true;
        }
        return false;
    }

    public static String generateResultPage(String page, String connectionString, Properties parameters) throws Exception
    {
//        String query = parameters.getProperty("query");
//        String pageNumber = parameters.getProperty("page");
//
//        int pageCount = getPageCount(page);
//        String queryKey = getQueryKey(page);
//        ParsedDescription[] descriptions = getLinks(page);
//        if( pageNumber == null )
//        {
//            pageNumber = "1";
//        }
//        if( queryKey == null )
//        {
//            queryKey = "1";
//        }
//        if( descriptions != null )
//        {
//            DatabaseHandler dbHandler = new DatabaseHandler(connectionString);
//            StringBuffer result = new StringBuffer();
//            result.append("<html>\n<head>\n");
//            result.append(getJavaScript() + "\n");
//            result.append(getStyle() + "\n");
//            result.append("</head>\n<body onLoad=\"initButtons()\">\n");
//
//            result.append("<div id=\"t\">\n");
//
//            result.append("<table border=0 width=\"100%\"><tr><td>" + "\n");
//            result.append("    <p align=\"left\">");
//            result.append("    <form enctype=\"application/x-www-form-urlencoded\" name=\"EntrezForm\"" + "\n");
//            result.append("    method=\"post\"" + "\n");
//            result.append("    action=\"proxy?http://www.ncbi.nlm.nih.gov/sites/entrez?db=pubmed&cmd=search\"" + "\n");
//            result.append("    id=\"EntrezForm\">" + "\n");
//            result.append("      <input type=\"submit\" value=\"Page\"/>" + "\n");
//            if( query != null )
//            {
//                result.append("      <input name=\"term\" type=\"hidden\" value=\"" + query + "\" id=\"term\" cmd=\"Go\"/>" + "\n");
//            }
//            result.append("      <input name=\"queryKey\" type=\"hidden\" value=\"" + queryKey + "\" id=\"queryKey\" cmd=\"Go\"/>" + "\n");
//            result.append("      <input name=\"page\" type=\"text\" value=\"" + pageNumber + "\" id=\"pageNumber\" size=\"5\" cmd=\"Go\"/>"
//                    + "\n");
//            result.append("      &nbsp;of " + pageCount + "\n");
//            result.append("      &nbsp;(\n");
//
//            if( pageNumber != null && Integer.parseInt(pageNumber) > 1 )
//                result.append("      <a href=\"#\" onclick=\"previous(" + pageNumber + ")\">Previous</a>&nbsp;\n");
//
//            if( pageNumber != null && pageCount > -1 && Integer.parseInt(pageNumber) < pageCount )
//                result.append("      <a href=\"#\"  onclick=\"next(" + pageNumber + ")\">Next</a>&nbsp;\n");
//
//            result.append("      )\n");
//            result.append("    </form>" + "\n");
//            result.append("    </p>");
//            result.append("</td><td align=\"center\">" + "\n");
//            if( query != null )
//            {
//                result.append("Results for: <b>" + query + "</b> \n");
//            }
//            result.append("</td><td align=\"right\">" + "\n");
//
//            result.append("    <form method=\"post\" action=\"action\" target=\"entityView\">");
//            result.append("    <p align=\"right\">");
//            result.append("    <input type=submit name=\"action\" id=\"add\" value=\"Add\">");
//            result.append("    <input type=submit name=\"action\" id=\"edit\" value=\"Edit\">");
//            result.append("    <input type=submit name=\"action\" id=\"attach\" value=\"Attach\">");
//            result.append("    <input type=submit name=\"action\" id=\"categories\" value=\"Categories\">");
//            result.append("    </p>");
//            result.append("</td></tr></table>\n");
//
//            result.append("</div>\n");
//            result.append("<div id=\"d\"><br><br><br><br>\n");
//
//            result.append("<table border=\"0\" cellpadding=\"3\" cellspacing=\"3\" width=\"100%\">");
//
//            List<RelatedEntitiesDescription> related = dbHandler.getRelatedentitiesDescriptions();
//            for( int i = 0; i < descriptions.length; i++ )
//            {
//                result.append("<tr>");
//                boolean isInDatabase = dbHandler.isInDatabase(descriptions[i].getId());
//                generateCell1(descriptions[i], result, isInDatabase, dbHandler);
//                generateCell2(descriptions[i], result, isInDatabase);
//                generateCell3(descriptions[i], result, isInDatabase, dbHandler);
//                generateCell4(descriptions[i], result, dbHandler, related);
//                result.append("</tr>");
//            }
//            result.append("</table>");
//            result.append("</form>");
//            result.append("</div>\n");
//            result.append("</body>\n</html>");
//            return result.toString();
//        }
        return null;
    }
//
//    protected static void generateCell1(ParsedDescription pd, StringBuffer result, boolean isInDatabase, DatabaseHandler dbHandler)
//            throws Exception
//    {
//        boolean hasAttachments = dbHandler.hasAttachment(pd.getId());
//        String fullTextUrl = dbHandler.getFullTextURL(pd.getId());
//        result.append("<td valign=\"top\" width=\"50\" nowrap>");
//        result.append(pd.getResultNumber());
//        result.append("&nbsp;<input type=\"checkbox\" name=\"elements\" value=\"" + pd.getId() + "\" onClick=\"selectRecord("
//                + isInDatabase + "," + hasAttachments + ")\"/>");
//        if( !isInDatabase )
//        {
//            result.append("<div align=\"center\"><img src=\"noindatabase.gif\" border=0/></div>");
//        }
//        else if( fullTextUrl != null )
//        {
//            result.append("<div align=\"center\"><a href=\"" + fullTextUrl
//                    + "\" target=\"_blank\"><img src=\"hasurl.gif\" border=0/></a></div>");
//        }
//        else if( hasAttachments )
//        {
//            DatabaseConnector connector = dbHandler.getConnector();
//            ResultSet rs = null;
//            try
//            {
//                rs = connector.executeQuery("SELECT id FROM publications WHERE PMID=" + pd.getId());
//                if( rs.next() )
//                {
//                    int pID = rs.getInt(1);
//                    result.append("<div align=\"center\"><a href=\"q?_t_=attachments&publicationID=" + pID
//                            + "\" target=\"entityView\"><img src=\"hasattachment.gif\" border=0/></a></div>");
//                }
//            }
//            finally
//            {
//                connector.close(rs);
//            }
//        }
//
//        result.append("</td>\n");
//    }
//
//    protected static void generateCell2(ParsedDescription pd, StringBuffer result, boolean isInDatabase) throws Exception
//    {
//        result.append("<td width=\"600\" valign=\"top\">");
//        if( isInDatabase )
//        {
//            result.append("\t<font color=\"red\">" + pd.getTitle() + "</font>(<a href=\"http://www.ncbi.nlm.nih.gov/pubmed"
//                    + pd.getTitleLink()
//                    + "\" target=\"_blank\">PubMed</a>, <a href=\"q?_t_=biblio2.publications&_bn_=list_1record_no_empty_fields&PMID="
//                    + pd.id + "\"  target=\"entityView\">Biblio</a>)<br>");
//        }
//        else
//        {
//            result.append("\t<a href=\"http://www.ncbi.nlm.nih.gov/pubmed" + pd.getTitleLink() + "\" target=\"_blank\">" + pd.getTitle()
//                    + "</a><br>");
//        }
//        result.append("\t" + pd.getDescription());
//        result.append("<font size=\"2\">");
//        if( pd.getJournal() != null )
//        {
//            result.append("<br><i>").append(pd.getJournal()).append("</i>");
//        }
//        result.append("<br><i>PMID: ").append(pd.getId()).append("</i>");
//        result.append("</font>");
//        result.append("</td>");
//    }
//
//    protected static void generateCell3(ParsedDescription pd, StringBuffer result, boolean isInDatabase, DatabaseHandler dbHandler)
//    {
//        result.append("<td width=\"1\">&nbsp;</td>");
//
//        result.append("<td valign=\"top\">");
//        if( isInDatabase )
//        {
//            result.append(dbHandler.getShortDescription(pd.getId()));
//        }
//        else
//        {
//            result.append("\t&nbsp;");
//        }
//        result.append("</td>\n");
//
//        result.append("<td width=\"1\">&nbsp;</td>");
//    }
//
//    protected static void generateCell4(ParsedDescription pd, StringBuffer result, DatabaseHandler dbHandler,
//            List<RelatedEntitiesDescription> related) throws Exception
//    {
//        result.append("<td valign=\"top\">");
//        result.append("Related links: <br>");
//
//        for( RelatedEntitiesDescription red : related )
//        {
//            String sql = red.getListQuery().replaceAll("@PMID@", pd.getId());
//            ResultSet rs = dbHandler.getConnector().executeQuery(sql);
//            StringBuffer values = new StringBuffer();
//            List<String> entityList = new ArrayList<String>();
//            while( rs.next() )
//            {
//                entityList.add(rs.getString(1));
//            }
//            rs.close();
//            for( String entity : entityList )
//            {
//                rs = dbHandler.getConnector().executeQuery(red.getLink().replaceAll("@ENTITYID@", entity));
//                if( rs.next() )
//                {
//                    values.append("<a href=\"").append(rs.getString(1)).append("\">").append(entity).append("</a>").append(", ");
//                }
//            }
//            if( values.length() > 0 )
//            {
//                String entities = values.substring(0, values.length() - 2);
//                result.append("&nbsp;<i>" + red.getDatabase() + ":</i> " + entities + "<br>");
//            }
//        }
//
//        result.append("</td>\n");
//    }
//
//    protected static int getPageCount(String page)
//    {
//        String resultCount = "";
//        String marker = "<meta name=\"ncbi_resultcount\" content=\"";
//        int start = page.indexOf(marker);
//        if( start != -1 )
//        {
//            start += marker.length();
//            if( start != -1 )
//            {
//                int end = page.indexOf("\"", start + 1);
//                if( end != -1 )
//                {
//                    resultCount = page.substring(start, end);
//                }
//            }
//        }
//
//        String pageSize = "";
//        marker = "<meta name=\"ncbi_pagesize\" content=\"";
//        start = page.indexOf(marker);
//        if( start != -1 )
//        {
//            start += marker.length();
//            if( start != -1 )
//            {
//                int end = page.indexOf("\"", start + 1);
//                if( end != -1 )
//                {
//                    pageSize = page.substring(start, end);
//                }
//            }
//        }
//
//        try
//        {
//            double rCount = (double)Integer.parseInt(resultCount);
//            double pSize = (double)Integer.parseInt(pageSize);
//            if( rCount % pSize == 0 )
//            {
//                return (int) ( rCount / pSize );
//            }
//            else
//            {
//                return (int) ( rCount / pSize ) + 1;
//            }
//        }
//        catch( NumberFormatException e )
//        {
//        }
//
//        return -1;
//    }
//
//    protected static String getQueryKey(String page)
//    {
//        int start = page.indexOf("name=\"EntrezSystem2.PEntrez.DbConnector.LastQueryKey\"");
//        if( start != -1 )
//        {
//            String searchString = "value=\"";
//            start = page.indexOf(searchString, start);
//            if( start != -1 )
//            {
//                int end = page.indexOf("\"", start + searchString.length() + 1);
//                if( end != -1 )
//                {
//                    return page.substring(start + searchString.length(), end);
//                }
//            }
//        }
//        return null;
//    }
//
//    protected static ParsedDescription[] getLinks(String page)
//    {
//        int start = page.indexOf("<div class=\"rprtnum ");
//        if( start != -1 )
//        {
//            start = page.indexOf("<", start);
//            String resultBlock = page.substring(start);
//            String[] elements = resultBlock.split("<div class=\"rprtnum ");
//            List<ParsedDescription> results = new ArrayList<ParsedDescription>();
//            for( int i = 0; i < elements.length; i++ )
//            {
//                if( elements[i].trim().length() > 0 )
//                {
//                    ParsedDescription pd = parseDescription(elements[i]);
//                    if( pd != null )
//                    {
//                        results.add(pd);
//                    }
//                }
//            }
//            if( results.size() > 0 )
//            {
//                return results.toArray(new ParsedDescription[results.size()]);
//            }
//        }
//        return null;
//    }
//
//    protected static ParsedDescription parseDescription(String input)
//    {
//        ParsedDescription pd = new ParsedDescription();
//        int pos = 0;
//        int start;
//
//        //get rowNumber
//        String searchString = "sid=\"";
//        if( ( start = input.indexOf(searchString, pos) ) != -1 )
//        {
//            int end = input.indexOf("\"", start + searchString.length() + 1);
//            if( end != -1 )
//            {
//                pd.setResultNumber(input.substring(start + searchString.length(), end));
//                pos = end + 1;
//            }
//            else
//                return null;
//        }
//        else
//            return null;
//
//        //get ID
//        searchString = "value=\"";
//        int oldStart = start;
//        if( ( start = input.indexOf(searchString, pos) ) != -1 )
//        {
//            int end = input.indexOf("\"", start + searchString.length() + 1);
//            if( end != -1 )
//            {
//                pd.setId(input.substring(start + searchString.length(), end));
//                pos = end + 1;
//            }
//        }
//        else
//        {
//            start = oldStart;
//        }
//
//        //get title link
//        start = input.indexOf("<p class=\"title\">", start);
//        searchString = "<a href=\"/pubmed/";
//        oldStart = start;
//        if( ( start = input.indexOf(searchString, pos) ) != -1 )
//        {
//            int end = input.indexOf("\"", start + searchString.length() + 1);
//            if( end != -1 )
//            {
//                pd.setTitleLink("/" + input.substring(start + searchString.length(), end));
//                pos = end + 1;
//            }
//        }
//        else
//        {
//            start = oldStart;
//        }
//
//        //get title text
//        searchString = ">";
//        oldStart = start;
//        if( ( start = input.indexOf(searchString, pos) ) != -1 )
//        {
//            int end = input.indexOf("</a>", start + searchString.length() + 1);
//            if( end != -1 )
//            {
//                pd.setTitle(input.substring(start + searchString.length(), end));
//                pos = end + 1;
//            }
//        }
//        else
//        {
//            start = oldStart;
//        }
//
//        //get description
//        String description = "";
//        searchString = "<p class=\"desc\">";
//        oldStart = start;
//        if( ( start = input.indexOf(searchString, pos) ) != -1 )
//        {
//            int end = input.indexOf("<", start + searchString.length() + 1);
//            if( end != -1 )
//            {
//                description += input.substring(start + searchString.length(), end) + "<br>";
//                pos = end + 1;
//            }
//        }
//        else
//        {
//            start = oldStart;
//        }
//        pd.setDescription(description);
//
//        //get journal
//        searchString = "<span class=\"jrnl\"";
//        oldStart = start;
//        if( ( start = input.indexOf(searchString, pos) ) != -1 )
//        {
//            start = input.indexOf(">", start);
//            if(start != -1)
//            {
//                int end = input.indexOf("</span>", start + 1);
//                if( end != -1 )
//                {
//                    pd.setJournal(input.substring(start + 1, end));
//                    pos = end + 1;
//                }
//            }
//        }
//        else
//        {
//            start = oldStart;
//        }
//
//        return pd;
//    }
//
//    protected static String getJavaScript()
//    {
//        StringBuffer result = new StringBuffer();
//        result.append("<script language=\"Javascript1.1\">").append("\n");
//        result.append("<!--").append("\n");
//
//        result.append("function initButtons()").append("\n");
//        result.append("{").append("\n");
//        result.append("    document.getElementById('add').disabled = true;").append("\n");
//        result.append("    document.getElementById('edit').disabled = true;").append("\n");
//        result.append("    document.getElementById('attach').disabled = true;").append("\n");
//        result.append("    document.getElementById('categories').disabled = true;").append("\n");
//        result.append("}").append("\n");
//
//        result.append("function selectRecord(isInDatabase, hasAttachments)").append("\n");
//        result.append("{").append("\n");
//        result.append("    var sCount = 0;").append("\n");
//        result.append("    var ch = document.getElementsByName('elements');").append("\n");
//        result.append("    for(i=0;i<ch.length; i++)").append("\n");
//        result.append("    {").append("\n");
//        result.append("    if(ch[i].checked == true) sCount=sCount+1;").append("\n");
//        result.append("    }").append("\n");
//
//        result.append("    if(sCount == 0)").append("\n");
//        result.append("    {").append("\n");
//        result.append("        document.getElementById('add').disabled = true;").append("\n");
//        result.append("        document.getElementById('edit').disabled = true;").append("\n");
//        result.append("        document.getElementById('attach').disabled = true;").append("\n");
//        result.append("        document.getElementById('categories').disabled = true;").append("\n");
//        result.append("    }").append("\n");
//        result.append("    else").append("\n");
//        result.append("   {").append("\n");
//        result.append("        if(sCount == 1)").append("\n");
//        result.append("        {").append("\n");
//        result.append("            document.getElementById('add').disabled = isInDatabase;").append("\n");
//        result.append("            document.getElementById('edit').disabled = !isInDatabase;").append("\n");
//        result.append("            document.getElementById('attach').disabled = !isInDatabase || hasAttachments;").append("\n");
//        result.append("       }").append("\n");
//        result.append("      else").append("\n");
//        result.append("    {").append("\n");
//        result.append("        document.getElementById('add').disabled = true;").append("\n");
//        result.append("        document.getElementById('edit').disabled = true;").append("\n");
//        result.append("        document.getElementById('attach').disabled = true;").append("\n");
//        result.append("    }").append("\n");
//        result.append("    document.getElementById('categories').disabled = false;").append("\n");
//        result.append(" }").append("\n");
//        result.append("}").append("\n");
//
//        result.append("function previous(page)").append("\n");
//        result.append("{").append("\n");
//        result.append("    document.getElementById('pageNumber').value = page-1;").append("\n");
//        result.append("    document.forms[0].submit();").append("\n");
//        result.append("    return false;").append("\n");
//        result.append("}").append("\n");
//
//        result.append("function next(page)").append("\n");
//        result.append("{").append("\n");
//        result.append("    document.getElementById('pageNumber').value = page+1;").append("\n");
//        result.append("    document.forms[0].submit();").append("\n");
//        result.append("    return false;").append("\n");
//        result.append("}").append("\n");
//
//        result.append(" //-->").append("\n");
//        result.append(" </script>").append("\n");
//        return result.toString();
//    }
//
//    protected static String getStyle()
//    {
//        StringBuffer result = new StringBuffer();
//        result.append("<style>").append("\n");
//        result.append("html,body{height:100%;}").append("\n");
//        result.append("body{margin:0;padding:0;}").append("\n");
//        result.append("#t{width:100%;height:70px;background:#fff;top:0px;right:0px;}").append("\n");
//        result.append("/*For IE*/").append("\n");
//        result.append("*html{overflow:hidden;}").append("\n");
//        result.append("*html #t{position:absolute;}").append("\n");
//        result.append("*html #d{height:100%;overflow:auto;}").append("\n");
//        result.append("/*For MZ, Opera*/").append("\n");
//        result.append("html>body #t{position:fixed;}").append("\n");
//        result.append("</style>").append("\n");
//        return result.toString();
//    }
}
