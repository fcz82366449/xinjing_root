package cn.easy.xinjing.bean.api;

/**
 * Created by chenzy on 2016/10/6.
 */
public class ContentSearchBean extends ApiBasePageBean {
    private String diseaseId;
    private String therapyId;
    private Integer type;
    private String keyword;

    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getTherapyId() {
        return therapyId;
    }

    public void setTherapyId(String therapyId) {
        this.therapyId = therapyId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
