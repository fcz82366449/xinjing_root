package cn.easy.xinjing.bean.api;


import cn.easy.xinjing.domain.ClickRecord;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by chenzhongyi on 16/9/18.
 */
public class ClickRecordBean extends ApiBasePageBean {
    /**场景*/
    private String contentId;
    /**点播时间*/
    private String clickDate;
    /**医院*/
    private String hospitalId;
    /**患者id**/
    private String patientcaseId;

    /**场景名称**/
    private String contentName;
    /**治疗师**/
    private String uservrName;

    public String getUservrName() {
        return uservrName;
    }

    public void setUservrName(String uservrName) {
        this.uservrName = uservrName;
    }

    /**病案号**/
    private String recordno;
    /**开始时间**/
    private String beginDate;
    /**结束时间**/
    private String endDate;
    /**点播类型**/
    private Integer type;



    public String gender ;
    public String mobile ;
    public String vrname ;
    public String bornDate ;
    public String name ;
    public String hospitalName ;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVrname() {
        return vrname;
    }

    public void setVrname(String vrname) {
        this.vrname = vrname;
    }

    public String getBornDate() {
        return bornDate;
    }

    public void setBornDate(String bornDate) {
        this.bornDate = bornDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getRecordno() {
        return recordno;
    }

    public void setRecordno(String recordno) {
        this.recordno = recordno;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    private List<Map<String, Object>> clickpageMap;

   private int totalPages;

   private long totalElements;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public List<Map<String, Object>> getClickpageMap() {
        return clickpageMap;
    }

    public void setClickpageMap(List<Map<String, Object>> clickpageMap) {
        this.clickpageMap = clickpageMap;
    }

    public String getPatientcaseId() {
        return patientcaseId;
    }

    public void setPatientcaseId(String patientcaseId) {
        this.patientcaseId = patientcaseId;
    }
    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }


    public String getContentId() { return contentId; }
    public void setContentId(String contentId) { this.contentId = contentId; }
    public String getClickDate() { return clickDate; }
    public void setClickDate(String clickDate) { this.clickDate = clickDate; }

}
