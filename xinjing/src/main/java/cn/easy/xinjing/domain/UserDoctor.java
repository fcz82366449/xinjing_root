package cn.easy.xinjing.domain;

import cn.easy.base.core.repository.annotation.Hiddenable;
import cn.easy.base.domain.User;
import cn.easy.base.domain.core.AtEntity;
import cn.easy.base.domain.core.IHiddenEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
@Entity
@Hiddenable
@Table(name = "xj_user_doctor")
public class UserDoctor extends AtEntity implements IHiddenEntity {
    /**
     * 用户ID
     */
    private String userId;
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
     * 身份证号
     */
    private String idNumber;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 星级
     */
    private Integer level;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 排班信息
     */
    private String scheduleInfo;
    private String pushId;
    /**
     * 个人头像
     */
    private String headPictureUrl;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 待收金额
     */
    private BigDecimal receivableAmount;
    private String region;
    /**
     * 个性签名
     */
    private String signature;
    /**
     * 最低价格
     */
    private BigDecimal minPrice = new BigDecimal(500);
    /**
     * 是否隐藏
     */
    private Integer hidden = 0;
    /**
     * 咨询次数
     */
    private Integer consultationTimes;

    /**
     * 积分
     */
    private Integer point;

    //级联对象
    private User user;

    private Integer professionalTitleId;
    private Integer workplaceType;
    private String position;
    private String department;
    private String psychologicalConsultantImageUrl;
    private String employeeImageUrl;
    private String doctorProfessionImageUrl;
    private String professionalQualificationImageUrl;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, nullable = false, updatable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getScheduleInfo() {
        return scheduleInfo;
    }

    public void setScheduleInfo(String scheduleInfo) {
        this.scheduleInfo = scheduleInfo;
    }

    @Override
    public Integer getHidden() {
        return hidden;
    }

    @Override
    public void setHidden(Integer hidden) {
        this.hidden = hidden;
    }

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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(BigDecimal receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public Integer getConsultationTimes() {
        if(consultationTimes == null) {
            consultationTimes = 0;
        }
        return consultationTimes;
    }

    public void setConsultationTimes(Integer consultationTimes) {
        this.consultationTimes = consultationTimes;
    }

    public Integer getProfessionalTitleId() {
        return professionalTitleId;
    }

    public void setProfessionalTitleId(Integer professionalTitleId) {
        this.professionalTitleId = professionalTitleId;
    }

    public Integer getWorkplaceType() {
        return workplaceType;
    }

    public void setWorkplaceType(Integer workplaceType) {
        this.workplaceType = workplaceType;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPsychologicalConsultantImageUrl() {
        return psychologicalConsultantImageUrl;
    }

    public void setPsychologicalConsultantImageUrl(String psychologicalConsultantImageUrl) {
        this.psychologicalConsultantImageUrl = psychologicalConsultantImageUrl;
    }

    public String getEmployeeImageUrl() {
        return employeeImageUrl;
    }

    public void setEmployeeImageUrl(String employeeImageUrl) {
        this.employeeImageUrl = employeeImageUrl;
    }

    public String getDoctorProfessionImageUrl() {
        return doctorProfessionImageUrl;
    }

    public void setDoctorProfessionImageUrl(String doctorProfessionImageUrl) {
        this.doctorProfessionImageUrl = doctorProfessionImageUrl;
    }

    public String getProfessionalQualificationImageUrl() {
        return professionalQualificationImageUrl;
    }

    public void setProfessionalQualificationImageUrl(String professionalQualificationImageUrl) {
        this.professionalQualificationImageUrl = professionalQualificationImageUrl;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}
