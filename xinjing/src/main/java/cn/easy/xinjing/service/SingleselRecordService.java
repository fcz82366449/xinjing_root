package cn.easy.xinjing.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.easy.base.service.BaseService;
import cn.easy.base.utils.jdbc.PaginationHelper;
import cn.easy.xinjing.domain.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.easy.base.bean.PageBean;
import cn.easy.xinjing.domain.SingleselRecord;
import cn.easy.xinjing.repository.SingleselRecordDao;

@Component
public class SingleselRecordService extends BaseService<SingleselRecord> {
    @Autowired
    private SingleselRecordDao	singleselRecordDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public Page<SingleselRecord> search(Map<String, Object> searchParams, PageBean pageBean) {
        return singleselRecordDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<SingleselRecord> search(Map<String, Object> searchParams) {
        return singleselRecordDao.findAll(spec(searchParams));
    }
    public List<SingleselRecord> findByHiddenAndEvaluatingIdOrderByCreatedAtDesc(int hidden,String evaluatingId){
        return singleselRecordDao.findByHiddenAndEvaluatingIdOrderByCreatedAtDesc(hidden,evaluatingId);
    }
    public SingleselRecord[] getRecord(String evaluationPeople){
        return singleselRecordDao.getRecord(evaluationPeople,0);
    }
    public BigDecimal[] getRecordScore(String evaluationPeople){
        return singleselRecordDao.getRecordScore(evaluationPeople,0);
    }
    public List<SingleselRecord> findByEvaluationPeople(String evaluationPeople){
        return singleselRecordDao.findByEvaluationPeople(evaluationPeople);
    }




    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        singleselRecordDao.delete(id);
    }

    public SingleselRecord getOne(String id) {
        return singleselRecordDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public SingleselRecord save(SingleselRecord singleselRecord) {
        return singleselRecordDao.save(singleselRecord);
    }


}


