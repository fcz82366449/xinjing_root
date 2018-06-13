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
import cn.easy.xinjing.domain.Banks;
import cn.easy.xinjing.repository.BanksDao;

@Component
public class BanksService extends BaseService<Banks> {
    @Autowired
    private BanksDao	banksDao;

    public Page<Banks> search(Map<String, Object> searchParams, PageBean pageBean) {
        return banksDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<Banks> search(Map<String, Object> searchParams) {
        return banksDao.findAll(spec(searchParams));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        banksDao.delete(id);
    }

    public Banks getOne(String id) {
        return banksDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Banks save(Banks banks) {
        return banksDao.save(banks);
    }

}


