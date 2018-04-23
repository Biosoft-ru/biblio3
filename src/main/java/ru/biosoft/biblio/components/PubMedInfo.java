package ru.biosoft.biblio.components;

import com.developmentontheedge.be5.api.Component;
import com.developmentontheedge.be5.api.Request;
import com.developmentontheedge.be5.api.Response;
import com.developmentontheedge.be5.env.Injector;
import ru.biosoft.biostoreapi.DefaultConnectionProvider;

import java.util.List;

import static ru.biosoft.biblio.BiblioUtils.BIOSTORE_SERVER_NAME;


public class PubMedInfo implements Component
{
    @Override
    public void generate(Request req, Response res, Injector injector)
    {
        String jwtoken = req.getNonEmpty("jwtoken");
        String username = req.get("username");

        DefaultConnectionProvider provider = new DefaultConnectionProvider("micro.biouml.org");//todo BIOSTORE_SERVER_NAME

        List<String> projects = provider.getProjectListWithToken(username, jwtoken);

        res.sendAsRawJson(projects);
    }

}
