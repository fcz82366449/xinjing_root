package cn.easy.xinjing.bean.api;

import cn.easy.base.domain.User;
import cn.easy.xinjing.domain.UserPatient;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by chenzhongyi on 16/9/18.
 */
public class PatientAppReturnBean extends ApiBaseBean {
    private String userId;
    private String realname;
    private String email;
    private String mobile;
    private String recordno;

    public String getRecordno() {
        return recordno;
    }

    public void setRecordno(String recordno) {
        this.recordno = recordno;
    }

    /**
     * 医保类型
     */
    private Integer medicareType;
    /**
     * 医保卡号
     */
    private String medicareCardNumber;
    /**
     * 身份证号
     */
    private String idNumber;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 家庭住址
     */
    private String address;
    /**
     * 紧急联系人
     */
    private String emergencyContact;
    /**
     * 紧急联系人电话
     */
    private String emergencyContactPhone;
    /**
     * 性别
     */
    private Integer gender;

    public static PatientAppReturnBean valueOf(User user, UserPatient userPatient) {
        if(user == null || userPatient == null) {
            return null;
        }
        PatientAppReturnBean patientBean = new PatientAppReturnBean();
        try {
            PropertyUtils.copyProperties(patientBean, user);
            PropertyUtils.copyProperties(patientBean, userPatient);
        } catch (IllegalAccessException | InvocationTargetException |NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
        patientBean.setUserId(user.getId());
        return patientBean;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getMedicareType() {
        return medicareType;
    }

    public void setMedicareType(Integer medicareType) {
        this.medicareType = medicareType;
    }

    public String getMedicareCardNumber() {
        return medicareCardNumber;
    }

    public void setMedicareCardNumber(String medicareCardNumber) {
        this.medicareCardNumber = medicareCardNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }
}
