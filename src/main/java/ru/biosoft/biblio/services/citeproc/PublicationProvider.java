package ru.biosoft.biblio.services.citeproc;

import com.developmentontheedge.be5.api.services.DbService;
import com.developmentontheedge.be5.databasemodel.RecordModel;
import com.developmentontheedge.be5.databasemodel.DatabaseModel;
import javax.inject.Inject;
import de.undercouch.citeproc.ItemDataProvider;
import de.undercouch.citeproc.csl.CSLItemData;
import de.undercouch.citeproc.csl.CSLItemDataBuilder;
import de.undercouch.citeproc.csl.CSLName;
import de.undercouch.citeproc.csl.CSLNameBuilder;
import de.undercouch.citeproc.csl.CSLType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class PublicationProvider implements ItemDataProvider
{
    private final DatabaseModel database;
    private final DbService db;

    @Inject
    public PublicationProvider(DatabaseModel database, DbService db)
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
                .author(getAuthors(rec.getValueAsString("authors")))
                .issued(Integer.parseInt(rec.getValueAsString("year")), mapMonth(rec.getValueAsString("month")), 1)
                //.containerTitle("Dummy journal")
                .build();
    }

    private CSLName[] getAuthors(String authors)
    {
        List<CSLName> names = Arrays.stream(authors.split(", ")).map(name ->
        {
            String[] strings = name.split(" ");
            return new CSLNameBuilder().given(strings[1]).family(strings[0]).build();
        }).collect(Collectors.toList());

        return names.toArray(new CSLName[0]);
    }

    private int mapMonth(String month)
    {
        if("Jan".equals(month))return 1;
        if("Feb".equals(month))return 2;
        if("Mar".equals(month))return 3;
        if("Apr".equals(month))return 4;
        if("May".equals(month))return 5;
        if("Jun".equals(month))return 6;
        if("Jul".equals(month))return 7;
        if("Aug".equals(month))return 8;
        if("Sep".equals(month))return 9;
        if("Nov".equals(month))return 10;
        if("Apr".equals(month))return 11;
        return 12;
    }

    public String[] getIds()
    {
        return db.stringArray("SELECT ID FROM publications");
    }
}