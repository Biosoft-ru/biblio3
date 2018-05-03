package ru.biosoft.biblio;

import com.developmentontheedge.be5.api.helpers.UserInfoHolder;
import ru.biosoft.biostoreapi.DefaultConnectionProvider;
import ru.biosoft.biostoreapi.JWToken;
import ru.biosoft.biostoreapi.Project;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.biosoft.biblio.BiblioUtils.BIOSTORE_PROJECTS;


public class BioStore
{
    private static final String BIOSTORE_SERVER_NAME = "biblio.biouml.org";

    public static final String BIOSTORE_TOKEN = "biostore-token";

    public static final DefaultConnectionProvider api = new DefaultConnectionProvider(BIOSTORE_SERVER_NAME);

    public static List<Project> getProjectList()
    {
        return api.getProjectList((JWToken) UserInfoHolder.getSession().get(BIOSTORE_TOKEN));
    }

    public static List<Project> loadProjectListToSession()
    {
        List<Project> projects = api.getProjectList((JWToken) UserInfoHolder.getSession().get(BIOSTORE_TOKEN));

        List<String> projectNames = projects.stream().map(Project::getProjectName).collect(toList());

        UserInfoHolder.getSession().set(BIOSTORE_PROJECTS, projectNames);

        return projects;
    }

    public static void createProject(String projectName, int permission)
    {
        //api.createProjectWithPermissions((JWToken) UserInfoHolder.getSession().get(BIOSTORE_TOKEN), projectName, permission);
    }
}
