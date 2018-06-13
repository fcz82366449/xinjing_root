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
import cn.easy.xinjing.domain.PrescriptionContent;
import cn.easy.xinjing.repository.PrescriptionContentDao;

@Component
public class PrescriptionContentService extends BaseService<PrescriptionContent> {
    @Autowired
    private PrescriptionContentDao prescriptionContentDao;

    public Page<PrescriptionContent> search(Map<String, Object> searchParams, PageBean pageBean) {
        return prescriptionContentDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        prescriptionContentDao.delete(id);
    }

    public PrescriptionContent getOne(String id) {
        return prescriptionContentDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public PrescriptionContent save(PrescriptionContent prescriptionContent) {
        return prescriptionContentDao.save(prescriptionContent);
    }

    public void save(List<PrescriptionContent> prescriptionContentList) {
        prescriptionContentDao.save(prescriptionContentList);
    }

    public List<PrescriptionContent> findByPrescriptionId(String prescriptionId) {
        return prescriptionContentDao.findByPrescriptionId(prescriptionId);
    }
}


