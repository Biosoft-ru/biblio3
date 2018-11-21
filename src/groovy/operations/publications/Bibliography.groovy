package publications

import com.developmentontheedge.be5.databasemodel.util.DpsUtils
import com.developmentontheedge.be5.server.operations.support.GOperationSupport

import java.util.stream.Collectors

class Bibliography extends GOperationSupport
{
    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        params.add("citationID", "Style") {
            TAG_LIST_ATTR = queries.getTagsFromSelectionView("citations")
        }

        params.add("type", "Output format") {
            TAG_LIST_ATTR = [["html","html"], ["text","text"], ["rtf","rtf"],
                             ["asciidoc","asciidoc"], ["fo","fo"]] as String[][]
        }

        return DpsUtils.setValues(params, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        def attID = db.one("SELECT ID FROM attachments WHERE ownerID = 'citations." + params.getValue("citationID") + "'")
        def ids = Arrays.stream(context.records).map({x -> x.toString()}).collect(Collectors.joining(","))

        String url = request.getServerUrl() + "/api/bibliography?" +
                "type=${params.getValue("type")}&publicationIDs=${ids}&citationFileID=${attID}"

        def content = meta.getStaticPageContent(userInfo.getLanguage(), "bibliography.be")
                .replace("OPEN_URL", url + "&_download_=no")
                .replace("DOWNLOAD_URL", url + "&_download_=yes")

        setResultFinished(content)
    }
}
