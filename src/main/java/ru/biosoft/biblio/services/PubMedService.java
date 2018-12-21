package ru.biosoft.biblio.services;

import com.developmentontheedge.be5.base.services.Be5Caches;
import com.developmentontheedge.be5.query.model.beans.QRec;
import com.developmentontheedge.be5.query.services.QueriesService;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import ru.biosoft.biblio.util.BioStore;
import ru.biosoft.biostoreapi.JWToken;
import ru.biosoft.biostoreapi.Project;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toList;


public class PubMedService
{
    private static final Logger log = Logger.getLogger(PubMedService.class.getName());

    private static final LoadingCache<JWToken, List<Project>> cache = Caffeine.newBuilder()
            .maximumSize(1_000)
            .expireAfterWrite(6, TimeUnit.HOURS)
            .refreshAfterWrite(10, TimeUnit.MINUTES)
            .recordStats()
            .build(BioStore.api::getProjectList);

    private final QueriesService operationHelper;

    @Inject
    public PubMedService(Be5Caches be5Caches, QueriesService operationHelper)
    {
        this.operationHelper = operationHelper;

        be5Caches.registerCache("pubMedInfo jwtoken", cache);
    }

    public Map<Long, PublicationProject> getData(String jwtoken, String username, List<String> PMIDs)
    {
        long start = System.currentTimeMillis();
        List<Project> projects = cache.get(new JWToken(username, jwtoken));
        long time1 = System.currentTimeMillis() - start;

        long start2 = System.currentTimeMillis();

        Map<Long, PublicationProject> data = getData(projects, PMIDs);

        log.fine("PubMedInfo - " + time1 + ", " + (System.currentTimeMillis() - start2));

        return data;
    }

    private Map<Long, PublicationProject> getData(List<Project> projects, List<String> PMIDs)
    {
        List<String> projectNames = projects.stream().map(Project::getProjectName).collect(toList());

        Map<Long, PublicationProject> publicationProjects = new HashMap<>();
        List<QRec> list = operationHelper.query(
                "publications", "PubMedInfo Data", ImmutableMap.of(
                        "projects", projectNames,
                        "PMIDs", PMIDs
                ));

        for (QRec dps : list)
        {
            Long pmid = dps.getLong("PMID");
            PublicationProject publicationProject;

            if(publicationProjects.containsKey(pmid))
            {
                publicationProject = publicationProjects.get(pmid);
            }
            else
            {
                publicationProject = new PublicationProject(dps.getLong("publicationID"), new ArrayList<>());
                publicationProjects.put(pmid, publicationProject);
            }

            publicationProject.projects.add(new ProjectModel(
                    dps.getLong("categoryID"),
                    dps.getString("projectName"),
                    dps.getString("status"),
                    (int)dps.getValue("importance"),
                    dps.getString("keyWords"),
                    dps.getString("comment")
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
