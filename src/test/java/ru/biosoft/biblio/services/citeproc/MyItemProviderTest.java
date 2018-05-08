package ru.biosoft.biblio.services.citeproc;

import de.undercouch.citeproc.CSL;
import de.undercouch.citeproc.output.Bibliography;
import de.undercouch.citeproc.output.Citation;
import org.junit.Test;
import ru.biosoft.biblio.services.citeproc.MyItemProvider;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MyItemProviderTest
{
    @Test
    public void test() throws Exception
    {
        CSL citeproc = new CSL(new MyItemProvider(), "ieee");
        citeproc.setOutputFormat("html");

        citeproc.registerCitationItems("ID-1", "ID-2", "ID-0");
        List<Citation> s1 = citeproc.makeCitation("ID-1");
        System.out.println(s1.get(0).getText());
        //=> [1] (for the "ieee" style)

        List<Citation> s2 = citeproc.makeCitation("ID-2");
        System.out.println(s2.get(0).getText());
        //=> [2]
        Bibliography bibl = citeproc.makeBibliography();
        for (String entry : bibl.getEntries()) {
            System.out.println(entry);
        }
    }

}