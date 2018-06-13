package cn.easy.xinjing.bean.api;

/**
 * 用于“获取单个内容接口的返回：ext”
 * Created by fanchengzhi on 2017/5/6.
 */
public class ContentExtBean {
    /**内容表ID*/
    private String id;
    /**内容*/
    private String content;

    /**视频类型字节kb**/
    private Long videosize;

    private int videopassword;

    public int getVideopassword() {
        return videopassword;
    }

    public void setVideopassword(int videopassword) {
        this.videopassword = videopassword;
    }

    public Long getVideosize() {
        return videosize;
    }

    public void setVideosize(Long videosize) {
        this.videosize = videosize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
