package ru.biosoft.biblio.services.citeproc;


import ru.biosoft.biblio.util.StaxStreamProcessor;

import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
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
    public static String readStringFromURL(String requestURL) throws IOException
    {
        try (Scanner scanner = new Scanner(new URL(requestURL).openStream(),
                StandardCharsets.UTF_8.toString()))
        {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
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
