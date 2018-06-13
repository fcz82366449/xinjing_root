package cn.easy.xinjing.domain;

import cn.easy.base.domain.core.AtEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "xj_user_doctor_approve")
public class UserDoctorApprove extends AtEntity {
	/**用户ID*/
	private String userId;
	/**真实姓名*/
	private String realname;
	/**性别*/
	private Integer gender;
	/**地区编码*/
	private String region;
	/**职称Id*/
	private Integer professionalTitleId;
	/**工作场所类型*/
	private Integer workplaceType;
	/**在职医院*/
	private String hospital;
	/**职位*/
	private String position;
	/**科室*/
	private String department;
	/**头像*/
	private String headPictureUrl;
	/**自我介绍*/
	private String introduction;
	/**专长*/
	private String expertise;
	/**心理咨询师证*/
	private String psychologicalConsultantImageUrl;
	/**工作证*/
	private String employeeImageUrl;
	/**医师职业证*/
	private String doctorProfessionImageUrl;
	/**职称资格证*/
	private String professionalQualificationImageUrl;
	/**状态*/
	private Integer status;
	/**审批人*/
	private String approver;
	/**审核意见*/
	private String remark;

	private Timestamp approveAt;
	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }
	public String getRealname() { return realname; }
	public void setRealname(String realname) { this.realname = realname; }
	public Integer getGender() { return gender; }
	public void setGender(Integer gender) { this.gender = gender; }
	public String getRegion() { return region; }
	public void setRegion(String region) { this.region = region; }
	public Integer getProfessionalTitleId() { return professionalTitleId; }
	public void setProfessionalTitleId(Integer professionalTitleId) { this.professionalTitleId = professionalTitleId; }
	public Integer getWorkplaceType() { return workplaceType; }
	public void setWorkplaceType(Integer workplaceType) { this.workplaceType = workplaceType; }
	public String getHospital() { return hospital; }
	public void setHospital(String hospital) { this.hospital = hospital; }
	public String getPosition() { return position; }
	public void setPosition(String position) { this.position = position; }
	public String getDepartment() { return department; }
	public void setDepartment(String department) { this.department = department; }
	public String getHeadPictureUrl() { return headPictureUrl; }
	public void setHeadPictureUrl(String headPictureUrl) { this.headPictureUrl = headPictureUrl; }
	public String getIntroduction() { return introduction; }
	public void setIntroduction(String introduction) { this.introduction = introduction; }
	public String getExpertise() { return expertise; }
	public void setExpertise(String expertise) { this.expertise = expertise; }
	public String getPsychologicalConsultantImageUrl() { return psychologicalConsultantImageUrl; }
	public void setPsychologicalConsultantImageUrl(String psychologicalConsultantImageUrl) { this.psychologicalConsultantImageUrl = psychologicalConsultantImageUrl; }
	public String getEmployeeImageUrl() { return employeeImageUrl; }
	public void setEmployeeImageUrl(String employeeImageUrl) { this.employeeImageUrl = employeeImageUrl; }
	public String getDoctorProfessionImageUrl() { return doctorProfessionImageUrl; }
	public void setDoctorProfessionImageUrl(String doctorProfessionImageUrl) { this.doctorProfessionImageUrl = doctorProfessionImageUrl; }
	public String getProfessionalQualificationImageUrl() { return professionalQualificationImageUrl; }
	public void setProfessionalQualificationImageUrl(String professionalQualificationImageUrl) { this.professionalQualificationImageUrl = professionalQualificationImageUrl; }
	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status; }
	public String getApprover() { return approver; }
	public void setApprover(String approver) { this.approver = approver; }
	public String getRemark() { return remark; }
	public void setRemark(String remark) { this.remark = remark; }

	public Timestamp getApproveAt() {
		return approveAt;
	}

	public void setApproveAt(Timestamp approveAt) {
		this.approveAt = approveAt;
	}
}
