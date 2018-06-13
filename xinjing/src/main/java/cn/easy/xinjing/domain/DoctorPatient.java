package cn.easy.xinjing.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.easy.base.domain.core.AtEntity;

@Entity
@Table(name = "xj_doctor_patient")
public class DoctorPatient extends AtEntity {
	/**医生Id*/
	private String doctorId;
	/**患者Id*/
	private String patientId;
	public String getDoctorId() { return doctorId; }
	public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
	public String getPatientId() { return patientId; }
	public void setPatientId(String patientId) { this.patientId = patientId; }

}
