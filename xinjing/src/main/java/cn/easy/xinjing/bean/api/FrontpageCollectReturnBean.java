package cn.easy.xinjing.bean.api;

import cn.easy.base.domain.core.AtEntity;

import java.sql.Timestamp;

/**
 * Created by raytine on 2017/6/30.
 */
public class FrontpageCollectReturnBean extends AtEntity {
    /**主题*/
    private String themes;
    /**摘要*/
    private String abstracts;
    /**作者*/
    private String author;
    /**发布时间*/
    private Timestamp releaseTime;
    /**发布人*/
    private String rublisher;
    /**内容*/
    private String description;
    /**浏览次数*/
    private Integer browseTimes;
    /**点赞次数*/
    private Integer thumbupTimes;
    /**缩略图*/
    private String coverPic;
    /**是否轮播*/
    private Integer carousel;
    /**链接*/
    private String linkurl;
    /**排序*/
    private Integer sort;
    /**备注*/
    private String remark;
    /**状态*/
    private Integer status;
    /**是否隐藏*/
    private Integer hidden = 0;
    /**是否收藏*/
    private Integer isCollect;

    public Integer getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(Integer isCollect) {
        this.isCollect = isCollect;
    }

    private String frontpageTypeId;

    public String getFrontpageTypeId() {
        return frontpageTypeId;
    }

    public void setFrontpageTypeId(String frontpageTypeId) {
        this.frontpageTypeId = frontpageTypeId;
    }

    public String getThemes() { return themes; }
    public void setThemes(String themes) { this.themes = themes; }
    public String getAbstracts() { return abstracts; }
    public void setAbstracts(String abstracts) { this.abstracts = abstracts; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Timestamp getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Timestamp releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getRublisher() { return rublisher; }
    public void setRublisher(String rublisher) { this.rublisher = rublisher; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getBrowseTimes() { return browseTimes; }
    public void setBrowseTimes(Integer browseTimes) { this.browseTimes = browseTimes; }
    public Integer getThumbupTimes() { return thumbupTimes; }
    public void setThumbupTimes(Integer thumbupTimes) { this.thumbupTimes = thumbupTimes; }
    public String getCoverPic() { return coverPic; }
    public void setCoverPic(String coverPic) { this.coverPic = coverPic; }
    public Integer getCarousel() { return carousel; }
    public void setCarousel(Integer carousel) { this.carousel = carousel; }
    public String getLinkurl() { return linkurl; }
    public void setLinkurl(String linkurl) { this.linkurl = linkurl; }
    public Integer getSort() { return sort; }
    public void setSort(Integer sort) { this.sort = sort; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getHidden() { return hidden; }

    public void setHidden(Integer hidden) { this.hidden = hidden; }
}
