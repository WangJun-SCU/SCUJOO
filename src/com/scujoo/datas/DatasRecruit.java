package com.scujoo.datas;

public class DatasRecruit {
	private String id;
	private String name;
	private String recruitTime;
	private String recruitPlace;
	
	public DatasRecruit(String id,String name,String recruitTime,String recruitPlace)
	{
		setId(id);
		setName(name);
		setRecruitTime(recruitTime);
		setRecruitPlace(recruitPlace);
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
	public String getRecruitTime() {
		return recruitTime;
	}
	public void setRecruitTime(String recruitTime) {
		this.recruitTime = recruitTime;
	}
	public String getRecruitPlace() {
		return recruitPlace;
	}
	public void setRecruitPlace(String recruitPlace) {
		this.recruitPlace = recruitPlace;
	}
	
	

}
