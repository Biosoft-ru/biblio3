package ru.biosoft.biblio.util;

import com.developmentontheedge.be5.servlet.UserInfoHolder;
import ru.biosoft.biostoreapi.DefaultConnectionProvider;
import ru.biosoft.biostoreapi.JWToken;
import ru.biosoft.biostoreapi.Project;
import ru.biosoft.biostoreapi.ProjectUser;

import java.util.List;

import static java.util.stream.Collectors.toList;


public class BioStore
{
    private static final String BIOSTORE_SERVER_NAME = "biblio.biouml.org";

    public static final String BIOSTORE_PROJECTS = "biostore_projects";
    public static final String BIOSTORE_TOKEN = "biostore-token";

    public static final DefaultConnectionProvider api = new DefaultConnectionProvider(BIOSTORE_SERVER_NAME);

    public static List<Project> getProjectList()
    {
        return api.getProjectList(getToken());
    }

    public static List<Project> loadProjectListToSession()
    {
        List<Project> projects = api.getProjectList(getToken());

        List<String> projectNames = projects.stream().map(Project::getProjectName).collect(toList());

        UserInfoHolder.getSession().set(BIOSTORE_PROJECTS, projectNames);

        return projects;
    }

    public static void createProjectWithPermissions(String projectName, int permission)
    {
        api.createProjectWithPermissions(getToken(), projectName, permission);
    }

    public static List<ProjectUser> getProjectUsers(String projectName)
    {
        return api.getProjectUsers(getToken(), projectName);
    }

    public static void addUserToProject(String userToAdd, String projectName)
    {
        api.addUserToProject(getToken(), userToAdd, projectName);
    }

    public static void changeUserRoleInProject(String projectName, String userToChange, String newRole)
    {
        api.changeUserRoleInProject(getToken(), projectName, userToChange, newRole);
    }

    private static JWToken getToken()
    {
        return (JWToken) UserInfoHolder.getSession().get(BIOSTORE_TOKEN);
    }
}
