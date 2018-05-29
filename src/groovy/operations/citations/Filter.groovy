package citations

import com.developmentontheedge.be5.api.helpers.FilterHelper
import com.developmentontheedge.be5.model.beans.GDynamicPropertySetSupport
import com.developmentontheedge.be5.operation.model.TransactionalOperation
import com.developmentontheedge.be5.operations.FilterOperation

import javax.inject.Inject


class Filter extends FilterOperation implements TransactionalOperation
{
    @Inject private FilterHelper filterHelper

    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        def dps = dpsHelper.addDpForColumns(new GDynamicPropertySetSupport(),
                getInfo().getEntity(), ["name", "title", "format", "parent"], context.operationParams)

        dps.edit("format"){
            TAG_LIST_ATTR = helper.getTagsFromCustomSelectionView("citations", "formats")
        }


        dps.add("category", "Category"){
            TAG_LIST_ATTR = helper.getTagsFromSelectionView("citationCategories")
        }

        return filterHelper.processFilterParams(dps, presetValues, context.getOperationParams())
    }

}
