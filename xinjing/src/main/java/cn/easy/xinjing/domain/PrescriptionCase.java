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
@Table(name = "xj_prescription_case")
public class PrescriptionCase extends AtEntity implements IHiddenEntity {
	/**用户ID*/
	private String userId;
	/**患者编码*/
	private String code;
	/**患者名称*/
	private String name;
	/**性别*/
	private Integer gender;
	/**病历号*/
	private String recordno;
	/**ICD10码*/
	private String icd10;
	/**ICD10码*/
	private String icd10name;

	public String getIcd10name() {
		return icd10name;
	}

	public void setIcd10name(String icd10name) {
		this.icd10name = icd10name;
	}

	/**出生日期*/
	private Date bornDate;
	/**入院日期*/
	private Date hospitalDate;
	/**文化程度*/
	private String schooling;
	/**婚姻状况*/
	private String marriage;
	/**医保类型*/
	private Integer medicareType;
	/**医保卡号*/
	private String medicareCardNumber;
	/**身份证号*/
	private String idNumber;
	/**年龄*/
	private Integer age;
	/**家庭住址*/
	private String address;
	/**紧急联系人*/
	private String emergencyContact;
	/**紧急联系人电话*/
	private String emergencyContactPhone;
	/**手机号码**/
	private String mobile;
	/**医院id**/
	private String hospitalId;
	/**医院id**/
	private String hospitalName;

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	private Date ts;

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public String getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**设备ID*/
	private String pushId;
	/**是否隐藏*/
	private Integer hidden = 0;
	/**来源vr室*/
	private String vrroomid;
	/**数据来源*/
	private Integer source;
	/**状态*/
	private Integer status;

	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVrroomid() {
		return vrroomid;
	}

	public void setVrroomid(String vrroomid) {
		this.vrroomid = vrroomid;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }
	public String getCode() { return code; }
	public void setCode(String code) { this.code = code; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public Integer getGender() { return gender; }
	public void setGender(Integer gender) { this.gender = gender; }
	public String getRecordno() { return recordno; }
	public void setRecordno(String recordno) { this.recordno = recordno; }
	public String getIcd10() { return icd10; }
	public void setIcd10(String icd10) { this.icd10 = icd10; }
	public Date getBornDate() { return bornDate; }
	public void setBornDate(Date bornDate) { this.bornDate = bornDate; }
	public Date getHospitalDate() { return hospitalDate; }
	public void setHospitalDate(Date hospitalDate) { this.hospitalDate = hospitalDate; }
	public String getSchooling() { return schooling; }
	public void setSchooling(String schooling) { this.schooling = schooling; }
	public String getMarriage() { return marriage; }
	public void setMarriage(String marriage) { this.marriage = marriage; }
	public Integer getMedicareType() { return medicareType; }
	public void setMedicareType(Integer medicareType) { this.medicareType = medicareType; }
	public String getMedicareCardNumber() { return medicareCardNumber; }
	public void setMedicareCardNumber(String medicareCardNumber) { this.medicareCardNumber = medicareCardNumber; }
	public String getIdNumber() { return idNumber; }
	public void setIdNumber(String idNumber) { this.idNumber = idNumber; }
	public Integer getAge() { return age; }
	public void setAge(Integer age) { this.age = age; }
	public String getAddress() { return address; }
	public void setAddress(String address) { this.address = address; }
	public String getEmergencyContact() { return emergencyContact; }
	public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
	public String getEmergencyContactPhone() { return emergencyContactPhone; }
	public void setEmergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; }
	public String getPushId() { return pushId; }
	public void setPushId(String pushId) { this.pushId = pushId; }
	@Override
public Integer getHidden() { return hidden; }
	@Override
public void setHidden(Integer hidden) { this.hidden = hidden; }

}
