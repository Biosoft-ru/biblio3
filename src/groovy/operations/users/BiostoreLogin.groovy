package users

import com.developmentontheedge.be5.api.FrontendConstants
import com.developmentontheedge.be5.api.helpers.UserHelper
import com.developmentontheedge.be5.env.Inject
import com.developmentontheedge.be5.metadata.RoleType
import com.developmentontheedge.be5.modules.core.operations.users.Login
import com.developmentontheedge.be5.operation.OperationResult
import com.google.common.collect.ImmutableList
import ru.biosoft.biostoreapi.DefaultConnectionProvider

import java.util.stream.Collectors

import static ru.biosoft.biblio.Utils.BIOSTORE_PROJECTS
import static ru.biosoft.biblio.Utils.BIOSTORE_PROJECTS_SQL
import static ru.biosoft.biblio.Utils.SERVER_NAME


class BiostoreLogin extends Login
{
    @Inject UserHelper userHelper

    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        super.getParameters(presetValues)

        dps.edit("user_name") {CAN_BE_NULL = true}
        dps.edit("user_pass") {CAN_BE_NULL = true}

        dps.add("server_name", "Сервер") {CAN_BE_NULL = true; value = presetValues.get("server_name")}
        dps.moveTo("server_name", 0)

        return dps
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        def user_name = dps.getValueAsString("user_name") == null ? "" : dps.getValueAsString("user_name")
        def user_pass = dps.getValueAsString("user_pass") == null ? "" : dps.getValueAsString("user_pass")
        def server_name = dps.getValueAsString("server_name")

        if(server_name == null)
        {
            super.invoke(parameters)
        }
        else
        {
            DefaultConnectionProvider provider = new DefaultConnectionProvider(server_name)

            try
            {
                def projectList = provider.getProjectList(user_name, user_pass)

                def roles = ImmutableList.of("Annotator", RoleType.ROLE_ADMINISTRATOR, RoleType.ROLE_SYSTEM_DEVELOPER)//todo remove ROLE_ADMINISTRATOR

                userHelper.saveUser(user_name, roles, roles, meta.getLocale(null), request.getRemoteAddr(), session)

                session.set(BIOSTORE_PROJECTS, projectList)
                session.set(BIOSTORE_PROJECTS_SQL, projectList.stream()
                            .map({v -> "'${v}'"})
                            .collect(Collectors.joining(", ")))

                session.set(SERVER_NAME, server_name)

                setResult(OperationResult.finished(null, FrontendConstants.UPDATE_USER_INFO))
            }
            catch (SecurityException e)
            {
                setResult(OperationResult.error(e.getMessage()))
            }
        }
    }
}
