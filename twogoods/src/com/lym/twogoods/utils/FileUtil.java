package com.lym.twogoods.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
/**
 * 与文件操作相关的工具类
 * 
 * @author yao
 *
 * */
public class FileUtil {
	
	 /** 
     * 创建文件夹，如果文件夹存在则不进行创建。 
     * @param path 
     * @throws Exception 
     * 
     *  @author yao
     */  
    public static void createFolder(String path) throws Exception{  
        path = separatorReplace(path);  
        File folder = new File(path);  
        if(folder.isDirectory()){  
            return;  
        }else if(folder.isFile()){  
            deleteFile(path);  
        }  
        folder.mkdirs();  
    }  
      
    /** 
     * 创建一个新的文件夹，如果文件夹存在，则删除后再创建。 
     * @param path 
     * @throws Exception 
     * 
     * @author yao
     */  
    public static void createNewFolder(String path) throws Exception{  
        path = separatorReplace(path);  
        File folder = new File(path);  
        if(folder.isDirectory()){  
            deleteFolder(path);  
        }else if(folder.isFile()){  
            deleteFile(path);  
        }  
        folder.mkdirs();  
    }  
      
    /** 
     * 创建一个文件，如果文件存在则不进行创建。 
     * @param path 
     * @throws Exception 
     * 
     * @author yao
     */  
    public static File createFile(String path) throws Exception{  
        path = separatorReplace(path);  
        File file = new File(path);  
        if(file.isFile()){  
            return file;  
        }else if(file.isDirectory()){  
            deleteFolder(path);  
        }  
        return createFile(file);  
    }  
      
    /** 
     * 创建一个新文件，如果存在同名的文件或文件夹将会删除该文件或文件夹， 
     * 如果父目录不存在则创建父目录。 
     * @param path 
     * @throws Exception 
     * 
     * @author yao
     */  
    public static File createNewFile(String path) throws Exception{  
        path = separatorReplace(path);  
        File file = new File(path);  
        if(file.isFile()){  
            deleteFile(path);  
        }else if(file.isDirectory()){  
            deleteFolder(path);  
        }  
        return createFile(file);  
    }  
      
    /** 
     * 分隔符替换 
     * window下测试通过 
     * @param path 
     * @return 
     * 
     * @author yao
     */  
    public static String separatorReplace(String path){  
        return path.replace("\\","/");  
    }  
      
    /** 
     * 创建文件及其父目录。 
     * @param file 
     * @throws Exception 
     * 
     * @author yao
     */  
    public static File createFile(File file) throws Exception{  
        createParentFolder(file);  
        if(!file.createNewFile()) {  
            throw new Exception("create file failure!");  
        }  
        return file;  
    }  
      
    /** 
     * 创建父目录 
     * @param file 
     * @throws Exception 
     * 
     * @author yao
     */  
    private static void createParentFolder(File file) throws Exception{  
        if(!file.getParentFile().exists()) {  
            if(!file.getParentFile().mkdirs()) {  
                throw new Exception("create parent directory failure!");  
            }  
        }  
    }  
      
    /** 
     * 根据文件路径删除文件，如果路径指向的文件不存在或删除失败则抛出异常。 
     * @param path 
     * @return 
     * @throws Exception 
     * 
     *  @author yao
     */  
    public static void deleteFile(String path) throws Exception {  
        path = separatorReplace(path);  
        File file = getFile(path);      
        if(!file.delete()){  
            throw new Exception("delete file failure");  
        }                        
    }  
      
    /** 
     * 删除指定目录中指定前缀和后缀的文件。 
     * @param dir 
     * @param prefix 
     * @param suffix 
     * @throws Exception 
     * 
     *  @author yao
     */  
    public static void deleteFile(String dir,String prefix,String suffix) throws Exception{       
        dir = separatorReplace(dir);  
        File directory = getFolder(dir);  
        File[] files = directory.listFiles();  
        for(File file:files){  
            if(file.isFile()){  
                String fileName = file.getName();  
                if(fileName.startsWith(prefix)&&fileName.endsWith(suffix)){  
                    deleteFile(file.getAbsolutePath());  
                }  
            }  
        }      
    }  
      
    /** 
     * 根据路径删除文件夹，如果路径指向的目录不存在则抛出异常， 
     * 若存在则先遍历删除子项目后再删除文件夹本身。 
     * @param path 
     * @throws Exception  
     * 
     * @author yao
     */  
    public static void deleteFolder(String path) throws Exception {  
        path = separatorReplace(path);  
        File folder = getFolder(path);  
        File[] files = folder.listFiles();   
        for(File file:files) {                  
            if(file.isDirectory()){  
                deleteFolder(file.getAbsolutePath());  
            }else if(file.isFile()){                      
                deleteFile(file.getAbsolutePath());                                   
            }  
        }    
        folder.delete();   
    }  
      
    /** 
     * 查找目标文件夹下的目标文件 
     * @param dir 
     * @param fileName 
     * @return 
     * @throws FileNotFoundException 
     * 
     * @author yao
     */  
    public static File searchFile(String dir,String fileName) throws FileNotFoundException{  
        dir = separatorReplace(dir);  
        File f = null;  
        File folder = getFolder(dir);  
        File[] files = folder.listFiles();   
        for(File file:files) {                  
            if(file.isDirectory()){  
                f =  searchFile(file.getAbsolutePath(),fileName);  
                if(f!=null){  
                    break;  
                }  
            }else if(file.isFile()){   
                if(file.getName().equals(fileName)){  
                    f = file;  
                    break;  
                }                                                             
            }  
        }            
        return f;  
    }  
            
    /** 
     * 获得文件类型。 
     * @param path：文件路径 
     * @return 
     * @throws FileNotFoundException  
     * 
     * @author yao
     */  
    public static String getFileType(String path) throws FileNotFoundException {  
        path = separatorReplace(path);  
        File file = getFile(path);  
        String fileName = file.getName();  
        String[] strs = fileName.split("\\.");  
        if(strs.length<2){  
            return "unknownType";  
        }  
        return strs[strs.length-1];  
    }  
      
    /** 
     * 根据文件路径，获得该路径指向的文件的大小。 
     * @param path 
     * @return 
     * @throws FileNotFoundException 
     * 
     * @author yao
     */  
    public static long getFileSize(String path) throws FileNotFoundException{  
        path = separatorReplace(path);        
        File file = getFile(path);  
        return file.length();  
    }  
      
    /** 
     * 根据文件夹路径，获得该路径指向的文件夹的大小。 
     * 遍历该文件夹及其子目录的文件，将这些文件的大小进行累加，得出的就是文件夹的大小。 
     * @param path 
     * @return 
     * @throws FileNotFoundException 
     * 
     * @author yao
     */  
    public static long getFolderSize(String path) throws FileNotFoundException{  
        path = separatorReplace(path);                
        long size = 0;  
        File folder = getFolder(path);  
        File[] files = folder.listFiles();  
        for(File file:files){  
            if(file.isDirectory()){  
                size += getFolderSize(file.getAbsolutePath());  
            }else if(file.isFile()){  
                size += file.length();  
            }  
        }  
        return size;  
    }  
      
    /** 
     * 通过路径获得文件， 
     * 若不存在则抛异常， 
     * 若存在则返回该文件。 
     * @param path 
     * @return 
     * @throws FileNotFoundException 
     * 
     * @author yao
     */  
    public static File getFile(String path) throws FileNotFoundException{  
        path = separatorReplace(path);                
        File file = new File(path);  
        if(!file.isFile()){  
            throw new FileNotFoundException("file not found!");  
        }  
        return file;  
    }  
      
    /** 
     * 通过路径获得文件夹， 
     * 若不存在则抛异常， 
     * 若存在则返回该文件夹。 
     * @param path 
     * @return 
     * @throws FileNotFoundException
     * 
     *  @author yao
     */  
    public static File getFolder(String path) throws FileNotFoundException{  
        path = separatorReplace(path);                
        File folder = new File(path);  
        if(!folder.isDirectory()){  
            throw new FileNotFoundException("folder not found!");  
        }  
        return folder;  
    }  
      
    /** 
     * 获得文件最后更改时间。 
     * @param path 文件的目录
     * @return 
     * @throws FileNotFoundException 
     * 
     * @author yao
     */  
    public static Date getFileLastModified(String path) throws FileNotFoundException{  
        path = separatorReplace(path);                
        File file = getFile(path);  
        return new Date(file.lastModified());  
    }  
      
    /** 
     * 获得文件夹最后更改时间。 
     * @param path 文件夹的目录
     * @return 
     * @throws FileNotFoundException 
     * 
     * @author yao
     */  
    public static Date getFolderLastModified(String path) throws FileNotFoundException{  
        path = separatorReplace(path);        
        File folder = getFolder(path);  
        return new Date(folder.lastModified());  
    }  
}
