package ru.biosoft.biblio.services.citeproc;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;


public class StyleServiceTest
{
    @Test
    public void addStyle() throws Exception
    {
        InputStream ieee = Thread.currentThread().getContextClassLoader().getResourceAsStream("ieee.csl");

        StyleService.StyleInfo info = new StyleService().getInfo(ieee);

        System.out.println(info);

        assertEquals("IEEE", info.title);
        assertEquals(Timestamp.from(Instant.parse("2018-03-31T04:01:14Z")), info.updated);
        assertEquals("numeric", info.format);
        assertEquals(Arrays.asList("engineering", "generic-base"), info.categories);
    }

}