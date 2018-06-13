package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.ContentVideo;

public interface ContentVideoDao extends PagingAndSortingRepository<ContentVideo, String>, JpaSpecificationExecutor<ContentVideo> {

    ContentVideo findByContentId(String contentId);
}
