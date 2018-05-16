package citations

import com.developmentontheedge.be5.model.Base64File
import com.developmentontheedge.be5.operation.TransactionalOperation
import com.developmentontheedge.be5.operation.support.GOperationSupport
import ru.biosoft.biblio.util.StaxStreamProcessor

import javax.json.Json
import javax.json.JsonArray
import javax.json.JsonObject
import javax.json.JsonReader
import javax.json.JsonReaderFactory
import java.nio.file.Files


class InsertFromZotero extends GOperationSupport implements TransactionalOperation
{
    private final static String stylesUrl = "https://www.zotero.org/styles-files/styles.json"

    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        dps.add("count", "Count") {
            TYPE = Long
            value = 3
        }

        return dpsHelper.setValues(dps, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        Set<String> citationNames = new HashSet<>(db.scalarList("SELECT name FROM citations"))

        InputStreamReader reader
        try{
            reader = new InputStreamReader(new URL(stylesUrl).openStream())
            JsonReader jsonReader = Json.createReader(reader)
            def array = jsonReader.readArray()

            for (int i = 0; i < Math.min(array.size(), dps.getValueAsLong("count")); i++)
            {
                def styleName = array.getJsonObject(i).getString("href")
                if(!citationNames.contains(styleName))
                {
                    addStyle(array.getJsonObject(i).getString("href"))
                }
            }
        }finally{
            if(reader != null)reader.close()
        }

//        def file = (Base64File) dps.$file
//
//        def name = file.name.replace('.csl', '')



//        RecordModel<Long> rec = database.citations.getBy([name: name])
//
//        Long id
//        if(rec == null)
//        {
//            CSL citeproc = new CSL(new DummyProvider(), new String(file.data))
//            citeproc.setOutputFormat("html")
//
//            citeproc.registerCitationItems("ID-1", "ID-2", "ID-3", "ID-4", "ID-5")
//
//            def line1 = citeproc.makeCitation("ID-1").get(0).getText()
//            def line2 = citeproc.makeCitation("ID-1", "ID-2").get(0).getText()
//            def line3 = citeproc.makeCitation("ID-1", "ID-2", "ID-5").get(0).getText()
//
//            Bibliography bibl = citeproc.makeBibliography()
//
//            id = database.citations.add([
//                    name: name,
//                    //title: dps.$title,
//                    inline: "${line1}<br/>${line2}<br/>${line3}".toString(),
//                    bibliography: (bibl.getEntries()[0] + bibl.getEntries()[1])
//            ])
//
//            database.attachments.add([
//                    ownerID : "citations." + id,
//                    name    : file.name,
//                    data    : file.data,
//                    mimeType: file.mimeTypes,
//                    type    : "other"
//            ])
//        }
//        else
//        {
//            id = rec.getPrimaryKey()
//        }
//
//        database.citation2user.add([
//                citationID: id,
//                user_name: userInfo.getUserName()
//        ])

    }



}
