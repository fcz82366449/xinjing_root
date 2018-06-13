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
import cn.easy.xinjing.domain.Calllimit;
import cn.easy.xinjing.repository.CalllimitDao;

@Component
public class CalllimitService extends BaseService<Calllimit> {
    @Autowired
    private CalllimitDao	calllimitDao;

    public Page<Calllimit> search(Map<String, Object> searchParams, PageBean pageBean) {
        return calllimitDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<Calllimit> search(Map<String, Object> searchParams, Sort... sort) {
        return calllimitDao.findAll(spec(searchParams), sort == null || sort.length == 0  ? new Sort(Direction.DESC, "createdAt") : sort[0]);
    }
    public Calllimit findOne(Map<String, Object> searchParams) {
        return calllimitDao.findOne(spec(searchParams));
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        calllimitDao.delete(id);
    }

    public Calllimit getOne(String id) {
        return calllimitDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Calllimit save(Calllimit calllimit) {
        return calllimitDao.save(calllimit);
    }

}


