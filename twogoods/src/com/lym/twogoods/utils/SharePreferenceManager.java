package com.lym.twogoods.utils;

import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

/**
 * <p>
 * 	SharePreferences管理类
 * </p>
 * 
 * <p>该类采用单例模式</p>
 * 
 * @author 麦灿标
 * */
public class SharePreferenceManager {

	private final static String TAG = "SharePreferenceManager";
	
	/** SharePreferenceManager唯一实例对象 */
	private static SharePreferenceManager sSpManager = new SharePreferenceManager();
	
	/** 默认的SharePreferences文件名 */
	private final String defaultSpName = "default_sharepreferences";
	
	/** 创建SharePreference文件默认的操作模式,私有模式,只能
	 *  由当前应用和与当前应用相同UserID的Application使用 */
	private final int defaultSpMode = Context.MODE_PRIVATE;
	
	/** 设置SharePreferences文件名 */
	private final String settingsSpName = "settings";
	
	/** 设置SharePreferences文件模式 */
	private final int settingsSpMode = Context.MODE_PRIVATE;
	
	/** 登录信息SharePreferences文件名 */
	private final String loginSpName = "loginmessage";
	
	/** 登录信息SharePreferences文件模式 */
	private final int loginSpMode = Context.MODE_PRIVATE;
	
	/** 位置SharePreferences文件名 */
	private final String locationSpName = "location";
	
	/** 位置SharePreferences文件模式 */
	private final int locationSpMode = Context.MODE_PRIVATE;
	
	 
	/**
	 * <p>
	 * 	获取SharePreferenceManager唯一实例对象
	 * </p>
	 * 
	 * @return 返回SharePreferenceManager唯一实例对象
	 * */
	public static SharePreferenceManager getInstance() {
		return sSpManager;
	}
	
	/**
	 * <p>
	 * 	添加一个键值对到默认的SharePreference文件中.
	 * </p>
	 * 
	 * @param context 上下文 
	 * @param key  键,不能为空
	 * @param value 值
	 * 
	 * @return 添加成功返回true,失败返回false.
	 * */
	public boolean putBoolean(Context context, String key, boolean value) {
		return putBoolean(context, defaultSpName, defaultSpMode, key, value);
	}
	
	/**
	 * <p>
	 * 	添加一个键值对到指定的SharePreference文件中.注意如果该文件不存在会被自动创建.
	 * </p>
	 * 
	 * @param context 上下文 
	 * @param name 指定的SharePreference文件名,不能为空
	 * @param mode 创建的SharePreference文件模式
	 * @param key  键,不能为空
	 * @param value 值
	 * 
	 * @return 添加成功返回true,失败返回false.
	 * */
	public boolean putBoolean(Context context, String name, int mode, String key, boolean value) {
		if(!checkParameters(context, name, mode, key)) {
			return false;
		}
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}
	
	/**
	 * <p>
	 * 	添加一个键值对到默认的SharePreference文件中.
	 * </p>
	 * 
	 * @param context 上下文 
	 * @param key  键,不能为空
	 * @param value 值
	 * 
	 * @return 添加成功返回true,失败返回false.
	 * */
	public boolean putFloat(Context context, String key, float value) {
		return putFloat(context, defaultSpName, defaultSpMode, key, value);
	}
	
	/**
	 * <p>
	 * 	添加一个键值对到指定的SharePreference文件中.注意如果该文件不存在会被自动创建.
	 * </p>
	 * 
	 * @param context 上下文 
	 * @param name 指定的SharePreference文件名,不能为空
	 * @param mode 创建的SharePreference文件模式
	 * @param key  键,不能为空
	 * @param value 值
	 * 
	 * @return 添加成功返回true,失败返回false.
	 * */
	public boolean putFloat(Context context, String name, int mode, String key, float value) {
		if(!checkParameters(context, name, mode, key)) {
			return false;
		}
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		Editor editor = sp.edit();
		editor.putFloat(key, value);
		return editor.commit();
	}
	
	/**
	 * <p>
	 * 	添加一个键值对到默认的SharePreference文件中.
	 * </p>
	 * 
	 * @param context 上下文 
	 * @param key  键,不能为空
	 * @param value 值
	 * 
	 * @return 添加成功返回true,失败返回false.
	 * */
	public boolean putInt(Context context, String key, int value) {
		return putInt(context, defaultSpName, defaultSpMode, key, value);
	}
	
	/**
	 * <p>
	 * 	添加一个键值对到指定的SharePreference文件中.注意如果该文件不存在会被自动创建.
	 * </p>
	 * 
	 * @param context 上下文 
	 * @param name 指定的SharePreference文件名,不能为空
	 * @param mode 创建的SharePreference文件模式
	 * @param key  键,不能为空
	 * @param value 值
	 * 
	 * @return 添加成功返回true,失败返回false.
	 * */
	public boolean putInt(Context context, String name, int mode, String key, int value) {
		if(!checkParameters(context, name, mode, key)) {
			return false;
		}
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		Editor editor = sp.edit();
		editor.putInt(key, value);
		return editor.commit();
	}
	
	/**
	 * <p>
	 * 	添加一个键值对到默认的SharePreference文件中.
	 * </p>
	 * 
	 * @param context 上下文 
	 * @param key  键,不能为空
	 * @param value 值
	 * 
	 * @return 添加成功返回true,失败返回false.
	 * */
	public boolean putLong(Context context, String key, long value) {
		return putLong(context, defaultSpName, defaultSpMode, key, value);
	}
	
	/**
	 * <p>
	 * 	添加一个键值对到指定的SharePreference文件中.注意如果该文件不存在会被自动创建.
	 * </p>
	 * 
	 * @param context 上下文 
	 * @param name 指定的SharePreference文件名,不能为空
	 * @param mode 创建的SharePreference文件模式
	 * @param key  键,不能为空
	 * @param value 值
	 * 
	 * @return 添加成功返回true,失败返回false.
	 * */
	public boolean putLong(Context context, String name, int mode, String key, long value) {
		if(!checkParameters(context, name, mode, key)) {
			return false;
		}
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		Editor editor = sp.edit();
		editor.putLong(key, value);
		return editor.commit();
	}
	
	/**
	 * <p>
	 * 	添加一个键值对到默认的SharePreference文件中.
	 * </p>
	 * 
	 * @param context 上下文 
	 * @param key  键,不能为空
	 * @param value 值
	 * 
	 * @return 添加成功返回true,失败返回false.
	 * */
	public boolean putString(Context context, String key, String value) {
		return putString(context, defaultSpName, defaultSpMode, key, value);
	}
	
	/**
	 * <p>
	 * 	添加一个键值对到指定的SharePreference文件中.注意如果该文件不存在会被自动创建.
	 * </p>
	 * 
	 * @param context 上下文 
	 * @param name 指定的SharePreference文件名,不能为空
	 * @param mode 创建的SharePreference文件模式
	 * @param key  键,不能为空
	 * @param value 值
	 * 
	 * @return 添加成功返回true,失败返回false.
	 * */
	public boolean putString(Context context, String name, int mode, String key, String value) {
		if(!checkParameters(context, name, mode, key)) {
			return false;
		}
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		Editor editor = sp.edit();
		editor.putString(key, value);
		return editor.commit();
	}
	
	/**
	 * <p>
	 * 	添加一个键值对到默认的SharePreference文件中.
	 * </p>
	 * 
	 * @param context 上下文 
	 * @param key  键,不能为空
	 * @param values 值
	 * 
	 * @return 添加成功返回true,失败返回false.
	 * */
	public boolean putStringSet(Context context, String key, Set<String> values) {
		return putStringSet(context, defaultSpName, defaultSpMode, key, values);
	}
	
	/**
	 * <p>
	 * 	添加一个键值对到指定的SharePreference文件中.注意如果该文件不存在会被自动创建.
	 * </p>
	 * 
	 * @param context 上下文 
	 * @param name 指定的SharePreference文件名,不能为空
	 * @param mode 创建的SharePreference文件模式
	 * @param key  键,不能为空
	 * @param values 值
	 * 
	 * @return 添加成功返回true,失败返回false.
	 * */
	public boolean putStringSet(Context context, String name, int mode, String key, Set<String> values) {
		if(!checkParameters(context, name, mode, key)) {
			return false;
		}
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		Editor editor = sp.edit();
		editor.putStringSet(key, values);
		return editor.commit();
	}
	
	/**
	 * <p>
	 * 	从默认SharePreference文件中获取指定键对应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param defValue 获取失败时返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public boolean getBoolean(Context context, String key, boolean defValue) {
		return getBoolean(context, defaultSpName, defaultSpMode, key, defValue);
	}
	
	/**
	 * <p>
	 * 	从指定SharePreference文件中获取指定键对应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param name 指定的SharePreference文件,不能为空
	 * @param mode 创建的SharePreference文件模式
	 * @param key 键
	 * @param defValue 获取失败时返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public boolean getBoolean(Context context, String name, int mode, String key, boolean defValue) {
		if(!checkParameters(context, name, mode, key)) {
			return defValue;
		}
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		return sp.getBoolean(key, defValue);
	}
	
	/**
	 * <p>
	 * 	从默认SharePreference文件中获取指定键对应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param defValue 获取失败时返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public float getFloat(Context context, String key, float defValue) {
		return getFloat(context, defaultSpName, defaultSpMode, key, defValue);
	}
	
	/**
	 * <p>
	 * 	从指定SharePreference文件中获取指定键对应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param name 指定的SharePreference文件,不能为空
	 * @param mode 创建的SharePreference文件模式
	 * @param key 键
	 * @param defValue 获取失败时返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public float getFloat(Context context, String name, int mode, String key, float defValue) {
		if(!checkParameters(context, name, mode, key)) {
			return defValue;
		}
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		return sp.getFloat(key, defValue);
	}
	
	/**
	 * <p>
	 * 	从默认SharePreference文件中获取指定键对应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param defValue 获取失败时返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public int getInt(Context context, String key, int defValue) {
		return getInt(context, defaultSpName, defaultSpMode, key, defValue);
	}
	
	/**
	 * <p>
	 * 	从指定SharePreference文件中获取指定键对应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param name 指定的SharePreference文件,不能为空
	 * @param mode 创建的SharePreference文件模式
	 * @param key 键
	 * @param defValue 获取失败时返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public int getInt(Context context, String name, int mode, String key, int defValue) {
		if(!checkParameters(context, name, mode, key)) {
			return defValue;
		}
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		return sp.getInt(key, defValue);
	}
	
	/**
	 * <p>
	 * 	从默认SharePreference文件中获取指定键对应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param defValue 获取失败时返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public long getLong(Context context, String key, long defValue) {
		return getLong(context, defaultSpName, defaultSpMode, key, defValue);
	}
	
	/**
	 * <p>
	 * 	从指定SharePreference文件中获取指定键对应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param name 指定的SharePreference文件,不能为空
	 * @param mode 创建的SharePreference文件模式
	 * @param key 键
	 * @param defValue 获取失败时返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public long getLong(Context context, String name, int mode, String key, long defValue) {
		if(!checkParameters(context, name, mode, key)) {
			return defValue;
		}
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		return sp.getLong(key, defValue);
	}
	
	/**
	 * <p>
	 * 	从默认SharePreference文件中获取指定键对应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param defValue 获取失败时返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public String getString(Context context, String key, String defValue) {
		return getString(context, defaultSpName, defaultSpMode, key, defValue);
	}
	
	/**
	 * <p>
	 * 	从指定SharePreference文件中获取指定键对应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param name 指定的SharePreference文件,不能为空
	 * @param mode 创建的SharePreference文件模式
	 * @param key 键
	 * @param defValue 获取失败时返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public String getString(Context context, String name, int mode, String key, String defValue) {
		if(!checkParameters(context, name, mode, key)) {
			return defValue;
		}
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		return sp.getString(key, defValue);
	}
	
	/**
	 * <p>
	 * 	从默认SharePreference文件中获取指定键对应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param defValue 获取失败时返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public Set<String> getStringSet(Context context, String key, Set<String> defValue) {
		return getStringSet(context, defaultSpName, defaultSpMode, key, defValue);
	}
	
	/**
	 * <p>
	 * 	从指定SharePreference文件中获取指定键对应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param name 指定的SharePreference文件,不能为空
	 * @param mode 创建的SharePreference文件模式
	 * @param key 键
	 * @param defValue 获取失败时返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public Set<String> getStringSet(Context context, String name, int mode, String key, Set<String> defValue) {
		if(!checkParameters(context, name, mode, key)) {
			return defValue;
		}
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		return sp.getStringSet(key, defValue);
	}
	
	/**
	 * <p>
	 * 	从默认SharePreference文件中获取所有键值对
	 * </p>
	 * 
	 * @param context 上下文
	 * 
	 * @return 获取成功返回包含所有键值对的Map,获取失败返回null
	 * */
	public Map<String, ?> getAll(Context context) {
		return getAll(context, defaultSpName, defaultSpMode);
	}
	
	/**
	 * <p>
	 * 	从指定SharePreference文件中获取所有键值对
	 * </p>
	 * 
	 * @param context 上下文
	 * @param name 指定的SharePreference文件,不能为空
	 * @param mode 创建的SharePreference文件模式
	 * 
	 * @return 获取成功返回包含所有键值对的Map,获取失败返回null
	 * */
	@SuppressWarnings("deprecation")
	public Map<String, ?> getAll(Context context, String name, int mode) {
		if(context == null) {
			return null;
		}
		if(TextUtils.isEmpty(name)) {
			return null;
		}
		if(mode != Context.MODE_PRIVATE
				|| mode != Context.MODE_WORLD_READABLE
				|| mode != Context.MODE_WORLD_WRITEABLE) {
			return null;
		}
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		return sp.getAll();
	}
	
	//检查参数的合法性
	@SuppressWarnings("deprecation")
	private boolean checkParameters(Context context, String name, int mode, String key) {
		if(context == null) {
			return false;
		}
		if(TextUtils.isEmpty(name)) {
			return false;
		}
		if(mode != Context.MODE_PRIVATE
				|| mode != Context.MODE_WORLD_READABLE
				|| mode != Context.MODE_WORLD_WRITEABLE) {
			return false;
		}
		//不允许存空键
		if(TextUtils.isEmpty(key)) {
			return false;
		}
		return true;
	}
	
	/**
	 * <p>
	 * 	添加设置信息到相应的SharePreferences文件中
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param value 值
	 * 
	 * @return 设置成功返回true,失败返回false
	 * */
	public boolean setSettingsBoolean(Context context, String key, boolean value) {
		return putBoolean(context, settingsSpName, settingsSpMode,key, value);
	}
	
	/**
	 * <p>
	 * 	添加设置信息到相应的SharePreferences文件中
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param value 值
	 * 
	 * @return 设置成功返回true,失败返回false
	 * */
	public boolean setSettingsFloat(Context context, String key, float value) {
		return putFloat(context, settingsSpName, settingsSpMode,key, value);
	}
	
	/**
	 * <p>
	 * 	添加设置信息到相应的SharePreferences文件中
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param value 值
	 * 
	 * @return 设置成功返回true,失败返回false
	 * */
	public boolean setSettingsInt(Context context, String key, int value) {
		return putInt(context, settingsSpName, settingsSpMode,key, value);
	}
	
	/**
	 * <p>
	 * 	添加设置信息到相应的SharePreferences文件中
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param value 值
	 * 
	 * @return 设置成功返回true,失败返回false
	 * */
	public boolean setSettingsLong(Context context, String key, long value) {
		return putLong(context, settingsSpName, settingsSpMode,key, value);
	}
	
	/**
	 * <p>
	 * 	添加设置信息到相应的SharePreferences文件中
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param value 值
	 * 
	 * @return 设置成功返回true,失败返回false
	 * */
	public boolean setSettingsString(Context context, String key, String value) {
		return putString(context, settingsSpName, settingsSpMode,key, value);
	}
	
	/**
	 * <p>
	 * 	添加设置信息到相应的SharePreferences文件中
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param values 值
	 * 
	 * @return 设置成功返回true,失败返回false
	 * */
	public boolean setSettingsStringSet(Context context, String key, Set<String> values) {
		return putStringSet(context, settingsSpName, settingsSpMode,key, values);
	}

	/**
	 * <p>
	 * 	添加用户登录信息到相应的SharePreferences文件中
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param value 值
	 * 
	 * @return 设置成功返回true,失败返回false
	 * */
	public boolean setLoginMessageBoolean(Context context, String key, boolean value) {
		return putBoolean(context, loginSpName, loginSpMode,key, value);
	}
	
	/**
	 * <p>
	 * 	添加用户登录信息到相应的SharePreferences文件中
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param value 值
	 * 
	 * @return 设置成功返回true,失败返回false
	 * */
	public boolean setLoginMessageFloat(Context context, String key, float value) {
		return putFloat(context, loginSpName, loginSpMode,key, value);
	}
	
	/**
	 * <p>
	 * 	添加用户登录信息到相应的SharePreferences文件中
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param value 值
	 * 
	 * @return 设置成功返回true,失败返回false
	 * */
	public boolean setLoginMessageInt(Context context, String key, int value) {
		return putInt(context, loginSpName, loginSpMode,key, value);
	}
	
	/**
	 * <p>
	 * 	添加用户登录信息到相应的SharePreferences文件中
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param value 值
	 * 
	 * @return 设置成功返回true,失败返回false
	 * */
	public boolean setLoginMessageLong(Context context, String key, long value) {
		return putLong(context, loginSpName, loginSpMode,key, value);
	}
	
	/**
	 * <p>
	 * 	添加用户登录信息到相应的SharePreferences文件中
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param value 值
	 * 
	 * @return 设置成功返回true,失败返回false
	 * */
	public boolean setLoginMessageString(Context context, String key, String value) {
		return putString(context, loginSpName, loginSpMode,key, value);
	}
	
	/**
	 * <p>
	 * 	添加用户登录信息到相应的SharePreferences文件中
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param values 值
	 * 
	 * @return 设置成功返回true,失败返回false
	 * */
	public boolean setLoginMessageStringSet(Context context, String key, Set<String> values) {
		return putStringSet(context, loginSpName, loginSpMode,key, values);
	}
	
	/**
	 * <p>
	 * 	添加位置信息到相应的SharePreferences文件中
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param value 值
	 * 
	 * @return 设置成功返回true,失败返回false
	 * */
	public boolean setLocationBoolean(Context context, String key, boolean value) {
		return putBoolean(context, locationSpName, locationSpMode,key, value);
	}
	
	/**
	 * <p>
	 * 	添加位置信息到相应的SharePreferences文件中
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param value 值
	 * 
	 * @return 设置成功返回true,失败返回false
	 * */
	public boolean setLocationFloat(Context context, String key, float value) {
		return putFloat(context, locationSpName, locationSpMode,key, value);
	}
	
	/**
	 * <p>
	 * 	添加位置信息到相应的SharePreferences文件中
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param value 值
	 * 
	 * @return 设置成功返回true,失败返回false
	 * */
	public boolean setLocationInt(Context context, String key, int value) {
		return putInt(context, locationSpName, locationSpMode,key, value);
	}
	
	/**
	 * <p>
	 * 	添加位置信息到相应的SharePreferences文件中
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param value 值
	 * 
	 * @return 设置成功返回true,失败返回false
	 * */
	public boolean setLocationLong(Context context, String key, long value) {
		return putLong(context, locationSpName, locationSpMode,key, value);
	}
	
	/**
	 * <p>
	 * 	添加位置信息到相应的SharePreferences文件中
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param value 值
	 * 
	 * @return 设置成功返回true,失败返回false
	 * */
	public boolean setLocationString(Context context, String key, String value) {
		return putString(context, locationSpName, locationSpMode,key, value);
	}
	
	/**
	 * <p>
	 * 	添加位置信息到相应的SharePreferences文件中
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param values 值
	 * 
	 * @return 设置成功返回true,失败返回false
	 * */
	public boolean setLocationStringSet(Context context, String key, Set<String> values) {
		return putStringSet(context, locationSpName, locationSpMode,key, values);
	}
	
	/**
	 * <p>
	 * 	从设置SharePreferences文件中获取相应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param defValue 获取失败返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public boolean getSettingsBoolean(Context context, String key, boolean defValue) {
		return getBoolean(context, settingsSpName, settingsSpMode, key, defValue);
	}
	
	/**
	 * <p>
	 * 	从设置SharePreferences文件中获取相应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param defValue 获取失败返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public float getSettingsFloat(Context context, String key, float defValue) {
		return getFloat(context, settingsSpName, settingsSpMode, key, defValue);
	}
	
	/**
	 * <p>
	 * 	从设置SharePreferences文件中获取相应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param defValue 获取失败返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public int getSettingsInt(Context context, String key, int defValue) {
		return getInt(context, settingsSpName, settingsSpMode, key, defValue);
	}
	
	/**
	 * <p>
	 * 	从设置SharePreferences文件中获取相应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param defValue 获取失败返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public long getSettingsLong(Context context, String key, long defValue) {
		return getLong(context, settingsSpName, settingsSpMode, key, defValue);
	}
	
	/**
	 * <p>
	 * 	从设置SharePreferences文件中获取相应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param defValue 获取失败返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public String getSettingsString(Context context, String key, String defValue) {
		return getString(context, settingsSpName, settingsSpMode, key, defValue);
	}
	
	/**
	 * <p>
	 * 	从设置SharePreferences文件中获取所有键值对
	 * </p>
	 * 
	 * @param context 上下文
	 * 
	 * @return 获取成功返回设置SharePreferences文件所有键值对,获取失败返回null
	 * */
	public Map<String, ?> getSettingsAll(Context context) {
		return getAll(context, settingsSpName, settingsSpMode);
	}
	
	/**
	 * <p>
	 * 	从登录信息SharePreferences文件中获取相应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param defValue 获取失败返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public boolean getLoginMessageBoolean(Context context, String key, boolean defValue) {
		return getBoolean(context, loginSpName, loginSpMode, key, defValue);
	}
	
	/**
	 * <p>
	 * 	从登录信息SharePreferences文件中获取相应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param defValue 获取失败返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public float getLoginMessageFloat(Context context, String key, float defValue) {
		return getFloat(context, loginSpName, loginSpMode, key, defValue);
	}
	
	/**
	 * <p>
	 * 	从登录信息SharePreferences文件中获取相应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param defValue 获取失败返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public int getLoginMessageInt(Context context, String key, int defValue) {
		return getInt(context, loginSpName, loginSpMode, key, defValue);
	}
	
	/**
	 * <p>
	 * 	从登录信息SharePreferences文件中获取相应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param defValue 获取失败返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public long getLoginMessageLong(Context context, String key, long defValue) {
		return getLong(context, loginSpName, loginSpMode, key, defValue);
	}
	
	/**
	 * <p>
	 * 	从登录信息SharePreferences文件中获取相应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param defValue 获取失败返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public String getLoginMessageString(Context context, String key, String defValue) {
		return getString(context, loginSpName, loginSpMode, key, defValue);
	}
	
	/**
	 * <p>
	 * 	从登录信息SharePreferences文件中获取所有键值对
	 * </p>
	 * 
	 * @param context 上下文
	 * 
	 * @return 获取成功返回设置SharePreferences文件所有键值对,获取失败返回null
	 * */
	public Map<String, ?> getLoginMessageAll(Context context) {
		return getAll(context, loginSpName, loginSpMode);
	}
	
	/**
	 * <p>
	 * 	从位置SharePreferences文件中获取相应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param defValue 获取失败返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public boolean getLocationBoolean(Context context, String key, boolean defValue) {
		return getBoolean(context, locationSpName, locationSpMode, key, defValue);
	}
	
	/**
	 * <p>
	 * 	从位置SharePreferences文件中获取相应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param defValue 获取失败返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public float getLocationFloat(Context context, String key, float defValue) {
		return getFloat(context, locationSpName, locationSpMode, key, defValue);
	}
	
	/**
	 * <p>
	 * 	从位置SharePreferences文件中获取相应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param defValue 获取失败返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public int getLocationInt(Context context, String key, int defValue) {
		return getInt(context, locationSpName, locationSpMode, key, defValue);
	}
	
	/**
	 * <p>
	 * 	从位置SharePreferences文件中获取相应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param defValue 获取失败返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public long getLocationLong(Context context, String key, long defValue) {
		return getLong(context, locationSpName, locationSpMode, key, defValue);
	}
	
	/**
	 * <p>
	 * 	从位置SharePreferences文件中获取相应的值
	 * </p>
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param defValue 获取失败返回的值
	 * 
	 * @return 获取成功返回相应的值,获取失败返回defValue
	 * */
	public String getLocationString(Context context, String key, String defValue) {
		return getString(context, locationSpName, locationSpMode, key, defValue);
	}
	
	/**
	 * <p>
	 * 	从位置SharePreferences文件中获取所有键值对
	 * </p>
	 * 
	 * @param context 上下文
	 * 
	 * @return 获取成功返回设置SharePreferences文件所有键值对,获取失败返回null
	 * */
	public Map<String, ?> getLoactionAll(Context context) {
		return getAll(context, locationSpName, locationSpMode);
	}
	
}
