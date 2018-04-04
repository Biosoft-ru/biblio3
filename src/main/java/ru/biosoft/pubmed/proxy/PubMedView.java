/* $Id: PubMedView.java,v 1.1 2008/10/31 05:58:03 tolstyh Exp $ */

package ru.biosoft.pubmed.proxy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.IOException;

public class PubMedView extends HttpServlet
{
    protected long getLastModified(HttpServletRequest request)
    {
        return -1;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        generateFrame(request, response);
    }

    protected void generateFrame(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String queryString = request.getQueryString();
        if( queryString != null )
        {
            int startIndex = queryString.indexOf("http://");
            if( startIndex != -1 )
            {
                queryString = queryString.substring(startIndex);
            }
            else
            {
                queryString = null;
            }
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.write("<html><head>");
        out.write("<title>PubMed viewer</title></head>");
        out.write("<frameset rows=\"70%,30%\" frameborder=\"1\" framespacing=\"0\">");

        if( queryString == null || queryString.length() == 0 )
        {
            out.write("<frame name=\"mainView\" src=\"pubmedForm.html\">");
        }
        else
        {
            out.write("<frame name=\"mainView\" src=\"proxy?" + queryString + "\">");
        }


        // stub
        out.write("<frame name=\"entityView\" src=\"empty.html\">");

        out.write("</frameset>");
        out.write("</html>");
    }
}
