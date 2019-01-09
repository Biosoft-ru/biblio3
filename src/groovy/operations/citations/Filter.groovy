package citations

import com.developmentontheedge.be5.server.helpers.FilterHelper
import com.developmentontheedge.be5.base.model.GDynamicPropertySetSupport
import com.developmentontheedge.be5.operation.TransactionalOperation
import com.developmentontheedge.be5.server.operations.FilterOperation

import javax.inject.Inject


class Filter extends FilterOperation implements TransactionalOperation
{
    @Inject private FilterHelper filterHelper

    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        def dps = dpsHelper.addDpForColumns(new GDynamicPropertySetSupport(),
                getInfo().getEntity(), ["name", "title", "format", "parent"], context.params)

        dps.edit("format"){
            TAG_LIST_ATTR = queries.getTagsFromCustomSelectionView("citations", "formats")
        }


        dps.add("category", "Category"){
            TAG_LIST_ATTR = queries.getTagsFromSelectionView("citationCategories")
        }

        return filterHelper.processFilterParams(dps, presetValues, context.getParams())
    }

}
