package ru.biosoft.biblio;

import com.developmentontheedge.be5.operations.InsertOperation;
import com.developmentontheedge.be5.util.Utils;
import com.developmentontheedge.beans.DynamicPropertySet;

import java.io.Writer;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.developmentontheedge.be5.metadata.DatabaseConstants.SELECTION_VIEW;


public class InsertPublication extends InsertOperation
{
//    public Object getParameters(Writer out, DatabaseConnector connector, Map presetValues) throws Exception
//    {
//        DynamicPropertySet params = (DynamicPropertySet)super.getParameters(out, connector, presetValues);
//
//        String displayName = new JDBCRecordAdapterAsQuery(connector, "SELECT displayName AS \"displayName\" FROM entities WHERE name = '"
//                + entity + "'").getString("displayName");
//        List<String> vc = new ArrayList<String>();
//        if( !connector.isOracle() )
//        {
//            String[] cats = getTagsFromSelectionView(connector, "categories");
//            for( int i = 0; i < cats.length; i++ )
//            {
//                String entry = cats[i];
//                if( !entry.startsWith(displayName) )
//                    continue;
//                entry = entry.substring(displayName.length());
//                if( entry.startsWith(": ") )
//                    entry = entry.substring(2);
//
//                vc.add(entry);
//            }
//        }
//        else
//        {
//            String selSql = "SELECT q.query AS \"query\" FROM queries q WHERE q.name = '" + SELECTION_VIEW
//                    + "' AND q.table_name = 'categories'";
//            String sql = new JDBCRecordAdapterAsQuery(connector, selSql).getString("query");
//
//            String pkt = Utils.findPrimaryKeyName(connector, "categories");
//            sql = Utils.addRecordFilter(connector, sql, "categories", pkt, Collections.singletonMap("entity", entity), false, userInfo);
//            String[] cats = getTagsFromQuery(connector, sql, null);
//
//            for( int i = 0; i < cats.length; i++ )
//            {
//                vc.add(cats[i]);
//            }
//        }
//
//        DynamicProperty prop = new DynamicProperty("categoryID", "Category", String.class);
//        prop.setShortDescription("Category to be assigned");
//        prop.setAttribute(TAG_LIST_ATTR, vc.toArray(new String[0]));
//        parameters.add(prop);
//
//        return params;
//    }

//    public void invoke(Writer out, DatabaseConnector connector) throws Exception
//    {
//        // check whether publication with the same OMID or UID already exists
//        String pmid = "" + parameters.getValue("PMID");
//        pmid = pmid.trim();
//
//        ResultSet rs = null;
//        if( pmid != null && pmid.length() > 1 && ! ( pmid.equals("null") ) )
//        {
//            try
//            {
//                rs = connector.executeQuery("SELECT p.ID, p.PMID " + "FROM publications p WHERE p.PMID='" + pmid + "'");
//
//                if( rs.next() )
//                {
//                    out.write("Publication with the same PMID already exists, PMID='" + pmid + "'.");
//                    return;
//                }
//            }
//            finally
//            {
//                connector.close(rs);
//            }
//        }
//
//        Object category = parameters.getValue("categoryID");
//        //remove "categoryID" to avoid exception in super.incoke()
//        parameters.remove("categoryID");
//
//        super.invoke(out, connector);
//
//        if( ( pmid != null && pmid.length() > 0 ) )
//        {
//            String table = connector.getAnalyzer().quoteIdentifier(entity);
//            ru.biosoft.biblio.MedlineImport.fill(out, connector, table, lastInsertID);
//        }
//
//        //find ID
//        String id;
//        rs = null;
//        try
//        {
//            rs = connector.executeQuery("SELECT id FROM publications WHERE PMID=" + Utils.safestr(connector, pmid));
//            if( rs.next() )
//            {
//                id = rs.getString(1);
//            }
//            else
//            {
//                throw new Exception("Can not find ID for publication with PMID = " + pmid);
//            }
//        }
//        finally
//        {
//            connector.close(rs);
//        }
//
//        //add category
//        DatabaseAnalyzer analyzer = connector.getAnalyzer();
//        boolean bNewApproach = Utils.getColumnSize(connector, "classifications", "entity") > 0;
//        boolean isNumericCategoryID = Utils.isNumericColumn(connector, "classifications", "categoryID");
//        ArrayList categories = new ArrayList();
//        categories.add(category);
//
//        // repeat for parent categories
//        Object wcat = category;
//        try
//        {
//            do
//            {
//                wcat = new JDBCRecordAdapterAsQuery(connector,
//                        "SELECT c1.parentID AS \"p\" FROM categories c1, categories c2 WHERE c1.ID = "
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
//        String idName = "";
//        String idValue = "";
//        if( connector.isOracle() )
//        {
//            idName = "ID, ";
//            idValue = "beIDGenerator.NEXTVAL, ";
//        }
//
//        String pk = primaryKey;
//
//        boolean isNumeric = Utils.isNumericColumn(connector, entity, pk);
//        String recordList = Utils.toInClause(new String[] {id}, isNumeric);
//
//        if( connector.isSQLServer() )
//            pk = "CAST( e." + analyzer.quoteIdentifier(pk) + " AS VARCHAR( 100 ) )";
//        else if( connector.isDb2() )
//            pk = "RTRIM( CAST( e." + analyzer.quoteIdentifier(pk) + " AS CHAR( 100 ) ) )";
//        else
//            pk = "e." + analyzer.quoteIdentifier(pk);
//
//        String sql = "INSERT INTO classifications( ";
//        if( bNewApproach )
//        {
//            sql += idName + "entity, recordID, categoryID ) ";
//            sql += "SELECT " + idValue + "'" + Utils.safestr(connector, entity) + "', " + pk + ", c.ID ";
//        }
//        else
//        {
//            String conc = connector.getAnalyzer().makeConcatExpr(new String[] {"'" + Utils.safestr(connector, entity) + ".'", pk});
//            sql += idName + "recordID, categoryID ) ";
//            sql += "SELECT " + idValue + conc + ", c.ID ";
//        }
//
//        sql += "FROM " + entity + " e, categories c ";
//        sql += "WHERE e." + analyzer.quoteIdentifier(primaryKey) + " IN " + recordList;
//        sql += "  AND c.ID IN " + catList;
//        connector.executeUpdate(sql);
//    }

//    public static class RoleTagEditor extends TagEditorSupport
//    {
//        static String[] roles = {"CompanyAdmin", "Operator"};
//
//        public RoleTagEditor()
//        {
//            super(roles, 0);
//        }
//    }
}
