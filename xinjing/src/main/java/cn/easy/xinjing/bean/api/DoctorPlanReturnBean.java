package cn.easy.xinjing.bean.api;

import cn.easy.xinjing.domain.Content;

import java.util.List;

/**
 * Created by raytine on 2017/8/21.
 */
public class DoctorPlanReturnBean extends ApiBaseBean{

    private String id;
    private String contentIds;
    private String name;

    private List<ContentSearchResultBean> contents;

    public List<ContentSearchResultBean> getContents() {
        return contents;
    }

    public void setContents(List<ContentSearchResultBean> contents) {
        this.contents = contents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContentIds() {
        return contentIds;
    }

    public void setContentIds(String contentIds) {
        this.contentIds = contentIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
