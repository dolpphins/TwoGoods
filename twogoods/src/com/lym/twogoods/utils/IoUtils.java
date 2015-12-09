package com.lym.twogoods.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * IO操作公共类
 * 
 * @author mao
 *
 */
public class IoUtils {

	/**
	 * 采用对象序列化方式实现对象彻底深度复制,注意该复制的对象必须实现Serializable接口
	 * 
	 * @return 复制成功返回复制后的对象,复制失败返回null. 
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T clone(T object) {
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			
			bais = new ByteArrayInputStream(baos.toByteArray());
			ois = new ObjectInputStream(bais);
			return (T) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			closeInputStream(ois);
			closeInputStream(bais);
			closeOutStream(oos);
			closeOutStream(baos);
		}
	}
	
	private static boolean closeInputStream(InputStream is) {
		if(is == null) {
			return false;
		}
		try {
			is.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean closeOutStream(OutputStream os) {
		if(os == null) {
			return false;
		}
		try {
			os.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
