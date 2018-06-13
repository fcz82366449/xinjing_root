package cn.easy.xinjing.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by chenzhongyi on 2016/9/28.
 */
@Component
public class GtPatientPushService extends GtPushService {
    @Value("${getui.patient.appId}")
    public void setAppId(String appId) {
        this.appId = appId;
    }
    @Value("${getui.patient.appKey}")
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
    @Value("${getui.patient.masterSecret}")
    public void setMasterSecret(String masterSecret) {
        this.masterSecret = masterSecret;
    }
    @Value("${getui.patient.url}")
    public void setUrl(String url) {
        this.url = url;
    }



}
