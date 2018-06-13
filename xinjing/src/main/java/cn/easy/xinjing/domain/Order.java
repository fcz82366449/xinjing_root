package cn.easy.xinjing.domain;

import cn.easy.base.core.repository.annotation.Hiddenable;
import cn.easy.base.domain.core.AtEntity;
import cn.easy.base.domain.core.IHiddenEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Hiddenable
@Table(name = "xj_order")
public class Order extends AtEntity implements IHiddenEntity {
    /**
     * 关联ID
     */
    private String objectId;
    /**
     * 关联类型
     */
    private String objectType;
    /**
     * 支付方式，1：微信支付，2：支付宝
     */
    private int payWay;
    /**
     * 商户订单号
     */
    private String outTradeNo;
    /**
     * 支付日期
     */
    private Date paidAt;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 发生金额
     */
    private BigDecimal amount;

    private String fromUserId;
    private String toUserId;

    /**
     * 是否隐藏
     */
    private Integer hidden = 0;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public int getPayWay() {
        return payWay;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Date getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Date paidAt) {
        this.paidAt = paidAt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public Integer getHidden() {
        return hidden;
    }

    @Override
    public void setHidden(Integer hidden) {
        this.hidden = hidden;
    }

}
