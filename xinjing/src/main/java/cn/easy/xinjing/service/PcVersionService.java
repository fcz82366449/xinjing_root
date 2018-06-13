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
import cn.easy.xinjing.domain.PcVersion;
import cn.easy.xinjing.repository.PcVersionDao;

@Component
public class PcVersionService extends BaseService<PcVersion> {
    @Autowired
    private PcVersionDao	pcVersionDao;

    public Page<PcVersion> search(Map<String, Object> searchParams, PageBean pageBean) {
        return pcVersionDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<PcVersion> search(Map<String, Object> searchParams, Sort... sort) {
        return pcVersionDao.findAll(spec(searchParams), sort == null || sort.length == 0  ? new Sort(Direction.DESC, "createdAt") : sort[0]);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        pcVersionDao.delete(id);
    }

    public PcVersion getOne(String id) {
        return pcVersionDao.findOne(id);
    }
    public PcVersion finByVersionCode(String versionCode) {
        return pcVersionDao.findByVersionCode(versionCode);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public PcVersion save(PcVersion pcVersion) {
        return pcVersionDao.save(pcVersion);
    }

}


