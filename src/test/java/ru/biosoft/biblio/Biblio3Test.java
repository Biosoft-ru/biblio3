package ru.biosoft.biblio;

import com.developmentontheedge.be5.server.ServerModule;
import com.developmentontheedge.be5.modules.core.CoreModule;
import com.developmentontheedge.be5.server.test.TestUtils;
import com.google.inject.Injector;
import com.google.inject.util.Modules;


public abstract class Biblio3Test extends TestUtils
{
    private static final Injector injector = initInjector(
            Modules.override(new ServerModule()).with(new SqlMockModule()),
            new CoreModule(),
            new BiblioModule()
    );

    @Override
    public Injector getInjector()
    {
        return injector;
    }
}
