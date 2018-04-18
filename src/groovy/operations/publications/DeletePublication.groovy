package publications

import com.developmentontheedge.be5.api.FrontendConstants
import com.developmentontheedge.be5.env.Inject
import com.developmentontheedge.be5.model.beans.GDynamicPropertySetSupport
import com.developmentontheedge.be5.operation.OperationResult
import com.developmentontheedge.be5.operation.TransactionalOperation
import com.developmentontheedge.be5.operations.DeleteOperation
import ru.biosoft.biblio.BiblioHelper


class DeletePublication extends DeleteOperation implements TransactionalOperation
{
    @Inject BiblioHelper biblioHelper

    String cat
    String projectID

    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        def dps = new GDynamicPropertySetSupport()
        cat = context.getOperationParams().get(FrontendConstants.CATEGORY_ID_PARAM)

        if(cat != null) {
            def rec = qRec.of("""SELECT cat.name FROM categories cat
                    INNER JOIN classifications pcls ON pcls.recordID = CONCAT('projectCategory.', ?)
                      AND cat.ID = pcls.categoryID""", cat)
            if(rec != null)projectID = rec.getString("name")
        }

        if(projectID == null)
        {
            setResult(OperationResult.error("Перейдите в категорию"))
            return null
//            return dpsHelper.addLabelRaw(dps, "Удалить publications из <b>всех</b> доступных категорий " +
//                                    session.get(BiblioUtils.BIOSTORE_PROJECTS).toString() + "?")
        }
        else
        {
            return dpsHelper.addLabelRaw(dps, "Удалить публикации из категории <b>" + projectID + "</b>?")
        }
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        for (String id : context.records)
        {
            biblioHelper.deleteCategories(cat, Long.parseLong(id))
            database.publication2project.remove(["projectID": projectID, "publicationID": Long.parseLong(id)])
        }

        //todo check all classifications removed and remove
        //super.invoke(parameters)
    }
}
