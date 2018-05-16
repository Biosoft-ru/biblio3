package ru.biosoft.biblio;

import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;

import ru.biosoft.biblio.components.BibliographyComponent;
import ru.biosoft.biblio.components.PubMedInfo;
import ru.biosoft.biblio.services.MedlineImport;
import ru.biosoft.biblio.services.PubMedService;
import ru.biosoft.biblio.services.citeproc.PublicationProvider;


public class BiblioModule extends ServletModule
{
    @Override
    protected void configureServlets()
    {
        bind(PubMedInfo.class).in(Scopes.SINGLETON);
        bind(BibliographyComponent.class).in(Scopes.SINGLETON);

        serve("/api/pubMedInfo*").with(PubMedInfo.class);
        serve("/api/bibliography*").with(BibliographyComponent.class);

        bind(PublicationProvider.class).in(Scopes.SINGLETON);
        bind(MedlineImport.class).in(Scopes.SINGLETON);
        bind(PubMedService.class).in(Scopes.SINGLETON);
    }
}
