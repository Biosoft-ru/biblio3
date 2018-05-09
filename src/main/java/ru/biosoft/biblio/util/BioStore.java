package ru.biosoft.biblio.util;

import com.developmentontheedge.be5.api.helpers.UserInfoHolder;
import ru.biosoft.biostoreapi.DefaultConnectionProvider;
import ru.biosoft.biostoreapi.JWToken;
import ru.biosoft.biostoreapi.Project;
import ru.biosoft.biostoreapi.ProjectUser;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.biosoft.biblio.util.BiblioUtils.BIOSTORE_PROJECTS;


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

    public static void createProjectWithPermissions(String projectName, int permission)
    {
        api.createProjectWithPermissions((JWToken) UserInfoHolder.getSession().get(BIOSTORE_TOKEN), projectName, permission);
    }

    public static List<ProjectUser> getProjectUsers(String projectName)
    {
        return api.getProjectUsers((JWToken) UserInfoHolder.getSession().get(BIOSTORE_TOKEN), projectName);
    }

    public static void addUserToProject(String userToAdd, String projectName)
    {
        api.addUserToProject((JWToken) UserInfoHolder.getSession().get(BIOSTORE_TOKEN), userToAdd, projectName);
    }

    public static void changeUserRoleInProject(String projectName, String userToChange, String newRole)
    {
        api.changeUserRoleInProject((JWToken) UserInfoHolder.getSession().get(BIOSTORE_TOKEN), projectName, userToChange, newRole);
    }
}
