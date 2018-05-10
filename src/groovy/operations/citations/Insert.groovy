package citations

import com.developmentontheedge.be5.databasemodel.RecordModel
import com.developmentontheedge.be5.model.Base64File
import com.developmentontheedge.be5.operation.TransactionalOperation
import com.developmentontheedge.be5.operation.support.GOperationSupport
import de.undercouch.citeproc.CSL
import de.undercouch.citeproc.output.Bibliography
import ru.biosoft.biblio.services.citeproc.DummyProvider


class Insert extends GOperationSupport implements TransactionalOperation
{
    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        dps.add("file", "Файл") {
            TYPE = Base64File
        }

        dps.add("title", "Title"){
            CAN_BE_NULL = true
        }

        return dpsHelper.setValues(dps, presetValues)
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        def file = (Base64File) dps.$file

        def name = file.name.replace('.csl', '')

        RecordModel<Long> rec = database.citations.getBy([name: name])

        Long id
        if(rec == null)
        {
            CSL citeproc = new CSL(new DummyProvider(), new String(file.data))
            citeproc.setOutputFormat("html")

            citeproc.registerCitationItems("ID-1", "ID-2", "ID-3", "ID-4", "ID-5")

            def line1 = citeproc.makeCitation("ID-1").get(0).getText()
            def line2 = citeproc.makeCitation("ID-1", "ID-2").get(0).getText()
            def line3 = citeproc.makeCitation("ID-1", "ID-2", "ID-5").get(0).getText()

            Bibliography bibl = citeproc.makeBibliography()

            id = database.citations.add([
                    name: name,
                    title: dps.$title,
                    inline: "${line1}<br/>${line2}<br/>${line3}".toString(),
                    bibliography: (bibl.getEntries()[0] + bibl.getEntries()[1])
            ])

            database.attachments.add([
                    ownerID : "citations." + id,
                    name    : file.name,
                    data    : file.data,
                    mimeType: file.mimeTypes,
                    type    : "other"
            ])
        }
        else
        {
            id = rec.getPrimaryKey()
        }

        database.citation2user.add([
                citationID: id,
                user_name: userInfo.getUserName()
        ])

    }
}
