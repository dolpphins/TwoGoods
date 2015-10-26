package com.lym.twogoods.message;

public class ImageFloder
{
	/*
	 * 图片的标志，如果是0代表某个文件夹。如果是1代表是全部图片,默认是0
	 */
	private int FLAG = 0;
	/**
	 * 图片的文件夹路径
	 */
	private String dir;

	/**
	 * 第一张图片的路径
	 */
	private String firstImagePath;

	/**
	 * 文件夹的名称
	 */
	private String name;

	/**
	 * 图片的数量
	 */
	private int count;
	
	public int getFlag()
	{
		return FLAG;
	}

	public void setFlag(int flag)
	{
		this.FLAG = flag;
		
	}

	public String getDir()
	{
		return dir;
	}

	public void setDir(String dir)
	{
		if(FLAG==0){
			this.dir = dir;
			int lastIndexOf = this.dir.lastIndexOf("/");
			this.name = this.dir.substring(lastIndexOf);
		}else{
			this.name = "所有图片";
		}
	}

	public String getFirstImagePath()
	{
		return firstImagePath;
	}

	public void setFirstImagePath(String firstImagePath)
	{
		this.firstImagePath = firstImagePath;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	

}
