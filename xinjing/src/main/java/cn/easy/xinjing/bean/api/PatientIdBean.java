package cn.easy.xinjing.bean.api;

/**
 * Created by chenzy on 2016/10/15.
 */
public class PatientIdBean extends ApiBaseBean {

    private String patientId;

    private String vrRoomId;

    public String getVrRoomId() {
        return vrRoomId;
    }

    public void setVrRoomId(String vrRoomId) {
        this.vrRoomId = vrRoomId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
