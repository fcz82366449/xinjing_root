package cn.easy.xinjing.service;

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
import cn.easy.xinjing.domain.ContentCollect;
import cn.easy.xinjing.repository.ContentCollectDao;

@Component
public class ContentCollectService extends BaseService<ContentCollect> {
    @Autowired
    private ContentCollectDao contentCollectDao;

    public Page<ContentCollect> search(Map<String, Object> searchParams, PageBean pageBean) {
        return contentCollectDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        contentCollectDao.delete(id);
    }

    public ContentCollect getOne(String id) {
        return contentCollectDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ContentCollect save(ContentCollect contentCollect) {
        return contentCollectDao.save(contentCollect);
    }

    public ContentCollect findByContentIdAndUserId(String contentId, String userId) {
        return contentCollectDao.findByContentIdAndUserId(contentId, userId);
    }
}


