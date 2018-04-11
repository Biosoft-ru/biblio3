package categories

import com.developmentontheedge.be5.operation.GOperationSupport

import static ru.biosoft.biblio.Utils.BIOSTORE_PROJECTS


class Insert extends GOperationSupport
{
    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        dpsHelper.addDpForColumns(dps, getInfo().getEntity(),
                ["name", "parentID", "description"], context.operationParams, presetValues)

        dps.add("entity") {
            HIDDEN = true
            value = "publications"
        }
        dps.edit("parentID") {
            CAN_BE_NULL = false
        }

        return dps
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        if(dps.getValueAsString("parentID") == database.categories.get([name: "Root"]).getValueAsString("ID"))
        {
            def projects = (String[])session.get(BIOSTORE_PROJECTS)
            if(!projects.contains(dps.getValueAsString("name")) && dps.getValueAsString("name") != "foo")//TODO delete foo
            {
                validator.setError(dps.getProperty("name"), "root category must be in " + projects.toString())
                return
            }
        }

        database.categories.add(dps)
    }
}
