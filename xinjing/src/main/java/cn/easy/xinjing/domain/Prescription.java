package cn.easy.xinjing.domain;

import cn.easy.base.core.repository.annotation.Hiddenable;
import cn.easy.base.domain.User;
import cn.easy.base.domain.core.AtEntity;
import cn.easy.base.domain.core.IHiddenEntity;
import cn.easy.xinjing.utils.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.math.BigDecimal;

@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler", "fieldHandler" })
@Entity
@Hiddenable
@Table(name = "xj_prescription")
public class Prescription extends AtEntity implements IHiddenEntity {
    /**
     * 状态
     */
    private Integer status = Constants.PRESCRIPTION_STATUS_INIT;
    /**
     * 支付状态
     */
    private Integer payStatus = Constants.PRESCRIPTION_PAY_STATUS_UNPAY;
    /**
     * 合计
     */
    private BigDecimal total = BigDecimal.ZERO;
    /**
     * 治疗建议
     */
    private String suggestion;
    /**
     * 医生Id
     */
    private String doctorId;
    /**
     * 医患Id
     */
    private String patientId;


    private Integer source;
    /**
     * 医生姓名
     */
    private String doctorName;
    /**
     * 住院号
     */
    private String admissionNumber;
    /**
     * 门诊号
     */
    private String outpatientNumber;
    /**
     * 病症
     */
    private String disease;
    /**
     * 是否隐藏
     */
    private Integer hidden = 0;

    private User doctor;

    private User patient;

    /**订单号*/
    private String billno;
    /**医院*/
    private String hospitalId;

    /**病案号版本患者id**/
    private String patientcaseId;

    public String getPatientcaseId() {
        return patientcaseId;
    }

    public void setPatientcaseId(String patientcaseId) {
        this.patientcaseId = patientcaseId;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctorId", referencedColumnName = "id", insertable = false, nullable = false, updatable = false)
    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patientId", referencedColumnName = "id", insertable = false, nullable = false, updatable = false)
    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public BigDecimal getTotal() {
        if(total == null) {
            return BigDecimal.ZERO;
        }
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getAdmissionNumber() {
        return admissionNumber;
    }

    public void setAdmissionNumber(String admissionNumber) {
        this.admissionNumber = admissionNumber;
    }

    public String getOutpatientNumber() {
        return outpatientNumber;
    }

    public void setOutpatientNumber(String outpatientNumber) {
        this.outpatientNumber = outpatientNumber;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    @Override
    public Integer getHidden() {
        return hidden;
    }

    @Override
    public void setHidden(Integer hidden) {
        this.hidden = hidden;
    }

}
