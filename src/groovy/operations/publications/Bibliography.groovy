package publications

import com.developmentontheedge.be5.operation.OperationResult
import com.developmentontheedge.be5.operation.TransactionalOperation
import com.developmentontheedge.be5.operations.DeleteOperation


class Bibliography extends DeleteOperation implements TransactionalOperation
{
    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {

    }

    @Override
    void invoke(Object parameters) throws Exception
    {
//        RecordModel record = injector.get(DatabaseModel.class).getEntity(entity).get(ID);
//
//        String filename    = record.getValueAsString("name");
//        String contentType = record.getValueAsString(typeColumn);
//        Object data        = record.getValue(dataColumn);
//        String charset = MoreObjects.
//                firstNonNull(charsetColumn != null ? record.getValueAsString(charsetColumn) : null, Charsets.UTF_8.name());
//
//

        setResult(OperationResult.finished())
    }
}
