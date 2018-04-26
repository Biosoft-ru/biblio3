package ru.biosoft.biblio.components;

import com.developmentontheedge.be5.api.Component;
import com.developmentontheedge.be5.api.Request;
import com.developmentontheedge.be5.api.Response;
import com.developmentontheedge.be5.inject.Injector;
import ru.biosoft.biblio.services.PubMedService;


public class PubMedInfo implements Component
{
    @Override
    public void generate(Request req, Response res, Injector injector)
    {
        PubMedService pubMedService = injector.get(PubMedService.class);

        String jwToken = req.getNonEmpty("jwtoken");
        String username = req.get("username");

        try
        {
            res.sendAsRawJson(TypedResponse.data(pubMedService.getData(jwToken, username)));
        }
        catch (SecurityException e)
        {
            res.sendAsRawJson(TypedResponse.error(e.getMessage()));
        }
    }

    public static class TypedResponse
    {
        public String type;
        public String message;
        public Object data;

        public static TypedResponse error(String message)
        {
            return new TypedResponse("error", message, null);
        }

        public static TypedResponse data(Object data)
        {
            return new TypedResponse("ok", null, data);
        }

        private TypedResponse(String type, String message, Object data)
        {
            this.type = type;
            this.message = message;
            this.data = data;
        }
    }

}
