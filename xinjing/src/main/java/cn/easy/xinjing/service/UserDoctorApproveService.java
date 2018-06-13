package cn.easy.xinjing.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.easy.base.config.utils.CustomUser;
import cn.easy.base.domain.User;
import cn.easy.base.service.BaseService;
import cn.easy.base.service.UserService;
import cn.easy.base.utils.BaseUtils;
import cn.easy.base.utils.DateTimeUtil;
import cn.easy.xinjing.domain.ApproveLog;
import cn.easy.xinjing.domain.UserDoctor;
import cn.easy.xinjing.utils.Constants;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.easy.base.bean.PageBean;
import cn.easy.xinjing.domain.UserDoctorApprove;
import cn.easy.xinjing.repository.UserDoctorApproveDao;

@Component
public class UserDoctorApproveService extends BaseService<UserDoctorApprove> {
    @Autowired
    private UserDoctorApproveDao userDoctorApproveDao;
    @Autowired
    private UserDoctorService userDoctorService;
    @Autowired
    private ApproveLogService approveLogService;
    @Autowired
    private UserService userService;

    public Page<UserDoctorApprove> search(Map<String, Object> searchParams, PageBean pageBean) {
        return userDoctorApproveDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<UserDoctorApprove> search(Map<String, Object> searchParams) {
        return userDoctorApproveDao.findAll(spec(searchParams), new Sort(Direction.DESC, "createdAt"));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        userDoctorApproveDao.delete(id);
    }

    public UserDoctorApprove getOne(String id) {
        return userDoctorApproveDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserDoctorApprove save(UserDoctorApprove userDoctorApprove) {
        return userDoctorApproveDao.save(userDoctorApprove);
    }

    public void approve(String id, int result, String remark, CustomUser customUser) {
        UserDoctorApprove userDoctorApprove = getOne(id);
        UserDoctor userDoctor = userDoctorService.getByUserId(userDoctorApprove.getUserId());
        User user = userService.findOne(userDoctorApprove.getUserId());

        if (result == Constants.USER_DOCTOR_APPROVE_STATUS_PASS) { //审核通过
            user.setRealname(userDoctorApprove.getRealname());
            user.setGender(userDoctorApprove.getGender());
            userService.save(user);

            userDoctor.setRegion(userDoctorApprove.getRegion());
            userDoctor.setProfessionalTitleId(userDoctorApprove.getProfessionalTitleId());
            userDoctor.setWorkplaceType(userDoctorApprove.getWorkplaceType());
            userDoctor.setHospital(userDoctorApprove.getHospital());
            userDoctor.setPosition(userDoctorApprove.getPosition());
            userDoctor.setDepartment(userDoctorApprove.getDepartment());
            userDoctor.setHeadPictureUrl(userDoctorApprove.getHeadPictureUrl());
            userDoctor.setIntroduction(userDoctorApprove.getIntroduction());
            userDoctor.setExpertise(userDoctorApprove.getExpertise());
            userDoctor.setPsychologicalConsultantImageUrl(userDoctorApprove.getPsychologicalConsultantImageUrl());
            userDoctor.setEmployeeImageUrl(userDoctorApprove.getEmployeeImageUrl());
            userDoctor.setDoctorProfessionImageUrl(userDoctorApprove.getDoctorProfessionImageUrl());
            userDoctor.setProfessionalQualificationImageUrl(userDoctorApprove.getProfessionalQualificationImageUrl());
            userDoctor.setStatus(Constants.DOCTOR_STATUS_NORMAL);
            userDoctorService.save(userDoctor);
        }
        userDoctorApprove.setStatus(result);
        userDoctorApprove.setRemark(remark);
        userDoctorApprove.setApprover(customUser.getId());
        userDoctorApprove.setApproveAt(DateTimeUtil.toTimestamp(new Date()));
        save(userDoctorApprove);

        //记录操作日志
        ApproveLog approveLog = new ApproveLog();
        approveLog.setObjectId(userDoctor.getId());
        approveLog.setObjectType(UserDoctor.class.getName());
        approveLog.setResult(BaseUtils.getConfigMap(Constants.DOCTOR_STATUS_ENUM_KEY).get(String.valueOf(userDoctor.getStatus())));
        approveLog.setRemark(remark);
        approveLogService.save(approveLog);
    }

    public UserDoctorApprove findLastByUserId(String userId) {
        return userDoctorApproveDao.findLastByUserId(userId);
    }
}


