package publications

import com.developmentontheedge.be5.model.beans.GDynamicPropertySetSupport
import com.developmentontheedge.be5.operation.TransactionalOperation
import groovy.transform.TypeChecked


@TypeChecked
class EditPublication extends InsertPublication implements TransactionalOperation
{
    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        def params = new HashMap<String, Object>(presetValues)
        def rec = database.publications[context.records[0]]
        params << rec.asMap()
        params << [
                inputType: presetValues.getOrDefault("inputType", rec.getValue("PMID") != null ? "PubMed" : "manually")
        ]

        dps = (GDynamicPropertySetSupport)super.getParameters(params)

        return dps//dpsHelper.setValues(dps, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        super.invoke(parameters)
    }
}
