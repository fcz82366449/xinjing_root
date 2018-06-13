package cn.easy.xinjing.bean.api;

/**
 * Created by chenzy on 2017-4-8.
 */
public class VrRoomAppTaskBean extends ApiBaseBean {
    /**指令类型*/
    private Integer type;
    /**指令内容*/
    private String content;
    /**Vr室管理员*/
    private String userId;
    /**处方内容ID*/
    private String prescriptionContentId;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrescriptionContentId() {
        return prescriptionContentId;
    }

    public void setPrescriptionContentId(String prescriptionContentId) {
        this.prescriptionContentId = prescriptionContentId;
    }
}
