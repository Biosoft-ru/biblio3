package publications

import com.developmentontheedge.be5.databasemodel.RecordModel
import com.developmentontheedge.be5.base.model.GDynamicPropertySetSupport
import com.developmentontheedge.be5.modules.core.services.impl.CategoriesHelper
import com.developmentontheedge.be5.server.operations.support.GOperationSupport
import com.developmentontheedge.be5.operation.TransactionalOperation
import com.developmentontheedge.be5.databasemodel.util.DpsUtils
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
        params = new GDynamicPropertySetSupport()

        params.add("inputType", "Input type" ) {
            TAG_LIST_ATTR = [["PubMed","PubMed"],["manually","Вручную"]] as String[][]
            RELOAD_ON_CHANGE = true
            CSS_CLASSES   = "col-lg-4"
            value         = presetValues.getOrDefault("inputType", "PubMed")
        }

        if(params.getValueAsString("inputType") == "PubMed")
        {
            dpsHelper.addDpForColumns(params, getInfo().getEntity(), ["PMID"], context.getParams())

            params.edit("PMID") {
                CAN_BE_NULL = false
                RELOAD_ON_CHANGE = true
                value = presetValues.get("PMID")
            }
        }
        else
        {
            dpsHelper.addDpExcludeColumns(params, getInfo().getEntity(), ["ID"], context.getParams())

            params.edit("PMID") { HIDDEN = true }

            params.edit("authors") { EXTRA_ATTRS = [["inputType", "textArea"], ["rows", "1"]] as String[][] }
            params.edit("title") { EXTRA_ATTRS = [["inputType", "textArea"], ["rows", "1"]] as String[][] }

            params.edit("year") { CSS_CLASSES = "col-lg-3" }
            params.edit("month") { CSS_CLASSES = "col-lg-3" }
            params.edit("volume") { CSS_CLASSES = "col-lg-3" }
            params.edit("issue") { CSS_CLASSES = "col-lg-3" }
            params.edit("pageFrom") { CSS_CLASSES = "col-lg-3" }
            params.edit("pageTo") { CSS_CLASSES = "col-lg-3" }
            params.edit("language") { CSS_CLASSES = "col-lg-3" }
            params.edit("publicationType") { CSS_CLASSES = "col-lg-3" }

            params.edit("ref") { CSS_CLASSES = "col-lg-4" }
            params.edit("PMCID") { CSS_CLASSES = "col-lg-4" }
            params.moveTo("PMCID",3)
        }

        params.add("categoryID", "Category") {
            TYPE = Long
            TAG_LIST_ATTR = queries.getTagsFromCustomSelectionView("categories", "For publications")
            value = presetValues.getOrDefault("categoryID", context.params.get(CATEGORY_ID_PARAM))
            RELOAD_ON_CHANGE = true
            GROUP_ID = 2; GROUP_NAME = "Category"
        }

        if(params.getValue("categoryID") != null &&
                Long.parseLong(params.getValueAsString("categoryID")) != db.oneLong("select id from categories where name = 'Root'"))
        {
            projectID = queries.qRec("""SELECT cat.name FROM categories cat
                    INNER JOIN classifications pcls ON pcls.recordID = CONCAT('projectCategory.', ?)
                      AND cat.ID = pcls.categoryID""", Long.parseLong(params.getValueAsString("categoryID"))).getString("name")

            dpsHelper.addDpForColumns(params, meta.getEntity("publication2project"), projectColumns, context.getParams())

            params.edit("status") { CSS_CLASSES = "col-lg-6" }
            params.edit("importance") { CSS_CLASSES = "col-lg-6" }
            params.edit("keyWords") { EXTRA_ATTRS = [["inputType", "textArea"], ["rows", "1"]] as String[][] }

            for (def columnName : projectColumns) {
                params.edit(columnName) { GROUP_ID = 1; GROUP_NAME = "Project columns " + projectID }
            }
        }

        if(context.records.length == 0 && params.getValue("PMID") != null)
        {
            if(validator.validate(params.getProperty("PMID")) && projectID != null &&
                    pmidExistInProject((Long)params.getValue("PMID"), projectID))
            {
                validator.setError(params.getProperty("PMID"), "Публикация с заданным PMID уже есть в категории " + projectID)
            }
        }

        return DpsUtils.setValues(params, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        Long PMID = (Long)params.getValue("PMID")

        String inputType = params.remove("inputType")
        Long categoryID = (Long)params.remove("categoryID")

        def projectInfo = extractProjectInfo(params)

        RecordModel<Long> publicationRecord
        if(PMID != null)publicationRecord = database.publications.getBy([PMID: PMID])

        Long publicationID
        if(publicationRecord == null)
        {
            publicationID = database.publications.add(params)

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

        addRedirectParams(context.params)
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
