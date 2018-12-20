package projectusers

import com.developmentontheedge.be5.server.queries.support.DpsTableBuilderSupport
import com.developmentontheedge.beans.DynamicPropertySet
import groovy.transform.TypeChecked
import ru.biosoft.biblio.util.BioStore

import javax.inject.Inject

@TypeChecked
class AllRecords extends DpsTableBuilderSupport
{
    @Inject BioStore bioStore

    @Override
    List<DynamicPropertySet> getTableModel()
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
