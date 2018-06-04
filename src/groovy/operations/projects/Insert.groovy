package projects

import com.developmentontheedge.be5.server.operations.support.GOperationSupport
import com.developmentontheedge.be5.operation.model.TransactionalOperation
import com.developmentontheedge.be5.base.util.DpsUtils
import com.developmentontheedge.be5.base.util.Utils
import groovy.transform.TypeChecked
import ru.biosoft.biblio.util.BioStore

import javax.inject.Inject

import static ru.biosoft.biostoreapi.Project.*


@TypeChecked
class Insert extends GOperationSupport implements TransactionalOperation
{
    @Inject BioStore bioStore

    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        dps.add("name", "Name")
        dps.add("permissions", "Permissions") {
            TAG_LIST_ATTR = [[PERMISSION_INFO, "Info"], [PERMISSION_READ, "Read"], [PERMISSION_WRITE, "Write"],
                             [PERMISSION_DELETE, "Delete"], [PERMISSION_ADMIN, "Admin"], [PERMISSION_ALL, "All"]] as String[][]
            MULTIPLE_SELECTION_LIST = true
        }

        return DpsUtils.setValues(dps, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        String name = dps.getValueAsString("name")
        int permission = getPermission((Integer[]) Utils.changeTypes((String[])dps.getValue("permissions"), Integer.class))

        if(database["categories"].count([name: name]) > 0)
        {
            validator.setError(dps.getProperty("name"), "already exists")
            return
        }

        bioStore.createProjectWithPermissions(name, permission)

        bioStore.loadProjectListToSession()

        long parentID = (Long)database["categories"].getBy([name: "Root"]).getValue("ID")

        def ID = database["categories"].add([
                entity: 'publications',
                name: name,
                parentID: parentID
        ])

        database["classifications"].add([
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
