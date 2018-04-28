package projects

import com.developmentontheedge.be5.query.TableBuilderSupport
import com.developmentontheedge.be5.query.impl.TableModel
import ru.biosoft.biblio.BioStore


class AllRecords extends TableBuilderSupport
{
    @Override
    TableModel getTableModel()
    {
        addColumns("Name", "Permissions")

        def projects = BioStore.getProjectList()

        for (def project : projects){
            addRow(cells(project.projectName, project.getPermissionsStr()))
        }

        return table(columns ,rows)
    }
}
