package citations

import com.developmentontheedge.be5.operation.model.TransactionalOperation
import com.developmentontheedge.be5.server.operations.support.GOperationSupport


class Delete extends GOperationSupport implements TransactionalOperation
{
    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        return null
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        for(def id : context.records)
        {
            database.citation2user.removeBy([
                    citationID: id,
                    user_name: userInfo.getUserName()
            ])
        }
    }
}
