package cn.easy.xinjing.bean.api;

/**
 * Created by raytine on 2017/8/5.
 */
public class VideoEncryptionBean extends ApiBaseBean{

    /**内容id*/
    private String contentId;
    /**字节数*/
    private String bytes;
    private Integer jmvalues;

    public Integer getJmvalues() {
        return jmvalues;
    }

    public void setJmvalues(Integer jmvalues) {
        this.jmvalues = jmvalues;
    }
    public String getContentId() { return contentId; }
    public void setContentId(String contentId) { this.contentId = contentId; }
    public String getBytes() { return bytes; }
    public void setBytes(String bytes) { this.bytes = bytes; }
}
