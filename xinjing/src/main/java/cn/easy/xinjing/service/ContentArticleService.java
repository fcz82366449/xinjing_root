package cn.easy.xinjing.service;

import cn.easy.base.bean.PageBean;
import cn.easy.base.service.BaseService;
import cn.easy.xinjing.domain.ContentArticle;
import cn.easy.xinjing.repository.ContentArticleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
public class ContentArticleService extends BaseService<ContentArticle> {
    @Autowired
    private ContentArticleDao	contentArticleDao;

    public Page<ContentArticle> search(Map<String, Object> searchParams, PageBean pageBean) {
        return contentArticleDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        contentArticleDao.delete(id);
    }

    public ContentArticle getOne(String id) {
        return contentArticleDao.findOne(id);
    }

    public ContentArticle findByContentId(String contentId) {
        return contentArticleDao.findByContentId(contentId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ContentArticle save(ContentArticle contentArticle) {
        return contentArticleDao.save(contentArticle);
    }

}


