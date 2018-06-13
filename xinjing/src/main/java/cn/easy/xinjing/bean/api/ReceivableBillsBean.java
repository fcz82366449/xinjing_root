package cn.easy.xinjing.bean.api;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by caosk on 17/4/16.
 */
public class ReceivableBillsBean extends ApiBaseBean {
    /**
     * 账单类型（1 充值 2 提现 3 咨询费 4 处方费 5 抽成6 其他)
     */
    @ApiModelProperty(value = "账单类型（1 充值 2 提现 3 咨询费 4 处方费 5 抽成 6 其他)")
    private int type;
    /**
     * 账单状态（1 处理中 2 成功 3 失败）
     */
    @ApiModelProperty(value = "账单状态（1 处理中 2 成功 3 失败）")
    private int state;
    /**
     * 账单金额
     */
    @ApiModelProperty(value = "账单金额")
    private BigDecimal amount;
    /**
     * 账单生成时间
     */
    @ApiModelProperty(value = "账单生成时间")
    private Date date;

    public ReceivableBillsBean(){}

    public ReceivableBillsBean(int type, int state, BigDecimal amount, Date date) {
        this.type = type;
        this.state = state;
        this.amount = amount;
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ReceivableBillsBean{" +
                "type=" + type +
                ", state=" + state +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
