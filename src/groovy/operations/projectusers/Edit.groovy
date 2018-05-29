package projectusers

import com.developmentontheedge.be5.operation.model.TransactionalOperation
import com.developmentontheedge.be5.server.operations.support.GOperationSupport
import com.developmentontheedge.be5.base.util.DpsUtils
import ru.biosoft.biblio.util.BioStore


class Edit extends GOperationSupport implements TransactionalOperation
{
    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        dps.add("projectName", "projectName") {
            READ_ONLY = true
            value = (String)context.getOperationParams().get("projectName")
        }

        dps.add("email", "Email") {
            READ_ONLY = true
            value = context.record
        }

        dps.add("newRole", "Role") {
            TAG_LIST_ATTR = [["Administrator", "Administrator"], ["User", "User"] ] as String[][]
        }

        return DpsUtils.setValues(dps, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        BioStore.changeUserRoleInProject(
                dps.getValueAsString("projectName"),
                dps.getValueAsString("email"),
                dps.getValueAsString("newRole"))
    }

}
