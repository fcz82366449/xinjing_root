package cn.easy.xinjing.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.easy.base.domain.core.AtEntity;
import cn.easy.xinjing.utils.Constants;

@Entity
@Table(name = "xj_app_token")
public class AppToken extends AtEntity {
    /**
     * APP版本信息
     */
    private String appVersion;
    /**
     * 设备系统平台
     */
    private String deviceSystem;
    /**
     * 设备系统版本
     */
    private String deviceVersion;
    /**
     * 设备型号
     */
    private String deviceModel;
    /**
     * 客户端唯一标识
     */
    private String appId;
    /**
     * 推广通道
     */
    private String channel;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户类型
     */
    private Integer userType;

    private Integer status = Constants.APP_TOKEN_STATUS_VALID;

    private Date logoutAt;

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDeviceSystem() {
        return deviceSystem;
    }

    public void setDeviceSystem(String deviceSystem) {
        this.deviceSystem = deviceSystem;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getLogoutAt() {
        return logoutAt;
    }

    public void setLogoutAt(Date logoutAt) {
        this.logoutAt = logoutAt;
    }
}
