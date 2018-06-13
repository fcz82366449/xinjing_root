package cn.easy.xinjing.bean.api;

/**
 * Created by raytine on 2017/6/23.
 */
public class GameEfsBean extends  ApiBasePageBean{

    /**ip*/
    private String ip;
    /**VR室桌面用户**/
    private String loginuserid;
    /**VR室**/
    private String vrRoomId;

    public String getVrRoomId() {
        return vrRoomId;
    }

    public void setVrRoomId(String vrRoomId) {
        this.vrRoomId = vrRoomId;
    }

    /**mac地址*/
    private String mac;
    /**硬盘地址*/
    private String disk;
    /**是否隐藏*/
    private Integer hidden = 0;

    public String getLoginuserid() {
        return loginuserid;
    }

    public void setLoginuserid(String loginuserid) {
        this.loginuserid = loginuserid;
    }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public String getMac() { return mac; }
    public void setMac(String mac) { this.mac = mac; }
    public String getDisk() { return disk; }
    public void setDisk(String disk) { this.disk = disk; }

    public Integer getHidden() { return hidden; }

    public void setHidden(Integer hidden) { this.hidden = hidden; }
}
