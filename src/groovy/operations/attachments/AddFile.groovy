package attachments

import com.developmentontheedge.be5.model.Base64File
import com.developmentontheedge.be5.operation.support.GOperationSupport


class AddFile extends GOperationSupport
{
    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        dpsHelper.addDpForColumns(dps, getInfo().getEntity(),
                ["publicationID", "public"], context.operationParams, presetValues)

        dps.add("file", "Файл") {
            TYPE = Base64File
        }

//        dps.edit("name", "Название"){
//            CAN_BE_NULL = true
//            MESSAGE = "Если не заполнено берётся имя файла"
//        }

        return dps
    }

    @Override
    void invoke(Object parameters) throws Exception
    {
        def file = (Base64File) dps.$file

        //name          : dps.$name != null ? dps.$name : file.name,
        database.attachments.add([
                publicationID : dps.$publicationID,
                public        : dps.$public,
                name          : file.name,
                data          : file.data,
                mimeType      : file.mimeTypes
        ])
    }
}
