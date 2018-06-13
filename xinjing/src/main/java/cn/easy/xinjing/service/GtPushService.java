package cn.easy.xinjing.service;

import com.gexin.rp.sdk.base.IAliasResult;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzhongyi on 2016/9/28.
 */
@Component
public class GtPushService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${getui.appId}")
    protected String appId = "appId";
    @Value("${getui.appKey}")
    protected String appKey = "appKey";
    @Value("${getui.masterSecret}")
    protected String masterSecret = "masterSecret";
    @Value("${getui.url}")
    protected String url = "http://sdk.open.api.igexin.com/apiex.htm";

    /**
     * 全局推送
     * @param title
     * @param content
     */
    public void pushMessageToAll(String title, String content) {
        pushMessageToAll(title, content, null);
    }

    /**
     * 全局推送
     * @param title
     * @param content
     * @param url
     */
    public void pushMessageToAll(String title, String content, String url) {
        LinkTemplate template = getLinkTemplate(title, content, url);

        List<String> appIds = new ArrayList<>();
        appIds.add(appId);

        AppMessage message = new AppMessage();
        message.setData(template);
        message.setAppIdList(appIds);
        message.setOffline(true);
        message.setOfflineExpireTime(1000 * 600);
        IPushResult ret = getGtPush().pushMessageToApp(message);
        System.out.println(ret.getResponse().toString());
    }

    /**
     * 终端与别名绑定
     *
     * @param alias
     * @param clientId
     */
    public void bindAlias(String alias, String clientId) {
        IAliasResult ret = getGtPush().bindAlias(appId, alias, clientId);

        System.out.println(ret.getErrorMsg());
    }

    /**
     * 单个用户推送消息[以终端ID]
     *
     * @param clientId
     * @param title
     * @param content
     */
    public void pushMessageToClientId(String clientId, String title, String content) {
        pushMessageToClientId(clientId, title, content, null);
    }

    /**
     * 单个用户推送消息[以终端ID]
     *
     * @param clientId
     * @param title
     * @param content
     */
    public void pushMessageToClientId(String clientId, String title, String content, String url) {
        LinkTemplate template = getLinkTemplate(title, content, url);

        SingleMessage message = new SingleMessage();
        message.setOffline(true);

        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(template);
        message.setPushNetWorkType(0);
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(clientId);
        IPushResult ret = null;
        try {
            ret = getGtPush().pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = getGtPush().pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            System.out.println(ret.getResponse().toString());
        } else {
            System.out.println("服务器响应异常");
        }
    }

    /**
     * 单个用户推送消息[以别名]
     *
     * @param alias
     * @param title
     * @param content
     */
    public void pushMessageToAlias(String alias, String title, String content) {
        pushMessageToAlias(alias, title, content, null);
    }

    /**
     * 单个用户推送消息[以别名]
     *
     * @param alias
     * @param title
     * @param content
     */
    public void pushMessageToAlias(String alias, String title, String content, String url) {
        LinkTemplate template = getLinkTemplate(title, content, url);

        SingleMessage message = new SingleMessage();
        message.setOffline(true);

        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(template);
        message.setPushNetWorkType(0);
        Target target = new Target();
        target.setAppId(appId);
        target.setAlias(alias);
        IPushResult ret = null;
        try {
            ret = getGtPush().pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = getGtPush().pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            System.out.println(ret.getResponse().toString());
        } else {
            System.out.println("服务器响应异常");
        }
    }

    private LinkTemplate getLinkTemplate(String title, String content, String url) {
        LinkTemplate template = new LinkTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTitle(title);
        template.setText(content);
        if (StringUtils.isNotBlank(url)) {
            template.setUrl(url);
        }
        return template;
    }

    private IGtPush getGtPush() {
        return new IGtPush(url, appKey, masterSecret);
    }

}
