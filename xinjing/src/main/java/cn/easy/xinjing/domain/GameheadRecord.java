package cn.easy.xinjing.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.easy.base.domain.core.AtEntity;
import cn.easy.base.domain.core.IHiddenEntity;
import cn.easy.base.core.repository.annotation.Hiddenable;
import org.joda.time.DateTime;

@Entity
@Hiddenable
@Table(name = "xj_gamehead_record")
public class GameheadRecord extends AtEntity implements IHiddenEntity {
	/**VR室登陆人用户id*/
	private String loginuserid;
	/**患者id*/
	private String patientid;
	/**游戏id*/
	private String gameid;
	/**内容处方id*/
	private String contentid;
	/**VR室id*/
	private String vrroomid;
	/**游戏总时长*/
	private String gamedatatime;
	/**客户端点击播放的时间*/
	private Date clickdatatime;
	/**是否隐藏*/
	private Integer hidden = 0;
	/**医院id*/
	private String hospitalId ;

	public String getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getLoginuserid() { return loginuserid; }
	public void setLoginuserid(String loginuserid) { this.loginuserid = loginuserid; }
	public String getPatientid() { return patientid; }
	public void setPatientid(String patientid) { this.patientid = patientid; }
	public String getGameid() { return gameid; }
	public void setGameid(String gameid) { this.gameid = gameid; }
	public String getContentid() { return contentid; }
	public void setContentid(String contentid) { this.contentid = contentid; }
	public String getVrroomid() { return vrroomid; }
	public void setVrroomid(String vrroomid) { this.vrroomid = vrroomid; }
	public String getGamedatatime() { return gamedatatime; }
	public void setGamedatatime(String gamedatatime) { this.gamedatatime = gamedatatime; }

	public Date getClickdatatime() {
		return clickdatatime;
	}

	public void setClickdatatime(Date clickdatatime) {
		this.clickdatatime = clickdatatime;
	}

	@Override
public Integer getHidden() { return hidden; }
	@Override
public void setHidden(Integer hidden) { this.hidden = hidden; }

}
