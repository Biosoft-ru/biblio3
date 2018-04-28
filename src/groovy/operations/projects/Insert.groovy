package projects

import com.developmentontheedge.be5.operation.GOperationSupport
import com.developmentontheedge.be5.operation.TransactionalOperation


class Insert extends GOperationSupport implements TransactionalOperation
{
    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        dps.add("name", "Категория")

        return dpsHelper.setValues(dps, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        String name = dps.getValueAsString("name")

        if(database.categories.count([name: name]) > 0)
        {
            validator.setError(dps.getProperty("name"), "already exists")
            return
        }

        long parentID = (Long)database.categories.get([name: "Root"]).getValue("ID")

        def ID = database.categories.add([
                entity: 'publications',
                name: name,
                parentID: parentID
        ])

        database.classifications.add([
                recordID  : "projectCategory." + ID,
                categoryID: ID
        ])
    }
}
