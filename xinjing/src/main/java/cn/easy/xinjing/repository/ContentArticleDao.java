package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.ContentArticle;

public interface ContentArticleDao extends PagingAndSortingRepository<ContentArticle, String>, JpaSpecificationExecutor<ContentArticle> {

    ContentArticle findByContentId(String contentId);
}
