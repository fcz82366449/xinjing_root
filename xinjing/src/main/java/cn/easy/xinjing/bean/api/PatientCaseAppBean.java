package cn.easy.xinjing.bean.api;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by chenzhongyi on 16/9/18.
 */
public class PatientCaseAppBean extends ApiBasePageBean {

    /**VR室ID**/
    private String roomId;

    /**病历号**/
    private String clinichistoryNo;
    /**姓名**/
    private String name;

    /**出生年月 例:1993-11-23**/
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**性别 1 男 2 女 0 保密**/
    private Integer sex;

    /**手机号码**/
    private String phone;

    /**婚姻状况 1已婚 2未婚 0保密**/
    private Integer maritalStatus;

    /**文化程度 1文盲、2小学、3初中、4高中、5大学、6研究生及以上**/
    private Integer educationDegree;

    /**医保卡号**/
    private String medicalInsuranceCardNo;
    /**病症id**/
    private String diseaseId;

    /**病症名称**/
    private String disease;

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    /**备注**/
    private String remark;
    /**是否能修改个人信息**/
    private Integer canModify;

    /**要修改的患者id**/
    private String patientId;

    private Date ts;

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }



    public Integer getCanModify() {
        return canModify;
    }

    public void setCanModify(Integer canModify) {
        this.canModify = canModify;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getClinichistoryNo() {
        return clinichistoryNo;
    }

    public void setClinichistoryNo(String clinichistoryNo) {
        this.clinichistoryNo = clinichistoryNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Integer maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Integer getEducationDegree() {
        return educationDegree;
    }

    public void setEducationDegree(Integer educationDegree) {
        this.educationDegree = educationDegree;
    }

    public String getMedicalInsuranceCardNo() {
        return medicalInsuranceCardNo;
    }

    public void setMedicalInsuranceCardNo(String medicalInsuranceCardNo) {
        this.medicalInsuranceCardNo = medicalInsuranceCardNo;
    }
}
