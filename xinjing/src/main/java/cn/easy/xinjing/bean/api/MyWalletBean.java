package cn.easy.xinjing.bean.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by caosk on 17/4/16.
 */
@ApiModel(value = "我的钱包", description = "我的钱包模型")
public class MyWalletBean extends ApiBaseBean {
    /**
     * 账户余额
     */
    @ApiModelProperty(value = "账户余额")
    private BigDecimal accountBalance;
    /**
     * 待收余额
     */
    @ApiModelProperty(value = "待收余额")
    private BigDecimal dueInBalance;
    /**
     * 账单列表
     */
    @ApiModelProperty(value = "账单列表")
    private List<ReceivableBillsBean> bills = new ArrayList<ReceivableBillsBean>();

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public BigDecimal getDueInBalance() {
        return dueInBalance;
    }

    public void setDueInBalance(BigDecimal dueInBalance) {
        this.dueInBalance = dueInBalance;
    }

    public List<ReceivableBillsBean> getBills() {
        return bills;
    }

    public void setBills(List<ReceivableBillsBean> bills) {
        this.bills = bills;
    }

    public void addBill(int type, int state, BigDecimal amount, Date date){
        bills.add(new ReceivableBillsBean(type, state, amount, date));
    }

    @Override
    public String toString() {
        return "MyWalletBean{" +
                "accountBalance=" + accountBalance +
                ", dueInBalance=" + dueInBalance +
                ", bills=" + bills +
                '}';
    }

}
