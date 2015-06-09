package com.scujoo.datas;

public class DatasInternship {
	
	private String id;
	private String name;
	private String publishTime;
	private String position;
	
	public DatasInternship(String id,String name,String publishTime,String position)
	{
		setId(id);
		setName(name);
		setPublishTime(publishTime);
		setPosition(position);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	
	

}
