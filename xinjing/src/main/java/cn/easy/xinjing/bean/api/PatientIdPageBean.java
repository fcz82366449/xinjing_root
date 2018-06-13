package cn.easy.xinjing.bean.api;

/**
 * Created by chenzy on 2016/10/15.
 */
public class PatientIdPageBean extends ApiBasePageBean {

    private String patientId;

    private String doctorId;
    private Integer status;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

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
