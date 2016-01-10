package com.lym.twogoods.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;

import com.lym.twogoods.manager.DiskCacheManager;

import android.text.TextUtils;
import android.util.Log;
/**
 * 与文件操作相关的工具类
 * 
 * @author 尧俊锋
 *
 * */
public class FileUtil {
	
	/**
	 * 判断文件或目录是否存在
	 * 
	 * @param path 要判断的文件或目录路径
	 * @return 存在返回true，不存在返回false
	 * 
	 * @author mao
	 */
	public static boolean exist(String path) {
		if(TextUtils.isEmpty(path)) {
			return false;
		}
		File f = new File(path);
		return f.exists();
	}
	
	 /** 
     * 创建文件夹，如果文件夹存在则不进行创建。 
     * @param 要创建的文件夹路径 
     * 
     * 
     *  @author 尧俊锋
     */  
    public static void createFolder(String path) {  
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
     * 创建一个文件，如果文件存在则不进行创建。 
     * @param path 文件的路径
     * 
     * @author 尧俊锋
     */  
    public static File createFile(String path) {  
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
     * 分隔符替换 
     * window下测试通过 
     * @param path 
     * @return 
     * 
     * @author 尧俊锋
     */  
    public static String separatorReplace(String path){  
        return path.replace("\\","/");  
    }  
      
    /** 
     * 创建文件及其父目录。 如果创建失败，返回null
     * @param file 
     * @throws Exception 
     * 
     * @author 尧俊锋
     */  
    public static File createFile(File file) {  
        try {
			createParentFolder(file);
			if(!file.createNewFile()) {  
				return null;
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}  
        
        return file;  
    }  
      
    /** 
     * 创建父目录 
     * @param file 
     * @return false 创建失败。true 创建成功
     * @author 尧俊锋
     */  
    private static boolean createParentFolder(File file) {  
        if(!file.getParentFile().exists()) {  
            if(!file.getParentFile().mkdirs()) {
            	return false;
            }  
        }  
        return true;
    }  
      
    /** 
     * 根据文件路径删除文件，如果路径指向的文件不存在或删除失败则返回false。 
     * @param path 文件路径
     * @return 路径指向的文件不存在或删除失败则返回false。 否则返回true
     * 
     *  @author 尧俊锋
     */  
    public static boolean deleteFile(String path)  {  
        path = separatorReplace(path);  
        File file = getFile(path);  
        if(file==null)
        	return false;
        if(!file.delete()){  
        	return false;
        }                
        return true;
    }  
      
    /** 
     * 删除指定文件夹目录中指定前缀和后缀的文件。 
     * @param dir 指定文件夹路径
     * @param prefix 文件前缀
     * @param suffix 文件后缀
     * 
     * @return 文件夹不存在或者没有给定前缀和后缀的文件，返回false;否则返回true
     * 
     *  @author 尧俊锋
     */  
    public static boolean deleteFile(String dir,String prefix,String suffix) {       
        dir = separatorReplace(dir);  
        File directory = getFolder(dir); 
        if(directory==null)
        	return false;
        File[] files = directory.listFiles();  
        for(File file:files){  
            if(file.isFile()){  
                String fileName = file.getName();  
                if(fileName.startsWith(prefix)&&fileName.endsWith(suffix)){  
                	if(deleteFile(file.getAbsolutePath()))
                		return true;
                }  
            }  
        }   
        return false;
    }  
      
    /** 
     * 根据路径删除文件夹，如果路径指向的目录不存在则返回false， 
     * 若存在则先遍历删除子项目后再删除文件夹本身。 
     * @param path 文件夹路径
     * 
     * @return 如果文件夹不存在或者删除不成功，返回false.否则返回true
     * @author 尧俊锋
     */  
    public static boolean deleteFolder(String path) {  
        path = separatorReplace(path);  
        File folder = getFolder(path);  
        if(folder==null)
        	return false;
        File[] files = folder.listFiles();   
        for(File file:files) {                  
            if(file.isDirectory()){  
                deleteFolder(file.getAbsolutePath());  
            }else if(file.isFile()){                      
                deleteFile(file.getAbsolutePath());                                   
            }  
        }    
       return folder.delete();   
    }  
      
    /** 
     * 查找目标文件夹下的目标文件 
     * @param dir 文件夹路径
     * @param fileName 文件名字
     * @return 如果文件夹不存在，或者在文件夹中找不到文件，返回null。否则返回目标文件
     * 
     * @author 尧俊锋
     */  
    public static File searchFile(String dir,String fileName) {  
        dir = separatorReplace(dir);  
        File f = null;  
        File folder = getFolder(dir); 
        if(folder==null)
        	return null;
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
     * 
     * @return 如果给定路径不存在文件,或者是未知的文件类型都会返回null。否则返回文件的类型。
     * 
     * @author 尧俊锋
     */  
    public static String getFileType(String path){  
        path = separatorReplace(path);  
        File file = getFile(path);  
        if(file==null)
        	return null;
        String fileName = file.getName();  
        String[] strs = fileName.split("\\.");  
        if(strs.length<2){  
            return null;  
        }  
        return strs[strs.length-1];  
    }  
      
    /** 
     * <p>
     * 根据路径，获得该路径指向的文件的大小。 
     * </p>
     * 
     * @param path 文件的路径
     * @return 如果返回值小于0，说明给定路径不存在文件。否则返回值为文件的大小
     * 
     * @author 尧俊锋
     */  
    public static long getFileSize(String path) {  
        path = separatorReplace(path);        
        File file = getFile(path); 
        if(file==null)
        	return -1;
        return file.length();  
    }  
      
    /** 
     * <p>
     * 根据文件夹路径，获得该路径指向的文件夹的大小。 
     * </p>
     * 遍历该文件夹及其子目录的文件，将这些文件的大小进行累加，得出的就是文件夹的大小。 
     * 
     * @param path 文件夹的路径
     * @return 如果返回值小于0，说明给定路径不存在文件夹。否则返回值为文件夹的大小
     * 
     * @author 尧俊锋
     */  
    public static long getFolderSize(String path){  
        path = separatorReplace(path);                
        long size = 0;  
        File folder = getFolder(path);  
        if(folder==null)
        	return -1;
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
     * 若不存在返回null,
     * 若存在则返回该文件。 
     * @param path 
     * @return 
     * @throws FileNotFoundException 
     * 
     * @author 尧俊锋
     */  
    public static File getFile(String path){  
        path = separatorReplace(path);                
        File file = new File(path);  
        if(file.isFile()){ 
        	return file;
        }  
        return null;  
    }  
      
    /** 
     * 通过路径获得文件夹， 
     * 若不存在则返回空， 
     * 若存在则返回该文件夹。 
     * @param path 文件夹的路径
     * @return 若该路径不存在文件夹，返回null;否则返回文件夹
     * 
     *  @author 尧俊锋
     */  
    public static File getFolder(String path) {  
        path = separatorReplace(path);                
        File folder = new File(path);  
        if(folder.isDirectory()){ 
        	return folder;
        }  
        return null;  
    }  
      
    /** 
     * 获得文件最后更改时间。 如果给定路径的文件不存在，则返回空
     * @param path 文件的目录
     * @return 如果给定路径的文件不存在，返回null;否则返回文件的最后修改时间
     * 
     * @author 尧俊锋
     */  
    public static Date getFileLastModified(String path) {  
        path = separatorReplace(path);                
        File file = getFile(path);  
        if(file==null)
        	return null;
        return new Date(file.lastModified());  
    }  
      
    /** 
     * 获得文件夹最后更改时间。 如果给定路径的文件夹不存在，则返回空
     * @param path 文件夹的目录
     * @return  如果给定路径的文件夹不存在，则返回null;否则返回最后修改时间
     * 
     * @author 尧俊锋
     */  
    public static Date getFolderLastModified(String path){  
        path = separatorReplace(path);        
        File folder = getFolder(path); 
        if(folder==null)
        	return null;
        return new Date(folder.lastModified());  
    }  
    
    public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值
    /**
     * 获取文件指定文件的指定单位的大小
     * 
     * @param filePath
     *            文件路径
     * @param sizeType
     *            获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
     File file = new File(filePath);
     long blockSize = 0;
     try {
      if (file.isDirectory()) {
       blockSize = getFileSizes(file);
      } else {
       blockSize = getFileSize(file);
      }
     } catch (Exception e) {
      e.printStackTrace();
      Log.e("获取文件大小", "获取失败!");
     }
     return FormetFileSize(blockSize, sizeType);
    }
    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     * 
     * @param filePath
     *            文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
     File file = new File(filePath);
     long blockSize = 0;
     try {
      if (file.isDirectory()) {
       blockSize = getFileSizes(file);
      } else {
       blockSize = getFileSize(file);
      }
     } catch (Exception e) {
      e.printStackTrace();
      Log.e("获取文件大小", "获取失败!");
     }
     return FormetFileSize(blockSize);
    }
    
    /**
     * 获取指定文件大小
     * 
     * @param f
     * @return
     */
    public static long getFileSize(File file){
     long size = 0;
     try {
     if (file.exists()) {
      FileInputStream fis = null;
	  fis = new FileInputStream(file);
	  size = fis.available();
      } else {
       file.createNewFile();
       Log.e("获取文件大小", "文件不存在!");
      }
	} catch (Exception e) {
		e.printStackTrace();
	}
     return size;
    }
    /**
     * 获取指定文件夹
     * 
     * @param f
     * @return
     */
    public static long getFileSizes(File f) {
     long size = 0;
     File flist[] = f.listFiles();
     for (int i = 0; i < flist.length; i++) {
      if (flist[i].isDirectory()) {
       size = size + getFileSizes(flist[i]);
      } else {
       size = size + getFileSize(flist[i]);
      }
     }
     return size;
    }
    /**
     * 转换文件大小
     * 
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
     DecimalFormat df = new DecimalFormat("#.00");
     String fileSizeString = "";
     String wrongSize = "0B";
     if (fileS == 0) {
      return wrongSize;
     }
     if (fileS < 1024) {
      fileSizeString = df.format((double) fileS) + "B";
     } else if (fileS < 1048576) {
      fileSizeString = df.format((double) fileS / 1024) + "KB";
     } else if (fileS < 1073741824) {
      fileSizeString = df.format((double) fileS / 1048576) + "MB";
     } else {
      fileSizeString = df.format((double) fileS / 1073741824) + "GB";
     }
     return fileSizeString;
    }
    /**
     * 转换文件大小,指定转换的类型
     * 
     * @param fileS
     * @param sizeType
     * @return
     */
    public static double FormetFileSize(double fileS, int sizeType) {
     DecimalFormat df = new DecimalFormat("#.00");
     double fileSizeLong = 0;
     switch (sizeType) {
     case SIZETYPE_B:
      fileSizeLong = Double.valueOf(df.format((double) fileS));
      break;
     case SIZETYPE_KB:
      fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
      break;
     case SIZETYPE_MB:
      fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
      break;
     case SIZETYPE_GB:
      fileSizeLong = Double.valueOf(df
        .format((double) fileS / 1073741824));
      break;
     default:
      break;
     }
     return fileSizeLong;
    }
    /**
     * 拿到压缩后的图片文件路径
     * @param filepath 要被压缩的图片路径
     * @param folderPath 被压缩后的图片存放的文件夹路径
     * @return path 被压缩后的图片路径
     */
    public static String getCompressFile(String filepath,String folderPath)
    {
    	String filename = "sent"+TimeUtil.getCurrentMilliSecond()+".jpg";
		String path = folderPath+filename;
		ImageUtil.saveBitmap(path,ImageUtil.compressImage(filepath));
		return path;
    }
}
