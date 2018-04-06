package publications

import com.developmentontheedge.be5.env.Inject
import com.developmentontheedge.be5.operation.GOperationSupport
import com.developmentontheedge.be5.operation.OperationResult
import ru.biosoft.biblio.MedlineImport


class FillFromMedline extends GOperationSupport
{
    @Inject MedlineImport medlineImport

    @Override
    void invoke(Object parameters) throws Exception
    {
        def records = context.records

        for(int i=0; i< records.length; i++)
        {
            medlineImport.fill(getInfo().getEntityName(), records[i])
        }

        setResult(OperationResult.finished())
    }
}
