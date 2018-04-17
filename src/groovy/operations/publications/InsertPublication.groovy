package publications

import com.developmentontheedge.be5.databasemodel.RecordModel
import com.developmentontheedge.be5.env.Inject
import com.developmentontheedge.be5.model.beans.GDynamicPropertySetSupport
import com.developmentontheedge.be5.operation.GOperationSupport
import com.developmentontheedge.be5.operation.TransactionalOperation
import ru.biosoft.biblio.BiblioHelper
import ru.biosoft.biblio.MedlineImport

import static com.developmentontheedge.be5.api.FrontendConstants.CATEGORY_ID_PARAM


class InsertPublication extends GOperationSupport implements TransactionalOperation
{
    @Inject MedlineImport medlineImport
    @Inject BiblioHelper biblioHelper

    def projectColumns = ["status", "importance", "keyWords", "comment"]
    RecordModel publicationRecord
    String projectID

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
            dpsHelper.addDpExcludeColumns(dps, getInfo().getEntity(), ["ID"], context.getOperationParams())

            dps.edit("PMID") { CSS_CLASSES = "col-lg-3" }
            dps.edit("ref") { CSS_CLASSES = "col-lg-9" }

            dps.edit("authors") { EXTRA_ATTRS = [["inputType", "textArea"], ["rows", "1"]] as String[][] }
            dps.edit("affiliation") { EXTRA_ATTRS = [["inputType", "textArea"], ["rows", "1"]] as String[][] }
            dps.edit("title") { EXTRA_ATTRS = [["inputType", "textArea"], ["rows", "1"]] as String[][] }

            dps.edit("year") { CSS_CLASSES = "col-lg-3" }
            dps.edit("month") { CSS_CLASSES = "col-lg-3" }
            dps.edit("volume") { CSS_CLASSES = "col-lg-3" }
            dps.edit("issue") { CSS_CLASSES = "col-lg-3" }
            dps.edit("pageFrom") { CSS_CLASSES = "col-lg-3" }
            dps.edit("pageTo") { CSS_CLASSES = "col-lg-3" }
            dps.edit("language") { CSS_CLASSES = "col-lg-3" }
            dps.edit("publicationType") { CSS_CLASSES = "col-lg-3" }
        }

        dps.add("categoryID", "Category") {
            TAG_LIST_ATTR = helper.getTagsFromCustomSelectionView("categories", "Children Of Root", [entity: getInfo().getEntityName()])
            value = context.operationParams.get(CATEGORY_ID_PARAM)
            RELOAD_ON_CHANGE = true
            GROUP_ID = 2; GROUP_NAME = "Категория"
        }

        if(presetValues.get("categoryID") != null)
        {
            projectID = qRec.of("""SELECT cat.name FROM categories cat
                    INNER JOIN classifications pcls ON pcls.recordID = CONCAT('projectCategory.', ?)
                      AND cat.ID = pcls.categoryID""", Long.parseLong((String)presetValues.get("categoryID"))).getString("name")

            dpsHelper.addDpForColumns(dps, meta.getEntity("publication2project"),
                    projectColumns, context.getOperationParams())

            dps.edit("status") { CSS_CLASSES = "col-lg-6" }
            dps.edit("importance") { CSS_CLASSES = "col-lg-6" }
            dps.edit("keyWords") { EXTRA_ATTRS = [["inputType", "textArea"], ["rows", "1"]] as String[][] }

            for (def columnName : projectColumns) {
                dps.edit(columnName) { GROUP_ID = 1; GROUP_NAME = "Поля для проекта " + projectID }
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

        String inputType = dps.remove("inputType")
        String category = dps.remove("categoryID")

        def projectInfo = new GDynamicPropertySetSupport()
        for (def columnName : projectColumns) {
            projectInfo.add(dps.getProperty(columnName))
            dps.remove(columnName)
        }

        Long publicationID

        if(publicationRecord == null)
        {
            publicationID = Long.parseLong(database.publications.add(dps))

            if(inputType == "PubMed")
            {
                if( ( pmid != null && pmid.length() > 0 ) )
                {
                    medlineImport.fill("publications", publicationID)
                }
            }
        }
        else
        {
            publicationID = Long.parseLong(publicationRecord.getId())
        }

        biblioHelper.updateCategories(category, publicationID)

        updateProjectInfo(publicationID, projectInfo)

        addRedirectParams(context.operationParams)
    }

    private void updateProjectInfo(Long publicationID, GDynamicPropertySetSupport projectInfo)
    {
        def record = database.publication2project.get([publicationID: publicationID, projectID: projectID])
        if(record == null)
        {
            projectInfo.add("publicationID"){TYPE = Long; value = publicationID}
            projectInfo.add("projectID"){value = projectID}
            database.publication2project.add(projectInfo)
        }
        else
        {
            database.publication2project.set(record.getId(), projectInfo)
        }
    }
}
