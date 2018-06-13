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
import cn.easy.xinjing.domain.ContentPic;
import cn.easy.xinjing.repository.ContentPicDao;

@Component
public class ContentPicService extends BaseService<ContentPic> {
    @Autowired
    private ContentPicDao	contentPicDao;

    public Page<ContentPic> search(Map<String, Object> searchParams, PageBean pageBean) {
        return contentPicDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        contentPicDao.delete(id);
    }

    public ContentPic getOne(String id) {
        return contentPicDao.findOne(id);
    }

    public ContentPic findByContentId(String contentId) {
        return contentPicDao.findByContentId(contentId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ContentPic save(ContentPic contentPic) {
        return contentPicDao.save(contentPic);
    }

}


