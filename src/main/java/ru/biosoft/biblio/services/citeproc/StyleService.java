package ru.biosoft.biblio.services.citeproc;


import com.developmentontheedge.be5.databasemodel.impl.DatabaseModel;
import com.google.common.collect.ImmutableMap;
import de.undercouch.citeproc.CSL;
import de.undercouch.citeproc.output.Bibliography;
import ru.biosoft.biblio.util.StaxStreamProcessor;

import javax.inject.Inject;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class StyleService
{
    private final DatabaseModel database;

    @Inject
    public StyleService(DatabaseModel database)
    {
        this.database = database;
    }

    public static String readStringFromURL(String requestURL) throws Exception
    {
        try (Scanner scanner = new Scanner(new URL(requestURL).openStream(),
                StandardCharsets.UTF_8.toString()))
        {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }

    public Long addStyle(String name, String xml, String url) throws Exception
    {
        InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));

        StyleService.StyleInfo info = getInfo(stream);

        CSL citeproc = new CSL(new DummyProvider(), url != null ? url : xml, "en-US");
        citeproc.setOutputFormat("html");

        citeproc.registerCitationItems("ID-1", "ID-2", "ID-3", "ID-4", "ID-5");

        String line1 = citeproc.makeCitation("ID-1").get(0).getText();
        String line2 = citeproc.makeCitation("ID-1", "ID-2").get(0).getText();
        String line3 = citeproc.makeCitation("ID-1", "ID-2", "ID-5").get(0).getText();

        Bibliography bibl = citeproc.makeBibliography();

        Long id = database.<Long>getEntity("citations").add(ImmutableMap.<String, Object>builder()
                .put("name", name)
                .put("title", info.title)
                .put("format", info.format)
                .put("parent", info.parent)
                .put("updated", info.updated)

                .put("inline", line1 + "<br/>"+line2+"<br/>" + line3)
                .put("bibliography", (bibl.getEntries()[0] + bibl.getEntries()[1]))
                .build()
        );

        //TODO add info.categories

        database.getEntity("attachments").add(ImmutableMap.of(
                "ownerID" , "citations." + id,
                "name"    , name,
                "data"    , xml.getBytes(StandardCharsets.UTF_8),
                "mimeType", "application/xml",
                "type"    , "other"
        ));

        return id;
    }

    StyleInfo getInfo(InputStream inputStream) throws Exception
    {
        StyleInfo styleInfo = new StyleInfo();
        try (StaxStreamProcessor processor = new StaxStreamProcessor(inputStream))
        {
            while (processor.doUntilInParent(XMLEvent.START_ELEMENT, "info"))
            {
                if ("title".equals(processor.getLocalName()))
                {
                    styleInfo.title = processor.getText();
                }

                if ("link".equals(processor.getLocalName())){
                    if("independent-parent".equals(processor.getAttribute("rel"))){
                        styleInfo.parent = processor.getAttribute("href");
                    }
                }

                if ("category".equals(processor.getLocalName()))
                {
                    String format = processor.getAttribute("citation-format");
                    String field = processor.getAttribute("field");

                    if(format != null){
                        styleInfo.format = format;
                    }else{
                        styleInfo.categories.add(field);
                    }
                }

                if ("updated".equals(processor.getLocalName()))
                {
                    String utc = processor.getText().replace("+00:00", "Z");
                    styleInfo.updated = Timestamp.from(Instant.parse(utc));
                }
            }
        }

        return styleInfo;
    }

    public class StyleInfo
    {
        public String title;
        public String format;
        public List<String> categories = new ArrayList<>();
        public String parent;
        public Timestamp updated;

        @Override
        public String toString()
        {
            return "StyleInfo{" +
                    "title='" + title + '\'' +
                    ", format='" + format + '\'' +
                    ", categories=" + categories +
                    ", parent='" + parent + '\'' +
                    ", updated=" + updated +
                    '}';
        }
    }
}
