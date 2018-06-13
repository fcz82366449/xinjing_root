package cn.easy.xinjing.repository;

import cn.easy.xinjing.domain.ContentGame;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ContentGameDao extends PagingAndSortingRepository<ContentGame, String>, JpaSpecificationExecutor<ContentGame> {

    ContentGame findByContentId(String contentId);
}
