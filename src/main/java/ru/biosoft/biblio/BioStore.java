package ru.biosoft.biblio;

import com.developmentontheedge.be5.api.helpers.UserInfoHolder;
import ru.biosoft.biostoreapi.DefaultConnectionProvider;
import ru.biosoft.biostoreapi.JWToken;
import ru.biosoft.biostoreapi.Project;

import java.util.List;


public class BioStore
{
    private static final String BIOSTORE_SERVER_NAME = "biblio.biouml.org";

    public static final String BIOSTORE_TOKEN = "biostore-token";

    public static final DefaultConnectionProvider api = new DefaultConnectionProvider(BIOSTORE_SERVER_NAME);

    public static List<Project> getProjectList()
    {
        return api.getProjectList((JWToken) UserInfoHolder.getSession().get(BIOSTORE_TOKEN));
    }
//
//    public static void createProject(String projectName, int permission)
//    {
//        api.createProject((JWToken) UserInfoHolder.getSession().get(BIOSTORE_TOKEN), projectName, permission);
//    }
}
