// $Id: ActionServlet.java,v 1.2 2008/11/03 10:43:31 tolstyh Exp $
package ru.biosoft.pubmed.proxy;

import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Proxy servlet for PubMed database
 */
public class ActionServlet extends HttpServlet
{
    protected static final String ADD_COMMAND = "Add";
    protected static final String EDIT_COMMAND = "Edit";
    protected static final String ATTACH_COMMAND = "Attach";
    protected static final String CATEGORIES_COMMAND = "Categories";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            fillErrorPage(response.getWriter(), new Exception("This servlet doesn't support GET method"));
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
            response.setContentType("text/html");

            String[] values = request.getParameterValues("elements");
            if( values == null )
            {
                fillErrorPage(response.getWriter(), new Exception("Incorrect parameters"));
                return;
            }
            String[] actions = request.getParameterValues("action");
            if( actions != null )
            {
                String command = actions[0];
                if( command.equals(ADD_COMMAND) )
                {
                    //add command
                    doAddCommand(request, response, values);
                }
                else if( command.equals(EDIT_COMMAND) )
                {
                    //edit command
                    doEditCommand(request, response, values);
                }
                else if( command.equals(ATTACH_COMMAND) )
                {
                    //attach command
                    doAttachCommand(request, response, values);
                }
                else if( command.equals(CATEGORIES_COMMAND) )
                {
                    //categories command
                    doCategoriesCommand(request, response, values);
                }
            }
            else
            {
                fillErrorPage(response.getWriter(), new Exception("Action is null"));
            }
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
    }

    /**
     * Do add command
     */
    protected void doAddCommand(HttpServletRequest request, HttpServletResponse response, String[] values) throws Exception
    {
//        if( values == null || values.length == 0 )
//        {
//            fillErrorPage(response.getWriter(), new Exception("No PMID to add"));
//            return;
//        }
//        try
//        {
//            String connectionString = getServletContext().getInitParameter("connectString");
//            DatabaseHandler dbHandler = new DatabaseHandler(connectionString);
//            if( !dbHandler.isInDatabase(values[0]) )
//            {
//                DatabaseConnector connector = dbHandler.getConnector();
//
//                int operationidId;
//                ResultSet rs = null;
//                try
//                {
//                    rs = connector.executeQuery("SELECT id FROM operations WHERE table_name like '%publications' AND name='AddFromPubMed'");
//                    if( !rs.next() )
//                    {
//                        fillErrorPage(response.getWriter(), new Exception("Can not find categories operation"));
//                        return;
//                    }
//                    operationidId = rs.getInt(1);
//                }
//                finally
//                {
//                    connector.close(rs);
//                }
//
//                String recParams = "&_rec_" + values[0] + "=1";
//
//                String redirectString = "o?_t_=biblio2.publications&_op_" + operationidId + "=" + recParams.toString();
//                response.sendRedirect(redirectString);
//            }
//            else
//            {
//                fillErrorPage(response.getWriter(), new Exception("This publication is already in database"));
//            }
//        }
//        catch( Exception e )
//        {
//            fillErrorPage(response.getWriter(), e);
//            return;
//        }
    }

    /**
     * Do edit command
     */
    protected void doEditCommand(HttpServletRequest request, HttpServletResponse response, String[] values) throws Exception
    {
//        if( values == null || values.length == 0 )
//        {
//            fillErrorPage(response.getWriter(), new Exception("No PMID to add"));
//            return;
//        }
//        try
//        {
//            String connectionString = getServletContext().getInitParameter("connectString");
//            DatabaseHandler dbHandler = new DatabaseHandler(connectionString);
//            if( dbHandler.isInDatabase(values[0]) )
//            {
//                DatabaseConnector connector = dbHandler.getConnector();
//
//                int operationidId;
//                ResultSet rs = null;
//                try
//                {
//                    rs = connector.executeQuery("SELECT id FROM operations WHERE table_name like '%publications' AND name='EditPubMed'");
//                    if( !rs.next() )
//                    {
//                        fillErrorPage(response.getWriter(), new Exception("Can not find edit operation"));
//                        return;
//                    }
//                    operationidId = rs.getInt(1);
//                }
//                finally
//                {
//                    connector.close(rs);
//                }
//
//                String recParams = "&_rec_" + values[0] + "=1";
//
//                String redirectString = "o?_t_=biblio2.publications&_op_" + operationidId + "=" + recParams.toString();
//                response.sendRedirect(redirectString);
//            }
//            else
//            {
//                fillErrorPage(response.getWriter(), new Exception("Can not find publication in database"));
//            }
//        }
//        catch( Exception e )
//        {
//            fillErrorPage(response.getWriter(), e);
//            return;
//        }
    }

    /**
     * Do attach command
     */
    protected void doAttachCommand(HttpServletRequest request, HttpServletResponse response, String[] values) throws Exception
    {
//        if( values == null || values.length == 0 )
//        {
//            fillErrorPage(response.getWriter(), new Exception("No PMID to add"));
//            return;
//        }
//        try
//        {
//            String connectionString = getServletContext().getInitParameter("connectString");
//            DatabaseHandler dbHandler = new DatabaseHandler(connectionString);
//            if( dbHandler.isInDatabase(values[0]) )
//            {
//                DatabaseConnector connector = dbHandler.getConnector();
//
//                int publicationId;
//                ResultSet rs = null;
//                try
//                {
//                    rs = connector.executeQuery("SELECT id FROM biblio2.publications WHERE PMID='" + values[0] + "'");
//                    if( !rs.next() )
//                    {
//                        fillErrorPage(response.getWriter(), new Exception("Can not find publication in database"));
//                        return;
//                    }
//                    publicationId = rs.getInt(1);
//                }
//                finally
//                {
//                    connector.close(rs);
//                }
//
//                int operationidId;
//                try
//                {
//                    rs = connector.executeQuery("SELECT id FROM operations WHERE table_name like '%attachments' AND name='Add File'");
//                    if( !rs.next() )
//                    {
//                        fillErrorPage(response.getWriter(), new Exception("Can not find attach operation"));
//                        return;
//                    }
//                    operationidId = rs.getInt(1);
//                }
//                finally
//                {
//                    connector.close(rs);
//                }
//
//                response.sendRedirect("o?_t_=biblio2.attachments&_op_" + operationidId + "=&publicationID=" + publicationId);
//            }
//        }
//        catch( Exception e )
//        {
//            fillErrorPage(response.getWriter(), e);
//            return;
//        }
    }

    /**
     * Do categories command
     */
    protected void doCategoriesCommand(HttpServletRequest request, HttpServletResponse response, String[] values) throws Exception
    {
//        if( values == null || values.length == 0 )
//        {
//            fillErrorPage(response.getWriter(), new Exception("No PMID to add"));
//            return;
//        }
//        try
//        {
//            String connectionString = getServletContext().getInitParameter("connectString");
//            DatabaseHandler dbHandler = new DatabaseHandler(connectionString);
//            DatabaseConnector connector = dbHandler.getConnector();
//
//            int operationidId;
//            StringBuffer recParams = new StringBuffer();
//            ResultSet rs = null;
//            try
//            {
//                rs = connector.executeQuery("SELECT id FROM operations WHERE table_name like '%publications' AND name='Categories'");
//                if( !rs.next() )
//                {
//                    fillErrorPage(response.getWriter(), new Exception("Can not find categories operation"));
//                    return;
//                }
//                operationidId = rs.getInt(1);
//
//                for( String value : values )
//                {
//                    rs = connector.executeQuery("SELECT id FROM biblio2.publications WHERE PMID='" + value + "'");
//                    if( rs.next() )
//                    {
//                        recParams.append("&_rec_").append(rs.getString(1)).append("=1");
//                    }
//                }
//            }
//            finally
//            {
//                connector.close(rs);
//            }
//
//            String redirectString = "o?_t_=biblio2.publications&_op_" + operationidId + "=" + recParams.toString();
//            response.sendRedirect(redirectString);
//        }
//        catch( Exception e )
//        {
//            fillErrorPage(response.getWriter(), e);
//            return;
//        }
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
}
