package cn.easy.xinjing.bean.api;

import cn.easy.xinjing.domain.DoctorBankCard;

/**
 * Created by raytine on 2017/6/1.
 */
public class DoctorBankReturnBean {

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

    public Integer getHidden() { return hidden; }

    public void setHidden(Integer hidden) { this.hidden = hidden; }
}
