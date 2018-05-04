package projects

import com.developmentontheedge.be5.operation.GOperationSupport
import com.developmentontheedge.be5.operation.TransactionalOperation
import com.developmentontheedge.be5.util.Utils
import ru.biosoft.biblio.BioStore
import ru.biosoft.biostoreapi.Permission


class Insert extends GOperationSupport implements TransactionalOperation
{
    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        dps.add("name", "Категория")
        dps.add("permissions", "Permissions") {
            TAG_LIST_ATTR = [[Permission.INFO, "Info"], [Permission.READ, "Read"], [Permission.WRITE, "Write"],
                             [Permission.DELETE, "Delete"], [Permission.ADMIN, "Admin"], [Permission.ALL, "All"]] as String[][]
            MULTIPLE_SELECTION_LIST = true
        }

        dps.add("user_name", "Логин")
        dps.add("user_pass", "Пароль") { PASSWORD_FIELD = true }

        return dpsHelper.setValues(dps, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        String name = dps.getValueAsString("name")
        int permission = getPermission((Integer[]) Utils.changeTypes((String[])dps.getValue("permissions"), Integer.class))

        if(database.categories.count([name: name]) > 0)
        {
            validator.setError(dps.getProperty("name"), "already exists")
            return
        }

        //BioStore.createProject(name, 3)
        BioStore.api.createProjectWithPermissions(dps.getValueAsString("user_name"), dps.getValueAsString("user_pass"),
                name, permission)

        BioStore.loadProjectListToSession()

        long parentID = (Long)database.categories.getBy([name: "Root"]).getValue("ID")

        def ID = database.categories.add([
                entity: 'publications',
                name: name,
                parentID: parentID
        ])

        database.classifications.add([
                recordID  : "projectCategory." + ID,
                categoryID: ID
        ])
    }

    private static int getPermission(Integer[] permissions)
    {
        int res = 0
        for(int permission: permissions) res = res | permission

        return res
    }
}
