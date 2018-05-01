package publications

import com.developmentontheedge.be5.api.FrontendConstants
import com.developmentontheedge.be5.databasemodel.RecordModel
import com.developmentontheedge.be5.model.beans.GDynamicPropertySetSupport
import com.developmentontheedge.be5.operation.OperationResult
import com.developmentontheedge.be5.operation.TransactionalOperation


class EditPublication extends InsertPublication implements TransactionalOperation
{
    RecordModel publication2projectRecord
    RecordModel publicationRec

    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        String cat = context.getOperationParams().get(FrontendConstants.CATEGORY_ID_PARAM)

        if(cat != null) {
            def rec = helper.qRec("""SELECT cat.name FROM categories cat
                    INNER JOIN classifications pcls ON pcls.recordID = CONCAT('projectCategory.', ?)
                      AND cat.ID = pcls.categoryID""", cat)
            if(rec != null)projectID = rec.getString("name")
        }
        if(projectID == null) {
            setResult(OperationResult.error("Перейдите в категорию"))
            return null
        }

        publicationRec = database.publications.get(Long.parseLong(context.records[0]))

        publication2projectRecord = database.publication2project.get([
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

        dps = (GDynamicPropertySetSupport)super.getParameters(presets)

        dps.edit("inputType")  { READ_ONLY = true }
        dps.edit("categoryID") { READ_ONLY = true }

        return dps
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        if(dps.getProperty("PMID") != null && dps.getValue("PMID") != publicationRec.getValue("PMID")
                && pmidExistInProject((Long)dps.getValue("PMID"), projectID))
        {
            validator.setError(dps.getProperty("PMID"), "Публикация с заданным PMID уже есть в категории " + projectID)
            return
        }

        dps.remove("inputType")
        dps.remove("categoryID")

        def projectInfo = extractProjectInfo(dps)
        projectInfo.add("publicationID"){TYPE = Long; value = context.records[0]}

        database.publications.set(context.records[0], dps)

        if(publication2projectRecord == null){
            database.publication2project.add(projectInfo)
        }else{
            publication2projectRecord.update(projectInfo.asMap())
        }
    }
}
