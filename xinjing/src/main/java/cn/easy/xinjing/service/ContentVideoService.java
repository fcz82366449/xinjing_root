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
import cn.easy.xinjing.domain.ContentVideo;
import cn.easy.xinjing.repository.ContentVideoDao;

@Component
public class ContentVideoService extends BaseService<ContentVideo> {
    @Autowired
    private ContentVideoDao contentVedioDao;

    public Page<ContentVideo> search(Map<String, Object> searchParams, PageBean pageBean) {
        return contentVedioDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        contentVedioDao.delete(id);
    }

    public ContentVideo getOne(String id) {
        return contentVedioDao.findOne(id);
    }

    public ContentVideo findByContentId(String contentId) {
        return contentVedioDao.findByContentId(contentId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ContentVideo save(ContentVideo contentVedio) {
        return contentVedioDao.save(contentVedio);
    }

}


