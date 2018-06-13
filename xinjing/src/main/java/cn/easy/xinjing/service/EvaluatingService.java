package cn.easy.xinjing.service;

import java.util.List;
import java.util.Map;

import cn.easy.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.easy.base.bean.PageBean;
import cn.easy.xinjing.domain.Evaluating;
import cn.easy.xinjing.repository.EvaluatingDao;

@Component
public class EvaluatingService extends BaseService<Evaluating> {
    @Autowired
    private EvaluatingDao	evaluatingDao;
    public Page<Evaluating> findByHiddenAndStatus(int status, int hidden, Pageable pageable){
        return evaluatingDao.findByHiddenAndStatus(status,hidden,pageable);
    }
    public Page<Evaluating> search(Map<String, Object> searchParams, PageBean pageBean) {
        return evaluatingDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<Evaluating> findByStatusAndHidden(int status, int hidden){
        return evaluatingDao.findByStatusAndHidden(status,hidden);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        evaluatingDao.delete(id);
    }

    public Evaluating getOne(String id) {
        return evaluatingDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Evaluating save(Evaluating evaluating) {
        return evaluatingDao.save(evaluating);
    }

}


