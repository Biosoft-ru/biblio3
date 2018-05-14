package publications

import com.developmentontheedge.be5.model.StaticPagePresentation
import com.developmentontheedge.be5.model.jsonapi.JsonApiModel
import com.developmentontheedge.be5.model.jsonapi.ResourceData
import com.developmentontheedge.be5.operation.OperationResult
import com.developmentontheedge.be5.operation.support.GOperationSupport
import com.developmentontheedge.be5.util.HashUrlUtils

import java.util.stream.Collectors

import static com.developmentontheedge.be5.api.FrontendActions.*
import static com.developmentontheedge.be5.api.FrontendConstants.STATIC_ACTION
import static com.developmentontheedge.be5.api.RestApiConstants.SELF_LINK


class Bibliography extends GOperationSupport
{
    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        dps.add("citationID", "Style") {
            TAG_LIST_ATTR = helper.getTagsFromSelectionView("citations")
        }

        dps.add("type", "Output format") {
            TAG_LIST_ATTR = [["html","html"], ["text","text"], ["rtf","rtf"],
                             ["asciidoc","asciidoc"], ["fo","fo"]] as String[][]
        }

        return dpsHelper.setValues(dps, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        def attID = db.one("SELECT ID FROM attachments WHERE ownerID = 'citations." + dps.getValue("citationID") + "'")
        def ids = Arrays.stream(context.records).map({x -> x.toString()}).collect(Collectors.joining(","))

        String url = request.getBaseUrl() + "/api/bibliography?" +
                "type=${dps.getValue("type")}&publicationIDs=${ids}&citationFileID=${attID}"


        //setResult(OperationResult.redirect())

        //redirectThisOperation()
        //HashUrlUtils.getUrl(this).toString()

        setResult(OperationResult.finished(null, [updateDocument(
                JsonApiModel.data(new ResourceData(STATIC_ACTION,
                        new StaticPagePresentation("title", "test"),
                        Collections.emptyMap()), "")
        )]))

        //setResult(OperationResult.finished("Download Will Start Shortly", [redirect(url), goBack()]))

        //window.open blocked by browser usually
        //setResult(OperationResult.finished(null, [goBack(), openNewWindow(url)]))
    }
}
