package publications

import com.developmentontheedge.be5.env.Inject
import com.developmentontheedge.be5.operation.GOperationSupport
import com.developmentontheedge.be5.operation.TransactionalOperation
import com.developmentontheedge.be5.util.Utils
import ru.biosoft.biblio.MedlineImport


class EditProjectInfo extends GOperationSupport implements TransactionalOperation
{
    @Inject MedlineImport medlineImport

    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        dpsHelper.addDpExcludeAutoIncrement(dps, meta.getEntity("publication2project"), context.getOperationParams())

        dps.edit("publicationID") {
            READ_ONLY = true
            value = context.records[0]
        }

        dps.edit("projectID") {
            READ_ONLY = true
            value = qRec.beSql("""SELECT cat.name FROM categories cat
                    INNER JOIN classifications cls ON cls.recordID = CONCAT('publications.', ?)
                    INNER JOIN classifications pcls 
                       ON pcls.recordID = CONCAT('projectCategory.', cls.categoryID)
                      AND cat.ID = pcls.categoryID""", Long.parseLong(context.records[0])).getString("name")
        }

        return dpsHelper.setValues(dps, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {

    }

}
