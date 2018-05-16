package publications

import com.developmentontheedge.be5.operation.support.GOperationSupport
import com.developmentontheedge.be5.operation.OperationResult
import javax.inject.Inject
import ru.biosoft.biblio.services.MedlineImport


class FillFromMedline extends GOperationSupport
{
    @Inject MedlineImport medlineImport

    @Override
    void invoke(Object parameters) throws Exception
    {
        Long[] records = context.getRecords()

        for(int i=0; i< records.length; i++)
        {
            medlineImport.fill(getInfo().getEntityName(), records[i])
        }
    }
}
