package ru.biosoft.biblio;

import com.developmentontheedge.be5.modules.core.CoreModule;
import com.developmentontheedge.be5.server.ScopedServerModule;
import com.developmentontheedge.be5.server.servlet.Be5ServletListener;
import com.developmentontheedge.be5.server.servlet.TemplateModule;

import com.google.inject.Guice;
import com.google.inject.Injector;


public class BiblioServletConfig extends Be5ServletListener
{
    @Override
    protected Injector getInjector()
    {
        return Guice.createInjector(getStage(),
                new CoreModule(),
                new ScopedServerModule(),
                new TemplateModule(),
                new BiblioModule()
        );
    }
}
