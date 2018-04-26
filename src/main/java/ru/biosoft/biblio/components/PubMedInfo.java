package ru.biosoft.biblio.components;

import com.developmentontheedge.be5.api.Component;
import com.developmentontheedge.be5.api.Request;
import com.developmentontheedge.be5.api.Response;
import com.developmentontheedge.be5.api.helpers.OperationHelper;
import com.developmentontheedge.be5.inject.Injector;
import com.developmentontheedge.beans.DynamicPropertySet;
import ru.biosoft.biostoreapi.DefaultConnectionProvider;
import ru.biosoft.biostoreapi.Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static ru.biosoft.biblio.BiblioUtils.BIOSTORE_SERVER_NAME;


public class PubMedInfo implements Component
{
    private OperationHelper operationHelper;

    @Override
    public void generate(Request req, Response res, Injector injector)
    {
        operationHelper = injector.get(OperationHelper.class);

        String jwtoken = req.getNonEmpty("jwtoken");
        String username = req.get("username");

        DefaultConnectionProvider provider = new DefaultConnectionProvider(BIOSTORE_SERVER_NAME);

        try
        {
            List<Project> projects = provider.getProjectListWithToken(username, jwtoken);

            res.sendAsRawJson(TypedResponse.data(getData(projects)));
        }
        catch (SecurityException e)
        {
            res.sendAsRawJson(TypedResponse.error(e.getMessage()));
        }
    }

    private Map<Long, PublicationProject> getData(List<Project> projects)
    {
        List<String> projectNames = projects.stream().map(Project::getProjectName).collect(toList());

        Map<Long, PublicationProject> publicationProjects = new HashMap<>();
        List<DynamicPropertySet> list = operationHelper.readAsRecordsFromQuery(
                "publications", "PubMedInfo Data", Collections.singletonMap("projects", projectNames));

        for (DynamicPropertySet dps : list)
        {
            Long pmid = dps.getValueAsLong("PMID");
            PublicationProject publicationProject;

            if(publicationProjects.containsKey(pmid))
            {
                publicationProject = publicationProjects.get(pmid);
            }
            else
            {
                publicationProject = new PublicationProject(dps.getValueAsLong("publicationID"), new ArrayList<>());
                publicationProjects.put(pmid, publicationProject);
            }

            publicationProject.projects.add(new ProjectModel(
                    dps.getValueAsLong("categoryID"),
                    dps.getValueAsString("projectName"),
                    dps.getValueAsString("status"),
                    (int)dps.getValue("importance"),
                    dps.getValueAsString("keyWords"),
                    dps.getValueAsString("comment")
            ));
        }

        return publicationProjects;
    }

    public class PublicationProject
    {
        public final long id;
        public final List<ProjectModel> projects;

        public PublicationProject(long id, List<ProjectModel> projects)
        {
            this.id = id;
            this.projects = projects;
        }
    }

    public class ProjectModel
    {
        public final long categoryID;
        public final String name;
        public final String status;
        public final int importance;
        public final String keyWords;
        public final String comment;

        public ProjectModel(long categoryID, String name, String status, int importance, String keyWords, String comment)
        {
            this.categoryID = categoryID;
            this.name = name;
            this.status = status;
            this.importance = importance;
            this.keyWords = keyWords;
            this.comment = comment;
        }
    }
}
