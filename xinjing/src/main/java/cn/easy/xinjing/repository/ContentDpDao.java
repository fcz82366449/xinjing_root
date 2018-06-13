package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.ContentDp;

public interface ContentDpDao extends PagingAndSortingRepository<ContentDp, String>, JpaSpecificationExecutor<ContentDp> {

}
