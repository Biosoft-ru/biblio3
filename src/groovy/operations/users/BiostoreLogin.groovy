package users

import com.developmentontheedge.be5.api.FrontendConstants
import com.developmentontheedge.be5.api.helpers.UserHelper
import com.developmentontheedge.be5.inject.Inject
import com.developmentontheedge.be5.modules.core.operations.users.Login
import com.developmentontheedge.be5.operation.OperationResult
import com.developmentontheedge.be5.operation.OperationStatus
import com.google.common.collect.ImmutableList
import ru.biosoft.biostoreapi.DefaultConnectionProvider

import static java.util.stream.Collectors.toList
import static ru.biosoft.biblio.BiblioUtils.BIOSTORE_PROJECTS
import static ru.biosoft.biblio.BiblioUtils.BIOSTORE_SERVER_NAME


class BiostoreLogin extends Login
{
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
            if(dps.getValueAsString("user_pass") == null)
            {
                validator.setError(dps.getProperty("user_pass"), "Пароль не может быть пустым")
                return
            }
            super.invoke(parameters)
        }

        if(getStatus() == OperationStatus.ERROR || dps.getValueAsString("user_name") == null)
        {
            DefaultConnectionProvider provider = new DefaultConnectionProvider(BIOSTORE_SERVER_NAME)

            def user_name = dps.getValueAsString("user_name") == null ? "" : dps.getValueAsString("user_name")
            def user_pass = dps.getValueAsString("user_pass") == null ? "" : dps.getValueAsString("user_pass")

            try
            {
                def projects = provider.getProjectList(user_name, user_pass)
                List<String> projectNames = projects.stream().map({p -> p.getProjectName()}).collect(toList());

                def roles = ImmutableList.of("Annotator")

                userHelper.saveUser(user_name, roles, roles, meta.getLocale(null), request.getRemoteAddr(), session)

                session.set(BIOSTORE_PROJECTS, projectNames)

                setResult(OperationResult.finished(null, FrontendConstants.UPDATE_USER_INFO))
            }
            catch (SecurityException e)
            {
                setResult(OperationResult.error(e))
            }
        }
        else
        {
            session.set(BIOSTORE_PROJECTS, Collections.emptyList())
        }
    }
}
