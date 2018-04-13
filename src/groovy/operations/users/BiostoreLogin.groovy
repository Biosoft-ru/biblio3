package users

import com.developmentontheedge.be5.api.FrontendConstants
import com.developmentontheedge.be5.api.helpers.UserHelper
import com.developmentontheedge.be5.env.Inject
import com.developmentontheedge.be5.modules.core.operations.users.Login
import com.developmentontheedge.be5.operation.OperationResult
import com.developmentontheedge.be5.operation.OperationStatus
import com.google.common.collect.ImmutableList
import ru.biosoft.biostoreapi.DefaultConnectionProvider

import java.util.stream.Collectors

import static ru.biosoft.biblio.Utils.BIOSTORE_PROJECTS


class BiostoreLogin extends Login
{
    private String serverName = "biblio.biouml.org"

    @Inject UserHelper userHelper

    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        super.getParameters(presetValues)

        dps.edit("user_name") {CAN_BE_NULL = true}
        dps.edit("user_pass") {CAN_BE_NULL = true}

        return dps
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        if(dps.getValueAsString("user_name") != null)
        {
            super.invoke(parameters)
        }

        if(getStatus() == OperationStatus.ERROR || dps.getValueAsString("user_name") == null)
        {
            DefaultConnectionProvider provider = new DefaultConnectionProvider(serverName)

            def user_name = dps.getValueAsString("user_name") == null ? "" : dps.getValueAsString("user_name")
            def user_pass = dps.getValueAsString("user_pass") == null ? "" : dps.getValueAsString("user_pass")

            try
            {
                def projectList = new ArrayList(provider.getProjectList(user_name, user_pass))
//                projectList = new ArrayList()
//                projectList.add("foo")//todo remove

                def roles = ImmutableList.of("Annotator")

                userHelper.saveUser(user_name, roles, roles, meta.getLocale(null), request.getRemoteAddr(), session)

                session.set(BIOSTORE_PROJECTS, projectList)

                setResult(OperationResult.finished(null, FrontendConstants.UPDATE_USER_INFO))
            }
            catch (SecurityException e)
            {
                setResult(OperationResult.error(e.getMessage()))
            }
        }
    }
}
