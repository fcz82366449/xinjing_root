package cn.easy.xinjing.service;

import java.util.List;
import java.util.Map;

import cn.easy.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.easy.base.bean.PageBean;
import cn.easy.xinjing.domain.Hospital;
import cn.easy.xinjing.repository.HospitalDao;

@Component
public class HospitalService extends BaseService<Hospital> {
    @Autowired
    private HospitalDao	hospitalDao;

    public Page<Hospital> search(Map<String, Object> searchParams, PageBean pageBean) {
        return hospitalDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        hospitalDao.delete(id);
    }

    public Hospital getOne(String id) {
        return hospitalDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Hospital save(Hospital hospital) {
        return hospitalDao.save(hospital);
    }

    public List<Hospital> findByCarousel(String carousel) {
        return hospitalDao.findByCarousel(carousel);
    }

    public List<Hospital> findByIsdisplay(int isdisplay) {
        return hospitalDao.findByIsdisplay(isdisplay);
    }
}


