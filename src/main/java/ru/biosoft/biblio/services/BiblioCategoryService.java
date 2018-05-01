package ru.biosoft.biblio.services;

import com.developmentontheedge.be5.api.services.SqlService;
import com.developmentontheedge.be5.util.Utils;

import java.util.ArrayList;
import java.util.List;


public class BiblioCategoryService
{
    private final SqlService db;

    public BiblioCategoryService(SqlService db)
    {
        this.db = db;
    }

    public List<Long> getParentCategories(Long categoryID)
    {
        List<Long> categories = new ArrayList<>();
        Long cat = categoryID;

        while (cat != null) {
            categories.add(cat);

            cat = db.getLong("SELECT c1.parentID FROM categories c1 WHERE c1.ID = ?", cat);
        }

        return categories;
    }

    public List<Long> getChildCategories(Long categoryID)
    {
        List<Long> categories = new ArrayList<>();
        categories.add(categoryID);

        //bfs
        int i = 0;
        do {
            categories.addAll(db.selectListLong("SELECT id FROM categories c WHERE c.parentID = ?", categories.get(i)));
        }
        while (++i < categories.size());

        return categories;
    }

    public void removeWithChildCategories(Long categoryID, Long publicationID)
    {
        List<Long> categories = getChildCategories(categoryID);

        db.update("DELETE FROM classifications WHERE recordID = CONCAT('publications.', " + publicationID + ")" +
                "AND categoryID IN " + Utils.inClause(categories.size()), categories.toArray());
    }

    public void addWithParentCategories(Long categoryID, Long publicationID)
    {
        List<Long> categories = getParentCategories(categoryID);

        db.update("DELETE FROM classifications WHERE recordID = CONCAT('publications.', " + publicationID + ")" +
                "AND categoryID IN " + Utils.inClause(categories.size()), categories.toArray());

        db.insert("INSERT INTO classifications (recordID, categoryID)" +
                "SELECT CONCAT('publications.', " + publicationID + "), c.ID FROM categories c " +
                "WHERE id IN " + Utils.inClause(categories.size()), categories.toArray());
    }

}
