package cn.easy.xinjing.service;

import cn.easy.base.bean.PageBean;
import cn.easy.base.service.BaseService;
import cn.easy.xinjing.domain.SectionOfficeDisease;
import cn.easy.xinjing.repository.SectionOfficeDiseaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public class SectionOfficeDiseaseService extends BaseService<SectionOfficeDisease> {
    @Autowired
    private SectionOfficeDiseaseDao	sectionOfficeDiseaseDao;

    public Page<SectionOfficeDisease> search(Map<String, Object> searchParams, PageBean pageBean) {
        return sectionOfficeDiseaseDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<SectionOfficeDisease> search(Map<String, Object> searchParams) {
        return sectionOfficeDiseaseDao.findAll(spec(searchParams), new Sort(Direction.DESC, "createdAt"));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        sectionOfficeDiseaseDao.delete(id);
    }

    public SectionOfficeDisease getOne(String id) {
        return sectionOfficeDiseaseDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public SectionOfficeDisease save(SectionOfficeDisease sectionOfficeDisease) {
        return sectionOfficeDiseaseDao.save(sectionOfficeDisease);
    }

    public List<SectionOfficeDisease> findBySectionOfficeId(String sectionOfficeId){
         return sectionOfficeDiseaseDao.findBySectionOfficeId(sectionOfficeId);
    }

    public List<SectionOfficeDisease> findByDiseaseId(String diseaseId){
         return sectionOfficeDiseaseDao.findByDiseaseId(diseaseId);
    }
}


