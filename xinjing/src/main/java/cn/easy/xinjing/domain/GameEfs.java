package cn.easy.xinjing.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.easy.base.domain.core.AtEntity;
import cn.easy.base.domain.core.IHiddenEntity;
import cn.easy.base.core.repository.annotation.Hiddenable;

@Entity
@Hiddenable
@Table(name = "xj_gameEfs")
public class GameEfs extends AtEntity implements IHiddenEntity {
	/**ip*/
	private String ip;
	/**VR室桌面用户**/
	private String vruser;
	/**VR室**/
	private String vrRoomId;

	public String getVrRoomId() {
		return vrRoomId;
	}

	public void setVrRoomId(String vrRoomId) {
		this.vrRoomId = vrRoomId;
	}

	/**mac地址*/
	private String mac;
	/**硬盘地址*/
	private String disk;
	/**是否隐藏*/
	private Integer hidden = 0;

	public String getVruser() {
		return vruser;
	}

	public void setVruser(String vruser) {
		this.vruser = vruser;
	}

	public String getIp() { return ip; }
	public void setIp(String ip) { this.ip = ip; }
	public String getMac() { return mac; }
	public void setMac(String mac) { this.mac = mac; }
	public String getDisk() { return disk; }
	public void setDisk(String disk) { this.disk = disk; }
	@Override
public Integer getHidden() { return hidden; }
	@Override
public void setHidden(Integer hidden) { this.hidden = hidden; }

}
