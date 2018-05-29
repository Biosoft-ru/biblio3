package projects

import com.developmentontheedge.be5.base.FrontendConstants
import com.developmentontheedge.be5.queries.support.TableBuilderSupport
import com.developmentontheedge.be5.query.model.CellModel
import com.developmentontheedge.be5.query.model.TableModel
import com.developmentontheedge.be5.base.util.HashUrl
import ru.biosoft.biblio.util.BioStore


class AllRecords extends TableBuilderSupport
{
    @Override
    TableModel getTableModel()
    {
        addColumns("Name", "Permissions")

        def projects = BioStore.getProjectList()

        for (def project : projects)
        {
            List<CellModel> cells = new ArrayList<CellModel>()

            cells.add(new CellModel(project.projectName).add("link", "url",
                    new HashUrl(FrontendConstants.TABLE_ACTION, "_project_users_", "All records")
                            .named("projectName", project.projectName).toString()))

            cells.add(new CellModel(project.getPermissionsStr()))

            addRow(cells)
        }

        return table(columns, rows)
    }
}
