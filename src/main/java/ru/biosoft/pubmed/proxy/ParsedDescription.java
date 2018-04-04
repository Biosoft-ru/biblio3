// $Id: ParsedDescription.java,v 1.2 2010/02/19 06:11:45 tolstyh Exp $
package ru.biosoft.pubmed.proxy;

public class ParsedDescription
{
    protected String id;
    protected String title;
    protected String titleLink;
    protected String description;
    protected String resultNumber;
    protected String journal;
    public String getJournal()
    {
        return journal;
    }
    public void setJournal(String journal)
    {
        this.journal = journal;
    }
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public String getTitleLink()
    {
        return titleLink;
    }
    public void setTitleLink(String titleLink)
    {
        this.titleLink = titleLink;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    public String getResultNumber()
    {
        return resultNumber;
    }
    public void setResultNumber(String resultNumber)
    {
        this.resultNumber = resultNumber;
    }
}
