package ru.biosoft.biblio.controllers;

import com.developmentontheedge.be5.databasemodel.DatabaseModel;
import com.developmentontheedge.be5.databasemodel.RecordModel;
import com.developmentontheedge.be5.server.servlet.support.ApiControllerSupport;
import com.developmentontheedge.be5.server.util.RequestUtils;
import com.developmentontheedge.be5.web.Request;
import com.developmentontheedge.be5.web.Response;
import de.undercouch.citeproc.CSL;
import de.undercouch.citeproc.output.Bibliography;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.biosoft.biblio.services.citeproc.PublicationProvider;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;

import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * Example url
 * /api/bibliography?type=text&publicationIDs=1,2,4,5&citationFileID=5
 */
public class BibliographyController extends ApiControllerSupport
{
    private final DatabaseModel database;
    private final PublicationProvider publicationProvider;

    @Inject
    public BibliographyController(DatabaseModel database, PublicationProvider publicationProvider)
    {
        this.database = database;
        this.publicationProvider = publicationProvider;
    }

    @Override
    public void generate(Request req, Response res, String requestSubUrl)
    {
        String type           = req.getNonEmpty("type");
        String publicationIDs = req.getNonEmpty("publicationIDs");
        String citationFileID     = req.getNonEmpty("citationFileID");

        boolean download      = !"no".equals(req.get("_download_"));

        RecordModel record = database.getEntity("attachments")
                .get(Long.parseLong(citationFileID));
        if (record == null)
        {
            throw new RuntimeException("File not found.");
        }

        String style = new String((byte[]) record.getValue("data"), UTF_8);

        String independentParentLink = getIndependentParentLink(style);
        if(independentParentLink != null){
            style = independentParentLink;
        }

        CSL csl;
        try
        {
            csl = new CSL(publicationProvider, style);
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

        RequestUtils.sendFile(res, download, "Bibliography" + ext, mimeType, UTF_8.name(),
                new ByteArrayInputStream(out.toString().getBytes(UTF_8)));
    }

    /**
     * copy from de.undercouch.citeproc.CSL
     *
     * Parse a string representing a dependent parent style and
     * get link to its independent parent style
     * @param style the dependent style
     * @return the link to the parent style or <code>null</code> if the link
     * could not be found
     */
    private String getIndependentParentLink(String style)
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource src = new InputSource(new StringReader(style));
            Document doc = builder.parse(src);
            NodeList links = doc.getElementsByTagName("link");
            for (int i = 0; i < links.getLength(); ++i)
            {
                Node n = links.item(i);
                Node relAttr = n.getAttributes().getNamedItem("rel");
                if (relAttr != null)
                {
                    if ("independent-parent".equals(relAttr.getTextContent()))
                    {
                        Node hrefAttr = n.getAttributes().getNamedItem("href");
                        if (hrefAttr != null)
                        {
                            return hrefAttr.getTextContent();
                        }
                    }
                }
            }
        }
        catch (ParserConfigurationException | IOException | SAXException e)
        {
            throw new RuntimeException(e);
        }

        return null;
    }

}
