package projectusers

import com.developmentontheedge.be5.server.queries.support.TableBuilderSupport
import com.developmentontheedge.be5.query.model.TableModel
import groovy.transform.TypeChecked
import ru.biosoft.biblio.util.BioStore

import javax.inject.Inject


@TypeChecked
class AllRecords extends TableBuilderSupport
{
    @Inject BioStore bioStore

    @Override
    TableModel getTableModel()
    {
        addColumns("User", "Role")

        def projectUsers = bioStore.getProjectUsers(parameters.get("projectName").toString())

        for (def projectUser : projectUsers)
        {
            addRow(projectUser.user, cells(projectUser.user, projectUser.role))
        }

        return table(columns, rows, true)
    }
}
