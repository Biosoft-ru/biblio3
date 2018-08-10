package ru.biosoft.biblio;

import com.developmentontheedge.be5.modules.core.CoreModule;
import com.developmentontheedge.be5.server.servlet.Be5GuiceServletContextListener;
import com.developmentontheedge.be5.server.servlet.TemplateModule;

import com.google.inject.Guice;
import com.google.inject.Injector;


public class BiblioServletConfig extends Be5GuiceServletContextListener
{
    @Override
    protected Injector getInjector()
    {
        return Guice.createInjector(getStage(),
                new CoreModule(),
                new TemplateModule(),
                new BiblioModule()
        );
    }
}
