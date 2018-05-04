package projectusers

import com.developmentontheedge.be5.query.TableBuilderSupport
import com.developmentontheedge.be5.query.impl.TableModel
import ru.biosoft.biblio.BioStore


class AllRecords extends TableBuilderSupport
{
    @Override
    TableModel getTableModel()
    {
        addColumns("User", "Role")

        def projectUsers = BioStore.getProjectUsers(parameters.get("projectName").toString())

        for (def projectUser : projectUsers)
        {
            addRow(cells(projectUser.user, projectUser.role))
        }

        return table(columns, rows)
    }
}
