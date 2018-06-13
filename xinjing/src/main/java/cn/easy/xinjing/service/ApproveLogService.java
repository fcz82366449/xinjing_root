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
import cn.easy.xinjing.domain.ApproveLog;
import cn.easy.xinjing.repository.ApproveLogDao;

@Component
public class ApproveLogService extends BaseService<ApproveLog> {
    @Autowired
    private ApproveLogDao	approveLogDao;

    public Page<ApproveLog> search(Map<String, Object> searchParams, PageBean pageBean) {
        return approveLogDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        approveLogDao.delete(id);
    }

    public ApproveLog getOne(String id) {
        return approveLogDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ApproveLog save(ApproveLog approveLog) {
        return approveLogDao.save(approveLog);
    }

    public List<ApproveLog> findByObjectIdAndObjectType(String objectId, String objectType) {
        return approveLogDao.findByObjectIdAndObjectTypeOrderByCreatedAtDesc(objectId, objectType);
    }
}


