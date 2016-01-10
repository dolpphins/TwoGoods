package com.lym.twogoods.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
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
			close(ois);
			close(bais);
			close(oos);
			close(baos);
		}
	}
	
	/**
	 * 关闭流
	 * 
	 * @param stream 要关闭的流
	 * @return 关闭成功返回true，失败返回false
	 */
	public static boolean close(Closeable stream) {
		if(stream != null) {
			try {
				stream.close();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return false;
	}
}
