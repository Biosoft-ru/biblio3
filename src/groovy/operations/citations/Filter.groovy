package citations

import com.developmentontheedge.be5.groovy.GDynamicPropertySetSupport
import com.developmentontheedge.be5.operation.TransactionalOperation
import com.developmentontheedge.be5.server.operations.FilterOperation
import com.developmentontheedge.beans.DynamicPropertySet

class Filter extends FilterOperation implements TransactionalOperation
{
    @Override
    DynamicPropertySet getFilterParameters(Map<String, Object> presetValues) throws Exception
    {
        def dps = dpsHelper.addDpForColumns(new GDynamicPropertySetSupport(),
                getInfo().getEntity(), ["name", "title", "format", "parent"], context.params)

        dps.edit("format"){
            TAG_LIST_ATTR = queries.getTagsFromCustomSelectionView("citations", "formats")
        }

        dps.add("category", "Category"){
            TAG_LIST_ATTR = queries.getTagsFromSelectionView("citationCategories")
        }

        return dps
    }

}
