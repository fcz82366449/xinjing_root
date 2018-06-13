package cn.easy.xinjing.bean.api;

import cn.easy.xinjing.utils.Constants;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by chenzhongyi on 16/9/18.
 */
public class DoctorBean extends ApiBaseBean {
    /** userId */
    private String id;
    private String realname;
    private String email;
    private String mobile;
    /**
     * 从业资格证
     */
    private String certificate;
    /**
     * 身份证正面
     */
    private String idCardFront;
    /**
     * 身份证反面
     */
    private String idCardInverse;
    /**
     * 照片
     */
    private String photo;
    /**
     * 在职医院
     */
    private String hospital;
    /**
     * 入职日期
     */
    private Date entryDate;
    /**
     * 自我介绍
     */
    private String introduction;
    /**
     * 专长
     */
    private String expertise;
    /**
     * 身份证号码
     */
    private String idNumber;
    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别
     */
    private Integer gender;

    private String headPictureUrl;
    private String region;
    private String regionFullName;
    private String signature;

    private Integer status;
    /**
     * 职称
     */
    private String title;

    /**
     * 咨询次数
     */
    private Integer consultationTimes;

    private BigDecimal minPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 0代表不出诊，1代表出诊
     *
     * @return
     */
    public Integer getIsVisitPatient() {
        if (Constants.DOCTOR_STATUS_NORMAL == status) {
            return 1;
        } else if (Constants.DOCTOR_STATUS_STOP == status) {
            return 0;
        }
        return -1;
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

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getIdCardFront() {
        return idCardFront;
    }

    public void setIdCardFront(String idCardFront) {
        this.idCardFront = idCardFront;
    }

    public String getIdCardInverse() {
        return idCardInverse;
    }

    public void setIdCardInverse(String idCardInverse) {
        this.idCardInverse = idCardInverse;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getHeadPictureUrl() {
        return headPictureUrl;
    }

    public void setHeadPictureUrl(String headPictureUrl) {
        this.headPictureUrl = headPictureUrl;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


    public String getRegionFullName() {
        return regionFullName;
    }

    public void setRegionFullName(String regionFullName) {
        this.regionFullName = regionFullName;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getConsultationTimes() {
        return consultationTimes;
    }

    public void setConsultationTimes(Integer consultationTimes) {
        this.consultationTimes = consultationTimes;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }
}
