package projectusers

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
        addColumns("User", "Role")

        def projectUsers = bioStore.getProjectUsers(parameters.get("projectName").toString())

        for (def projectUser : projectUsers)
        {
            addRow(projectUser.user, cells(projectUser.user, projectUser.role))
        }

        return table(true)
    }
}
