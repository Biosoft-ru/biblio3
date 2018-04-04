/** $Id: FillFromMedlineOperation.java,v 1.2 2005/04/04 09:03:05 fedor Exp $ */

package ru.biosoft.biblio;

import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.sql.ResultSet;
import java.util.StringTokenizer;

import com.developmentontheedge.be5.metadata.DatabaseConstants;
import com.developmentontheedge.be5.operation.OperationSupport;
import ru.biosoft.util.TextUtil;


public class FillFromMedlineOperation extends OperationSupport implements DatabaseConstants
{
    @Override
    public void invoke(Object parameters) throws Exception
    {

    }
    //    public void invoke( Writer out, DatabaseConnector connector ) throws Exception
//    {
//        if( records.length == 0 )
//        {
//            out.write( "No records were specified!<br />\n" );
//            return;
//        }
//
//        String table = connector.getAnalyzer().quoteIdentifier(entity);
//
//        for(int i=0;i<records.length; i++)
//            MedlineImport.fill(out, connector, table, records[i]);
//    }
}
