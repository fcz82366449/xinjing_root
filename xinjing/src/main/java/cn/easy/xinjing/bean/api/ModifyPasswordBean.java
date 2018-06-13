package cn.easy.xinjing.bean.api;

/**
 * Created by chenzhongyi on 16/9/18.
 */
public class ModifyPasswordBean extends ApiBaseBean {
    private String username;
    private String oldPassword;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
