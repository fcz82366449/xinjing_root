package cn.easy.xinjing.bean.api;

/**
 * Created by raytine on 2017/6/29.
 */
public class CalllimitBean extends ApiBaseBean{

    /**医患Id*/
    private String patientId;

    /**状态*/
    private Integer status;

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }


}
