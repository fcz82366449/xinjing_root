package cn.easy.xinjing.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.easy.base.domain.core.AtEntity;

@Entity
@Table(name = "xj_capital_flow")
public class CapitalFlow extends AtEntity {
	/**用户ID*/
	private String userId;
	/**心景订单ID*/
	private String orderId;
	/**
	 * 费用类型
	 * @see cn.easy.xinjing.utils.Constants
	 */
	private Integer feeType;
	/**
	 * 收支类型
	 * @see cn.easy.xinjing.utils.Constants
	 */
	private Integer type;
	/**状态*/
	private Integer status;
	/**金额*/
	private BigDecimal amount;
	/**账户余额*/
	private BigDecimal balance;
	/**账单生成时间*/
	private Date happenedTime;
	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getFeeType() {
		return feeType;
	}

	public void setFeeType(Integer feeType) {
		this.feeType = feeType;
	}

	public Integer getType() { return type; }
	public void setType(Integer type) { this.type = type; }
	public Integer getStatus() { return status; }
	public void setStatus(Integer status) { this.status = status; }
	public BigDecimal getAmount() { return amount; }
	public void setAmount(BigDecimal amount) { this.amount = amount; }
	public BigDecimal getBalance() { return balance; }
	public void setBalance(BigDecimal balance) { this.balance = balance; }
	public Date getHappenedTime() { return happenedTime; }
	public void setHappenedTime(Date happenedTime) { this.happenedTime = happenedTime; }

}
