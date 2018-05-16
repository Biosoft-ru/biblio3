package ru.biosoft.biblio.services.citeproc;

import de.undercouch.citeproc.CSL;
import de.undercouch.citeproc.output.Bibliography;
import de.undercouch.citeproc.output.Citation;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class DummyProviderTest
{
    @Test
    public void test() throws Exception
    {
        CSL citeproc = new CSL(new DummyProvider(), "ieee");
        citeproc.setOutputFormat("html");

        citeproc.registerCitationItems("ID-1", "ID-2", "ID-0");

        List<Citation> s1 = citeproc.makeCitation("ID-1");

        assertEquals("[1]", s1.get(0).getText());

        List<Citation> s2 = citeproc.makeCitation("ID-2");

        assertEquals("[2]", s2.get(0).getText());

        Bibliography bibl = citeproc.makeBibliography();
        for (String entry : bibl.getEntries()) {
            System.out.println(entry);
        }
    }

    @Test
    public void test2() throws Exception
    {
        CSL citeproc = new CSL(new DummyProvider(), "academic-medicine");
        citeproc.setOutputFormat("html");

        citeproc.registerCitationItems("ID-1", "ID-2", "ID-0");


        Bibliography bibl = citeproc.makeBibliography();
        for (String entry : bibl.getEntries()) {
            System.out.println(entry);
        }
    }

}