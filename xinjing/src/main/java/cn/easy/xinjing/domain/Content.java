package cn.easy.xinjing.domain;

import cn.easy.base.core.repository.annotation.Hiddenable;
import cn.easy.base.domain.core.AtEntity;
import cn.easy.base.domain.core.IHiddenEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Hiddenable
@Table(name = "xj_content")
public class Content extends AtEntity implements IHiddenEntity {
    /**
     * 助记码
     */
    private String helpCode;
    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 是否免费
     */
    private Integer isFree;
    /**
     * 价格
     */
    private BigDecimal price;
    private String description;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 封面
     */
    private String coverPic;
    /**
     * 是否隐藏
     */
    private Integer hidden = 0;
    /**
     * 点击量
     */
    private Integer clicks;
    /**
     * 时长
     */
    private Integer duration;

    private Date videoupdateAt;

    public Date getVideoupdateAt() {
        return videoupdateAt;
    }

    public void setVideoupdateAt(Date videoupdateAt) {
        this.videoupdateAt = videoupdateAt;
    }

    private String otherdisease;

    public String getOtherdisease() {
        return otherdisease;
    }

    public void setOtherdisease(String otherdisease) {
        this.otherdisease = otherdisease;
    }

    public String getHelpCode() {
        return helpCode;
    }

    public void setHelpCode(String helpCode) {
        this.helpCode = helpCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsFree() {
        return isFree;
    }

    public void setIsFree(Integer isFree) {
        this.isFree = isFree;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    @Override
    public Integer getHidden() {
        return hidden;
    }

    @Override
    public void setHidden(Integer hidden) {
        this.hidden = hidden;
    }

    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
