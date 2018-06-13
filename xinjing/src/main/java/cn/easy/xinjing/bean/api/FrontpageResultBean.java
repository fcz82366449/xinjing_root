package cn.easy.xinjing.bean.api;

import cn.easy.xinjing.domain.FrontpageType;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by raytine on 2017/5/22.
 */
public class FrontpageResultBean {

    private  String id;
    /**主题*/
    private String themes;
    /**摘要*/
    private String abstracts;
    /**作者*/
    private String author;
    /**发布时间*/
    private Timestamp releaseTime;

    private String coverPic;

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThemes() {
        return themes;
    }

    public void setThemes(String themes) {
        this.themes = themes;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Timestamp getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Timestamp releaseTime) {
        this.releaseTime = releaseTime;
    }




}
