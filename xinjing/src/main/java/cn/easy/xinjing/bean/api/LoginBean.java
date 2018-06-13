package cn.easy.xinjing.bean.api;

/**
 * Created by chenzhongyi on 16/9/18.
 */
public class LoginBean {
    private String username;
    private String password;

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
    private String pushId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

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
}
