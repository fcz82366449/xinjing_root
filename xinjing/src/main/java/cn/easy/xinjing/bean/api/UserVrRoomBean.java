package cn.easy.xinjing.bean.api;

/**
 * Created by caosk on 2017/2/22.
 */
public class UserVrRoomBean extends ApiBaseBean {
    private String userId;
    private String username;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
