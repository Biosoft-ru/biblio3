// $Id: AddOperation.java,v 1.4 2008/11/03 10:43:31 tolstyh Exp $
package ru.biosoft.pubmed.proxy;

import com.developmentontheedge.be5.metadata.DatabaseConstants;
import com.developmentontheedge.be5.operation.OperationSupport;
import com.developmentontheedge.be5.util.Utils;
import com.developmentontheedge.beans.DynamicProperty;
import com.developmentontheedge.beans.DynamicPropertySetSupport;

import java.io.PrintWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.developmentontheedge.beans.BeanInfoConstants.TAG_LIST_ATTR;


public class AddOperation extends OperationSupport implements DatabaseConstants
{
    public static final String PMCLINK_TEMPLATE = "https://www.pubmedcentral.nih.gov/picrender.fcgi?artid=@id@&blobtype=pdf";

    private DynamicPropertySetSupport parameters = new DynamicPropertySetSupport();
    private String entry = null;

    @Override
    public Object getParameters(Map<String, Object> presetValues) throws Exception
    {
//        if( records.length == 0 )
//            return null;
//
//        List<String> vc = new ArrayList<String>();
//        int rootID = 1;
//        try
//        {
//            ResultSet rs = connector.executeQuery("SELECT root FROM categoriesEx WHERE entity='biblio2.publications'");
//            if( rs.next() )
//            {
//                rootID = rs.getInt(1);
//            }
//        }
//        catch( Exception e )
//        {
//            //use global root as root for this classification
//        }
//        String categoriesSQL = " select c1.ID AS \"Code\", concat( ifnull( concat(c8.name, '->' ), '' ), "
//                + "ifnull( concat( c7.name, '->' ), '' ), ifnull( concat( c6.name, '->' ), '' ), "
//                + "ifnull( concat( c5.name, '->' ), '' ), ifnull( concat( c4.name, '->' ), '' ), "
//                + "ifnull( concat( c3.name, '->' ), '' ), ifnull( concat( c2.name, '->' ), '' ), "
//                + "c1.name) AS \"Category\" from biblio2.categories c1 left join biblio2.categories c2 on c1.parentID = c2.ID "
//                + "left join biblio2.categories c3 on c2.parentID = c3.ID left join biblio2.categories c4 on c3.parentID = c4.ID "
//                + "left join biblio2.categories c5 on c4.parentID = c5.ID left join biblio2.categories c6 on c5.parentID = c6.ID "
//                + "left join biblio2.categories c7 on c6.parentID = c7.ID left join biblio2.categories c8 on c7.parentID = c8.ID "
//                + "WHERE c1.entity LIKE '%publications' and (c8.ID=" + rootID + " or c7.ID=" + rootID + " or c6.ID=" + rootID
//                + " or c5.ID=" + rootID + " or c4.ID=" + rootID + " or c3.ID=" + rootID + " or c2.ID=" + rootID + " or c1.ID=" + rootID
//                + ") " + "order by c1.entity, 2";
//
//        ResultSet rs = connector.executeQuery(categoriesSQL);
//        while( rs.next() )
//        {
//            String entry = rs.getString(2) + OperationSupport.TAG_DELIMITER + rs.getString(1);
//            vc.add(entry);
//        }
//
//        String keywords = null;
//        String comment = null;
//        Integer importance = new Integer(3);
//        String url = null;
//        String status = null;
//        boolean isNew = true;
//
//        rs = null;
//        try
//        {
//            rs = connector.executeQuery("SELECT keyWords,comment,importance,url,status FROM biblio2.publications WHERE PMID="
//                    + Utils.safestr(connector, records[0]));
//            if( rs.next() )
//            {
//                isNew = false;
//                keywords = rs.getString(1);
//                comment = rs.getString(2);
//                importance = rs.getInt(3);
//                url = rs.getString(4);
//                status = rs.getString(5);
//            }
//        }
//        finally
//        {
//            connector.close(rs);
//        }
//
//        if( isNew )
//        {
//            entry = MedlineImport.getEntryPromPubMed(out, records[0]);
//            if( entry != null )
//            {
//                String pmcid = MedlineImport.getField(entry, "PMC");
//                if( pmcid != null )
//                {
//                    int pos = pmcid.indexOf("PMC");
//                    url = PMCLINK_TEMPLATE.replaceAll("@id@", pmcid.substring(pos + 3).trim());
//                }
//            }
//        }
//
//        DynamicProperty prop = new DynamicProperty("pmid", "PubMed ID", String.class, records[0]);
//        prop.setReadOnly(true);
//        parameters.add(prop);
//
//        prop = new DynamicProperty("url", "URL", String.class, url);
//        prop.setCanBeNull(true);
//        parameters.add(prop);
//
//        prop = new DynamicProperty("importance", "Importance", Integer.class, importance);
//        parameters.add(prop);
//
//        prop = new DynamicProperty("status", "Status", String.class, status);
//        prop.setCanBeNull(true);
//        parameters.add(prop);
//
//        prop = new DynamicProperty("categoryID", "Category", String.class);
//        prop.setShortDescription("Category to be assigned");
//        prop.setAttribute(TAG_LIST_ATTR, vc.toArray(new String[0]));
//        parameters.add(prop);
//
//        prop = new DynamicProperty("keywords", "Key words", String.class, keywords);
//        prop.setCanBeNull(true);
//        prop.setAttribute(HtmlFormPropertyInspector.NCOLUMNS_ATTR, "100");
//        prop.setAttribute(HtmlFormPropertyInspector.NROWS_ATTR, "1");
//        parameters.add(prop);
//
//        prop = new DynamicProperty("comment", "Comment", String.class, comment);
//        prop.setCanBeNull(true);
//        prop.setAttribute(HtmlFormPropertyInspector.NCOLUMNS_ATTR, "100");
//        prop.setAttribute(HtmlFormPropertyInspector.NROWS_ATTR, "2");
//        parameters.add(prop);
//
        return parameters;
    }

    @Override
    public void invoke(Object parameters) throws Exception
    {
//        if( records.length == 0 )
//        {
//            out.write(localizedMessage("No records were selected") + "!<br />\n");
//            return;
//        }
//
//        String pmid = (String)parameters.getValue("pmid");
//        String id;
//        ResultSet rs = null;
//        try
//        {
//            rs = connector.executeQuery("SELECT id FROM biblio2.publications WHERE PMID=" + Utils.safestr(connector, pmid));
//            if( !rs.next() )
//            {
//                if( entry == null )
//                {
//                    out.write(localizedMessage("Can not load data from PubMed server") + "!<br />\n");
//                    return;
//                }
//
//                //add publication
//                MedlineImport.insertIntoDatabase(new PrintWriter(System.out), connector, "biblio2.publications", entry);
//
//                ResultSet rs2 = null;
//                try
//                {
//                    rs2 = connector.executeQuery("SELECT id FROM biblio2.publications WHERE PMID=" + pmid);
//                    if( !rs2.next() )
//                    {
//                        out.write("Can not import data from PubMed server");
//                        return;
//                    }
//                    id = rs2.getString(1);
//                }
//                finally
//                {
//                    connector.close(rs2);
//                }
//            }
//            else
//            {
//                id = rs.getString(1);
//            }
//        }
//        finally
//        {
//            connector.close(rs);
//        }
//
//        String query = "UPDATE biblio2.publications SET";
//        String comment = (String)parameters.getValue("comment");
//        if( comment == null )
//            comment = "";
//        query += " comment='" + Utils.safestr(connector, comment) + "',";
//
//        String keywords = (String)parameters.getValue("keywords");
//        if( keywords == null )
//            keywords = "";
//        query += " keyWords='" + Utils.safestr(connector, keywords) + "',";
//
//        Integer importance = (Integer)parameters.getValue("importance");
//        if( importance != null )
//        {
//            query += " importance=" + importance + ",";
//        }
//        String url = (String)parameters.getValue("url");
//        if( url != null )
//        {
//            query += " url='" + Utils.safestr(connector, url) + "',";
//        }
//        String status = (String)parameters.getValue("status");
//        if( status != null )
//        {
//            query += " status='" + Utils.safestr(connector, status) + "',";
//        }
//        query = query.substring(0, query.length() - 1) + " WHERE id=" + id;
//        connector.executeUpdate(query);
//
//        //add category
//        DatabaseAnalyzer analyzer = connector.getAnalyzer();
//
//        boolean bNewApproach = Utils.getColumnSize(connector, "classifications", "entity") > 0;
//
//        Object category = parameters.getValue("categoryID");
//
//        boolean isNumericCategoryID = Utils.isNumericColumn(connector, "classifications", "categoryID");
//        ArrayList categories = new ArrayList();
//        categories.add(category);
//
//        // repeat for parent categories
//        Object wcat = category;
//        try
//        {
//            do
//            {
//                wcat = new JDBCRecordAdapterAsQuery(connector,
//                        "SELECT c1.parentID AS \"p\" FROM biblio2.categories c1, biblio2.categories c2 WHERE c1.ID = "
//                                + ( isNumericCategoryID ? wcat : "'" + wcat + "'" )
//                                + " AND c2.ID = c1.parentID AND c2.parentID IS NOT NULL").getValue("p");
//
//                categories.add(wcat);
//            }
//            while( true );
//        }
//        catch( JDBCRecordAdapterAsQuery.NoRecord ignore )
//        {
//        }
//
//        String catList = Utils.toInClause(categories, isNumericCategoryID);
//
//        String idName = "";
//        String idValue = "";
//        if( connector.isOracle() )
//        {
//            idName = "ID, ";
//            idValue = "beIDGenerator.NEXTVAL, ";
//        }
//
//        String pk = primaryKey;
//
//        boolean isNumeric = Utils.isNumericColumn(connector, entity, pk);
//        String recordList = Utils.toInClause(new String[] {id}, isNumeric);
//
//        if( connector.isSQLServer() )
//            pk = "CAST( e." + analyzer.quoteIdentifier(pk) + " AS VARCHAR( 100 ) )";
//        else if( connector.isDb2() )
//            pk = "RTRIM( CAST( e." + analyzer.quoteIdentifier(pk) + " AS CHAR( 100 ) ) )";
//        else
//            pk = "e." + analyzer.quoteIdentifier(pk);
//
//        String sql = "INSERT INTO biblio2.classifications( ";
//        if( bNewApproach )
//        {
//            sql += idName + "entity, recordID, categoryID ) ";
//            sql += "SELECT " + idValue + "'" + Utils.safestr(connector, entity) + "', " + pk + ", c.ID ";
//        }
//        else
//        {
//            String conc = connector.getAnalyzer().makeConcatExpr(new String[] {"'" + Utils.safestr(connector, entity) + ".'", pk});
//            sql += idName + "recordID, categoryID ) ";
//            sql += "SELECT " + idValue + conc + ", c.ID ";
//        }
//
//        sql += "FROM " + entity + " e, biblio2.categories c ";
//        sql += "WHERE e." + analyzer.quoteIdentifier(primaryKey) + " IN " + recordList;
//        sql += "  AND c.ID IN " + catList;
//        connector.executeUpdate(sql);
//        out.write("<script language=\"Javascript1.1\">");
//        out.write("<!--" + "\n");
//        out.write("var formObj = parent.frames[0].document.forms[0];" + "\n");
//        out.write("formObj.submit();" + "\n");
//        out.write("//-->" + "\n");
//        out.write("</script>");
//        out.write("Operation complete<br><br>");
    }
}
