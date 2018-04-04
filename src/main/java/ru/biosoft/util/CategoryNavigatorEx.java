/* $Id: CategoryNavigatorEx.java,v 1.4 2010/02/18 09:53:37 tolstyh Exp $ */
package ru.biosoft.util;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Allows to specify database and root category or each entity.
 * These information is stored in specal table categoriesEx:
 * - entity
 * - db
 * - root - start category
 */
public class CategoryNavigatorEx// extends CategoryNavigator
{
    //private static LoggingHandle cat = Logger.getHandle(CategoryNavigatorEx.class);
//
//    public static class Category
//    {
//        public String entity;
//        public String db;
//        public String root;
//
//        public String toString()
//        {
//            return entity + ":" + db + ", " + root;
//        }
//    }
//
//    private static Map<String, Category> categoriesMap;
//
//    private static void initCategories(DatabaseConnector connector)
//    {
//        if( categoriesMap == null )
//        {
//            categoriesMap = new HashMap();
//
//            ResultSet rs = null;
//            try
//            {
//                rs = connector.executeQuery("SELECT entity, db, root FROM categoriesEx");
//                Category c;
//
//                while( rs.next() )
//                {
//                    c = new Category();
//                    c.entity = rs.getString(1);
//                    c.db     = rs.getString(2);
//                    c.root   = rs.getString(3);
//
//                    categoriesMap.put(c.entity, c);
//                }
//
//                Logger.info(cat, "CategoriesEx map: " + categoriesMap);
//
//            }
//            catch(Exception e)
//            {
//                Logger.error(cat, "Can not load categoriesMap, error="  + e, e);
//            }
//            finally
//            {
//                connector.close(rs);
//            }
//        }
//    }
//
//    public void init( DatabaseConnector connector, String category, QueryInfo qi, UserInfo ui, Map messages ) throws Exception
//    {
//        initCategories(connector);
//
//        super.init(connector, category, qi, ui, messages);
//
//        Category c  = categoriesMap.get(entity);
//        if( c != null )
//        {
//            this.root = c.root;
//
//            if( category == null )
//                this.category = c.root;
//
//            if( !Utils.isEmpty(c.db) )
//            {
//                categoriesTable = c.db + ".categories";
//                classificationsTable = c.db + ".classifications";
//            }
//        }
//    }
//
//    public boolean hasCategories()
//    {
//        if( categoriesMap.containsKey(entity) )
//            return !Utils.isEmpty( categoriesMap.get(entity).root );
//
//        return !Utils.isEmpty(root);
//    }
}
