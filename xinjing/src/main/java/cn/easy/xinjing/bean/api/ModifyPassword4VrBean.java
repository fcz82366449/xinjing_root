package cn.easy.xinjing.bean.api;

/**
 * Created by chenzhongyi on 16/9/18.
 */
public class ModifyPassword4VrBean extends ApiBaseBean{
    private String oldPassword;
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
