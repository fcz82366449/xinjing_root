package cn.easy.xinjing.service;

import java.util.List;
import java.util.Map;

import cn.easy.base.service.BaseService;
import cn.easy.xinjing.domain.DiseaseContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.easy.base.bean.PageBean;
import cn.easy.xinjing.domain.FrontpageFtype;
import cn.easy.xinjing.repository.FrontpageFtypeDao;

@Component
public class FrontpageFtypeService extends BaseService<FrontpageFtype> {
    @Autowired
    private FrontpageFtypeDao	frontpageFtypeDao;

    public Page<FrontpageFtype> search(Map<String, Object> searchParams, PageBean pageBean) {
        return frontpageFtypeDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        frontpageFtypeDao.delete(id);
    }

    public FrontpageFtype getOne(String id) {
        return frontpageFtypeDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public FrontpageFtype save(FrontpageFtype frontpageFtype) {
        return frontpageFtypeDao.save(frontpageFtype);
    }

    public List<FrontpageFtype> findByFrontpageId(String frontpageId) {
        return frontpageFtypeDao.findByFrontpageId(frontpageId);
    }
    public List<FrontpageFtype> findByFrontpageTypeIdIn(String[] frontpageTypeId) {
        return frontpageFtypeDao.findByFrontpageTypeIdIn(frontpageTypeId);
    }
    public List<FrontpageFtype> findByFrontpageTypeId(String frontpageTypeId) {
        return frontpageFtypeDao.findByFrontpageTypeId(frontpageTypeId);
    }


}


