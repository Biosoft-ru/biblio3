// $Id: DatabaseHandler.java,v 1.4 2008/11/03 10:43:31 tolstyh Exp $
package ru.biosoft.pubmed.proxy;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler
{
//    protected DatabaseConnector connector;
//
//    public DatabaseHandler(String connectionString)
//    {
//        connector = new JDBCDatabaseConnector();
//        connector.setConnectString(connectionString);
//    }
//
//    public boolean isInDatabase(String pmid) throws Exception
//    {
//        String query = "SELECT * FROM biblio2.publications WHERE PMID=" + pmid;
//
//        ResultSet rs = null;
//        try
//        {
//            rs = connector.executeQuery(query);
//            if( rs.next() )
//            {
//                return true;
//            }
//        }
//        finally
//        {
//            connector.close(rs);
//        }
//        return false;
//    }
//
//    public String getShortDescription(String pmid)
//    {
//        StringBuffer result = new StringBuffer();
//        StringBuffer category = new StringBuffer();
//        String keywords = "";
//        String comment = "";
//        String query = "SELECT keyWords,comment,id FROM biblio2.publications WHERE PMID=" + pmid;
//
//        ResultSet rs = null;
//        ResultSet rsq = null;
//        try
//        {
//            rs = connector.executeQuery(query);
//            if( rs.next() )
//            {
//                keywords = rs.getString(1);
//                comment = rs.getString(2);
//                int id = rs.getInt(3);
//
//                query = "SELECT cat.name FROM biblio2.classifications cl, biblio2.categories cat WHERE cat.id=cl.categoryID AND cl.recordID LIKE '%publications."
//                        + id + "'";
//                rsq = connector.executeQuery(query);
//                while( rsq.next() )
//                {
//                    if( rsq.getString(1).equals("Root") )
//                    {
//                        if( category.length() > 1 )
//                            category.append("<br>");
//                    }
//                    else
//                    {
//                        if( category.length() > 1 )
//                            category.append(" - ");
//
//                        category.append(rsq.getString(1));
//                    }
//                }
//            }
//
//        }
//        catch( Exception e )
//        {
//
//        }
//        finally
//        {
//            connector.close(rs);
//            connector.close(rsq);
//        }
//
//        if( category != null )
//        {
//            result.append("<div>");
//            result.append(category);
//            result.append("</div>");
//        }
//
//        if( keywords != null )
//        {
//            result.append("<div><font color=\"blue\">");
//            result.append(keywords);
//            result.append("</font></div>");
//        }
//
//        if( comment != null )
//        {
//            result.append("<div><i>");
//            result.append(comment);
//            result.append("</i></div>");
//        }
//
//        return result.toString();
//    }
//
//    public boolean hasAttachment(String pmid) throws Exception
//    {
//        String query = "SELECT a.id FROM biblio2.publications p, biblio2.attachments a WHERE p.id=a.publicationID AND p.PMID="
//                + pmid;
//
//        ResultSet rs = null;
//        try
//        {
//            rs = connector.executeQuery(query);
//            if( rs.next() )
//            {
//                return true;
//            }
//        }
//        finally
//        {
//            connector.close(rs);
//        }
//        return false;
//    }
//
//    public String getFullTextURL(String pmid) throws Exception
//    {
//        String query = "SELECT p.url FROM biblio2.publications p WHERE p.PMID=" + pmid;
//        ResultSet rs = null;
//        try
//        {
//            rs = connector.executeQuery(query);
//            if( rs.next() )
//            {
//                return rs.getString(1);
//            }
//        }
//        finally
//        {
//            connector.close(rs);
//        }
//        return null;
//    }
//
//    public List<RelatedEntitiesDescription> getRelatedentitiesDescriptions() throws Exception
//    {
//        List<RelatedEntitiesDescription> result = new ArrayList<RelatedEntitiesDescription>();
//
//        String query = "SELECT `database`,`listQuery`,`link` FROM biblio2.relatedEntities";
//        ResultSet rs = null;
//        try
//        {
//            rs = connector.executeQuery(query);
//            while( rs.next() )
//            {
//                RelatedEntitiesDescription red = new RelatedEntitiesDescription();
//                red.setDatabase(rs.getString(1));
//                red.setListQuery(rs.getString(2));
//                red.setLink(rs.getString(3));
//                result.add(red);
//            }
//        }
//        finally
//        {
//            connector.close(rs);
//        }
//        return result;
//    }
//
//    public DatabaseConnector getConnector()
//    {
//        return connector;
//    }
}
