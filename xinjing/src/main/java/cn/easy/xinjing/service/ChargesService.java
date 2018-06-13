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
import cn.easy.xinjing.domain.Charges;
import cn.easy.xinjing.repository.ChargesDao;

@Component
public class ChargesService extends BaseService<Charges> {
    @Autowired
    private ChargesDao	chargesDao;

    public Page<Charges> search(Map<String, Object> searchParams, PageBean pageBean) {
        return chargesDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<Charges> search(Map<String, Object> searchParams, Sort... sort) {
        return chargesDao.findAll(spec(searchParams), sort == null || sort.length == 0  ? new Sort(Direction.DESC, "createdAt") : sort[0]);
    }

    public Charges findOne(Map<String, Object> searchParams) {
        return chargesDao.findOne(spec(searchParams));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        chargesDao.delete(id);
    }

    public Charges getOne(String id) {
        return chargesDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Charges save(Charges charges) {
        return chargesDao.save(charges);
    }

}


