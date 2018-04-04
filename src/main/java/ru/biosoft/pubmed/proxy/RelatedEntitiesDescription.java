// $Id: RelatedEntitiesDescription.java,v 1.1 2008/10/31 11:16:03 tolstyh Exp $
package ru.biosoft.pubmed.proxy;

public class RelatedEntitiesDescription
{
    protected String database;
    protected String listQuery;
    protected String link;

    public String getDatabase()
    {
        return database;
    }
    public void setDatabase(String database)
    {
        this.database = database;
    }
    public String getListQuery()
    {
        return listQuery;
    }
    public void setListQuery(String listQuery)
    {
        this.listQuery = listQuery;
    }
    public String getLink()
    {
        return link;
    }
    public void setLink(String link)
    {
        this.link = link;
    }
}
