package projects

import com.developmentontheedge.be5.operation.OperationResult
import com.developmentontheedge.be5.server.operations.support.GOperationSupport
import com.developmentontheedge.be5.operation.TransactionalOperation
import com.developmentontheedge.be5.databasemodel.util.DpsUtils
import com.developmentontheedge.be5.util.Utils
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
        params.add("name", "Name")
        params.add("permissions", "Permissions") {
            TAG_LIST_ATTR = [[PERMISSION_INFO, "Info"], [PERMISSION_READ, "Read"], [PERMISSION_WRITE, "Write"],
                             [PERMISSION_DELETE, "Delete"], [PERMISSION_ADMIN, "Admin"], [PERMISSION_ALL, "All"]] as String[][]
            MULTIPLE_SELECTION_LIST = true
        }

        return DpsUtils.setValues(params, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        String name = params.getValueAsString("name")
        int permission = getPermission((Integer[]) Utils.changeTypes((String[])params.getValue("permissions"), Integer.class))

        long parentID = (Long)database["categories"].getBy([name: "Root"]).getValue("ID")

        if(database["categories"].count([parentID: parentID, name: name]) > 0)
        {
            validator.setError(params.getProperty("name"), "already exists")
            return
        }

        try{
            bioStore.createProjectWithPermissions(name, permission)
            bioStore.loadProjectListToSession()
        }catch (SecurityException e){
            setResult(OperationResult.error(e.getMessage(), e))
        }

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
