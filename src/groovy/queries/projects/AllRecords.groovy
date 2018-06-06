package projects

import com.developmentontheedge.be5.base.FrontendConstants
import com.developmentontheedge.be5.server.queries.support.TableBuilderSupport
import com.developmentontheedge.be5.query.model.CellModel
import com.developmentontheedge.be5.query.model.TableModel
import com.developmentontheedge.be5.base.util.HashUrl
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
        addColumns("Name", "Permissions")

        def projects = bioStore.getProjectList()

        for (def project : projects)
        {
            List<CellModel> cells = new ArrayList<CellModel>()

            cells.add(new CellModel(project.projectName).option("link", "url",
                    new HashUrl(FrontendConstants.TABLE_ACTION, "_project_users_", "All records")
                            .named("projectName", project.projectName).toString()))

            cells.add(new CellModel(project.getPermissionsStr()))

            addRow(cells)
        }

        return table(columns, rows)
    }
}
