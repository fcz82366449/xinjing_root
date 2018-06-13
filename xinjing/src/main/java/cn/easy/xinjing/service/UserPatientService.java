package cn.easy.xinjing.service;

import cn.easy.base.bean.PageBean;
import cn.easy.base.domain.User;
import cn.easy.base.service.BaseService;
import cn.easy.base.service.UserService;
import cn.easy.xinjing.bean.api.PatientBean;
import cn.easy.xinjing.bean.api.PatientRegisterBean;
import cn.easy.xinjing.bean.api.PatientReturnBean;
import cn.easy.xinjing.bean.api.PationtLoginBean;
import cn.easy.xinjing.domain.UserPatient;
import cn.easy.xinjing.repository.UserPatientDao;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Component
public class UserPatientService extends BaseService<UserPatient> {
    @Autowired
    private UserPatientDao userPatientDao;
    @Autowired
    private UserService userService;

    public Page<UserPatient> search(Map<String, Object> searchParams, PageBean pageBean) {
        return userPatientDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<UserPatient> search(Map<String, Object> searchParams) {
        return userPatientDao.findAll(spec(searchParams), new Sort(Direction.DESC, "createdAt"));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        userPatientDao.delete(id);
    }

    public UserPatient getOne(String id) {
        return userPatientDao.findOne(id);
    }

    public UserPatient getByUserId(String userId) {
        return userPatientDao.findByUserId(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserPatient save(UserPatient userPatient) {
        return userPatientDao.save(userPatient);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(PatientRegisterBean patientRegisterBean) {
        User user = new User();
        UserPatient userPatient = new UserPatient();
        try {
            PropertyUtils.copyProperties(user, patientRegisterBean);
            PropertyUtils.copyProperties(userPatient, patientRegisterBean);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        user.setRealname(user.getUsername());
        user.setMobile(user.getUsername());
        user = userService.save(user);

        userPatient.setUserId(user.getId());
        save(userPatient);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveto(PationtLoginBean pationtLoginBean) {
        User user = new User();
        UserPatient userPatient = new UserPatient();
        try {
            PropertyUtils.copyProperties(user, pationtLoginBean);
            PropertyUtils.copyProperties(userPatient, pationtLoginBean);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        user.setRealname(user.getUsername());
        user.setMobile(user.getUsername());
        user = userService.save(user);

        userPatient.setUserId(user.getId());
        save(userPatient);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(PatientBean patientBean, User curUser) {
        User user = userService.findOne(curUser.getId());
        UserPatient userPatient = getByUserId(user.getId());
        try {
            PropertyUtils.copyProperties(user, patientBean);
            PropertyUtils.copyProperties(userPatient, patientBean);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        userService.save(user);
        save(userPatient);
    }

    public PatientBean getPatientBean(String userId) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        User user = userService.findOne(userId);
        if (user == null) {
            return null;
        }
        UserPatient userPatient = getByUserId(user.getId());
        if(userPatient == null) {
            return null;
        }

        return PatientBean.valueOf(user, userPatient);
    }


    public PatientReturnBean getPatientReturnBean(String userId) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException{
        User user = userService.findOne(userId);
        if (user == null) {
            return null;
        }
        UserPatient userPatient = getByUserId(user.getId());
        if(userPatient == null) {
            return null;
        }
        //构造返回的json对象
        PatientReturnBean patientReturnBean = new PatientReturnBean();
        patientReturnBean.setUserId(user.getId());
        patientReturnBean.setRealname(user.getRealname());
        patientReturnBean.setUsername(user.getUsername());
        patientReturnBean.setRegionName(userPatient.getAddress());


        return patientReturnBean;
    }


}


