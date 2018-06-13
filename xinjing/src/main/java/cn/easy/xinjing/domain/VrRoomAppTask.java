package cn.easy.xinjing.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.easy.base.domain.core.AtEntity;
import cn.easy.xinjing.utils.Constants;
import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "xj_vr_room_app_task")
public class VrRoomAppTask extends AtEntity {
	/**指令类型*/
	private Integer type;
	/**指令内容*/
	private String content;
	/**Vr室管理员*/
	private String userId;
	/**状态*/
	private Integer status;

	private String prescriptionContentId;

	@Transient
	@JsonIgnore
	public boolean isPlayType() {
		return getType() == Constants.VR_ROOM_APP_TASK_TYPE_PLAY;
	}
	@Transient
	@JsonIgnore
	public boolean isEndType() {
		return getType() == Constants.VR_ROOM_APP_TASK_TYPE_END;
	}

	public Integer getType() { return type; }
	public void setType(Integer type) { this.type = type; }
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }
	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }
	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status; }

	public String getPrescriptionContentId() {
		return prescriptionContentId;
	}

	public void setPrescriptionContentId(String prescriptionContentId) {
		this.prescriptionContentId = prescriptionContentId;
	}
}
