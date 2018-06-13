package cn.easy.xinjing.bean.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by chenzhongyi on 2017/4/17.
 */
@ApiModel("Vr室登录成功返回信息模型")
public class VrRoomLoginResultBean {
    @ApiModelProperty("用户Id")
    private String userId;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("真实姓名")
    private String realname;
    @ApiModelProperty("Token")
    private String token;
    @ApiModelProperty("VR室Id")
    private String vrRoomId;
    @ApiModelProperty("VR室名称")
    private String vrRoomName;

    @ApiModelProperty("医院名称")
    private String hospitalName;

    @ApiModelProperty("医院名称")
    private String hospitalId;

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVrRoomId() {
        return vrRoomId;
    }

    public void setVrRoomId(String vrRoomId) {
        this.vrRoomId = vrRoomId;
    }

    public String getVrRoomName() {
        return vrRoomName;
    }

    public void setVrRoomName(String vrRoomName) {
        this.vrRoomName = vrRoomName;
    }
}
