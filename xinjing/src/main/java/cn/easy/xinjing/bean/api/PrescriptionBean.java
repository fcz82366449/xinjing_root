package cn.easy.xinjing.bean.api;

import cn.easy.xinjing.domain.PrescriptionContent;
import cn.easy.xinjing.utils.Constants;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by chenzy on 2016/10/15.
 */
public class PrescriptionBean extends ApiBaseBean{
    private String id;
    private String userId;
    private String doctorId;
    private String suggestion;
    private int source;

    private BigDecimal total;
    /**
     * VR室相关字段
     * @return
     */
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
    private String creator;
    private Date createdAt;

    /**
     * 状态
     */
    private int status;
    /**
     * 支付状态
     */
    private int payStatus;

    /**
     * 新增医院
     */
    private String hospital;
    /**
     * VR室id
     */
    private String vrRoomId;


    public String getVrRoomId() {
        return vrRoomId;
    }

    public void setVrRoomId(String vrRoomId) {
        this.vrRoomId = vrRoomId;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    private List<PrescriptionContent> prescriptionContentList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public List<PrescriptionContent> getPrescriptionContentList() {
        return prescriptionContentList;
    }

    public void setPrescriptionContentList(List<PrescriptionContent> prescriptionContentList) {
        this.prescriptionContentList = prescriptionContentList;
    }
}
