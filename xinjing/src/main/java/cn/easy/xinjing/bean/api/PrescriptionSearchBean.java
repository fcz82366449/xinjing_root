package cn.easy.xinjing.bean.api;


/**
 * Created by chenzy on 2016/10/15.
 */
public class PrescriptionSearchBean extends ApiBasePageBean{
    private String patientId;
    private String doctorId;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

}
