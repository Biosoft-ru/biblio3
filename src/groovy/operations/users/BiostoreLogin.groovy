package users

import com.developmentontheedge.be5.server.FrontendActions
import com.developmentontheedge.be5.server.helpers.UserHelper
import com.developmentontheedge.be5.modules.core.operations.users.Login
import com.developmentontheedge.be5.operation.model.OperationResult
import com.developmentontheedge.be5.operation.model.OperationStatus
import com.google.common.collect.ImmutableList
import groovy.transform.TypeChecked
import ru.biosoft.biblio.util.BioStore
import javax.inject.Inject

import static ru.biosoft.biblio.util.BioStore.BIOSTORE_PROJECTS
import static ru.biosoft.biblio.util.BioStore.BIOSTORE_TOKEN


@TypeChecked
class BiostoreLogin extends Login
{
    @Inject UserHelper userHelper
    @Inject BioStore bioStore

    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        super.getParameters(presetValues)

        params.edit("user_name") {CAN_BE_NULL = true}
        params.edit("user_pass") {CAN_BE_NULL = true}

        return params
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        if(params.getValueAsString("user_name") != null)
        {
            if(params.getValueAsString("user_pass") == null)
            {
                validator.setError(params.getProperty("user_pass"), "Пароль не может быть пустым")
                return
            }
            super.invoke(parameters)
        }

        if(getStatus() == OperationStatus.ERROR || params.getValueAsString("user_name") == null)
        {
            def user_name = params.getValueAsString("user_name") == null ? "" : params.getValueAsString("user_name")
            def user_pass = params.getValueAsString("user_pass") == null ? "" : params.getValueAsString("user_pass")

            try
            {
                def token = BioStore.api.getJWToken(user_name, user_pass)
                session[BIOSTORE_TOKEN] = token

                def roles = ImmutableList.of("Annotator")

                userHelper.saveUser(user_name, roles, roles, meta.getLocale(Locale.US), request.getRemoteAddr())

                bioStore.loadProjectListToSession()

                setResult(OperationResult.finished(null,
                        FrontendActions.updateUserAndOpenDefaultRoute(loginService.getUserInfoModel())))
            }
            catch (SecurityException e)
            {
                setResult(OperationResult.error(e.getMessage(), e))
            }
        }
        else
        {
            session.set(BIOSTORE_PROJECTS, Collections.emptyList())
        }
    }
}
