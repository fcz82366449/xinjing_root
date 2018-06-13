package cn.easy.xinjing.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.easy.base.domain.core.AtEntity;

@Entity
@Table(name = "xj_doctor_plan_content")
public class DoctorPlanContent extends AtEntity {
	/**医生方案id*/
	private String doctorPlanId;
	/**内容Id*/
	private String contentId;
	/**状态*/
	private Integer status;

	public String getDoctorPlanId() { return doctorPlanId; }
	public void setDoctorPlanId(String doctorPlanId) { this.doctorPlanId = doctorPlanId; }
	public String getContentId() { return contentId; }
	public void setContentId(String contentId) { this.contentId = contentId; }
	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status; }

}
