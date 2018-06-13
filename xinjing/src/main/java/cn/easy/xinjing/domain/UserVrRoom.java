package cn.easy.xinjing.domain;

import cn.easy.base.domain.User;
import cn.easy.base.domain.core.AtEntity;
import cn.easy.base.domain.core.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "xj_user_vr_room")
public class UserVrRoom extends AtEntity {
	/**用户ID*/
	private String userId;
	/**VR室ID*/
	private String vrRoomId;
	/**备注*/
	private String remark;

	private Integer type;

	//级联对象
	private User user;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, nullable = false, updatable = false)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	//级联对象
	private VrRoom vrRoom;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vrRoomId", referencedColumnName = "id", insertable = false, nullable = false, updatable = false)
	public VrRoom getVrRoom() {
		return vrRoom;
	}

	public void setVrRoom(VrRoom vrRoom) {
		this.vrRoom = vrRoom;
	}

	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }
	public String getVrRoomId() { return vrRoomId; }
	public void setVrRoomId(String vrRoomId) { this.vrRoomId = vrRoomId; }
	public String getRemark() { return remark; }
	public void setRemark(String remark) { this.remark = remark; }

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
