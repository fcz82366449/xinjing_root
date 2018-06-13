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
@Table(name = "xj_calllimit")
public class Calllimit extends AtEntity implements IHiddenEntity {
	/**医生Id*/
	private String doctorId;
	/**医患Id*/
	private String patientId;
	/**医生Name*/
	private String doctorName;
	/**医患Name*/
	private String patientName;

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	/**状态*/
	private Integer payStatus;
	/**备注*/
	private String remark;
	/**是否隐藏*/
	private Integer hidden = 0;

	public String getDoctorId() { return doctorId; }
	public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
	public String getPatientId() { return patientId; }
	public void setPatientId(String patientId) { this.patientId = patientId; }
	public Integer getPayStatus() { return payStatus; }
	public void setPayStatus(Integer payStatus) { this.payStatus = payStatus; }
	public String getRemark() { return remark; }
	public void setRemark(String remark) { this.remark = remark; }
	@Override
public Integer getHidden() { return hidden; }
	@Override
public void setHidden(Integer hidden) { this.hidden = hidden; }

}
