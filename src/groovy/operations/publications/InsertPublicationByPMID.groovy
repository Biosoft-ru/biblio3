package publications

import com.developmentontheedge.be5.env.Inject
import com.developmentontheedge.be5.operation.GOperationSupport
import com.developmentontheedge.be5.operation.OperationResult
import com.developmentontheedge.be5.operation.TransactionalOperation
import com.developmentontheedge.be5.util.Utils
import ru.biosoft.biblio.MedlineImport

import static com.developmentontheedge.be5.api.FrontendConstants.CATEGORY_ID_PARAM


class InsertPublicationByPMID extends GOperationSupport implements TransactionalOperation
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
            TAG_LIST_ATTR = helper.getTagsFromSelectionView("categories", [entity: getInfo().getEntityName()])
            value = context.operationParams.get(CATEGORY_ID_PARAM)
        }

        return dpsHelper.setValues(dps, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        String pmid = ("" + dps.getValue("PMID")).trim()

        if(database.publications.count([PMID: Long.parseLong(pmid)]) > 0)
        {
            setResult(OperationResult.error("Publication with the same PMID already exists, PMID='" + pmid + "'."))
            return
        }

        String category = dps.getValueAsString("categoryID")

        //remove "categoryID" to avoid exception in super.invoke()
        dps.remove("categoryID")

        def id = database.publications.add(dps)

        if( ( pmid != null && pmid.length() > 0 ) )
        {
            medlineImport.fill("publications", Long.parseLong(id))
        }

        List<Long> categories = new ArrayList<>()
        Long cat = category != null ? Long.parseLong(category) : null

        while(cat != null)
        {
            categories.add(cat)

            cat = db.getLong("SELECT c1.parentID FROM categories c1, categories c2 WHERE c1.ID = ? " +
                                    "AND c2.ID = c1.parentID AND c2.parentID IS NOT NULL", cat)
        }

        db.insert("""INSERT INTO classifications (recordID, categoryID)
                     SELECT CONCAT('publications.', ${id}), c.ID FROM categories c 
                     WHERE id IN """ + Utils.inClause(categories.size()), categories as Long[])

        addRedirectParams(context.operationParams)
        //setResult(OperationResult.redirectToOperation("publications", context.queryName, "Edit", [selectedRows: id]))
    }

}
