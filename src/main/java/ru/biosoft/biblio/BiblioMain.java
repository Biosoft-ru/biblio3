package ru.biosoft.biblio;

import com.developmentontheedge.be5.jetty.EmbeddedJetty;

public class BiblioMain
{
    public static void main(String... args) throws Exception
    {
        new EmbeddedJetty().run();
    }
}
