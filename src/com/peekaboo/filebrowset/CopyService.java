/**
 * 
 */
package com.peekaboo.filebrowset;

import java.util.ArrayList;
import java.util.List;

/**
 * @author peekaboo
 *
 */
public class CopyService
{
    private static List<FileItemData> sSourceItems = new ArrayList<FileItemData>();
    private static boolean sIsCut = false;
    
    public static List<FileItemData> getSourceItems()
    {
        return sSourceItems;
    }
    public static boolean isCut()
    {
        return sIsCut;
    }
    
    public static void copy(List<FileItemData> items)
    {
        setSourceItems(items);
        sIsCut = false;
    }
    
    public static void cut(List<FileItemData> items)
    {
        setSourceItems(items);
        sIsCut = true;
    }
    
    public static void clean()
    {
        sSourceItems.clear();
    }

    private static void setSourceItems(List<FileItemData> items)
    {
    	sSourceItems.clear();
        sSourceItems.addAll(items);
    }
}
