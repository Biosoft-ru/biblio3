package publications

import com.developmentontheedge.be5.base.FrontendConstants
import com.developmentontheedge.be5.databasemodel.RecordModel
import com.developmentontheedge.be5.base.model.GDynamicPropertySetSupport
import com.developmentontheedge.be5.operation.model.OperationResult
import com.developmentontheedge.be5.operation.model.TransactionalOperation
import groovy.transform.TypeChecked

@TypeChecked
class EditPublication extends InsertPublication implements TransactionalOperation
{
    RecordModel publication2projectRecord
    RecordModel publicationRec

    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        String cat = context.getParams().get(FrontendConstants.CATEGORY_ID_PARAM)

        if(cat != null) {
            def rec = queries.qRec("""SELECT cat.name FROM categories cat
                    INNER JOIN classifications pcls ON pcls.recordID = CONCAT('projectCategory.', ?)
                      AND cat.ID = pcls.categoryID""", cat)
            if(rec != null)projectID = rec.getString("name")
        }
        if(projectID == null) {
            setResult(OperationResult.error("Перейдите в категорию"))
            return null
        }

        publicationRec = database["publications"].get(context.record)

        publication2projectRecord = database["publication2project"].getBy([
                publicationID: publicationRec.getPrimaryKey(),
                projectID    : projectID
        ])

        def presets = [:]
        presets << publicationRec.asMap()
        if(publication2projectRecord != null)presets << publication2projectRecord.asMap()
        presets << presetValues
        presets << [
                inputType: presetValues.getOrDefault("inputType", publicationRec.getValue("PMID") != null ? "PubMed" : "manually")
        ]

        params = (GDynamicPropertySetSupport)super.getParameters(presets)

        params.edit("inputType")  { READ_ONLY = true }
        params.edit("categoryID") { READ_ONLY = true }

        return params
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        if(params.getProperty("PMID") != null && params.getValue("PMID") != publicationRec.getValue("PMID")
                && pmidExistInProject((Long)params.getValue("PMID"), projectID))
        {
            validator.setError(params.getProperty("PMID"), "Публикация с заданным PMID уже есть в категории " + projectID)
            return
        }

        params.remove("inputType")
        params.remove("categoryID")

        def projectInfo = extractProjectInfo(params)
        projectInfo.add("publicationID"){TYPE = Long; value = context.records[0]}

        database["publications"].set(context.record, params)

        if(publication2projectRecord == null){
            database["publication2project"].add(projectInfo)
        }else{
            publication2projectRecord.update(projectInfo.asMap())
        }
    }
}
