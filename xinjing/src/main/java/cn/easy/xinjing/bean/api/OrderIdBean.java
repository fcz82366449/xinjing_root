package cn.easy.xinjing.bean.api;

import cn.easy.base.bean.common.ApiResultBean;

/**
 * Created by caosk on 16/12/22.
 */
public class OrderIdBean extends ApiResultBean {
    public String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
