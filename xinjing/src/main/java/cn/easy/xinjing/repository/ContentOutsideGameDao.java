package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.ContentOutsideGame;

public interface ContentOutsideGameDao extends PagingAndSortingRepository<ContentOutsideGame, String>, JpaSpecificationExecutor<ContentOutsideGame> {

    ContentOutsideGame findByContentId(String contentId);
}
