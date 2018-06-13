package cn.easy.xinjing.service;

import java.util.Map;
import java.util.List;

import cn.easy.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.easy.base.bean.PageBean;
import cn.easy.xinjing.domain.ContentHospital;
import cn.easy.xinjing.repository.ContentHospitalDao;

@Component
public class ContentHospitalService extends BaseService<ContentHospital> {
    @Autowired
    private ContentHospitalDao	contentHospitalDao;

    public Page<ContentHospital> search(Map<String, Object> searchParams, PageBean pageBean) {
        return contentHospitalDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<ContentHospital> search(Map<String, Object> searchParams, Sort... sort) {
        return contentHospitalDao.findAll(spec(searchParams), sort == null || sort.length == 0  ? new Sort(Direction.DESC, "createdAt") : sort[0]);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        contentHospitalDao.delete(id);
    }

    public ContentHospital getOne(String id) {
        return contentHospitalDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ContentHospital save(ContentHospital contentHospital) {
        return contentHospitalDao.save(contentHospital);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<ContentHospital> findByContentDpId(String contentId) {
        return contentHospitalDao.findByContentDpId(contentId);
    }

}


