package categories

import com.developmentontheedge.be5.operation.GOperationSupport
import com.developmentontheedge.be5.operation.TransactionalOperation

import static ru.biosoft.biblio.BiblioUtils.BIOSTORE_PROJECTS


class Insert extends GOperationSupport implements TransactionalOperation
{
    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        dpsHelper.addDpForColumnsWithoutTags(dps, getInfo().getEntity(),
                ["name", "parentID", "description"], presetValues)

        dps.add("entity") {
            HIDDEN = true
            value = "publications"
        }
        dps.edit("parentID") {
            CAN_BE_NULL = false
            TAG_LIST_ATTR = helper.getTagsFromSelectionView("categories", [entity: "publications"])
        }

        return dps
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        long parentID = Long.parseLong(dps.getValueAsString("parentID"))
        String name = dps.getValueAsString("name")

        if(database.categories.count([name: name]) > 0)
        {
            validator.setError(dps.getProperty("name"), "already exists")
            return
        }

        boolean isProjectCategory = parentID == (Long)database.categories.get([name: "Root"]).getValue("ID")

        if(isProjectCategory)
        {
            def projects = (List<String>)session.get(BIOSTORE_PROJECTS)
            if(!projects.contains(name))
            {
                validator.setError(dps.getProperty("name"), "root category must be in " + projects.toString())
                return
            }
        }

        def ID = database.categories.add(dps)

        if(isProjectCategory)
        {
            database.classifications.add([
                    recordID  : "projectCategory." + ID,
                    categoryID: ID
            ])
        }
        else
        {
            def id = db.insert("""INSERT INTO classifications (recordID, categoryID)
                         SELECT CONCAT('projectCategory.', ?), c.categoryID FROM classifications c 
                         WHERE c.recordID = CONCAT('projectCategory.', ?)""", ID, parentID)

            Objects.requireNonNull(id, "projectCategory not added")
        }

    }
}
