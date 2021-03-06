package publications

import com.developmentontheedge.be5.FrontendConstants
import com.developmentontheedge.be5.query.model.beans.QRec
import com.developmentontheedge.be5.groovy.GDynamicPropertySetSupport
import com.developmentontheedge.be5.modules.core.services.impl.CategoriesHelper
import com.developmentontheedge.be5.operation.OperationResult
import com.developmentontheedge.be5.operation.TransactionalOperation
import com.developmentontheedge.be5.server.operations.DeleteOperation
import groovy.transform.TypeChecked

import javax.inject.Inject


@TypeChecked
class DeletePublication extends DeleteOperation implements TransactionalOperation
{
    @Inject CategoriesHelper categoriesHelper
    QRec projectCategoryRec

    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        def dps = new GDynamicPropertySetSupport()
        def cat = context.getParams().get(FrontendConstants.CATEGORY_ID_PARAM)

        if(cat != null) {
            projectCategoryRec = queries.qRec("""SELECT cat.ID, cat.name FROM categories cat
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
            categoriesHelper.removeWithChildCategories(projectCategoryRec.getLong("ID"), "publications", id)
            database["publication2project"].removeBy([
                    projectID    : projectCategoryRec.getString("name"),
                    publicationID: id
            ])

            if(db.oneLong("SELECT count(1) FROM publication2project WHERE publicationID = ?", id) == 0)
            {
                super.invoke(parameters)
            }
        }

        setResult(OperationResult.finished())
    }
}
