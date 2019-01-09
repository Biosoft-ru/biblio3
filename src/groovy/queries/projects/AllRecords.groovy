package projects

import com.developmentontheedge.be5.base.FrontendConstants
import com.developmentontheedge.be5.base.util.HashUrl
import com.developmentontheedge.be5.query.model.CellModel
import com.developmentontheedge.be5.query.model.beans.QRec
import com.developmentontheedge.be5.server.queries.support.QueryExecutorSupport
import groovy.transform.TypeChecked
import ru.biosoft.biblio.util.BioStore

import javax.inject.Inject

@TypeChecked
class AllRecords extends QueryExecutorSupport
{
    @Inject BioStore bioStore

    @Override
    List<QRec> execute()
    {
        addColumns("Name", "Permissions")

        def projects = bioStore.getProjectList()

        for (def project : projects)
        {
            def cell1 = new CellModel(project.projectName).option("link", "url",
                    new HashUrl(FrontendConstants.TABLE_ACTION, "_project_users_", "All records")
                            .named("projectName", project.projectName).toString())

            def cell2 = new CellModel(project.getPermissionsStr())

            addRow(cells(cell1, cell2))
        }

        return table()
    }
}
