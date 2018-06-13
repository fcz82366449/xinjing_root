package cn.easy.xinjing.domain;

import cn.easy.base.core.repository.annotation.Hiddenable;
import cn.easy.base.domain.User;
import cn.easy.base.domain.core.AtEntity;
import cn.easy.base.domain.core.IHiddenEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
@Entity
@Hiddenable
@Table(name = "xj_user_patient")
public class UserPatient extends AtEntity implements IHiddenEntity {
	/**用户ID*/
	private String userId;
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
	private String pushId;
	/**是否隐藏*/
	private Integer hidden = 0;
	/**个人头像*/
	private String headPictureUrl;
	//级联对象
	private User user;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="userId", referencedColumnName="id", insertable=false, nullable=false, updatable=false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }
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
	@Override
	public Integer getHidden() { return hidden; }
	@Override
	public void setHidden(Integer hidden) { this.hidden = hidden; }

	public String getPushId() {
		return pushId;
	}

	public void setPushId(String pushId) {
		this.pushId = pushId;
	}

	public String getHeadPictureUrl() {
		return headPictureUrl;
	}

	public void setHeadPictureUrl(String headPictureUrl) {
		this.headPictureUrl = headPictureUrl;
	}
}
