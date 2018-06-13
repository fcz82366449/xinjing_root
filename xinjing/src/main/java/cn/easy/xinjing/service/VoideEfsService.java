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
import cn.easy.xinjing.domain.VoideEfs;
import cn.easy.xinjing.repository.VoideEfsDao;

@Component
public class VoideEfsService extends BaseService<VoideEfs> {
    @Autowired
    private VoideEfsDao	voideEfsDao;

    public Page<VoideEfs> search(Map<String, Object> searchParams, PageBean pageBean) {
        return voideEfsDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<VoideEfs> search(Map<String, Object> searchParams, Sort... sort) {
        return voideEfsDao.findAll(spec(searchParams), sort == null || sort.length == 0  ? new Sort(Direction.DESC, "createdAt") : sort[0]);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        voideEfsDao.delete(id);
    }

    public VoideEfs getOne(String id) {
        return voideEfsDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public VoideEfs save(VoideEfs voideEfs) {
        return voideEfsDao.save(voideEfs);
    }

}


