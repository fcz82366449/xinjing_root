package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.DoctorArticle;

public interface DoctorArticleDao extends PagingAndSortingRepository<DoctorArticle, String>, JpaSpecificationExecutor<DoctorArticle> {

}
