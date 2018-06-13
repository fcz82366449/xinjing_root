package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.Comment;

public interface CommentDao extends PagingAndSortingRepository<Comment, String>, JpaSpecificationExecutor<Comment> {

}
