package citations

import com.developmentontheedge.be5.operation.OperationResult
import com.developmentontheedge.be5.server.operations.support.GOperationSupport
import com.developmentontheedge.be5.databasemodel.util.DpsUtils
import ru.biosoft.biblio.services.citeproc.StyleService

import javax.inject.Inject
import javax.json.Json
import javax.json.JsonReader
import java.nio.charset.StandardCharsets
import java.util.logging.Level
import java.util.logging.Logger


class InsertFromZotero extends GOperationSupport
{
    private static final Logger log = Logger.getLogger(InsertFromZotero.class.getName());

    private final static String stylesUrl = "https://www.zotero.org/styles-files/styles.json"

    @Inject StyleService styleService

    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        params.add("count", "Count") {
            TYPE = Long
            value = 3
        }

        return DpsUtils.setValues(params, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        Set<String> citationNames = new HashSet<>(db.scalarList("SELECT name FROM citations"))

        int insertCount = 0
        int errorCount = 0
        InputStreamReader reader
        try{
            reader = new InputStreamReader(new URL(stylesUrl).openStream(), StandardCharsets.UTF_8)
            JsonReader jsonReader = Json.createReader(reader)
            def array = jsonReader.readArray()

            for (int i = 0; i < Math.min(array.size(), params.getValueAsLong("count")); i++)
            {
                def styleName = array.getJsonObject(i).getString("title")
                def styleHref = array.getJsonObject(i).getString("href")
                if(!citationNames.contains(styleName))
                {
                    def styleXml = StyleService.readStringFromURL(styleHref)

                    try{
                        db.useTransaction({ conn ->
                            def id = styleService.addStyle(styleName, styleXml, styleHref)

                            database.citation2user.add([
                                    citationID: id,
                                    user_name: "zotero"
                            ])
                        })

                        insertCount++
                    }catch (Exception e){
                        errorCount++
                        log.log(Level.SEVERE, "error on addStyle: " + styleName + styleXml, e)
                    }
                }
            }
        }finally{
            if(reader != null)reader.close()
        }

        setResult(OperationResult.finished(insertCount + " added, " + errorCount + " errors"))
    }
}
