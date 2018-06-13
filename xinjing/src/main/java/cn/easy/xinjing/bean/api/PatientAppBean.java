package cn.easy.xinjing.bean.api;

import cn.easy.base.domain.User;
import cn.easy.xinjing.domain.UserPatient;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by chenzhongyi on 16/9/18.
 */
public class PatientAppBean extends ApiBaseBean {
    private String patientId;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
