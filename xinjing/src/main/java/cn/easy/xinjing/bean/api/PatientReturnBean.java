package cn.easy.xinjing.bean.api;

import cn.easy.base.domain.User;
import cn.easy.xinjing.domain.UserPatient;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by chenzhongyi on 16/9/18.
 */
public class PatientReturnBean  {
    private String userId;
    private String realname;
    private String username;
    private String regionName;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}
