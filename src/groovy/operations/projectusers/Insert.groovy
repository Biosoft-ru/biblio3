package projectusers

import com.developmentontheedge.be5.operation.model.OperationResult
import com.developmentontheedge.be5.operation.model.TransactionalOperation
import com.developmentontheedge.be5.server.operations.support.GOperationSupport
import com.developmentontheedge.be5.databasemodel.util.DpsUtils
import groovy.transform.TypeChecked
import ru.biosoft.biblio.util.BioStore

import javax.inject.Inject


@TypeChecked
class Insert extends GOperationSupport implements TransactionalOperation
{
    @Inject BioStore bioStore

    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        params.add("projectName", "projectName") {
            READ_ONLY = true
            value = (String)context.getParams().get("projectName")
        }
        params.add("email", "Email")

        return DpsUtils.setValues(params, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        try{
            bioStore.addUserToProject(params.getValueAsString("email"), params.getValueAsString("projectName"))
        }catch (SecurityException e){
            setResult(OperationResult.error(e.getMessage(), e))
        }
    }

}
