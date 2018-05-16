package ru.biosoft.biblio.services.citeproc;


import ru.biosoft.biblio.util.StaxStreamProcessor;

import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class StyleService
{
    public void getInfo(String styleUrl)
    {
        try
        {
            InputStream inputStream = new URL(styleUrl).openStream();
            getInfo(inputStream);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public StyleInfo getInfo(InputStream inputStream) throws Exception
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
                    if("".equals(processor.getAttribute("rel"))){
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

    class StyleInfo
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
