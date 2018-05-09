/* $Id: TextUtil.java,v 1.1 2003/11/15 19:02:30 fedor Exp $ */
package ru.biosoft.biblio.util;


/**
 * Различные утилиты для работы с текстовыми данными.
 *
 * @version     2.5.0, 21 February 2000
 * @author      Fedor A. Kolpakov
 */
public class TextUtil
{
    /**
     * Returns specified field from the specified entry.
     *
     * <p>If field consists from several lines, they must be
     * a single block, without other fields inside them.
     *
     * @param entry entry text
     * @param field field (line) name
     * @param delimiter if the field consists from several lines
     * the specified string will be inserted as delimiter between lines.
     */
    public static String getField(String entry, String field, String delimiter)
    {
        int start = entry.indexOf(field);
        if( start == -1 )
            return null;
        if( start != 0 && entry.charAt(start-1) != '\n' )
        {
            start = entry.indexOf("\n"+field, start);
            if( start == -1 )
                return null;
            start++;
        }

        int end;
        StringBuffer answer = new StringBuffer();
        String s;
        boolean isFirst = true;
        while( entry.startsWith(field, start) || entry.startsWith("  ", start) || entry.startsWith("..", start))
        {
            end = entry.indexOf('\n', start + field.length() );
            s   = (entry.substring(start+field.length(), end)).trim();

            // append delimiter (space) between lines
            if( isFirst )
                isFirst = false;
            else
                answer.append(delimiter);

            answer.append(s);
            start = end+1;
        }

        return answer.toString();
    }

    /**
     * Returns specified field from the specified entry.<p>
     *
     * If the field consists from several lines space is used as delimiter.
     *
     * @param entry entry text
     * @param field field (line) name
     *
     * @see #getField(String entry, String field, String delimiter)
     */
    public static String getField(String entry, String field)
    {
        return getField(entry, field, " ");
    }

}
