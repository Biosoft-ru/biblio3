package citations

import com.developmentontheedge.be5.databasemodel.RecordModel
import com.developmentontheedge.be5.model.Base64File
import com.developmentontheedge.be5.operation.TransactionalOperation
import com.developmentontheedge.be5.operation.support.GOperationSupport
import ru.biosoft.biblio.services.citeproc.StyleService

import javax.inject.Inject


class Insert extends GOperationSupport implements TransactionalOperation
{
    @Inject StyleService styleService

    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        //only for not Dependent
        dps.add("file", "Файл") {
            TYPE = Base64File
        }

        return dpsHelper.setValues(dps, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        def file = (Base64File) dps.$file

        def name = file.name.replace('.csl', '')

        RecordModel<Long> rec = database.citations.getBy([name: name])

        Long id
        if (rec == null){
            id = styleService.addStyle(name, new String(file.data), null)
        }else{
            id = rec.getPrimaryKey()
        }

        database.citation2user.add([
                citationID: id,
                user_name: userInfo.getUserName()
        ])

    }
}
