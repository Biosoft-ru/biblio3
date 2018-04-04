// $Id: ProxyServlet.java,v 1.4 2012/12/03 10:50:34 lan Exp $
package ru.biosoft.pubmed.proxy;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Proxy servlet for PubMed database
 */
public class ProxyServlet extends HttpServlet
{
    protected static String proxySet = "true";
    protected static String proxyHost = "amber";
    protected static String proxyPort = "8080";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            PrintWriter writer = response.getWriter();
            response.setContentType("text/html");

            String pageUrl = request.getQueryString();
            if( pageUrl != null )
            {
                processQuery(pageUrl, writer, request);
            }
            else
            {
                fillErrorPage(writer, new Exception("Can not find url parameter"));
            }

            writer.close();
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            PrintWriter writer = response.getWriter();
            response.setContentType("text/html");

            String pageUrl = request.getQueryString();
            if( pageUrl != null )
            {
                processPostQuery(pageUrl, writer, request);
            }
            else
            {
                fillErrorPage(writer, new Exception("Can not find url parameter"));
            }

            writer.close();
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
    }
    /**
     * Process GET query
     */
    protected void processQuery(String pageUrl, PrintWriter writer, HttpServletRequest request)
    {
        setProxy();

        StringBuffer result = new StringBuffer();
        try
        {
            URL url = new URL(pageUrl);
            HttpURLConnection huc = (HttpURLConnection)url.openConnection();
            huc.setDoOutput(true);
            huc.setDoInput(true);
            huc.setUseCaches(false);
            huc.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            huc.setRequestProperty("User-Agent", "Mozilla/5.0");
            huc.setRequestProperty("Accept", "text/html");
            String cookie = (String)request.getSession().getAttribute("Cookie");
            if( cookie != null )
            {
                huc.setRequestProperty("Cookie", cookie);
            }
            huc.setRequestMethod("GET");
            huc.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
            String res = null;
            while( ( res = br.readLine() ) != null )
            {
                result.append(res);
            }

            List<String> cookieList = huc.getHeaderFields().get("Set-Cookie");
            StringBuffer cookies = new StringBuffer();
            {
                for( String value : cookieList )
                {
                    cookies.append(value).append(" ");
                }
            }
            request.getSession().setAttribute("Cookie", cookies.toString());

            br.close();
        }
        catch( Exception e )
        {
            fillErrorPage(writer, e);
        }

        Properties properties = new Properties();
        String term = request.getParameter("term");
        if( term != null )
        {
            properties.put("query", term);
        }
        String page = request.getParameter("page");
        if( page != null )
        {
            properties.put("page", page);
        }
        String queryKey = request.getParameter("queryKey");
        if( queryKey != null )
        {
            properties.put("queryKey", queryKey);
        }
        processPage(writer, result.toString(), properties);
    }
    /**
     * 
     */
    protected static void setProxy()
    {
        System.setProperty("proxySet", proxySet);
        System.setProperty("proxyHost", proxyHost);
        System.setProperty("proxyPort", proxyPort);
        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", proxyPort);
    }
    /**
     * Process POST query
     */
    protected void processPostQuery(String pageUrl, PrintWriter writer, HttpServletRequest request)
    {
        setProxy();

        String req = "EntrezSystem2.PEntrez.PubMed.Pubmed_SearchBar.CurrDb=pubmed"
                + "&EntrezSystem2.PEntrez.PubMed.Pubmed_SearchBar.SearchResourceList=pubmed"
                + "&EntrezSystem2.PEntrez.PubMed.Pubmed_SearchBar.FeedLimit=20"
                + "&EntrezSystem2.PEntrez.DbConnector.Db=pubmed"
                + "&EntrezSystem2.PEntrez.DbConnector.LastDb=pubmed"
                + "&EntrezSystem2.PEntrez.DbConnector.LastTabCmd=home";
        
        String term = request.getParameter("term");
        if( term != null )
        {
            req += "&EntrezSystem2.PEntrez.PubMed.Pubmed_SearchBar.Term=" + term
            + "&EntrezSystem2.PEntrez.DbConnector.Term="+term;
        }

        String page = request.getParameter("page");
        String queryKey = request.getParameter("queryKey");

        if( page != null )
        {
            req += "&EntrezSystem2.PEntrez.DbConnector.Cmd=PageChanged" 
                    + "&EntrezSystem2.PEntrez.PubMed.Pubmed_ResultsPanel.Entrez_Pager.CurrPage=" + page
                    + "&EntrezSystem2.PEntrez.PubMed.Pubmed_ResultsPanel.Pubmed_DisplayBar.PageSize=20";
        }

        if( queryKey != null )
        {
            req += "&EntrezSystem2.PEntrez.DbConnector.LastQueryKey=" + queryKey;
        }

        if( page != null && term == null )
        {
            //related articles pages
            req += "&EntrezSystem2.PEntrez.DbConnector.LinkName=pubmed_pubmed"
                    + "&EntrezSystem2.PEntrez.DbConnector.LinkReadableName=Related+Articles"
                    + "&EntrezSystem2.PEntrez.DbConnector.LinkSrcDb=pubmed" + "&EntrezSystem2.PEntrez.DbConnector.LastDb=pubmed"
                    + "&EntrezSystem2.PEntrez.DbConnector.TermToSearch=";
        }

        StringBuffer result = new StringBuffer();
        try
        {
            URL url = new URL("http://www.ncbi.nlm.nih.gov/pubmed");
            HttpURLConnection huc = (HttpURLConnection)url.openConnection();
            huc.setDoOutput(true);
            huc.setDoInput(true);
            huc.setUseCaches(false);
            huc.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            huc.setRequestProperty("User-Agent", "Mozilla/5.0");
            huc.setRequestProperty("Content-length", "" + req.length());
            huc.setRequestProperty("Accept", "text/html");
            String cookie = (String)request.getSession().getAttribute("Cookie");
            if( cookie != null )
            {
                huc.setRequestProperty("Cookie", cookie);
            }
            huc.setRequestMethod("POST");
            huc.connect();
            DataOutputStream dos = new DataOutputStream(huc.getOutputStream());
            dos.writeBytes(req);
            dos.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
            String res = null;
            while( ( res = br.readLine() ) != null )
            {
                result.append(res);
            }

            List<String> cookieList = huc.getHeaderFields().get("Set-Cookie");
            StringBuffer cookies = new StringBuffer();
            for( String value : cookieList )
            {
                value = value.replaceFirst("\\;.+$", "");
                if(cookies.length() > 0) cookies.append("; ");
                cookies.append(value);
            }
            request.getSession().setAttribute("Cookie", cookies.toString());

            br.close();
        }
        catch( Exception e )
        {
            fillErrorPage(writer, e);
        }

        Properties properties = new Properties();
        if( term != null )
        {
            properties.put("query", term);
        }
        if( page != null )
        {
            properties.put("page", page);
        }
        if( queryKey != null )
        {
            properties.put("queryKey", queryKey);
        }
        processPage(writer, result.toString(), properties);
    }
    /**
     * Create error page
     */
    protected void fillErrorPage(PrintWriter writer, Throwable t)
    {
        writer.println("<html>");
        writer.println("<body>");

        writer.println("<h3>" + t.getMessage() + "</h3>");

        writer.println("<p>");
        t.printStackTrace(writer);
        writer.println("</p>");

        writer.println("</body>");
        writer.println("</html>");
    }
    protected void processPage(PrintWriter writer, String page, Properties properties)
    {
        try
        {
            if( SearchResultPageGenerator.isResultData(page) )
            {
                String connectionString = getServletContext().getInitParameter("connectString");
                page = SearchResultPageGenerator.generateResultPage(page, connectionString, properties);
            }
            else
            {
                page = cutHeader(page);
                page = replaceLink(page, "href=");
                page = replaceLink(page, "action=");
            }
            writer.print(page);
        }
        catch( Throwable t )
        {
            fillErrorPage(writer, t);
        }
    }

    protected String replaceLink(String aSearch, String template)
    {
        String result = aSearch;
        if( result != null && result.length() > 0 )
        {
            int a = 0;
            int b = 0;
            while( true )
            {
                a = result.indexOf(template, b);
                if( a != -1 )
                {
                    int start = result.indexOf("\"", a);
                    int end = result.indexOf("\"", start + 1);
                    if( start != -1 && end != -1 && end > start )
                    {
                        String before = result.substring(start + 1, end);
                        if( before.startsWith("/") && !before.endsWith(".css") )
                        {
                            String after = "proxy?http://www.ncbi.nlm.nih.gov/pubmed" + before;
                            result = result.substring(0, start + 1) + after + result.substring(end);
                            b = start + after.length();
                        }
                        else
                        {
                            b = end + 1;
                        }
                    }
                    else
                        break;
                }
                else
                    break;
            }
        }
        return result;
    }

    protected String cutHeader(String input)
    {
        input = cutBlock(input, "<div id=\"NCBILogo\">", "</div>");
        input = cutBlock(input, "<div id=\"pubmed_head_logo\">", "</div>");
        input = cutBlock(input, "<table class=\"medium1\"", "</table>");
        input = cutBlock(input, "<div id=\"horiz_toolbar\">", "</div>");
        input = cutBlock(input, "<div id=\"SearchBarInputs\">", "</div>");

        return input;
    }

    protected String cutBlock(String input, String startString, String endString)
    {
        int start = input.indexOf(startString);
        if( start != -1 )
        {
            int end = input.indexOf(endString, start);
            if( end != -1 )
            {
                input = input.substring(0, start) + input.substring(end + endString.length(), input.length());
            }
        }
        return input;
    }
}
