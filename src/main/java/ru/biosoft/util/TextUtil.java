/* $Id: TextUtil.java,v 1.1 2003/11/15 19:02:30 fedor Exp $ */
package ru.biosoft.util;

import java.util.*;

/**
 * Различные утилиты для работы с текстовыми данными.
 *
 * @version     2.5.0, 21 February 2000
 * @author      Fedor A. Kolpakov
 */
public class TextUtil
{
    /**
     * Parse integer from the string.
     * String can contains leading and trailing spaces.
     *
     * @param s string that contains the integer
     * @param from start position in the string
     */
    public static int parseInt(String s, int from) throws NumberFormatException
    {
        if (s == null)
            throw new NumberFormatException("Util: null string");

      int result = 0;
      boolean negative = false;
       int i=from, max = s.length();
      if (max < i)
           throw new NumberFormatException("Util: empty substring");

        // cut liders spaces
        while(s.charAt(i) == ' ' && i < max)
         i++;

        if (s.charAt(i) == '-')
        {
         negative = true;
         i++;
      }

        if (s.charAt(i) == '+')
          i++;

        while (i < max)
        {
          int digit = Character.digit(s.charAt(i++), 10);
         if (digit < 0)
              break;
         result = result*10 + digit;
        }

      if (negative)
           return -result;
        return result;
    }

    /**
     * Returns string, that represents float value,
     * with specified number of decimal didits.
     *
     * <p>If the value indeed is the integer,
     * than the decimal digits are omitted.
     *
     * @param value float value
     * @param decDig number of decimal digits
     */
    public static String valueOf(float value, int decDig)
    {
        String s = String.valueOf(value);

        int trimPosition = s.indexOf('.');
        if( trimPosition == -1 )
            return s;

        if( decDig != 0 )
            trimPosition += decDig + 1;

        if(trimPosition >= s.length())
        {
            return s;
        }
        return s.substring(0, trimPosition);
    }

    /**
     * Insert the specified string in other
     * string in specified position.
     *
     * @param str the string for insertion.
     * @param offset the insertion positin.
     * @param what the inserted string.
     */
    public static String insertToString(String str, int offset, String what)
    {
        StringBuffer answer = new StringBuffer(str.substring(0, offset));
        answer.append(what);
        answer.append(str.substring(offset));
        return answer.toString();
    }

    ////////////////////////////////////////

    /**
     * Returns the specified section from the specified string.
     *
     * Section is substring between <section> and </section>.
     *
     * @param section section name
     * @param str the string containing this section
     * @param offset position in the string from wich first section
     * will be found.
     */
    public static String getSection(String section, String str, int offset)
    {
        if( str != null )
        {
            String beg = '<'   + section;
            String end = "</" + section + '>';

            int start = str.indexOf(beg, offset);
            if( start != -1 )
            {
                start += beg.length();
                start = str.indexOf(">", start) + 1;
                int stop = str.indexOf(end, start);
                if( stop != -1 )
                    return str.substring(start, stop).trim();
/*                else
// DEBUG

System.out.println( "_getSection: section has not end\n" +
                    "Section : "   + section + '\n' +
                    "source  : \n" + str);
*/
            }
        }

        return null;
    }

    /**
     * Returns the specified section from the specified string.
     *
     * Section is substring between <section> and </section>.
     *
     * @param section section name
     * @param str the string containing this section
     */
    public static String getSection(String section, String str)
    {
        return getSection(section, str, 0);
    }

    /**
     * Convert HTML document to text.
     */
    public static String convertHTML(String html)
    {
        if( html.indexOf('<') < 0 )
            return html;

        StringBuffer text = new StringBuffer();
        boolean theEnd = false;
        int start = 0;
        int end;

        while( ! theEnd )
        {
            start = html.indexOf('<', start);
            start = html.indexOf('>', start+1) +1;

            end   = html.indexOf('<', start+1);
            if( end < 0 )
            {
                end = html.length();
                theEnd = true;
            }

            text.append( html.substring(start, end) );
        }

        return text.toString();
    }

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

    /**
     * Add the field to the specified string
     * if the field value is not empty.
     *
     * @param str string to which the field name and value
     * will be added.
     * @param fieldName field name.
     * @param fieldValue field value
     * @param maxStrLen the maximum field string length.
     * If the field value is longer then maxFieldLen,
     * it will be divided to several strings.
     */
   public static void addField(StringBuffer str,
                         String fieldName, String fieldValue,
                         int maxStrLen, String delimiter)
    {
      if(fieldValue != null )
        {
         fieldValue = fieldValue.trim();
            if(fieldValue.length() == 0)
            return;

         StringTokenizer tokens = new StringTokenizer(fieldValue, " \t\r\n");
         StringBuffer value = new StringBuffer();
         while(tokens.hasMoreTokens())
         {
            value.append(fieldName);
            value.append(delimiter);

            while(tokens.hasMoreTokens())
            {
                   value.append(tokens.nextToken());
               if(value.length() > maxStrLen)
                  break;
            }

            value.append("\r\n");
            str.append(value);
         }
      }
   }

    public static void addField(StringBuffer str,
                                String fieldName, String fieldValue,
                                int maxFieldLen)
   {
      addField(str, fieldName, fieldValue, maxFieldLen, "  ");
   }

    public static void addField(StringBuffer str,
                                String fieldName, String fieldValue)
    {
        addField(str, fieldName, fieldValue, 65, "  ");
    }

    public static void addField(StringBuffer str,
                                String fieldName, String fieldValue,
                                String delimiter)
    {
        addField(str, fieldName, fieldValue, 65, delimiter);
    }

    //////////////////////////////////////////
    //
    //   Utils for HTML formatting
    //
    public static String htmlMakeHTML(String text)
    {
        StringBuffer str = new StringBuffer();
        str.setLength(0);
        str.append("<HTML>");
        str.append(text);
        str.append("</HTML>");
        return new String(str);
    }

    public static String htmlMakeBold(String text)
    {
        StringBuffer str = new StringBuffer();
        str.setLength(0);
        str.append("<B>");
        str.append(text);
        str.append("</B>");
        return new String(str);
    }

    public static String htmlMakeItalic(String text)
    {
        StringBuffer str = new StringBuffer();
        str.setLength(0);
        str.append("<I>");
        str.append(text);
        str.append("</I>");
        return new String(str);
    }

    public static String htmlMakeUnderline(String text)
    {
        StringBuffer str = new StringBuffer();
        str.setLength(0);
        str.append("<U>");
        str.append(text);
        str.append("</U>");
        return new String(str);
    }

    public static String htmlMakeParagraph(String text)
    {
        StringBuffer str = new StringBuffer();
        str.setLength(0);
        str.append("<P>");
        str.append(text);
        str.append("</P>");
        return new String(str);
    }

    public static String htmlMakeChangeFont(String text, int diff)
    {
        StringBuffer str = new StringBuffer();
        str.setLength(0);
        str.append("<FONT ");
        if(diff > 0)
        {
            str.append("SIZE=+"+diff);
        }
        else
        {
            str.append("SIZE="+diff);
        }
        str.append(">");
        str.append(text);
        str.append("</FONT>");
        return new String(str);
    }
}
