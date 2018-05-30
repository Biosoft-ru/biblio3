package publications

import com.developmentontheedge.be5.databasemodel.RecordModel
import com.developmentontheedge.be5.server.model.beans.GDynamicPropertySetSupport
import com.developmentontheedge.be5.modules.core.services.impl.CategoriesHelper
import com.developmentontheedge.be5.server.operations.support.GOperationSupport
import com.developmentontheedge.be5.operation.model.TransactionalOperation
import com.developmentontheedge.be5.base.util.DpsUtils
import com.developmentontheedge.beans.DynamicPropertySet
import ru.biosoft.biblio.services.MedlineImport

import javax.inject.Inject

import static com.developmentontheedge.be5.base.FrontendConstants.CATEGORY_ID_PARAM


class InsertPublication extends GOperationSupport implements TransactionalOperation
{
    @Inject MedlineImport medlineImport
    @Inject CategoriesHelper categoriesHelper

    def projectColumns = ["status", "importance", "keyWords", "comment"]
    String projectID

    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        //todo create new operation after error
        dps = new GDynamicPropertySetSupport()

        dps.add("inputType", "Ввод") {
            TAG_LIST_ATTR = [["PubMed","PubMed"],["manually","Вручную"]] as String[][]
            RELOAD_ON_CHANGE = true
            CSS_CLASSES   = "col-lg-4"
            value         = presetValues.getOrDefault("inputType", "PubMed")
        }

        if(dps.getValueAsString("inputType") == "PubMed")
        {
            dpsHelper.addDpForColumns(dps, getInfo().getEntity(), ["PMID"], context.getOperationParams())

            dps.edit("PMID") {
                CAN_BE_NULL = false
                RELOAD_ON_CHANGE = true
                value = presetValues.get("PMID")
            }
        }
        else
        {
            dpsHelper.addDpExcludeColumns(dps, getInfo().getEntity(), ["ID"], context.getOperationParams())

            dps.edit("PMID") { HIDDEN = true }

            dps.edit("authors") { EXTRA_ATTRS = [["inputType", "textArea"], ["rows", "1"]] as String[][] }
            dps.edit("title") { EXTRA_ATTRS = [["inputType", "textArea"], ["rows", "1"]] as String[][] }

            dps.edit("year") { CSS_CLASSES = "col-lg-3" }
            dps.edit("month") { CSS_CLASSES = "col-lg-3" }
            dps.edit("volume") { CSS_CLASSES = "col-lg-3" }
            dps.edit("issue") { CSS_CLASSES = "col-lg-3" }
            dps.edit("pageFrom") { CSS_CLASSES = "col-lg-3" }
            dps.edit("pageTo") { CSS_CLASSES = "col-lg-3" }
            dps.edit("language") { CSS_CLASSES = "col-lg-3" }
            dps.edit("publicationType") { CSS_CLASSES = "col-lg-3" }

            dps.edit("ref") { CSS_CLASSES = "col-lg-4" }
            dps.edit("PMCID") { CSS_CLASSES = "col-lg-4" }
            dps.moveTo("PMCID",3)
        }

        dps.add("categoryID", "Category") {
            TYPE = Long
            TAG_LIST_ATTR = helper.getTagsFromCustomSelectionView("categories", "For publications")
            value = presetValues.getOrDefault("categoryID", context.operationParams.get(CATEGORY_ID_PARAM))
            RELOAD_ON_CHANGE = true
            GROUP_ID = 2; GROUP_NAME = "Категория"
        }

        if(dps.getValue("categoryID") != null &&
                Long.parseLong(dps.getValueAsString("categoryID")) != db.oneLong("select id from categories where name = 'Root'"))
        {
            projectID = helper.qRec("""SELECT cat.name FROM categories cat
                    INNER JOIN classifications pcls ON pcls.recordID = CONCAT('projectCategory.', ?)
                      AND cat.ID = pcls.categoryID""", Long.parseLong(dps.getValueAsString("categoryID"))).getString("name")

            dpsHelper.addDpForColumns(dps, meta.getEntity("publication2project"), projectColumns, context.getOperationParams())

            dps.edit("status") { CSS_CLASSES = "col-lg-6" }
            dps.edit("importance") { CSS_CLASSES = "col-lg-6" }
            dps.edit("keyWords") { EXTRA_ATTRS = [["inputType", "textArea"], ["rows", "1"]] as String[][] }

            for (def columnName : projectColumns) {
                dps.edit(columnName) { GROUP_ID = 1; GROUP_NAME = "Поля для проекта " + projectID }
            }
        }

        if(context.records.length == 0 && dps.getValue("PMID") != null)
        {
            //TODO add methods for validation with ignore Exception isValid(), rename isError to getStatus()
            try {
                validator.checkErrorAndCast(dps.getProperty("PMID"))
            } catch (IllegalArgumentException ignore) {
            }

            if(!validator.isError(dps.getProperty("PMID")) && projectID != null &&
                    pmidExistInProject((Long)dps.getValue("PMID"), projectID))
            {
                validator.setError(dps.getProperty("PMID"), "Публикация с заданным PMID уже есть в категории " + projectID)
            }
        }

        return DpsUtils.setValues(dps, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        Long PMID = (Long)dps.getValue("PMID")

        String inputType = dps.remove("inputType")
        Long categoryID = (Long)dps.remove("categoryID")

        def projectInfo = extractProjectInfo(dps)

        RecordModel<Long> publicationRecord
        if(PMID != null)publicationRecord = database.publications.getBy([PMID: PMID])

        Long publicationID
        if(publicationRecord == null)
        {
            publicationID = database.publications.add(dps)

            if(inputType == "PubMed")
            {
                if( PMID != null )
                {
                    medlineImport.fill("publications", publicationID)
                }
            }
        }
        else
        {
            publicationID = publicationRecord.getPrimaryKey()
        }

        categoriesHelper.addWithParentCategories(categoryID, "publications", publicationID)

        projectInfo.add("publicationID"){TYPE = Long; value = publicationID}
        database.publication2project.add(projectInfo)

        addRedirectParams(context.operationParams)
        removeRedirectParam("pmid")
    }

    GDynamicPropertySetSupport extractProjectInfo(DynamicPropertySet dps)
    {
        def projectInfo = new GDynamicPropertySetSupport()
        for (def columnName : projectColumns) {
            projectInfo.add(dps.getProperty(columnName))
            dps.remove(columnName)
        }
        projectInfo.add("projectID"){value = projectID}

        return projectInfo
    }

    boolean pmidExistInProject(Long PMID, String projectID)
    {
        return db.oneLong("SELECT count(1) FROM publications p \n" +
                "INNER JOIN classifications cls \n" +
                "     ON cls.recordID = CONCAT('publications.', p.ID) \n" +
                "    AND categoryID = (SELECT id FROM categories WHERE name = ?)\n" +
                "WHERE p.PMID = ?", projectID, PMID) > 0
    }
}
