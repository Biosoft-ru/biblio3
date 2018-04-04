/** $Id: HttpAddFile.java,v 1.2 2004/05/19 13:16:54 fedor Exp $ */

package ru.biosoft.biblio;

import com.developmentontheedge.be5.operation.OperationSupport;

import java.text.SimpleDateFormat;
import java.io.Writer;
import java.util.Map;
import java.util.Date;


public class HttpAddFile extends OperationSupport
{
    @Override
    public void invoke(Object parameters) throws Exception
    {

    }
//    private String submitter_id;
//    private String bug_id;
//
//    public String getCustomAction()
//    {
//        StringBuffer action = new StringBuffer( super.getCustomAction() );
//        if( entity.equals( "resumes" ) )
//        {
//            action.append( "&actualAt=" ).append( new SimpleDateFormat( "yyyy-MM-dd" ).format( new Date() ) );
//        }
//        else if( entity.equals( "bugs.attachments" ) )
//        {
//            action = new StringBuffer( "insertBlob" );
//            action.append( '?' ).append( TABLE_NAME_PARAM ).append( '=' ).append( entity );
//            action.append( '&' ).append( BLOB_FILENAME_COLUMN_PARAM ).append( "=filename" );
//            action.append( '&' ).append( BLOB_TYPE_COLUMN_PARAM ).append( "=mimetype" );
//            action.append( '&' ).append( BLOB_DATA_COLUMN_PARAM ).append( "=thedata" );
//            action.append( "&submitter_id=" ).append( submitter_id );
//            action.append( "&bug_id=" ).append( bug_id );
//        }
//        return action.toString();
//    }
}
