package ru.biosoft.biblio.services.citeproc;

import de.undercouch.citeproc.ItemDataProvider;
import de.undercouch.citeproc.csl.CSLItemData;
import de.undercouch.citeproc.csl.CSLItemDataBuilder;
import de.undercouch.citeproc.csl.CSLType;


public class DummyProvider implements ItemDataProvider
{
    @Override
    public CSLItemData retrieveItem(String id)
    {
        if("ID-1".equals(id)){
            return new CSLItemDataBuilder()
                    .id(id)
                    .type(CSLType.WEBPAGE)
                    .title("citeproc-java: A Citation Style Language (CSL) processor for Java")
                    .author("Michel", "Krämer")
                    .issued(2016, 11, 20)
                    .URL("http://michel-kraemer.github.io/citeproc-java/")
                    .accessed(2018, 5, 16)
                    .build();
        }

        return new CSLItemDataBuilder()
                .id(id)
                .type(CSLType.ARTICLE_JOURNAL)
                .title("A dummy journal article")
                .author("John", "Smith")
                .issued(2013, 9, 6)
                .containerTitle("Dummy journal")
                .build();
    }

    public String[] getIds()
    {
        return new String[]{"ID-0", "ID-1", "ID-2", "ID-3", "ID-4", "ID-5"};
    }
}