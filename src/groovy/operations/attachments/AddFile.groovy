package attachments

import com.developmentontheedge.be5.api.impl.model.Base64File
import com.developmentontheedge.be5.operation.GOperationSupport


class AddFile extends GOperationSupport
{
    @Override
    Object getParameters(Map<String, Object> presetValues) throws Exception
    {
        dpsHelper.addDpForColumns(dps, getInfo().getEntity(),
                ["publicationID", "public"], presetValues)

        dps.add("file", "Файл") {
            TYPE = Base64File
        }

//        dps.edit("name", "Название"){
//            CAN_BE_NULL = true
//            MESSAGE = "Если не заполнено берётся имя файла"
//        }

        dpsHelper.setValues(dps, presetValues)

        return dpsHelper.setOperationParams(dps, context.getOperationParams())
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
