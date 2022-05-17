package com.basic.operations.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "appversion")
public class AppVersion {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name="native",strategy="native")
	private int id;
    private String appVersionAndroid;
    private String appVersionIOS;
    private String appLinkIOS;
    private String appLinkAndroid;
    @Column(name = "addVersionTime", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addVersionTime;
    
    private int compulsoryForAndroid;
    private int compulsoryForIOS;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAppVersionAndroid() {
		return appVersionAndroid;
	}
	public void setAppVersionAndroid(String appVersionAndroid) {
		this.appVersionAndroid = appVersionAndroid;
	}
	public String getAppVersionIOS() {
		return appVersionIOS;
	}
	public void setAppVersionIOS(String appVersionIOS) {
		this.appVersionIOS = appVersionIOS;
	}
	
	public String getAppLinkIOS() {
		return appLinkIOS;
	}
	public void setAppLinkIOS(String appLinkIOS) {
		this.appLinkIOS = appLinkIOS;
	}
	public String getAppLinkAndroid() {
		return appLinkAndroid;
	}
	public void setAppLinkAndroid(String appLinkAndroid) {
		this.appLinkAndroid = appLinkAndroid;
	}
	public Date getAddVersionTime() {
		return addVersionTime;
	}
	public void setAddVersionTime(Date addVersionTime) {
		this.addVersionTime = addVersionTime;
	}
	public int getCompulsoryForAndroid() {
		return compulsoryForAndroid;
	}
	public void setCompulsoryForAndroid(int compulsoryForAndroid) {
		this.compulsoryForAndroid = compulsoryForAndroid;
	}
	public int getCompulsoryForIOS() {
		return compulsoryForIOS;
	}
	public void setCompulsoryForIOS(int compulsoryForIOS) {
		this.compulsoryForIOS = compulsoryForIOS;
	}
    
}

