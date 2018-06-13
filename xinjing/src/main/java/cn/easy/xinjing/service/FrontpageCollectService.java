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
import cn.easy.xinjing.domain.FrontpageCollect;
import cn.easy.xinjing.repository.FrontpageCollectDao;

@Component
public class FrontpageCollectService extends BaseService<FrontpageCollect> {
    @Autowired
    private FrontpageCollectDao	frontpageCollectDao;

    public Page<FrontpageCollect> search(Map<String, Object> searchParams, PageBean pageBean) {
        return frontpageCollectDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<FrontpageCollect> search(Map<String, Object> searchParams, Sort... sort) {
        return frontpageCollectDao.findAll(spec(searchParams), sort == null || sort.length == 0  ? new Sort(Direction.DESC, "createdAt") : sort[0]);
    }

    public FrontpageCollect findOne(Map<String, Object> searchParams) {
        return frontpageCollectDao.findOne(spec(searchParams));
    }
    public List<FrontpageCollect> findAll(Map<String, Object> searchParams) {
        return frontpageCollectDao.findAll(spec(searchParams));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        frontpageCollectDao.delete(id);
    }

    public FrontpageCollect getOne(String id) {
        return frontpageCollectDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public FrontpageCollect save(FrontpageCollect frontpageCollect) {
        return frontpageCollectDao.save(frontpageCollect);
    }

}


