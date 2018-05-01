package publications

import com.developmentontheedge.be5.api.FrontendConstants
import com.developmentontheedge.be5.inject.Inject
import com.developmentontheedge.be5.model.QRec
import com.developmentontheedge.be5.model.beans.GDynamicPropertySetSupport
import com.developmentontheedge.be5.operation.OperationResult
import com.developmentontheedge.be5.operation.TransactionalOperation
import com.developmentontheedge.be5.operations.DeleteOperation
import ru.biosoft.biblio.services.BiblioCategoryService


class DeletePublication extends DeleteOperation implements TransactionalOperation
{
    @Inject BiblioCategoryService categoryService

    QRec projectCategoryRec

    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        def dps = new GDynamicPropertySetSupport()
        def cat = context.getOperationParams().get(FrontendConstants.CATEGORY_ID_PARAM)

        if(cat != null) {
            projectCategoryRec = helper.qRec("""SELECT cat.ID, cat.name FROM categories cat
                    INNER JOIN classifications pcls ON pcls.recordID = CONCAT('projectCategory.', ?)
                      AND cat.ID = pcls.categoryID""", cat)
        }

        if(projectCategoryRec == null)
        {
            setResult(OperationResult.error("Перейдите в категорию"))
            return null
        }
        else
        {
            return dpsHelper.addLabelRaw(dps, "Удалить публикации из проекта <b>" + projectCategoryRec.getString("name") + "</b>?")
        }
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        for (Long id : (Long[])context.records)
        {
            categoryService.removeWithChildCategories(projectCategoryRec.getLong("ID"), id)
            database.publication2project.remove([
                    projectID    : projectCategoryRec.getString("name"),
                    publicationID: id
            ])

            if(db.getLong("SELECT count(1) FROM publication2project WHERE publicationID = ?", id) == 0)
            {
                super.invoke(parameters)
            }
        }

        setResult(OperationResult.finished())
    }
}
