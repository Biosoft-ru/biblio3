package ru.biosoft.biblio.components;

import com.developmentontheedge.be5.api.Component;
import com.developmentontheedge.be5.api.Request;
import com.developmentontheedge.be5.api.Response;
import com.developmentontheedge.be5.api.helpers.OperationHelper;
import com.developmentontheedge.be5.env.Injector;
import ru.biosoft.biostoreapi.DefaultConnectionProvider;

import java.util.ArrayList;
import java.util.List;


public class PubMedInfo implements Component
{
    private OperationHelper operationHelper;

    @Override
    public void generate(Request req, Response res, Injector injector)
    {
        operationHelper = injector.get(OperationHelper.class);

        String jwtoken = req.getNonEmpty("jwtoken");
        String username = req.get("username");

        //DefaultConnectionProvider provider = new DefaultConnectionProvider(BIOSTORE_SERVER_NAME);
        DefaultConnectionProvider provider = new DefaultConnectionProvider(
                "http://localhost:8080/biostore", "micro.biouml.org");

        try
        {
            List<String> projects = provider.getProjectListWithToken(username, jwtoken);

            res.sendAsRawJson(TypedResponse.data(getData(projects)));
        }
        catch (SecurityException e)
        {
            res.sendAsRawJson(TypedResponse.error(e.getMessage()));
        }
    }

    private Object getData(List<String> projects)
    {
        List<PublicationProject> publicationProjects = new ArrayList<>();
        //QRec dps = operationHelper.readOneRecord("publications", "PubMedInfo Data", Collections.singletonMap("projects", projects));
        //dps.getValueAsLong()

        return publicationProjects;
    }

    public class PublicationProject
    {
        public long id;
        public List<Project> projects;

        public PublicationProject(long id, List<Project> projects)
        {
            this.id = id;
            this.projects = projects;
        }
    }

    public class Project
    {
        public long id;
        public String status;
        public int importance;
        public String keyWords;
        public String comment;

        public Project(long id, String status, int importance, String keyWords, String comment)
        {
            this.id = id;
            this.status = status;
            this.importance = importance;
            this.keyWords = keyWords;
            this.comment = comment;
        }
    }
}
