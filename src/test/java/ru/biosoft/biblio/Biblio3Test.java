package ru.biosoft.biblio;

import com.developmentontheedge.be5.modules.core.CoreModule;
import com.developmentontheedge.be5.test.TestUtils;
import com.google.inject.Injector;
import com.google.inject.util.Modules;


public abstract class Biblio3Test extends TestUtils
{
    private static final Injector injector = initInjector(
            Modules.override(
                    new CoreModule(),
                    new BiblioModule())
                .with(new DbMockTestModule())
    );

    @Override
    public Injector getInjector()
    {
        return injector;
    }
}
