package cn.easy.xinjing.service;

import cn.easy.base.bean.PageBean;
import cn.easy.base.service.BaseService;
import cn.easy.xinjing.domain.ContentGame;
import cn.easy.xinjing.repository.ContentGameDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
public class ContentGameService extends BaseService<ContentGame> {
    @Autowired
    private ContentGameDao	contentGameDao;

    public Page<ContentGame> search(Map<String, Object> searchParams, PageBean pageBean) {
        return contentGameDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        contentGameDao.delete(id);
    }

    public ContentGame findByContentId(String contentId) {
        return contentGameDao.findByContentId(contentId);
    }

    public ContentGame getOne(String id) {
        return contentGameDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ContentGame save(ContentGame contentGame) {
        return contentGameDao.save(contentGame);
    }

}


