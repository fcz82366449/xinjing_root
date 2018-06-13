package cn.easy.xinjing.bean.api;

import java.math.BigDecimal;

/**
 * Created by raytine on 2017/6/29.
 */
public class ChargesBean extends ApiBaseBean {

    /**患者id*/
    private String patientId;
    /**咨询金额*/
    private BigDecimal fees;
    /**备注*/
    private String remarks;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
