package ru.biosoft.biblio.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class StyleInfo
{
    public String title;
    public String format;
    public List<String> categories = new ArrayList<>();
    public String parent;
    public String inline;
    public String bibliography;
    public Timestamp updated;

    @Override
    public String toString()
    {
        return "StyleInfo{" +
                "title='" + title + '\'' +
                ", format='" + format + '\'' +
                ", categories=" + categories +
                ", parent='" + parent + '\'' +
                ", updated=" + updated +
                '}';
    }
}
