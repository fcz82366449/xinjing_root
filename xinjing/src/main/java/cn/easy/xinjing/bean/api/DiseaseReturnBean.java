package cn.easy.xinjing.bean.api;

/**
 * Created by raytine on 2017/7/11.
 */
public class DiseaseReturnBean {
    /**病症ID**/
    private String diseaseId;
    /**病症名称**/
    private String diseaseName;

    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }
}
