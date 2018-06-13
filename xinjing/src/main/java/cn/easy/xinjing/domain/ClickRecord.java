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
@Table(name = "xj_click_record")
public class ClickRecord extends AtEntity implements IHiddenEntity {
	/**场景*/
	private String contentId;
	/**点播时间*/
	private String clickDate;
	/**是否隐藏*/
	private Integer hidden = 0;
	/**点播用户*/
	private String userId;
	/**患者id**/
	private String patientcaseId;

	public String getPatientcaseId() {
		return patientcaseId;
	}

	public void setPatientcaseId(String patientcaseId) {
		this.patientcaseId = patientcaseId;
	}

	/**医院*/
	private String hospitalId;
	/**点播类型**/
	private Integer type;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContentId() { return contentId; }
	public void setContentId(String contentId) { this.contentId = contentId; }
	public String getClickDate() { return clickDate; }
	public void setClickDate(String clickDate) { this.clickDate = clickDate; }
	@Override
public Integer getHidden() { return hidden; }
	@Override
public void setHidden(Integer hidden) { this.hidden = hidden; }

}
