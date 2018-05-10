package ru.biosoft.biblio.components;

import com.developmentontheedge.be5.api.Component;
import com.developmentontheedge.be5.api.Request;
import com.developmentontheedge.be5.api.Response;
import com.developmentontheedge.be5.databasemodel.RecordModel;
import com.developmentontheedge.be5.databasemodel.impl.DatabaseModel;
import com.developmentontheedge.be5.inject.Injector;
import com.google.common.base.Charsets;
import de.undercouch.citeproc.CSL;
import de.undercouch.citeproc.output.Bibliography;
import ru.biosoft.biblio.services.citeproc.PublicationProvider;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;


/**
 * Example url
 * /api/bibliography?type=text&publicationIDs=1,2,4,5&citationFileID=5
 */
public class BibliographyComponent implements Component
{
    @Inject private DatabaseModel database;
    @Inject private PublicationProvider publicationProvider;

    @Override
    public void generate(Request req, Response res, Injector injector)
    {
        String type           = req.getNonEmpty("type");
        String publicationIDs = req.getNonEmpty("publicationIDs");
        String citationFileID     = req.getNonEmpty("citationFileID");

        RecordModel record = database.getEntity("attachments")
                .get(Long.parseLong(citationFileID));


        CSL csl;
        try
        {
            csl = new CSL(publicationProvider, new String((byte[])record.getValue("data")));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        csl.setOutputFormat(type);

        csl.registerCitationItems(publicationIDs.split(","));

        Bibliography bibl = csl.makeBibliography();
        String[] entries = bibl.getEntries();

        StringBuilder out = new StringBuilder();

        for (String line : entries){
            out.append(line);
        }

        String mimeType = "text/html";
        String ext = ".html";

        if("rtf".equals(type))
        {
            mimeType = "application/rtf";
            ext = ".rtf";
        }
        else if("text".equals(type))
        {
            mimeType = "text/plain";
            ext = ".txt";
        }
        else if("asciidoc".equals(type))
        {
            mimeType = "text/asciidoc";
            ext = ".adoc";
        }
        else if("fo".equals(type))
        {
            mimeType = "application/xml";
            ext = ".xml";
        }

        res.sendFile(false, "Bibliography" + ext, mimeType, Charsets.UTF_8.name(),
                new ByteArrayInputStream(out.toString().getBytes()));
    }

}
