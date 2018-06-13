package cn.easy.xinjing.service;

import cn.easy.base.bean.PageBean;
import cn.easy.base.service.BaseService;
import cn.easy.xinjing.domain.Comment;
import cn.easy.xinjing.repository.CommentDao;
import cn.easy.xinjing.utils.Constants;
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
public class CommentService extends BaseService<Comment> {
    @Autowired
    private CommentDao	commentDao;

    public Page<Comment> search(Map<String, Object> searchParams, PageBean pageBean) {
        return commentDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<Comment> search(Map<String, Object> searchParams) {
        return commentDao.findAll(spec(searchParams), new Sort(Direction.DESC, "createdAt"));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        commentDao.delete(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void allCanSee(String id) {
        Comment comment = commentDao.findOne(id);
        comment.setStatus(Constants.COMMENT_STATUS_ALL_CANSEE);
        commentDao.save(comment);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void noneCanSee(String id) {
        Comment comment = commentDao.findOne(id);
        comment.setStatus(Constants.COMMENT_STATUS_NONE_CANSEE);
        commentDao.save(comment);
    }

    public Comment getOne(String id) {
        return commentDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Comment save(Comment comment) {
        return commentDao.save(comment);
    }

}


