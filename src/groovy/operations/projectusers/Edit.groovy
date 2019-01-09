package projectusers

import com.developmentontheedge.be5.databasemodel.util.DpsUtils
import com.developmentontheedge.be5.operation.OperationResult
import com.developmentontheedge.be5.operation.TransactionalOperation
import com.developmentontheedge.be5.server.operations.support.GOperationSupport
import groovy.transform.TypeChecked
import ru.biosoft.biblio.util.BioStore

import javax.inject.Inject

@TypeChecked
class Edit extends GOperationSupport implements TransactionalOperation
{
    @Inject BioStore bioStore

    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        params.add("projectName", "projectName") {
            READ_ONLY = true
            value = (String)context.getParams().get("projectName")
        }

        params.add("email", "Email") {
            READ_ONLY = true
            value = context.record
        }

        params.add("newRole", "Role") {
            TAG_LIST_ATTR = [["Administrator", "Administrator"], ["User", "User"] ] as String[][]
        }

        return DpsUtils.setValues(params, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        try{
            bioStore.changeUserRoleInProject(
                    params.getValueAsString("projectName"),
                    params.getValueAsString("email"),
                    params.getValueAsString("newRole"))
        }catch (SecurityException e){
            setResult(OperationResult.error(e.getMessage(), e))
        }
    }

}
