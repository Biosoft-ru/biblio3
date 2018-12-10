package attachments

import com.developmentontheedge.be5.server.model.Base64File
import com.developmentontheedge.be5.server.operations.support.GOperationSupport
import groovy.transform.TypeChecked


@TypeChecked
class AddFile extends GOperationSupport
{
    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        dpsHelper.addDpForColumns(params, getInfo().getEntity(),
                ["publicationID", "public"], context.params, presetValues)

        params.add("file", "Файл") {
            TYPE = Base64File
        }

//        params.edit("name", "Название"){
//            CAN_BE_NULL = true
//            MESSAGE = "Если не заполнено берётся имя файла"
//        }

        return params
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        def file = (Base64File) params.getValue("file")

        //name          : params.$name != null ? params.$name : file.name,
        database["attachments"].add([
                publicationID : params.getValue("publicationID"),
                public        : params.getValue("public"),
                name          : file.name,
                data          : file.data,
                mimeType      : file.mimeTypes
        ])
    }
}
