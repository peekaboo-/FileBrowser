package com.peekaboo.filebrowset.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Locale;

import android.content.Context;
import android.widget.Toast;

public class FileUtil
{
//    public static boolean copyFile(File srcFile, File destFile) throws IOException
//    {
//        if ((!srcFile.exists()) || (srcFile.isDirectory())) {
//            return false;
//        }
//        if (!destFile.exists()) {
//           return new File(destFile.getAbsolutePath()).createNewFile();
//        }
//        return false;
//    }
    
    /** 
     * 复制单个文件 
     * @param oldPath String 原文件路径 如：c:/fqf.txt 
     * @param newPath String 复制后路径 如：f:/fqf.txt 
     * @return boolean 
     */ 
    public static boolean copyFile(String oldPath, String newPath) { 
        try { 
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(oldPath))); 
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newPath))); 
                String content = null;
                while ((content = bufferedReader.readLine()) != null) {
                    bufferedWriter.write(content, 0, content.length());
                }
                bufferedWriter.flush(); 
                bufferedReader.close(); 
                bufferedWriter.close();
            return true;
        } 
        catch (Exception e) { 
            System.out.println("复制单个文件操作出错"); 
            e.printStackTrace(); 
            return false;
        } 
    } 

   /** 
     * 复制整个文件夹内容 
     * @param oldPath String 原文件路径 如：c:/fqf 
     * @param newPath String 复制后路径 如：f:/fqf/ff 
     * @return boolean 
     */ 
   public static boolean copyFolder(String oldPath, String newPath) { 

       try { 
           (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹 
           File a=new File(oldPath); 
           String[] file=a.list(); 
           File temp=null; 
           for (int i = 0; i < file.length; i++) { 
               if(oldPath.endsWith(File.separator)){ 
                   temp=new File(oldPath+file[i]); 
               } 
               else{ 
                   temp=new File(oldPath+File.separator+file[i]); 
               } 

               if(temp.isFile()){ 
                   FileInputStream input = new FileInputStream(temp); 
                   FileOutputStream output = new FileOutputStream(newPath + "/" + 
                           (temp.getName()).toString()); 
                   byte[] b = new byte[1024 * 5]; 
                   int len; 
                   while ( (len = input.read(b)) != -1) { 
                       output.write(b, 0, len); 
                   } 
                   output.flush(); 
                   output.close(); 
                   input.close(); 
               } 
               if(temp.isDirectory()){//如果是子文件夹 
                   copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]); 
               } 
           } 
           return true;
       } 
       catch (Exception e) { 
           System.out.println("复制整个文件夹内容操作出错"); 
           e.printStackTrace(); 
           return false;
       } 

   }
   
   public static boolean reName(Context mContext, File file, String newPath)
   {
       boolean result = file.renameTo(new File(newPath));
       Toast.makeText(mContext, result ? "改名成功" : "改名失败", Toast.LENGTH_LONG).show();
       return result;
   }
   
   /**
    * return file extension without heading dot
    * 
    * @param fileName
    * @return
    */
   public static String getFileExtension(String fileName)
   {
       int dot_pos = fileName.lastIndexOf('.');
       if (0 <= dot_pos) {
           return fileName.substring(dot_pos + 1).toLowerCase(Locale.getDefault());
       }
       
       return "";
   }
   
}
