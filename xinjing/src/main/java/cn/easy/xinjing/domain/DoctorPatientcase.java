package cn.easy.xinjing.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.easy.base.domain.core.AtEntity;

@Entity
@Table(name = "xj_doctor_patientcase")
public class DoctorPatientcase extends AtEntity {


	/**医生Id*/
	private String doctorId;
	/**病案号患者Id*/
	private String prescriptioncaseId;
	/**备注*/
	private String remark;

	private String hospitalId;

	public String getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getDoctorId() { return doctorId; }
	public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
	public String getPrescriptioncaseId() { return prescriptioncaseId; }
	public void setPrescriptioncaseId(String prescriptioncaseId) { this.prescriptioncaseId = prescriptioncaseId; }
	public String getRemark() { return remark; }
	public void setRemark(String remark) { this.remark = remark; }

}
