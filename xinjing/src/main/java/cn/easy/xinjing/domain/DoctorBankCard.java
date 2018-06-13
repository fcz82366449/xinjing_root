package cn.easy.xinjing.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.easy.base.domain.core.AtEntity;
import cn.easy.base.domain.core.IHiddenEntity;
import cn.easy.base.core.repository.annotation.Hiddenable;

@Entity
@Hiddenable
@Table(name = "xj_doctor_bank_card")
public class DoctorBankCard extends AtEntity implements IHiddenEntity {

	/**银行档案主键*/
	private String bankId;

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}


	/**银行名称*/
	private String bankName;
	/**开户行*/
	private String depositBank;
	/**持卡人姓名*/
	private String cardholder;
	/**卡号*/
	private String cardNumber;
	/**医生Id*/
	private String doctorId;
	/**是否隐藏*/
	private Integer hidden = 0;
	public String getBankName() { return bankName; }
	public void setBankName(String bankName) { this.bankName = bankName; }
	public String getDepositBank() { return depositBank; }
	public void setDepositBank(String depositBank) { this.depositBank = depositBank; }
	public String getCardholder() { return cardholder; }
	public void setCardholder(String cardholder) { this.cardholder = cardholder; }
	public String getCardNumber() { return cardNumber; }
	public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
	public String getDoctorId() { return doctorId; }
	public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
	@Override
public Integer getHidden() { return hidden; }
	@Override
public void setHidden(Integer hidden) { this.hidden = hidden; }

}
