package ru.biosoft.biblio.util;

import com.developmentontheedge.be5.web.Session;
import ru.biosoft.biostoreapi.DefaultConnectionProvider;
import ru.biosoft.biostoreapi.JWToken;
import ru.biosoft.biostoreapi.Project;
import ru.biosoft.biostoreapi.ProjectUser;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

import static java.util.stream.Collectors.toList;


public class BioStore
{
    private final Provider<Session> sessionProvider;

    private static final String BIOSTORE_SERVER_NAME = "biblio.biouml.org";

    public static final String BIOSTORE_PROJECTS = "biostore_projects";
    public static final String BIOSTORE_TOKEN = "biostore-token";

    public static final DefaultConnectionProvider api = new DefaultConnectionProvider(BIOSTORE_SERVER_NAME);

    @Inject
    public BioStore(Provider<Session> sessionProvider)
    {
        this.sessionProvider = sessionProvider;
    }

    public List<Project> getProjectList()
    {
        return api.getProjectList(getToken());
    }

    public List<Project> loadProjectListToSession()
    {
        List<Project> projects = api.getProjectList(getToken());

        List<String> projectNames = projects.stream().map(Project::getProjectName).collect(toList());

        sessionProvider.get().set(BIOSTORE_PROJECTS, projectNames);

        return projects;
    }

    public void createProjectWithPermissions(String projectName, int permission)
    {
        api.createProjectWithPermissions(getToken(), projectName, permission);
    }

    public List<ProjectUser> getProjectUsers(String projectName)
    {
        return api.getProjectUsers(getToken(), projectName);
    }

    public void addUserToProject(String userToAdd, String projectName)
    {
        api.addUserToProject(getToken(), userToAdd, projectName);
    }

    public void changeUserRoleInProject(String projectName, String userToChange, String newRole)
    {
        api.changeUserRoleInProject(getToken(), projectName, userToChange, newRole);
    }

    private JWToken getToken()
    {
        return (JWToken) sessionProvider.get().get(BIOSTORE_TOKEN);
    }
}
