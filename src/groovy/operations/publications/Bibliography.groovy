package publications

import com.developmentontheedge.be5.server.services.DocumentGenerator
import com.developmentontheedge.be5.operation.model.OperationResult
import com.developmentontheedge.be5.server.operations.support.GOperationSupport
import com.developmentontheedge.be5.base.util.DpsUtils

import javax.inject.Inject

import java.util.stream.Collectors

import static com.developmentontheedge.be5.server.FrontendActions.*


class Bibliography extends GOperationSupport
{
    @Inject DocumentGenerator documentGenerator

    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        dps.add("citationID", "Style") {
            TAG_LIST_ATTR = queries.getTagsFromSelectionView("citations")
        }

        dps.add("type", "Output format") {
            TAG_LIST_ATTR = [["html","html"], ["text","text"], ["rtf","rtf"],
                             ["asciidoc","asciidoc"], ["fo","fo"]] as String[][]
        }

        return DpsUtils.setValues(dps, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        def attID = db.one("SELECT ID FROM attachments WHERE ownerID = 'citations." + dps.getValue("citationID") + "'")
        def ids = Arrays.stream(context.records).map({x -> x.toString()}).collect(Collectors.joining(","))

        String url = request.getServerUrl() + "/api/bibliography?" +
                "type=${dps.getValue("type")}&publicationIDs=${ids}&citationFileID=${attID}"

        def content = meta.getStaticPageContent(userInfo.getLanguage(), "bibliography.be")
                .replace("OPEN_URL", url + "&_download_=no")
                .replace("DOWNLOAD_URL", url + "&_download_=yes")

        setResult(OperationResult.finished(null, updateDocument(
                documentGenerator.createStaticPage("Bibliography", content, null)
        )))
    }
}
