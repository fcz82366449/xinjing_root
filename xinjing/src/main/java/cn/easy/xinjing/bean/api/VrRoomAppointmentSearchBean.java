package cn.easy.xinjing.bean.api;

/**
 * Created by chenzhongyi on 2017/1/18.
 */
public class VrRoomAppointmentSearchBean extends ApiBasePageBean {
    private String vrRoomId;
    private String keyword;

    public String getVrRoomId() {
        return vrRoomId;
    }

    public void setVrRoomId(String vrRoomId) {
        this.vrRoomId = vrRoomId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
