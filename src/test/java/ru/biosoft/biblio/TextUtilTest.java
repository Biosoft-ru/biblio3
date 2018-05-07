package ru.biosoft.biblio;

import org.junit.Test;

import static org.junit.Assert.*;

public class TextUtilTest
{
    @Test
    public void getField() throws Exception
    {
        assertEquals("3", TextUtil.getField("ID - 3\n", "ID -"));
    }

}