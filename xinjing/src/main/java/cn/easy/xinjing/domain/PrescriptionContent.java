package cn.easy.xinjing.domain;

import cn.easy.base.domain.core.AtEntity;
import cn.easy.xinjing.utils.Constants;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "xj_prescription_content")
public class PrescriptionContent extends AtEntity {
    /**
     * 电子处方Id
     */
    private String prescriptionId;
    /**
     * 内容Id
     */
    private String contentId;
    /**
     * 价格
     */
    private BigDecimal price = BigDecimal.ZERO;
    /**
     * 单价
     */
    private BigDecimal unitPrice = BigDecimal.ZERO;
    /**
     * 次数
     */
    private Integer times = 0;
    /**
     * 频率
     */
    private Integer frequency;
    /**
     * 周期单位
     */
    private Integer periodUnit;
    /**
     * 周期
     */
    private Integer period;
    /**
     * 状态
     */
    private Integer status = Constants.PRESCRIPTION_CONTENT_STATUS_INIT;
    /**
     * 使用次数
     */
    private Integer useTimes = 0;
    /**
     * 最后一次使用时间
     */
    private Date lastUseAt;


    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public BigDecimal getPrice() {
        if (price == null) {
            price = BigDecimal.ZERO;
        }
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getUnitPrice() {
        if (unitPrice == null) {
            unitPrice = BigDecimal.ZERO;
        }

        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getPeriodUnit() {
        return periodUnit;
    }

    public void setPeriodUnit(Integer periodUnit) {
        this.periodUnit = periodUnit;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(Integer useTimes) {
        this.useTimes = useTimes;
    }

    public Date getLastUseAt() {
        return lastUseAt;
    }

    public void setLastUseAt(Date lastUseAt) {
        this.lastUseAt = lastUseAt;
    }
}
