package cn.easy.xinjing.service;

import java.util.Map;
import java.util.List;

import cn.easy.base.service.BaseService;
import cn.easy.base.utils.ExtractUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.easy.base.bean.PageBean;
import cn.easy.xinjing.domain.DoctorPlanContent;
import cn.easy.xinjing.repository.DoctorPlanContentDao;

@Component
public class DoctorPlanContentService extends BaseService<DoctorPlanContent> {
    @Autowired
    private DoctorPlanContentDao	doctorPlanContentDao;

    public Page<DoctorPlanContent> search(Map<String, Object> searchParams, PageBean pageBean) {
        return doctorPlanContentDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<DoctorPlanContent> search(Map<String, Object> searchParams, Sort... sort) {
        return doctorPlanContentDao.findAll(spec(searchParams), sort == null || sort.length == 0  ? new Sort(Direction.DESC, "createdAt") : sort[0]);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        doctorPlanContentDao.delete(id);
    }

    public DoctorPlanContent getOne(String id) {
        return doctorPlanContentDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public DoctorPlanContent save(DoctorPlanContent doctorPlanContent) {
        return doctorPlanContentDao.save(doctorPlanContent);
    }


    public List<DoctorPlanContent> findByDoctorPlanId(String doctorPlanId) {
        return  doctorPlanContentDao.findByDoctorPlanId(doctorPlanId);
    }


}


