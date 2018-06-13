package cn.easy.xinjing.service;

import java.util.List;
import java.util.Map;

import cn.easy.base.service.BaseService;
import cn.easy.xinjing.domain.TherapyContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.easy.base.bean.PageBean;
import cn.easy.xinjing.domain.DiseaseContent;
import cn.easy.xinjing.repository.DiseaseContentDao;

@Component
public class DiseaseContentService extends BaseService<DiseaseContent> {
    @Autowired
    private DiseaseContentDao	diseaseContentDao;

    public Page<DiseaseContent> search(Map<String, Object> searchParams, PageBean pageBean) {
        return diseaseContentDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        diseaseContentDao.delete(id);
    }

    public DiseaseContent getOne(String id) {
        return diseaseContentDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public DiseaseContent save(DiseaseContent diseaseContent) {
        return diseaseContentDao.save(diseaseContent);
    }

    public List<DiseaseContent> findByContentId(String contentId) {
        return diseaseContentDao.findByContentId(contentId);
    }

    public List<DiseaseContent> findByDiseaseId(String diseaseId) {
        return diseaseContentDao.findByDiseaseId(diseaseId);
    }

}


