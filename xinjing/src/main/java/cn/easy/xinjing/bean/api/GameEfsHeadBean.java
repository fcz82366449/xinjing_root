package cn.easy.xinjing.bean.api;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

/**
 * Created by raytine on 2017/6/23.
 */
public class GameEfsHeadBean extends  ApiBasePageBean{

    /**ip**/
    private String ip;
    /**mac**/
    private String mac;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    /**VR室登陆人用户id**/
    private String loginuserid;
    /**患者id**/
    private String patientid;
    /**游戏id**/
    private String gameid;
    /**内容处方id**/
    private String contentid;
    /**VR室id**/
    private String vrroomid;
    /**游戏总时长**/
    private String gamedatatime;
    /**客户端点击播放的时间**/
    private String clickdatatime;

    public String getGamedatatime() {
        return gamedatatime;
    }

    public void setGamedatatime(String gamedatatime) {
        this.gamedatatime = gamedatatime;
    }

    public String getClickdatatime() {
        return clickdatatime;
    }

    public void setClickdatatime(String clickdatatime) {
        this.clickdatatime = clickdatatime;
    }

    private List<GameEfsItemsBean> items;

    public String getLoginuserid() {
        return loginuserid;
    }

    public void setLoginuserid(String loginuserid) {
        this.loginuserid = loginuserid;
    }

    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
    }

    public String getVrroomid() {
        return vrroomid;
    }

    public void setVrroomid(String vrroomid) {
        this.vrroomid = vrroomid;
    }

    public List<GameEfsItemsBean> getItems() {
        return items;
    }

    public void setItems(List<GameEfsItemsBean> items) {
        this.items = items;
    }
}
