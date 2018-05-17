package ru.biosoft.biblio;

import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;

import ru.biosoft.biblio.controllers.BibliographyController;
import ru.biosoft.biblio.controllers.PubMedInfo;
import ru.biosoft.biblio.services.MedlineImport;
import ru.biosoft.biblio.services.PubMedService;
import ru.biosoft.biblio.services.citeproc.PublicationProvider;
import ru.biosoft.biblio.services.citeproc.StyleService;


public class BiblioModule extends ServletModule
{
    @Override
    protected void configureServlets()
    {
        bind(PubMedInfo.class).in(Scopes.SINGLETON);
        bind(BibliographyController.class).in(Scopes.SINGLETON);

        serve("/api/pubMedInfo*").with(PubMedInfo.class);
        serve("/api/bibliography*").with(BibliographyController.class);

        bind(PublicationProvider.class).in(Scopes.SINGLETON);
        bind(MedlineImport.class).in(Scopes.SINGLETON);
        bind(PubMedService.class).in(Scopes.SINGLETON);
        bind(StyleService.class).in(Scopes.SINGLETON);
    }
}
