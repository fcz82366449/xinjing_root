package cn.easy.xinjing.service;

import cn.easy.base.bean.PageBean;
import cn.easy.base.service.BaseService;
import cn.easy.xinjing.domain.TherapyContent;
import cn.easy.xinjing.repository.TherapyContentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public class TherapyContentService extends BaseService<TherapyContent> {
    @Autowired
    private TherapyContentDao	therapyContentDao;

    public Page<TherapyContent> search(Map<String, Object> searchParams, PageBean pageBean) {
        return therapyContentDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        therapyContentDao.delete(id);
    }

    public TherapyContent getOne(String id) {
        return therapyContentDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public TherapyContent save(TherapyContent therapyContent) {
        return therapyContentDao.save(therapyContent);
    }

    public List<TherapyContent> findByContentId(String contentId){
        return therapyContentDao.findByContentId(contentId);
    }

    public List<TherapyContent> findByTherapyId(String therapyId) {
        return therapyContentDao.findByTherapyId(therapyId);
    }
}


