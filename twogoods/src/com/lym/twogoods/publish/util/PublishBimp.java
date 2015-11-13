package com.lym.twogoods.publish.util;

/*
 * <p>
 * 图片优化
 * <p>
 * 
 * @author 龙宇文
 */
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PublishBimp {
	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		 // 这个参数代表，不为bitmap分配内存空间，只记录一些该图片的信息（例如图片大小）
		//，说白了就是为了内存优化
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			//移位运算
			// 这一步是根据要设置的大小，使宽和高都能满足   
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000)) {
				// 重新取得流，注意：这里一定要再次加载，不能二次使用之前的流！   
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				// 这个参数表示 新生成的图片为原始图片的几分之一。
				//2往往使解码器更快。
				options.inSampleSize = (int) Math.pow(2.0D, i);
				//// 这里之前设置为了true，所以要改为false，否则就创建不出图片   
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}
}
