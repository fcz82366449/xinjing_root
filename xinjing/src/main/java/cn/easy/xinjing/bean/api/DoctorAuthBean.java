package cn.easy.xinjing.bean.api;

/**
 * Created by chenzhongyi on 16/9/26.
 */
public class DoctorAuthBean extends ApiBaseBean {
    private String realname;
    private Integer gender;
    private String region;
    private Integer professionalTitleId;
    private Integer workplaceType;
    /**
     * 在职医院
     */
    private String hospital;
    private String department;
    private String position;
    /**
     * 个人头像
     */
    private String headPictureUrl;
    /**
     * 自我介绍
     */
    private String introduction;
    /**
     * 专长
     */
    private String expertise;

    private String psychologicalConsultantImageUrl;
    private String employeeImageUrl;
    private String doctorProfessionImageUrl;
    private String professionalQualificationImageUrl;


    //返回字段
    private Integer status;
    private String regionFullName;
    private String professionalTitle;

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getProfessionalTitleId() {
        return professionalTitleId;
    }

    public void setProfessionalTitleId(Integer professionalTitleId) {
        this.professionalTitleId = professionalTitleId;
    }

    public Integer getWorkplaceType() {
        return workplaceType;
    }

    public void setWorkplaceType(Integer workplaceType) {
        this.workplaceType = workplaceType;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getHeadPictureUrl() {
        return headPictureUrl;
    }

    public void setHeadPictureUrl(String headPictureUrl) {
        this.headPictureUrl = headPictureUrl;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getPsychologicalConsultantImageUrl() {
        return psychologicalConsultantImageUrl;
    }

    public void setPsychologicalConsultantImageUrl(String psychologicalConsultantImageUrl) {
        this.psychologicalConsultantImageUrl = psychologicalConsultantImageUrl;
    }

    public String getEmployeeImageUrl() {
        return employeeImageUrl;
    }

    public void setEmployeeImageUrl(String employeeImageUrl) {
        this.employeeImageUrl = employeeImageUrl;
    }

    public String getDoctorProfessionImageUrl() {
        return doctorProfessionImageUrl;
    }

    public void setDoctorProfessionImageUrl(String doctorProfessionImageUrl) {
        this.doctorProfessionImageUrl = doctorProfessionImageUrl;
    }

    public String getProfessionalQualificationImageUrl() {
        return professionalQualificationImageUrl;
    }

    public void setProfessionalQualificationImageUrl(String professionalQualificationImageUrl) {
        this.professionalQualificationImageUrl = professionalQualificationImageUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRegionFullName() {
        return regionFullName;
    }

    public void setRegionFullName(String regionFullName) {
        this.regionFullName = regionFullName;
    }

    public String getProfessionalTitle() {
        return professionalTitle;
    }

    public void setProfessionalTitle(String professionalTitle) {
        this.professionalTitle = professionalTitle;
    }
}
