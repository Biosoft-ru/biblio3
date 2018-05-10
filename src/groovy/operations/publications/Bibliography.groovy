package publications

import com.developmentontheedge.be5.operation.OperationResult
import com.developmentontheedge.be5.operation.support.GOperationSupport

import java.util.stream.Collectors


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

        setResult(OperationResult.redirect(request.getBaseUrl()+ "/api/bibliography?type=${dps.getValue("type")}" +
                "&publicationIDs=" + ids +
                "&citationFileID=" + attID))
    }
}
