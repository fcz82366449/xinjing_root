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
import cn.easy.xinjing.domain.CapitalFlow;
import cn.easy.xinjing.repository.CapitalFlowDao;

@Component
public class CapitalFlowService extends BaseService<CapitalFlow> {
    @Autowired
    private CapitalFlowDao	capitalFlowDao;

    public Page<CapitalFlow> search(Map<String, Object> searchParams, PageBean pageBean) {
        return capitalFlowDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        capitalFlowDao.delete(id);
    }

    public CapitalFlow getOne(String id) {
        return capitalFlowDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CapitalFlow save(CapitalFlow capitalFlow) {
        return capitalFlowDao.save(capitalFlow);
    }

}


