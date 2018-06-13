package cn.easy.xinjing.bean.api;

/**
 * Created by chenzy on 2017-2-19.
 */
public class ContentSyncBean extends ApiBasePageBean {
    private String lastSyncAt;

    public String getLastSyncAt() {
        return lastSyncAt;
    }

    public void setLastSyncAt(String lastSyncAt) {
        this.lastSyncAt = lastSyncAt;
    }
}
