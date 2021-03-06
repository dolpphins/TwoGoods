package com.lym.twogoods.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.text.TextUtils;

/**
 * 与图片操作相关的工具类
 * 
 * @author 尧俊锋
 *
 * */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.lym.twogoods.screen.BaseScreen;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
/**
 * 与图片操作相关的工具类
 * 
 * @author 尧俊锋
 *
 * */
public class ImageUtil {
	
	
	 /**
	   * @description 计算图片的压缩比率
	   *
	   * @param options 原图片的参数
	   * @param reqWidth 目标的宽度
	   * @param reqHeight 目标的高度
	   * @retrue 缩放比
	   */
	  private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // 源图片的高度和宽度
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	    if (height > reqHeight || width > reqWidth) {
	      // 计算出实际宽高和目标宽高的比率
	      final int halfHeight = height / 2;
	      final int halfWidth = width / 2;
	      while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
	        inSampleSize *= 2;
	      }
	    }
	    return inSampleSize;
	  }
	  /**
	   * @description 通过传入的bitmap，进行压缩，得到符合标准的bitmap
	   *
	   * @param src
	   * @param dstWidth
	   * @param dstHeight
	   * 
	   * @return 缩放后的bitmap
	   * 
	   * @author 尧俊锋
	   */
	  private static Bitmap createScaleBitmap(Bitmap src, int dstWidth, int dstHeight, int inSampleSize) {
	    //如果inSampleSize是2的倍数，也就说这个src已经是我们想要的缩略图了，直接返回即可。
	    if (inSampleSize % 2 == 0) {
	      return src;
	    }
	    if(src==null)
	    	return null;
	    // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响，我们这里是缩小图片，所以直接设置为false
	    Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
	    if (src != dst) { // 如果没有缩放，那么不回收
	      src.recycle(); // 释放Bitmap的native像素数组
	    }
	    return dst;
	  }
	  /**
	   * @description 从Resources中加载图片
	   *
	    * @param res 项目资源。
		 * @param width 被缩放后的width,
		 * @param height 被缩放后的height
		 * 
		 * @return Bitmap
		 * 
		 * @author 尧俊锋
	   */
	  public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true; // 设置成了true,不占用内存，只获取bitmap宽高
	    BitmapFactory.decodeResource(res, resId, options); // 读取图片长款
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight); // 调用上面定义的方法计算inSampleSize值
	    // 使用获取到的inSampleSize值再次解析图片
	    options.inJustDecodeBounds = false;
	    Bitmap src = BitmapFactory.decodeResource(res, resId, options); // 载入一个稍大的缩略图
	    return createScaleBitmap(src, reqWidth, reqHeight, options.inSampleSize); // 进一步得到目标大小的缩略图
	  }
	  	/**
	  	 * 获取指定路径下的图片的指定大小的缩略图 
		 * 
		 * @param imagePath 完整的图片所在路径和图片名字。
		 * @param width 被缩放后的width,
		 * @param height 被缩放后的height
		 * 
		 * @return Bitmap
		 * 
		 * @author 尧俊锋
		 */
	  public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight) {
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(pathName, options);
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	    options.inJustDecodeBounds = false;
	    Bitmap src = BitmapFactory.decodeFile(pathName, options);
	    return createScaleBitmap(src, reqWidth, reqHeight, options.inSampleSize);
	  }

	/**
	 * 回收垃圾 recycle
	 * 
	 * @throws
	 * 
	 * @author 尧俊锋
	 */
	public static void recycle(Bitmap bitmap) {
		// 先判断是否已经回收
		if (bitmap != null && !bitmap.isRecycled()) {
			// 回收并且置为null
			bitmap.recycle();
			bitmap = null;
		}
		System.gc();
	}
	
	/**
	 * 从网上获取图片，返回图片在指定大小缩放后的bitmap。调用decodeUrl2InputStreamFromNet
	 * 和decodeUrl2InputStreamFromNet来实现
	 * @param url 图片的url
	 * @return 网络图片的bitmap
	 */
	public static Bitmap decodeBitmapFromNet(String url,int width,int height)
	{
		InputStream is = decodeUrl2InputStreamFromNet(url);
		return getBitmapThumbnail(is,width,height);
	}
	
	/**
	 * 通过网络url获取图片
	 *
	 * @param url 要获取的图片url
	 * 
	 * @param 获取成功返回一幅位图,获取失败返回null.
	 *  
	 * @author 麦灿标
	 * */
	public static Bitmap decodeBitmapFromNet(String url) {
		if(TextUtils.isEmpty(url)) {
			return null;
		}
		InputStream is = decodeUrl2InputStreamFromNet(url);
		return BitmapFactory.decodeStream(is);//如果is为null那么decodeStream方法会返回null
	}
	
	/**
	 * 从网上获取图片，返回图片的InputStream。
	 * @param url 图片的url
	 * @return 网络图片的InputStream
	 * @author 尧俊锋
	 */
	private static InputStream decodeUrl2InputStreamFromNet(String url)
	{
		URL myFileUrl = null; 
		InputStream is = null;
		try { 
			myFileUrl = new URL(url); 
		} catch (MalformedURLException e) { 
			e.printStackTrace(); 
		} 
		try { 
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection(); 
			conn.setDoInput(true); 
			conn.connect(); 
			is = conn.getInputStream(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
		return is;
	}
	
	
	/**
	 * 获得图片inputStream在缩放到指定大小后的bitmap
	 * @param is 图片的输入流
	 * @param width 要缩放到的宽度
	 * @param height 要缩放到的高度
	 * @author 尧俊锋
	 */
	private static Bitmap getBitmapThumbnail(InputStream is,int width,int height)
	{
		if(is==null)
			return null;
		final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
		Bitmap bt = null;
		bt = BitmapFactory.decodeStream(is, null, options);
		options.inSampleSize = calculateInSampleSize(options, width, height);
	    options.inJustDecodeBounds = false;
	    return createScaleBitmap(bt, width, height, options.inSampleSize);
	}

	/**
	 * <p>
	 * 获取给定路径的图片.考虑到需适应ImageView的大小,不建议使用这个方法获取bitmap.建议调用getImageThumbnail
	 * 或者decodeSampledBitmapFromFile来适应ImageView的大小。具体实现:先获取ImageView的宽高,作为getImageThumbnail和
	 * decodeSampledBitmapFromFile函数的相应参数,从而进行适应大小.
	 * </p>
	 * 
	 * @param imagePath 图片所在的完整路径和文件名
	 * 
	 * @return 如果imagPath指定的文件为空或者不能转化Bitmap,返回null;否则返回bitmap
	 * 
	 * @author 尧俊锋
	 */
	public static Bitmap getImage(String imagePath)
	{
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		int h = options.outHeight;
		int w = options.outWidth;
		options.inSampleSize = 1;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, w, h,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}
	
	
	/**
	 * 获取指定路径下的图片的指定大小的缩略图 getImageThumbnail，功能通过decodeSampledBitmapFromFile(
	 * imagePath, width, height)实现
	 * 
	 * @param imagePath 完整的图片所在路径和图片名字。
	 * @param width 被缩放后的width,
	 * @param height 被缩放后的height
	 * 
	 * @return Bitmap
	 * 
	 * @author 尧俊锋
	 */
	public static Bitmap getImageThumbnail(String imagePath, int width,
			int height) {
		return decodeSampledBitmapFromFile(imagePath, width, height);
	}
	
	/**
	 * 保存图片到指定路径。功能通过调用saveBitmap(String dirpath, String filename,
	 *		Bitmap bitmap)实现
	 * @param dirpath  图片的路径
	 * @param  filename bitmap文件的名字
	 * @param  bitmap 需要被存储的位图
	 * @param isdelete 待拓展的参数,现在true和false结果都一样
	 * 
	 */
	
	public static void saveBitmap(String dirpath, String filename,
			Bitmap bitmap,boolean isdelete) {
		saveBitmap(dirpath, filename, bitmap);
	}

	/**
	 * 保存位图到指定的目录里并且给文件命名
	 * 
	 * @param  dirpath---完整的路径
	 * @param  filename bitmap文件的名字
	 * @param  bitmap 需要被存储的位图
	 * @return void
	 * @author 尧俊锋
	 */
	public static void saveBitmap(String dirpath, String filename,
			Bitmap bitmap) {
		File dir = new File(dirpath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String path = dirpath+File.separator+filename;
		saveBitmap(path, bitmap);
	}
	
	/**
	 * 保存位图到指定的路径
	 * 
	 * @param imagepath 路径和名字
	 * @param bitmap 要被保存的位图
	 * 
	 * @author 尧俊锋
	 */
	public static void saveBitmap(String imagepath,Bitmap bitmap) {
		File file = new File(imagepath);
		if(file.exists())
			file.delete();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
				out.flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	

	public static void makeRootDirectory(String filePath) {
		File file = null;
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
		
	}

	/**
	 * 
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * 
	 * @return degree旋转的角度
	 * 
	 * @author 尧俊锋
	 **/
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;

	}

	/** 将图片旋转指定的角度
	  * @param angle 旋转的角度
	  * @param bitmap 要旋转的位图
	  * @return 旋转后的Bitmap
	  * @throws
	  * 
	  * @author 尧俊锋
	  */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * 将图片变为圆角
	 * 
	 * @param bitmap
	 *            原Bitmap图片
	 * @param pixels
	 *            图片圆角的弧度(单位:像素(px))
	 * @return 带有圆角的图片(Bitmap 类型)
	 * 
	 * @author 尧俊锋
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
	
	/**
	 * 将图片转化为圆形头像 
	 * 
	 * @param bitmap 要被转化的位图
	 * 
	 * @return 转化后得到的位图
	 * @author 尧俊锋
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;

			left = 0;
			top = 0;
			right = width;
			bottom = width;

			height = width;

			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;

			float clip = (width - height) / 2;

			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;

			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);// 设置画笔无锯齿

		canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

		// 以下有两种方法画圆,drawRounRect和drawCircle
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
		// canvas.drawCircle(roundPx, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
		canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

		return output;
	}
	
	
	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * 
     * @param context 上下文, deValue dp大小
     * 
     * @author 尧俊锋
     * 
     * @return 像素的数目
     */  
    public static int dp2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     * 
     * @param context 上下文, pxValue 像素数目
     * 
     * @author 尧俊锋
     * 
     * @return dp的大小
     */  
    public static int px2dp(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  

    /**
     * <p>获取Bitmap大小,该方法为版本兼容的.</p>
     * 
     * @param bitmap 指定的位图,注意不可为null.
     * 
     * @return 如果bitmap为null那么将返回-1,否则返回指定位图的大小,单位为字节.
     * 
     * @author 麦灿标
     * */
    @SuppressLint("NewApi")
	public static int sizeOfBitmap(Bitmap bitmap) {
    	if(bitmap == null) {
    		return -1;
    	}
    	//API 19及以上版本
    	if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    		return bitmap.getAllocationByteCount();
    	//API 12 ~ API 18
    	} else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
    		return bitmap.getByteCount();
    	//API 12以下
    	} else {
    		return bitmap.getRowBytes() * bitmap.getHeight();
    	}
    }
    
    /**
     * 按指定质量保存位图
     * 
     * @param bitmap
     * @param quality
     * @param path
     * @return
     */
    public static boolean saveBitmap(Bitmap bitmap, int quality, String path) {
    	if(bitmap == null || quality < 0
    					  || quality > 100
    					  || path == null) {
    		return false;
    	}
		
    	OutputStream os = null;
		try {
			os = new FileOutputStream(path);
			return bitmap.compress(CompressFormat.JPEG, quality, os);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			IoUtils.close(os);
		}
    }
    
   /**
    *  通过降低图片质量压缩图片大小
    * @param srcPath 图片路径
    * @return 压缩后的图片
    */
    public static Bitmap compressImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }
    
    private static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>1000) {    //循环判断如果压缩后图片是否大于1000kb,大于继续压缩        
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}