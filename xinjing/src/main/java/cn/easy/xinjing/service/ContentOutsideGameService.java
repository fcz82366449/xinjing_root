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
import cn.easy.xinjing.domain.ContentOutsideGame;
import cn.easy.xinjing.repository.ContentOutsideGameDao;

@Component
public class ContentOutsideGameService extends BaseService<ContentOutsideGame> {
    @Autowired
    private ContentOutsideGameDao	contentOutsideGameDao;

    public Page<ContentOutsideGame> search(Map<String, Object> searchParams, PageBean pageBean) {
        return contentOutsideGameDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        contentOutsideGameDao.delete(id);
    }

    public ContentOutsideGame getOne(String id) {
        return contentOutsideGameDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ContentOutsideGame save(ContentOutsideGame contentOutsideGame) {
        return contentOutsideGameDao.save(contentOutsideGame);
    }

    public ContentOutsideGame findByContentId(String contentId) {
        return contentOutsideGameDao.findByContentId(contentId);
    }
}


