// $Id: AddRemoveCategoryOperation.java,v 1.2 2008/11/03 10:43:31 tolstyh Exp $
package ru.biosoft.pubmed.proxy;

import com.developmentontheedge.be5.metadata.DatabaseConstants;
import com.developmentontheedge.be5.operation.OperationSupport;
import com.developmentontheedge.beans.DynamicPropertySetSupport;

import java.io.Writer;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;


public class AddRemoveCategoryOperation extends OperationSupport implements DatabaseConstants
{
    @Override
    public void invoke(Object parameters) throws Exception
    {

    }
    //    private DynamicPropertySetSupport parameters = new DynamicPropertySetSupport();
//
//    public Object getParameters(Writer out, DatabaseConnector connector, Map presetValues) throws Exception
//    {
//        if( records.length == 0 )
//            return null;
//
//        String displayName = new JDBCRecordAdapterAsQuery(connector, "SELECT displayName AS \"displayName\" FROM entities WHERE name = '"
//                + entity + "'").getString("displayName");
//        ArrayList vc = new ArrayList();
//        int rootID = 1;
//        try
//        {
//            ResultSet rs = connector.executeQuery("SELECT root FROM categoriesEx WHERE entity='biblio2.publications'");
//            if( rs.next() )
//            {
//                rootID = rs.getInt(1);
//            }
//        }
//        catch( Exception e )
//        {
//            //use global root as root for this classification
//        }
//        String categoriesSQL = " select c1.ID AS \"Code\", concat( ifnull( concat(c8.name, '->' ), '' ), "
//                + "ifnull( concat( c7.name, '->' ), '' ), ifnull( concat( c6.name, '->' ), '' ), "
//                + "ifnull( concat( c5.name, '->' ), '' ), ifnull( concat( c4.name, '->' ), '' ), "
//                + "ifnull( concat( c3.name, '->' ), '' ), ifnull( concat( c2.name, '->' ), '' ), "
//                + "c1.name) AS \"Category\" from biblio2.categories c1 left join biblio2.categories c2 on c1.parentID = c2.ID "
//                + "left join biblio2.categories c3 on c2.parentID = c3.ID left join biblio2.categories c4 on c3.parentID = c4.ID "
//                + "left join biblio2.categories c5 on c4.parentID = c5.ID left join biblio2.categories c6 on c5.parentID = c6.ID "
//                + "left join biblio2.categories c7 on c6.parentID = c7.ID left join biblio2.categories c8 on c7.parentID = c8.ID "
//                + "WHERE c1.entity LIKE '%publications' and (c8.ID=" + rootID + " or c7.ID=" + rootID + " or c6.ID=" + rootID
//                + " or c5.ID=" + rootID + " or c4.ID=" + rootID + " or c3.ID=" + rootID + " or c2.ID=" + rootID + " or c1.ID=" + rootID
//                + ") " + "order by c1.entity, 2";
//
//        ResultSet rs = connector.executeQuery(categoriesSQL);
//        while( rs.next() )
//        {
//            String entry = rs.getString(2) + OperationSupport.TAG_DELIMITER + rs.getString(1);
//            vc.add(entry);
//        }
//
//        DynamicProperty prop = new DynamicProperty("categoryID", "Category", String.class);
//        prop.setShortDescription("Category to be assigned or removed");
//        prop.setAttribute(TAG_LIST_ATTR, vc.toArray(new String[0]));
//        parameters.add(prop);
//
//        prop = new DynamicProperty("operationType", "Operation", String.class);
//        prop.setShortDescription("Whether to add or remove");
//        prop.setAttribute(TAG_LIST_ATTR, new String[] {"Add", "Remove"});
//        parameters.add(prop);
//
//        return parameters;
//    }
//
//    public void invoke(Writer out, DatabaseConnector connector) throws Exception
//    {
//        if( records.length == 0 )
//        {
//            out.write(localizedMessage("No records were selected") + "!<br />\n");
//            return;
//        }
//
//        DatabaseAnalyzer analyzer = connector.getAnalyzer();
//
//        boolean bNewApproach = Utils.getColumnSize(connector, "classifications", "entity") > 0;
//
//        Object category = parameters.getValue("categoryID");
//
//        boolean isNumericCategoryID = Utils.isNumericColumn(connector, "classifications", "categoryID");
//        ArrayList categories = new ArrayList();
//        categories.add(category);
//
//        Object wcat = category;
//        try
//        {
//            do
//            {
//                wcat = new JDBCRecordAdapterAsQuery(connector,
//                        "SELECT c1.parentID AS \"p\" FROM biblio2.categories c1, biblio2.categories c2 WHERE c1.ID = "
//                                + ( isNumericCategoryID ? wcat : "'" + wcat + "'" )
//                                + " AND c2.ID = c1.parentID AND c2.parentID IS NOT NULL").getValue("p");
//
//                categories.add(wcat);
//            }
//            while( true );
//        }
//        catch( JDBCRecordAdapterAsQuery.NoRecord ignore )
//        {
//        }
//
//        String catList = Utils.toInClause(categories, isNumericCategoryID);
//
//        out.write("<script language=\"Javascript1.1\">");
//        out.write("<!--" + "\n");
//        out.write("var formObj = parent.frames[0].document.forms[0];" + "\n");
//        out.write("formObj.submit();" + "\n");
//        out.write("//-->" + "\n");
//        out.write("</script>");
//
//        if( "Add".equals(parameters.getValue("operationType")) )
//        {
//            String idName = "";
//            String idValue = "";
//            if( connector.isOracle() )
//            {
//                idName = "ID, ";
//                idValue = "beIDGenerator.NEXTVAL, ";
//            }
//
//            String pk = primaryKey;
//            boolean isNumeric = Utils.isNumericColumn(connector, entity, pk);
//            String recordList = Utils.toInClause(records, isNumeric);
//
//            if( connector.isSQLServer() )
//                pk = "CAST( e." + analyzer.quoteIdentifier(pk) + " AS VARCHAR( 100 ) )";
//            else if( connector.isDb2() )
//                pk = "RTRIM( CAST( e." + analyzer.quoteIdentifier(pk) + " AS CHAR( 100 ) ) )";
//            else
//                pk = "e." + analyzer.quoteIdentifier(pk);
//
//            String sql = "INSERT INTO biblio2.classifications( ";
//            if( bNewApproach )
//            {
//                sql += idName + "entity, recordID, categoryID ) ";
//                sql += "SELECT " + idValue + "'" + Utils.safestr(connector, entity) + "', " + pk + ", c.ID ";
//            }
//            else
//            {
//                String conc = connector.getAnalyzer().makeConcatExpr(new String[] {"'" + Utils.safestr(connector, entity) + ".'", pk});
//                sql += idName + "recordID, categoryID ) ";
//                sql += "SELECT " + idValue + conc + ", c.ID ";
//            }
//
//            sql += "FROM " + entity + " e, biblio2.categories c ";
//            sql += "WHERE e." + analyzer.quoteIdentifier(primaryKey) + " IN " + recordList;
//            sql += "  AND c.ID IN " + catList;
//            int updateCount = connector.executeUpdate(sql);
//            out.write(localizedMessage("Category") + " " + catList + " " + localizedMessage("was added to") + " <b>" + updateCount
//                    + "</b> " + localizedMessage("records in") + " <i>" + entity + "</i><br />");
//            return;
//        }
//        String sql = "DELETE FROM biblio2.classifications ";
//        if( bNewApproach )
//        {
//            sql += "WHERE recordID IN " + Utils.toInClause(records);
//            sql += "   AND entity LIKE '" + Utils.safestr(connector, entity) + "' ";
//            sql += "   AND categoryID IN " + catList;
//        }
//        else
//        {
//            sql += "WHERE recordID LIKE '" + Utils.safestr(connector, entity) + ".%' ";
//            sql += "   AND categoryID IN " + catList;
//            if( connector.isOracle() )
//            {
//                sql += "   AND SUBSTR( recordID, " + ( entity.length() + 1 + 1 ) + " ) IN " + Utils.toInClause(records);
//            }
//            else if( connector.isSQLServer() )
//            {
//                sql += "   AND SUBSTRING( recordID, " + ( entity.length() + 1 + 1 ) + ", 100 ) IN " + Utils.toInClause(records);
//            }
//            else
//            {
//                sql += "   AND SUBSTRING( recordID, " + ( entity.length() + 1 + 1 ) + " ) IN " + Utils.toInClause(records);
//            }
//        }
//        int updateCount = connector.executeUpdate(sql);
//        out.write(localizedMessage("Category") + " " + catList + " " + localizedMessage("was removed from") + " <b>" + updateCount
//                + "</b> " + localizedMessage("records in") + " <i>" + entity + "</i><br />");
//    }
}
