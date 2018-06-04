package projectusers

import com.developmentontheedge.be5.operation.model.TransactionalOperation
import com.developmentontheedge.be5.server.operations.support.GOperationSupport
import com.developmentontheedge.be5.base.util.DpsUtils
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
        dps.add("projectName", "projectName") {
            READ_ONLY = true
            value = (String)context.getOperationParams().get("projectName")
        }
        dps.add("email", "Email")

        return DpsUtils.setValues(dps, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        bioStore.addUserToProject(dps.getValueAsString("email"), dps.getValueAsString("projectName"))
    }

}
