package cn.easy.xinjing.bean.api;

/**
 * Created by raytine on 2017/6/30.
 */
public class FrontpageCollectBean extends ApiBaseBean {
    /**行业新闻头条ID*/
    private String newsId;

    /**是否收藏*/
    private Integer isCollect;

    public String getNewsId() { return newsId; }
    public void setNewsId(String newsId) { this.newsId = newsId; }

    public Integer getIsCollect() { return isCollect; }
    public void setIsCollect(Integer isCollect) { this.isCollect = isCollect; }

}
