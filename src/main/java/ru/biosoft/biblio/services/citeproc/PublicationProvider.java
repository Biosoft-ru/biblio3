package ru.biosoft.biblio.services.citeproc;

import com.developmentontheedge.be5.api.services.SqlService;
import com.developmentontheedge.be5.databasemodel.RecordModel;
import com.developmentontheedge.be5.databasemodel.impl.DatabaseModel;
import de.undercouch.citeproc.ItemDataProvider;
import de.undercouch.citeproc.csl.CSLItemData;
import de.undercouch.citeproc.csl.CSLItemDataBuilder;
import de.undercouch.citeproc.csl.CSLType;


public class PublicationProvider implements ItemDataProvider
{
    private final DatabaseModel database;
    private final SqlService db;

    public PublicationProvider(DatabaseModel database, SqlService db)
    {
        this.database = database;
        this.db = db;
    }

    @Override
    public CSLItemData retrieveItem(String id)
    {
        RecordModel<Long> rec = database.<Long>getEntity("publications").get(Long.parseLong(id));

        return new CSLItemDataBuilder()
                .id(rec.getValueAsString("ID"))
                .type(CSLType.ARTICLE_JOURNAL)
                .title(rec.getValueAsString("title"))
                .author("John", "Smith")
                .issued(2013, 9, 6)
                .containerTitle("Dummy journal")
                .build();
    }

    public String[] getIds()
    {
        return db.stringArray("SELECT ID FROM publications");
    }
}