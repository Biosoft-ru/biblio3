package projects

import com.developmentontheedge.be5.operation.GOperationSupport
import com.developmentontheedge.be5.operation.TransactionalOperation
import ru.biosoft.biblio.BioStore


class Insert extends GOperationSupport implements TransactionalOperation
{
    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        dps.add("name", "Категория")
        dps.add("permission", "Permission")

        dps.add("user_name", "Логин")
        dps.add("user_pass", "Пароль")

        return dpsHelper.setValues(dps, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        String name = dps.getValueAsString("name")
        int permission = (int)dps.getValue("permission")

        if(database.categories.count([name: name]) > 0)
        {
            validator.setError(dps.getProperty("name"), "already exists")
            return
        }

        //BioStore.createProject(name, 3)
        BioStore.api.createProjectWithPermissions(dps.getValueAsString("user_name"), dps.getValueAsString("user_pass"),
                name, permission)

        long parentID = (Long)database.categories.get([name: "Root"]).getValue("ID")

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
}
