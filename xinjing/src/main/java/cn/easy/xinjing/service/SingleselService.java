package cn.easy.xinjing.service;

import java.util.List;
import java.util.Map;

import cn.easy.base.service.BaseService;
import cn.easy.xinjing.domain.SingleselRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.easy.base.bean.PageBean;
import cn.easy.xinjing.domain.Singlesel;
import cn.easy.xinjing.repository.SingleselDao;

@Component
public class SingleselService extends BaseService<Singlesel> {
    @Autowired
    private SingleselDao	singleselDao;
    public  List<Singlesel> findByEvaluatingId(String evaluatingId){
        return singleselDao.findByEvaluatingIdAndHiddenAndStatus(evaluatingId,0,2);
    }
    public Page<Singlesel> search(Map<String, Object> searchParams, PageBean pageBean) {
        return singleselDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<Singlesel> findByHiddenAndEvaluatingIdOrderByCreatedAtDesc(int hidden, String evaluatingId){
        return singleselDao.findByHiddenAndEvaluatingIdOrderByCreatedAtDesc(hidden,evaluatingId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        singleselDao.delete(id);
    }

    public Singlesel getOne(String id) {
        return singleselDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Singlesel save(Singlesel singlesel) {
        return singleselDao.save(singlesel);
    }

}


