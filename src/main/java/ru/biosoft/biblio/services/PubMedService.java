package ru.biosoft.biblio.services;

import com.developmentontheedge.be5.query.services.QueriesService;
import com.developmentontheedge.be5.base.services.Be5Caches;
import com.developmentontheedge.beans.DynamicPropertySet;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import javax.inject.Inject;
import ru.biosoft.biblio.util.BioStore;
import ru.biosoft.biostoreapi.JWToken;
import ru.biosoft.biostoreapi.Project;

import java.util.ArrayList;
import java.util.Date;
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
        long start = new Date().getTime();
        List<Project> projects = cache.get(new JWToken(username, jwtoken));
        long time1 = new Date().getTime() - start;

        long start2 = new Date().getTime();

        Map<Long, PublicationProject> data = getData(projects, PMIDs);

        log.fine("PubMedInfo - " + time1 + ", " + (new Date().getTime() - start2));

        return data;
    }

    private Map<Long, PublicationProject> getData(List<Project> projects, List<String> PMIDs)
    {
        List<String> projectNames = projects.stream().map(Project::getProjectName).collect(toList());

        Map<Long, PublicationProject> publicationProjects = new HashMap<>();
        List<DynamicPropertySet> list = operationHelper.readAsRecordsFromQuery(
                "publications", "PubMedInfo Data", ImmutableMap.of(
                        "projects", projectNames,
                        "PMIDs", PMIDs
                ));

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
