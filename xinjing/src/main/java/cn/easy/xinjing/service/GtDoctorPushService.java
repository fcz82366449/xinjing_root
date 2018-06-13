package cn.easy.xinjing.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by chenzhongyi on 2016/9/28.
 */
@Component
public class GtDoctorPushService extends GtPushService{
    @Value("${getui.doctor.appId}")
    public void setAppId(String appId) {
        this.appId = appId;
    }
    @Value("${getui.doctor.appKey}")
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
    @Value("${getui.doctor.masterSecret}")
    public void setMasterSecret(String masterSecret) {
        this.masterSecret = masterSecret;
    }
    @Value("${getui.doctor.url}")
    public void setUrl(String url) {
        this.url = url;
    }

}
