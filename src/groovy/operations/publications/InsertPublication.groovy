package publications

import com.developmentontheedge.be5.databasemodel.RecordModel
import com.developmentontheedge.be5.env.Inject
import com.developmentontheedge.be5.operation.GOperationSupport
import com.developmentontheedge.be5.operation.TransactionalOperation
import com.developmentontheedge.be5.util.Utils
import ru.biosoft.biblio.MedlineImport

import static com.developmentontheedge.be5.api.FrontendConstants.CATEGORY_ID_PARAM


class InsertPublication extends GOperationSupport implements TransactionalOperation
{
    @Inject MedlineImport medlineImport

    def projectColumns = ["status", "importance", "keyWords", "comment"]
    RecordModel publicationRecord

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

            dps.edit("PMID") {
                CAN_BE_NULL = false
                RELOAD_ON_CHANGE = true
            }

            if(presetValues.get("PMID") != null)
            {
                publicationRecord = database.publications.get([PMID: Long.parseLong((String)presetValues.get("PMID"))])
            }
        }
        else
        {
            dpsHelper.addDpExcludeColumns(dps, getInfo().getEntity(), ["ID","PMID"], context.getOperationParams())
        }

        dps.add("categoryID", "Category") {
            TAG_LIST_ATTR = helper.getTagsFromCustomSelectionView("categories", "Children Of Root", [entity: getInfo().getEntityName()])
            value = context.operationParams.get(CATEGORY_ID_PARAM)
            RELOAD_ON_CHANGE = true
            GROUP_ID = 2; GROUP_NAME = "Категория"
        }

        if(presetValues.get("categoryID") != null)
        {
            String projectID = qRec.of("""SELECT cat.name FROM categories cat
                    INNER JOIN classifications pcls ON pcls.recordID = CONCAT('projectCategory.', ?)
                      AND cat.ID = pcls.categoryID""", Long.parseLong((String)presetValues.get("categoryID"))).getString("name")

            dpsHelper.addDpForColumns(dps, meta.getEntity("publication2project"),
                    projectColumns, context.getOperationParams())

            dps.edit("status") { CSS_CLASSES = "col-lg-6" }
            dps.edit("importance") { CSS_CLASSES = "col-lg-6" }
            dps.edit("keyWords") { EXTRA_ATTRS = [["inputType", "textArea"], ["rows", "1"]] as String[][] }

            for (def columnName : projectColumns) {
                dps.edit(columnName) { GROUP_ID = 1; GROUP_NAME = "Поля проекта " + projectID }
            }

            if(publicationRecord != null)
            {
                def publication2projectRecord = database.publication2project.get([
                        publicationID: Long.parseLong(publicationRecord.getId()),
                        projectID    : projectID
                ])
                if(publication2projectRecord != null)dpsHelper.setValues(dps, publication2projectRecord)
            }
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

        def publicationID

        if(publicationRecord == null)
        {
            publicationID = database.publications.add(dps)

            if(inputType == "PubMed")
            {
                if( ( pmid != null && pmid.length() > 0 ) )
                {
                    medlineImport.fill("publications", Long.parseLong(publicationID))
                }
            }
        }
        else
        {
            publicationID = Long.parseLong(publicationRecord.getId())
        }

        List<Long> categories = new ArrayList<>()
        Long cat = category != null ? Long.parseLong(category) : null

        while(cat != null)
        {
            categories.add(cat)

            cat = db.getLong("SELECT c1.parentID FROM categories c1 WHERE c1.ID = ?", cat)
        }

        db.insert("""DELETE FROM classifications 
                     WHERE recordID = CONCAT('publications.', ${publicationID})
                       AND categoryID IN """ + Utils.inClause(categories.size()), categories as Long[])

        db.insert("""INSERT INTO classifications (recordID, categoryID)
                     SELECT CONCAT('publications.', ${publicationID}), c.ID FROM categories c 
                     WHERE id IN """ + Utils.inClause(categories.size()), categories as Long[])

        addRedirectParams(context.operationParams)
        //setResult(OperationResult.redirectToOperation("publications", context.queryName, "Edit", [selectedRows: id]))
    }

}
