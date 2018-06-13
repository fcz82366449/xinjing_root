package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.FrontpageCollect;

public interface FrontpageCollectDao extends PagingAndSortingRepository<FrontpageCollect, String>, JpaSpecificationExecutor<FrontpageCollect> {

}
