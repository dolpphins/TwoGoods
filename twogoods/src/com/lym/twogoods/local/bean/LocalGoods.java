package com.lym.twogoods.local.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;
import com.lym.twogoods.bean.Goods;

import android.text.TextUtils;

/**
 * 代表Goods类的本地类,主要用于OrmLite框架使用中
 * 
 * @author 麦灿标
 * */
public class LocalGoods {

	/** id,主键自增 */
	@DatabaseField(generatedId = true)
	private int id;
	
	/** GUID,全局唯一 */
	@DatabaseField
	private String GUID;
	
	/** 用户名,即贰货号,注意它需要符合一定的规则 */
	@DatabaseField
	private String username;
	
	/** 该用户头像存放在网络服务器上的可访问url */
	@DatabaseField
	private String head_url;
	
	/** 发布时间,格式:时间戳 */
	@DatabaseField(columnName="publish_time")
	private long publish_time;
	
	/** 发布位置,直接显示LBS定位结果即可,比如:广州市天河区 */
	@DatabaseField
	private String publish_location;
	
	/** 发布位置的经度 ,精确到分*/
	@DatabaseField
	private String location_longitude;
	
	/** 发布位置的纬度,精确到分 */
	@DatabaseField
	private String location_latitude;
	
	/** 定价,精确到元 */
	@DatabaseField(columnName="price")
	private int price;
	
	/** 商品描述 */
	@DatabaseField
	private String description;
	
	/** 商品图片网络可访问url集合,格式:json */
	@DatabaseField
	private String picturesUrls;

	/** 关注数,默认为0 */
	@DatabaseField(columnName="focus_num")
	private int focus_num = 0;
	
	/** 商品所在分类 */
	@DatabaseField
	private String category;
	
	/** 浏览数,默认为0 */
	@DatabaseField(columnName="browse_num")
	private int browse_num = 0;
	
	/** 语音在网络服务器上的可访问url */
	@DatabaseField
	private String voice_url;
	
	/** 联系方式,非空 */
	@DatabaseField
	private String phone;
	
	/** 期限,单位:天 */
	@DatabaseField
	private int expire;
	
	/** 评论数,默认为0 */
	@DatabaseField
	private int comment_num = 0;
	
	/** 更新时间,本地缓存过期使用,格式:时间戳 */
	@DatabaseField
	private long update_time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGUID() {
		return GUID;
	}

	public void setGUID(String gUID) {
		GUID = gUID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHead_url() {
		return head_url;
	}

	public void setHead_url(String head_url) {
		this.head_url = head_url;
	}

	public long getPublish_time() {
		return publish_time;
	}

	public void setPublish_time(long publish_time) {
		this.publish_time = publish_time;
	}

	public String getPublish_location() {
		return publish_location;
	}

	public void setPublish_location(String publish_location) {
		this.publish_location = publish_location;
	}

	public String getLocation_longitude() {
		return location_longitude;
	}

	public void setLocation_longitude(String location_longitude) {
		this.location_longitude = location_longitude;
	}

	public String getLocation_latitude() {
		return location_latitude;
	}

	public void setLocation_latitude(String location_latitude) {
		this.location_latitude = location_latitude;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getFocus_num() {
		return focus_num;
	}

	public void setFocus_num(int focus_num) {
		this.focus_num = focus_num;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getBrowse_num() {
		return browse_num;
	}

	public void setBrowse_num(int browse_num) {
		this.browse_num = browse_num;
	}

	public String getVoice_url() {
		return voice_url;
	}

	public void setVoice_url(String voice_url) {
		this.voice_url = voice_url;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

	public int getComment_num() {
		return comment_num;
	}

	public void setComment_num(int comment_num) {
		this.comment_num = comment_num;
	}

	public long getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(long update_time) {
		this.update_time = update_time;
	}
	
	public String getPictureUrlList() {
		return picturesUrls;
	}

	public void setPictureUrlList(String picturesUrlList) {
		this.picturesUrls = picturesUrlList;
	}
	
	
	/**
	 * 将json字符串转换为ArrayList列表,注意要转换的json格式必须为:
	 * ["url":"http://www.sds.com","url":"http://www.dfa.com"]
	 * 
	 * @param str 要解析的字符串
	 * 
	 * @return 转换成功返回转换后的ArrayList列表,失败返回null.
	 * */
	public static ArrayList<String> parsePictureUrlString(String str) {
		if(TextUtils.isEmpty(str)) {
			return null;
		}
		try {
			JSONArray jsonArray = new JSONArray(str);
			ArrayList<String> list = new ArrayList<String>();
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObejct = jsonArray.getJSONObject(i);
				String value = jsonObejct.getString("url");
				if(!TextUtils.isEmpty(value)) {
					list.add(value);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 将图片列表转换为json字符串
	 * 
	 * @param list 图片列表
	 * 
	 * @return 转换成功返回相应的json字符串,失败返回null.
	 * */
	public static String parsePictureUrlList(List<String> list) {
		if(list == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		int size = list.size();
		for(int i = 0; i < size; i++) {
			String s = list.get(i);
			if(!TextUtils.isEmpty(s)) {
				sb.append("\"url\":\"");
				sb.append(s);
				sb.append("\"");
			}
			if(i < size - 1) {
				sb.append(",");
			}
		}
		
		sb.append("]");
		
		return sb.toString();
	}
	
	/**
	 * 将Goods对象转换为LocalGoods对象
	 * 
	 * @param goods 要转换后的Goods对象
	 * 
	 * @return 转换成功返回LocalGoods对象,失败返回null.
	 * */
	public static LocalGoods valueOf(Goods goods) {
		if(goods == null) {
			return null;
		}
		try {
			LocalGoods localGoods = new LocalGoods();
			
			localGoods.setBrowse_num(goods.getBrowse_num());
			localGoods.setCategory(goods.getCategory());
			localGoods.setComment_num(goods.getComment_num());
			localGoods.setDescription(goods.getDescription());
			localGoods.setExpire(goods.getExpire());
			localGoods.setFocus_num(goods.getFocus_num());
			localGoods.setGUID(goods.getGUID());
			localGoods.setHead_url(goods.getHead_url());
			localGoods.setId(goods.getId());
			localGoods.setLocation_latitude(goods.getLocation_latitude());
			localGoods.setLocation_longitude(goods.getLocation_longitude());
			localGoods.setPhone(goods.getPhone());
			localGoods.setPictureUrlList(LocalGoods.parsePictureUrlList(goods.getPictureUrlList()));
			localGoods.setPrice(goods.getPrice());
			localGoods.setPublish_location(goods.getPublish_location());
			localGoods.setPublish_time(goods.getPublish_time());
			localGoods.setUpdate_time(goods.getUpdate_time());
			localGoods.setUsername(goods.getUsername());
			localGoods.setVoice_url(goods.getVoice_url());
			
			return localGoods;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 将LocalGoods对象转换为Goods对象
	 * 
	 * @param 要转换的LocalGoods对象
	 * 
	 * @return 转换成功返回相应的Goods对象,失败返回null.
	 * */
	public static Goods toGoods(LocalGoods localGoods) {
		if(localGoods == null) {
			return null;
		}
		try {
			Goods goods = new Goods();
			
			goods.setBrowse_num(localGoods.getBrowse_num());
			goods.setCategory(localGoods.getCategory());
			goods.setComment_num(localGoods.getComment_num());
			goods.setDescription(localGoods.getDescription());
			goods.setExpire(localGoods.getExpire());
			goods.setFocus_num(localGoods.getFocus_num());
			goods.setGUID(localGoods.getGUID());
			goods.setHead_url(localGoods.getHead_url());
			goods.setId(localGoods.getId());
			goods.setLocation_latitude(localGoods.getLocation_latitude());
			goods.setLocation_longitude(localGoods.getLocation_longitude());
			goods.setPhone(localGoods.getPhone());
			goods.setPictureUrlList(LocalGoods.parsePictureUrlString(localGoods.getPictureUrlList()));
			goods.setPrice(localGoods.getPrice());
			goods.setPublish_location(localGoods.getPublish_location());
			goods.setPublish_time(localGoods.getPublish_time());
			goods.setUpdate_time(localGoods.getUpdate_time());
			goods.setUsername(localGoods.getUsername());
			goods.setVoice_url(localGoods.getVoice_url());
			
			return goods;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取OrmLite框架下该数据库表中商品发布时间列列名
	 * 
	 * @return 商品发布时间列列名
	 * */
	public static String getPublishTimeColoumnString() {
		return "publish_time";
	}
	
	/**
	 * 获取OrmLite框架下该数据库表中商品价格列列名
	 * 
	 * @return 商品价格列列名
	 * */
	public static String getPriceColoumnString() {
		return "price";
	}
	
	/**
	 * 获取OrmLite框架下该数据库表中商品关注量列列名
	 * 
	 * @return 商品关注量列列名
	 * */
	public static String getFocusColoumnString() {
		return "focus_num";
	}
	
	/**
	 * 获取OrmLite框架下该数据库表中商品浏览量列列名
	 * 
	 * @return 商品浏览量列列名
	 * */
	public static String getBrowseColoumnString() {
		return "browse_num";
	}
}
