package cn.easy.xinjing.service;

import cn.easy.base.bean.PageBean;
import cn.easy.base.domain.User;
import cn.easy.base.service.AttachmentService;
import cn.easy.base.service.BaseService;
import cn.easy.base.service.UserService;
import cn.easy.base.utils.DateTimeUtil;
import cn.easy.xinjing.aop.ApiException;
import cn.easy.xinjing.bean.api.*;
import cn.easy.xinjing.domain.CapitalFlow;
import cn.easy.xinjing.domain.UserDoctor;
import cn.easy.xinjing.domain.UserDoctorApprove;
import cn.easy.xinjing.repository.UserDoctorDao;
import cn.easy.xinjing.utils.Constants;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Component
public class UserDoctorService extends BaseService<UserDoctor> {
    @Autowired
    private UserDoctorDao userDoctorDao;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDoctorApproveService userDoctorApproveService;
    @Autowired
    private CapitalFlowService capitalFlowService;

    public Page<UserDoctor> search(Map<String, Object> searchParams, PageBean pageBean) {
        return userDoctorDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }
    public List<UserDoctor> findAll(Map<String, Object> searchParams) {
        return userDoctorDao.findAll(spec(searchParams));
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        userDoctorDao.delete(id);
    }

    public UserDoctor getOne(String id) {
        return userDoctorDao.findOne(id);
    }

    public UserDoctor getByUserId(String userId) {
        return userDoctorDao.findByUserId(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserDoctor save(UserDoctor userDoctor) {
        return userDoctorDao.save(userDoctor);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void save(DoctorRegisterBean doctorRegisterBean) {
        User user = new User();
        UserDoctor userDoctor = new UserDoctor();
        try {
            PropertyUtils.copyProperties(user, doctorRegisterBean);
            PropertyUtils.copyProperties(userDoctor, doctorRegisterBean);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        user.setRealname(user.getUsername());
        user.setMobile(user.getUsername());
        user.setRegAt(DateTimeUtil.toTimestamp(new Date()));
        user = userService.save(user);

        userDoctor.setStatus(Constants.DOCTOR_STATUS_INIT);
        userDoctor.setUserId(user.getId());
        save(userDoctor);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void savexx(DoctorRegisterBean doctorRegisterBean,UserDoctor userDoctor,String userid) {
        User user = new User();
        try {
            PropertyUtils.copyProperties(user, doctorRegisterBean);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        user.setMobile(user.getUsername());
        user.setRegAt(DateTimeUtil.toTimestamp(new Date()));
        if(userid!=null){
            user.setId(userid);
        }
        user = userService.save(user);
        userDoctor.setStatus(Constants.DOCTOR_STATUS_INIT);
        userDoctor.setUserId(user.getId());
        UserDoctor userDoctorsave =  save(userDoctor);
        //增加医院审核记录
        if(userDoctorsave!=null){
            DoctorAuthBean doctorAuthBean = new DoctorAuthBean();

//            this.auth(doctorAuthBean,userid);
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updatexx(UserDoctor userDoctor1,UserDoctor userDoctor) {
//        User user = new User();
//        try {
//            PropertyUtils.copyProperties(userDoctor1, userDoctor);
////            PropertyUtils.copyProperties(userDoctor, doctorRegisterBean);
//        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//        user.setMobile(user.getUsername());
//        user.setRegAt(DateTimeUtil.toTimestamp(new Date()));
//        if(userid!=null){
//            user.setId(userid);
//        }
//        user = userService.save(user);

//        userDoctor.setStatus(Constants.DOCTOR_STATUS_INIT);
//        userDoctor.setUserId(user.getId());
        userDoctor1.setHospital(userDoctor.getHospital());
        save(userDoctor1);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void save(DoctorBean doctorBean, User curUser) {
        User user = userService.findOne(curUser.getId());
        UserDoctor userDoctor = userDoctorDao.findByUserId(user.getId());
        try {
            PropertyUtils.copyProperties(user, doctorBean);
            PropertyUtils.copyProperties(userDoctor, doctorBean);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        userService.save(user);
        save(userDoctor);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void auth(DoctorAuthBean doctorAuthBean, String currentUserId) {
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("EQ_userId", currentUserId);
        searchParams.put("EQ_status", Constants.USER_DOCTOR_APPROVE_STATUS_TO_CHECK);
        List<UserDoctorApprove> userDoctorApproveList = userDoctorApproveService.search(searchParams);
        if (!userDoctorApproveList.isEmpty()) {
            throw new ApiException(-1, "已存在待审核的认证信息，请勿重复提交");
        }

        UserDoctor userDoctor = getByUserId(currentUserId);
        if(userDoctor.getStatus() == Constants.DOCTOR_STATUS_INIT) {
            userDoctor.setStatus(Constants.DOCTOR_STATUS_TO_CHECK);
            save(userDoctor);
        }

        UserDoctorApprove userDoctorApprove = new UserDoctorApprove();
        userDoctorApprove.setUserId(currentUserId);
        try {
            PropertyUtils.copyProperties(userDoctorApprove, doctorAuthBean);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        userDoctorApprove.setStatus(Constants.USER_DOCTOR_APPROVE_STATUS_TO_CHECK);
        userDoctorApproveService.save(userDoctorApprove);
    }

    public List<UserDoctor> findByUserId(List<String> doctorIdList) {
        return userDoctorDao.findByUserIdIn(doctorIdList);
    }

    public MyWalletBean getMyWalletInfo(String userId, PageBean pageBean){
        Page<CapitalFlow> capitalFlowList = getCapitalFlows(userId, pageBean);

        MyWalletBean myWallet = new MyWalletBean();
        if(capitalFlowList != null && CollectionUtils.isNotEmpty(capitalFlowList.getContent())) {
            for (CapitalFlow flow : capitalFlowList.getContent()) {
                myWallet.addBill(flow.getType(), flow.getStatus(), flow.getAmount(), flow.getHappenedTime());
            }
        }
        UserDoctor userDoctor = this.getByUserId(userId);
        myWallet.setAccountBalance(userDoctor.getBalance());
        myWallet.setDueInBalance(userDoctor.getReceivableAmount());
        return myWallet;
    }

    public List<ReceivableBillsBean> getReceivableBills(String userId, PageBean pageBean){
        Page<CapitalFlow> capitalFlowList = getCapitalFlows(userId, pageBean);
        List<ReceivableBillsBean> list = new ArrayList<>();
        if(capitalFlowList != null && CollectionUtils.isNotEmpty(capitalFlowList.getContent())) {
            for (CapitalFlow flow : capitalFlowList.getContent()) {
                list.add(new ReceivableBillsBean(flow.getType(), flow.getStatus(), flow.getAmount(), flow.getHappenedTime()));
            }
        }
        return list;
    }

    private Page<CapitalFlow> getCapitalFlows(String userId, PageBean pageBean) {
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("EQ_userId", userId);
        searchParams.put("EQ_type", Constants.CAPITAL_FLOW_TYPE_INCOME);
        searchParams.put("EQ_status", Constants.CAPITAL_FLOW_STATUS_PROCESSING);
        return capitalFlowService.search(searchParams, pageBean);
    }
}


