package cn.easy.xinjing.service;

import cn.easy.base.bean.PageBean;
import cn.easy.base.service.BaseService;
import cn.easy.xinjing.domain.DiseaseTherapy;
import cn.easy.xinjing.repository.DiseaseTherapyDao;
import com.google.common.collect.Lists;
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
public class DiseaseTherapyService extends BaseService<DiseaseTherapy> {
    @Autowired
    private DiseaseTherapyDao	diseaseTherapyDao;

    public Page<DiseaseTherapy> search(Map<String, Object> searchParams, PageBean pageBean) {
        return diseaseTherapyDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    /**
     * 获得所有的病种+疗法关联关系，根据diseaseId排序
     * @return
     */
    public List<DiseaseTherapy> findAll() {
        return Lists.newArrayList(diseaseTherapyDao.findAll(new Sort(Direction.DESC, "diseaseId")));
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        diseaseTherapyDao.delete(id);
    }

    public DiseaseTherapy getOne(String id) {
        return diseaseTherapyDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public DiseaseTherapy save(DiseaseTherapy diseaseTherapy) {
        return diseaseTherapyDao.save(diseaseTherapy);
    }

    List<DiseaseTherapy> findByTherapyId(String therapyId) {
        return diseaseTherapyDao.findByTherapyId(therapyId);
    }

    List<DiseaseTherapy> findByDiseaseId(String diseaseId) {
        return diseaseTherapyDao.findByDiseaseId(diseaseId);
    }
}


