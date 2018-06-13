package cn.easy.xinjing.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.easy.base.domain.core.AtEntity;

@Entity
@Table(name = "xj_app_version")
public class AppVersion extends AtEntity {
	/**APP编码*/
	private String appCode;
	/**下载地址*/
	private String downloadUrl;
	/**版本编号*/
	private String versionCode;
	/**版本描述*/
	private String versionDesc;
	/**版本名称*/
	private String versionName;
	/**版本大小*/
	private String versionSize;
	public String getAppCode() { return appCode; }
	public void setAppCode(String appCode) { this.appCode = appCode; }
	public String getDownloadUrl() { return downloadUrl; }
	public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
	public String getVersionCode() { return versionCode; }
	public void setVersionCode(String versionCode) { this.versionCode = versionCode; }
	public String getVersionDesc() { return versionDesc; }
	public void setVersionDesc(String versionDesc) { this.versionDesc = versionDesc; }
	public String getVersionName() { return versionName; }
	public void setVersionName(String versionName) { this.versionName = versionName; }
	public String getVersionSize() { return versionSize; }
	public void setVersionSize(String versionSize) { this.versionSize = versionSize; }

}
