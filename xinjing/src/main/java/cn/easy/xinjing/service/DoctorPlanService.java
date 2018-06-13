package cn.easy.xinjing.service;

import java.util.Map;
import java.util.List;

import cn.easy.base.service.BaseService;
import cn.easy.xinjing.domain.DoctorPlanContent;
import cn.easy.xinjing.domain.UserDoctor;
import cn.easy.xinjing.repository.DoctorPlanContentDao;
import cn.easy.xinjing.repository.UserDoctorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.easy.base.bean.PageBean;
import cn.easy.xinjing.domain.DoctorPlan;
import cn.easy.xinjing.repository.DoctorPlanDao;

@Component
public class DoctorPlanService extends BaseService<DoctorPlan> {
    @Autowired
    private DoctorPlanDao	doctorPlanDao;
    @Autowired
    private DoctorPlanContentDao doctorPlanContentDao;
    @Autowired
    private UserDoctorDao userDoctorDao;
    public Page<DoctorPlan> search(Map<String, Object> searchParams, PageBean pageBean) {
        return doctorPlanDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<DoctorPlan> search(Map<String, Object> searchParams, Sort... sort) {
        return doctorPlanDao.findAll(spec(searchParams), sort == null || sort.length == 0  ? new Sort(Direction.DESC, "createdAt") : sort[0]);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        doctorPlanDao.delete(id);
    }

    public DoctorPlan getOne(String id) {
        return doctorPlanDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public DoctorPlan save(DoctorPlan doctorPlan) {
        return doctorPlanDao.save(doctorPlan);
    }

    /**
     * 保存方案及内容关联表
     * @param doctorPlan
     * @param contentId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public DoctorPlan saveAndContentPlan(DoctorPlan doctorPlan,String contentId) {
        UserDoctor userDoctor = userDoctorDao.findByUserId(doctorPlan.getDoctorId());
        if(userDoctor==null){
             new Exception("查找不倒该医生!");
             return null;
        }
        doctorPlan.setHospitalId(userDoctor.getHospital());
        doctorPlan = doctorPlanDao.save(doctorPlan);
        if(doctorPlan!=null){
            List<DoctorPlanContent> doctorPlanContentList = doctorPlanContentDao.findByDoctorPlanId(doctorPlan.getId());
            for (DoctorPlanContent doctorPlanContent : doctorPlanContentList) {
                doctorPlanContentDao.delete(doctorPlanContent.getId());
            }
            //更新方案与内容的关系数据
            String[] contentIds = contentId.split(",");
            for(String id : contentIds){
                DoctorPlanContent  doctorPlanContent = new DoctorPlanContent();
                doctorPlanContent.setContentId(id);
                doctorPlanContent.setDoctorPlanId(doctorPlan.getId());
                doctorPlanContentDao.save(doctorPlanContent);
            }

        }



        return doctorPlan;
    }

    /**
     * 删除方案及内容关联表
     * @param doctorPlan
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public DoctorPlan deleteAndContentPlan(DoctorPlan doctorPlan) {
        UserDoctor userDoctor = userDoctorDao.findByUserId(doctorPlan.getDoctorId());
        if(userDoctor==null){
            new Exception("查找不倒该医生!");
            return null;
        }
        doctorPlanDao.delete(doctorPlan.getId());
        List<DoctorPlanContent> doctorPlanContentList = doctorPlanContentDao.findByDoctorPlanId(doctorPlan.getId());
        for (DoctorPlanContent doctorPlanContent : doctorPlanContentList) {
            doctorPlanContentDao.delete(doctorPlanContent.getId());
        }

        return doctorPlan;
    }





}


