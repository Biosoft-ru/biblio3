package projectusers

import com.developmentontheedge.be5.operation.TransactionalOperation
import com.developmentontheedge.be5.operations.support.GOperationSupport
import ru.biosoft.biblio.util.BioStore


class Insert extends GOperationSupport implements TransactionalOperation
{
    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        dps.add("projectName", "projectName") {
            READ_ONLY = true
            value = (String)context.getOperationParams().get("projectName")
        }
        dps.add("email", "Email")

        return dpsHelper.setValues(dps, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        BioStore.addUserToProject(dps.getValueAsString("email"), dps.getValueAsString("projectName"))
    }

}
