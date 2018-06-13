package cn.easy.xinjing.service;

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
import cn.easy.xinjing.domain.DoctorArticle;
import cn.easy.xinjing.repository.DoctorArticleDao;

@Component
public class DoctorArticleService extends BaseService<DoctorArticle> {
    @Autowired
    private DoctorArticleDao	doctorArticleDao;

    public Page<DoctorArticle> search(Map<String, Object> searchParams, PageBean pageBean) {
        return doctorArticleDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        doctorArticleDao.delete(id);
    }

    public DoctorArticle getOne(String id) {
        return doctorArticleDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public DoctorArticle save(DoctorArticle doctorArticle) {
        return doctorArticleDao.save(doctorArticle);
    }

}


