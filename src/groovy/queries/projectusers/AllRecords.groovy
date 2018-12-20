package projectusers

import com.developmentontheedge.be5.server.queries.support.QueryExecutorSupport
import com.developmentontheedge.beans.DynamicPropertySet
import groovy.transform.TypeChecked
import ru.biosoft.biblio.util.BioStore

import javax.inject.Inject

@TypeChecked
class AllRecords extends QueryExecutorSupport
{
    @Inject BioStore bioStore

    @Override
    List<DynamicPropertySet> execute()
    {
        addColumns("User", "Role")

        def projectUsers = bioStore.getProjectUsers(parameters.get("projectName").toString())

        for (def projectUser : projectUsers)
        {
            addRow(projectUser.user, cells(projectUser.user, projectUser.role))
        }

        return table(true)
    }
}
