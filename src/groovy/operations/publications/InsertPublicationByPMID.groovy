package publications

import com.developmentontheedge.be5.env.Inject
import com.developmentontheedge.be5.operation.GOperationSupport
import com.developmentontheedge.be5.operation.OperationResult
import com.developmentontheedge.be5.util.Utils
import ru.biosoft.biblio.MedlineImport


class InsertPublicationByPMID extends GOperationSupport
{
    @Inject MedlineImport medlineImport

    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        dpsHelper.addDpForColumns(dps, getInfo().getEntity(), ["PMID"])

        dps.edit("PMID") { CAN_BE_NULL = false }

//        String displayName = qRec.of("SELECT displayName AS \"displayName\" FROM entities WHERE name = ?",
//                getInfo().getEntityName()).getString("displayName")
//
//        List<String> vc = new ArrayList<>()
//        String[][] cats = helper.getTagsFromSelectionView("categories")
//        for( int i = 0; i < cats.length; i++ )
//        {
//            String entry = cats[i];
//            if( !entry.startsWith(displayName) )
//                continue;
//            entry = entry.substring(displayName.length());
//            if( entry.startsWith(": ") )
//                entry = entry.substring(2)
//
//            vc.add(entry)
//        }

        //prop.setShortDescription("Category to be assigned")
        //prop.setAttribute(TAG_LIST_ATTR, vc.toArray(new String[0]))

        dps.add("categoryID", "Category") {
            TAG_LIST_ATTR = helper.getTagsFromCustomSelectionView("categories",
                    "Selection view for entity", [entity: getInfo().getEntityName()])
        }

        return dpsHelper.setValues(dps, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        String pmid = ("" + dps.getValue("PMID")).trim()

        if(database.publications.count([PMID: pmid]) > 0)
        {
            setResult(OperationResult.error("Publication with the same PMID already exists, PMID='" + pmid + "'."))
            return
        }

        String category = dps.getValue("categoryID")
        //remove "categoryID" to avoid exception in super.invoke()
        dps.remove("categoryID")

        def id = database.publications.add(dps)

        Writer out = new StringWriter()

        if( ( pmid != null && pmid.length() > 0 ) )
        {
            //String table = connector.getAnalyzer().quoteIdentifier(entity)
            medlineImport.fill(out, getInfo().getEntityName(), id)
        }
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


        out.flush()
        setResult(OperationResult.finished(out.toString()))
    }

}
