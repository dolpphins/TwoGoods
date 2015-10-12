package com.lym.twogoods.bean;

import com.j256.ormlite.field.DatabaseField;

/**
 * <p>
 * 	商品信息实体类
 * </p>
 * <p>
 * 	该实体类使用Ormlite开源框架,对应的数据库表为goods表
 * </p>
 * 
 * @author 麦灿标
 * */
public class Goods {

	/** id,主键自增 */
	@DatabaseField(generatedId = true)
	private int id;
	
	/** GUID,全局唯一 */
	@DatabaseField
	private String GUID;
	
	/** 用户名,即贰货号,注意它需要符合一定的规则 */
	@DatabaseField
	private String username;
	
	/** 头像本地缓存存放位置,如果没有则是默认头像存放位置 
	 *  格式为:路径+GUID_head_pic.jpg,默认头像路
	 *  径格式:路径+default_head_pic.jpg.
	 * */
	@DatabaseField
	private String head_url;
	
	/** 发布时间,格式:时间戳 */
	@DatabaseField
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
	@DatabaseField
	private int price;
	
	/** 商品描述 */
	@DatabaseField
	private String description;
	
	/** 图片数,默认为0 */
	@DatabaseField
	private int pic_num = 0;
	
	/** 图片前缀 */
	@DatabaseField
	private String pic_prefix;
	
	/** 图片基本路径 */
	@DatabaseField
	private String pic_baseurl;
	
	/** 关注数,默认为0 */
	@DatabaseField
	private int focus_num = 0;
	
	/** 商品所在分类 */
	@DatabaseField
	private String category;
	
	/** 浏览数,默认为0 */
	@DatabaseField
	private int browse_num = 0;
	
	/** 语音所在路径 */
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

	public int getPic_num() {
		return pic_num;
	}

	public void setPic_num(int pic_num) {
		this.pic_num = pic_num;
	}

	public String getPic_prefix() {
		return pic_prefix;
	}

	public void setPic_prefix(String pic_prefix) {
		this.pic_prefix = pic_prefix;
	}

	public String getPic_baseurl() {
		return pic_baseurl;
	}

	public void setPic_baseurl(String pic_baseurl) {
		this.pic_baseurl = pic_baseurl;
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
}


