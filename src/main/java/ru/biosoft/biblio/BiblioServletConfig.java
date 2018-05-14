package ru.biosoft.biblio;

import com.developmentontheedge.be5.ServerModule;
import com.developmentontheedge.be5.modules.core.CoreModule;
import com.developmentontheedge.be5.servlet.TemplateModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;


public class BiblioServletConfig extends GuiceServletContextListener
{
    @Override
    protected Injector getInjector()
    {
        return Guice.createInjector(
                new ServerModule(),
                new CoreModule(),
                new TemplateModule(),
                new BiblioModule()
        );
    }
}