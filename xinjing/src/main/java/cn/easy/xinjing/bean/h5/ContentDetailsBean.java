package cn.easy.xinjing.bean.h5;

import cn.easy.base.domain.core.AtEntity;
import cn.easy.xinjing.domain.Disease;
import cn.easy.xinjing.domain.Therapy;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by raytine on 2017/5/31.
 */
public class ContentDetailsBean extends AtEntity {

    private List<Therapy> therapyList;

    private List<Disease> diseaseList;

    public List<Disease> getDiseaseList() {
        return diseaseList;
    }

    public void setDiseaseList(List<Disease> diseaseList) {
        this.diseaseList = diseaseList;
    }

    public List<Therapy> getTherapyList() {
        return therapyList;
    }

    public void setTherapyList(List<Therapy> therapyList) {
        this.therapyList = therapyList;
    }

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
     * 点击量
     */
    private Integer clicks;
    /**
     * 时长
     */
    private Integer duration;

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
