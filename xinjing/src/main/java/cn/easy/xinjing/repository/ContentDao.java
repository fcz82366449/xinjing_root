package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.Content;

public interface ContentDao extends PagingAndSortingRepository<Content, String>, JpaSpecificationExecutor<Content> {

}
