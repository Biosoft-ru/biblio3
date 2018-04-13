package publications

import com.developmentontheedge.be5.env.Inject
import com.developmentontheedge.be5.operation.GOperationSupport
import com.developmentontheedge.be5.operation.TransactionalOperation
import com.developmentontheedge.be5.util.Utils
import ru.biosoft.biblio.MedlineImport

import static com.developmentontheedge.be5.api.FrontendConstants.CATEGORY_ID_PARAM


class InsertPublication extends GOperationSupport implements TransactionalOperation
{
    @Inject MedlineImport medlineImport

    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        dps.add("inputType", "Ввод") {
            TAG_LIST_ATTR = [["PubMed","PubMed"],["Вручную","manually"]] as String[][]
            RELOAD_ON_CHANGE = true
            value         = presetValues.getOrDefault("inputType", "PubMed")
        }

        if(dps.getValueAsString("inputType") == "PubMed")
        {
            dpsHelper.addDpForColumns(dps, getInfo().getEntity(), ["PMID"], context.getOperationParams())

            dps.edit("PMID") { CAN_BE_NULL = false }
        }
        else
        {
            dpsHelper.addDpExcludeAutoIncrement(dps, getInfo().getEntity(), context.getOperationParams())
        }

        dps.add("categoryID", "Category") {
            TAG_LIST_ATTR = helper.getTagsFromCustomSelectionView("categories", "Children Of Root", [entity: getInfo().getEntityName()])
            value = context.operationParams.get(CATEGORY_ID_PARAM)
        }

        return dpsHelper.setValues(dps, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        String pmid = ("" + dps.getValue("PMID")).trim()

        String inputType = dps.getValueAsString("inputType")
        dps.remove("inputType")

        String category = dps.getValueAsString("categoryID")
        dps.remove("categoryID")

        def publicationsRecord = database.publications.get([PMID: Long.parseLong(pmid)])
        def id

        if(publicationsRecord == null)
        {
            id = database.publications.add(dps)

            if(inputType == "PubMed")
            {
                if( ( pmid != null && pmid.length() > 0 ) )
                {
                    medlineImport.fill("publications", Long.parseLong(id))
                }
            }
        }
        else
        {
            id = Long.parseLong(publicationsRecord.getId())
        }

        List<Long> categories = new ArrayList<>()
        Long cat = category != null ? Long.parseLong(category) : null

        while(cat != null)
        {
            categories.add(cat)

            cat = db.getLong("SELECT c1.parentID FROM categories c1 WHERE c1.ID = ?", cat)
        }

        db.insert("""DELETE FROM classifications 
                     WHERE recordID = CONCAT('publications.', ${id})
                       AND categoryID IN """ + Utils.inClause(categories.size()), categories as Long[])

        db.insert("""INSERT INTO classifications (recordID, categoryID)
                     SELECT CONCAT('publications.', ${id}), c.ID FROM categories c 
                     WHERE id IN """ + Utils.inClause(categories.size()), categories as Long[])

        addRedirectParams(context.operationParams)
        //setResult(OperationResult.redirectToOperation("publications", context.queryName, "Edit", [selectedRows: id]))
    }

}
