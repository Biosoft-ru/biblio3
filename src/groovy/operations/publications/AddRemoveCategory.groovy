package publications

import com.developmentontheedge.be5.inject.Inject
import com.developmentontheedge.be5.operation.GOperationSupport
import com.developmentontheedge.be5.operation.OperationResult
import com.developmentontheedge.be5.util.Utils
import com.google.common.collect.ObjectArrays
import ru.biosoft.biblio.services.BiblioCategoryService

import static com.developmentontheedge.be5.api.FrontendConstants.CATEGORY_ID_PARAM


class AddRemoveCategory extends GOperationSupport
{
    @Inject BiblioCategoryService categoryService

    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        if( context.records.length == 0 )
            return null

        dps.add("categoryID", "Category") {
            TYPE = Long
            TAG_LIST_ATTR = helper.getTagsFromCustomSelectionView("categories", "Children Of Root")
            value = presetValues.getOrDefault("categoryID", context.operationParams.get(CATEGORY_ID_PARAM))
        }

        dps.add("operationType", "Operation") {
            TAG_LIST_ATTR = [ ["Add",    "add to this category and parents"],
                              ["Remove", "remove from this category and children"] ] as String[][]
        }

        return dpsHelper.setValues(dps, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        if( context.records.length == 0 )
        {
            setResult(OperationResult.error("No records were selected"))
            return
        }

        Long categoryID = (Long)dps.getValue( "categoryID" )

        //String catList = Utils.toInClause( categories, isNumericCategoryID );

        String entity = getInfo().getEntityName()
        String pk = getInfo().getPrimaryKey()

        if( "Add".equals( dps.getValue( "operationType" ) ) )
        {
            categoryService.addWithParentCategories(categoryID, context.records[0])
//            db.insert("INSERT INTO classifications (recordID, categoryID)" +
//                    "SELECT CONCAT('"+entity+".', e."+pk+"), c.ID " +
//                    "FROM "+entity+" e, categories c " +
//                    "WHERE e."+pk+" IN " + Utils.inClause(context.records.length) +
//                    "  AND c.ID     IN " + Utils.inClause(categories.size()),
//                    ObjectArrays.concat(
//                            Utils.changeTypes(context.records, meta.getColumnType(getInfo().getEntity(), pk)),
//                            categories.toArray(),
//                            Object.class));


//            out.write( localizedMessage( "Category" ) + " " + catList + " " +
//                    localizedMessage( "was added to" ) + " <b>" + updateCount + "</b> " +
//                    localizedMessage( "records in" ) + " <i>" + entity + "</i><br />" );
        }
        else
        {
            categoryService.removeWithChildCategories(categoryID, context.records[0])
//            db.update("DELETE FROM classifications " +
//                    "WHERE recordID   IN " + Utils.inClause(context.records.length) +
//                    "  AND categoryID IN " + Utils.inClause(categories.size()),
//                    ObjectArrays.concat(
//                            Utils.addPrefix(context.records, entity + "."),
//                            categories.toArray(),
//                            Object.class));
        }
//        out.write( localizedMessage( "Category" ) + " " + catList + " " +
//                localizedMessage( "was removed from" ) + " <b>" + updateCount + "</b> " +
//                localizedMessage( "records in" ) + " <i>" + entity + "</i><br />" );
    }


}
