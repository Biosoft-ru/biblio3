package ru.biosoft.biblio.controllers;

import com.developmentontheedge.be5.web.Request;
import com.developmentontheedge.be5.web.Response;
import com.developmentontheedge.be5.server.servlet.support.ApiControllerSupport;
import ru.biosoft.biblio.services.PubMedService;

import javax.inject.Inject;
import java.util.List;


public class PubMedInfo extends ApiControllerSupport
{
    private final PubMedService pubMedService;

    @Inject
    public PubMedInfo(PubMedService pubMedService)
    {
        this.pubMedService = pubMedService;
    }

    @Override
    public void generate(Request req, Response res, String requestSubUrl)
    {
        String jwToken = req.getNonEmpty("jwtoken");
        String username = req.get("username");
        List<String> PMIDs = req.getList("PMIDs");

        try
        {
            res.sendAsJson(TypedResponse.data(pubMedService.getData(jwToken, username, PMIDs)));
        }
        catch (SecurityException e)
        {
            res.sendAsJson(TypedResponse.error(e.getMessage()));
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
