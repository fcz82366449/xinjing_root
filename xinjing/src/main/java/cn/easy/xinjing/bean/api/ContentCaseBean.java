package cn.easy.xinjing.bean.api;

import cn.easy.xinjing.domain.Content;

import java.util.List;

/**
 * Created by chenzy on 2016/10/6.
 */
public class ContentCaseBean extends ApiBaseBean {
    private List<String> contentId;

    public List<String> getContentId() {
        return contentId;
    }

    public void setContentId(List<String> contentId) {
        this.contentId = contentId;
    }
}
