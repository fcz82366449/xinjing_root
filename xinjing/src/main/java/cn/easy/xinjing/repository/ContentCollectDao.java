package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.ContentCollect;

public interface ContentCollectDao extends PagingAndSortingRepository<ContentCollect, String>, JpaSpecificationExecutor<ContentCollect> {

    ContentCollect findByContentIdAndUserId(String contentId, String userId);
}
