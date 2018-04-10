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

        return dps
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        if(dps.getValue("parentID") == null)
        {
            def projects = (List<String>)session.get(BIOSTORE_PROJECTS)
            if(!projects.contains(dps.getValueAsString("name")))
            {
                validator.setError(dps.getProperty("name"), "root category must be in " + projects.toString())
                return
            }
        }

        database.categories.add(dps)
    }
}
