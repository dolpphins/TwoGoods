package com.lym.twogoods.greenDao.twogoods;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * <p>
 * 	原Android项目贰货1+1使用Orm开源框架greenDAO,该类为greenDAO数据库初始化类
 * </p>
 * <p>
 * 	two_goods.db数据库相关初始化类,该数据库包含两个表:goods表和goods_detail表
 * </p>
 * 
 * @author 麦灿标
 * */
public class GoodsDB {

	public static void main(String[] args) {
		
		String defaultJavaPackage = "com.lym.twogoods.greenDao.twogoods";
		//数据库
		Schema schema = new Schema(1, defaultJavaPackage);
		
		Entity goodsEntity = addGoodsTable(schema);
		addGoodsDetailTable(schema, goodsEntity);
		
		String outDir = "../twogoods/src-gen/";
		try {
			new DaoGenerator().generateAll(schema, outDir);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//添加goods表,添加表成功返回对应的Entity对象,失败返回null
	private static Entity addGoodsTable(Schema schema) {
		if(schema == null) {
			return null;
		}
		Entity entity = schema.addEntity("goods");
		entity.addIdProperty().primaryKey().autoincrement();//ID,主键,自增
		entity.addStringProperty("GUID").notNull();//GUID
		entity.addStringProperty("username").notNull();//username
		entity.addStringProperty("head_url");//头像缓存url
		entity.addStringProperty("publish_time").notNull();//发布时间
		entity.addStringProperty("publish_location");//发布位置
		entity.addStringProperty("location_longitude");//发布位置经度
		entity.addStringProperty("location_latitude");//发布位置纬度
		entity.addStringProperty("price").notNull();//定价,单位为元,精确到元
		entity.addStringProperty("description");//商品描述

		entity.addStringProperty("pic_num");//图片数量
		entity.addStringProperty("pic_prefix");//图片前缀,使用该商品GUID
		entity.addStringProperty("pic_baseurl");//图片基本路径
		
		entity.addStringProperty("focus_num");//关注数
		entity.addStringProperty("category");//分类,默认为其它
		entity.addStringProperty("browse_num");//浏览数
		entity.addStringProperty("voice_url");//语音url
		entity.addStringProperty("phone");//联系方式
		entity.addStringProperty("age").notNull();//有效期,单位为天

		return entity;
	}
	
	//添加goods_detail表,添加表成功返回对应的Entity对象,失败返回null
	private static Entity addGoodsDetailTable(Schema schema, Entity goodsEntity) {
		if(schema == null) {
			return null;
		}
		Entity entity = schema.addEntity("goods_detail");
		entity.addIdProperty().primaryKey().autoincrement();//ID,主键,自增
		entity.addStringProperty("GUID").notNull();//GUID
		entity.addToOneWithoutProperty("goods_id", goodsEntity, "id");//外键
		entity.addStringProperty("comment_num");
		
		return entity;
	}
	
}
