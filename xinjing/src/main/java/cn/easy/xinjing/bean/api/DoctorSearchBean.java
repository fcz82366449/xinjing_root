package cn.easy.xinjing.bean.api;


import cn.easy.base.bean.PageBean;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by chenzy on 2016/10/15.
 */
public class DoctorSearchBean {
    private String region;
    private Integer isVisitPatient;
    private String keyword;
    private String disease;

    private int paging;

    @JsonIgnore
    public PageBean getPageBean() {
        PageBean pageBean = new PageBean();
        pageBean.setCurrentPage(getPaging());
        return pageBean;
    }

    public int getPaging() {
        return paging;
    }

    public void setPaging(int paging) {
        this.paging = paging;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * 0代表不出诊，1代表出诊
     * @return
     */
    public Integer getIsVisitPatient() {
        if(isVisitPatient == null) {
            return -1;
        }
        return isVisitPatient;
    }

    public void setIsVisitPatient(Integer isVisitPatient) {
        this.isVisitPatient = isVisitPatient;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }
}
