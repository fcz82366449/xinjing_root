package cn.easy.xinjing.repository;

import cn.easy.xinjing.domain.ContentPic;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ContentPicDao extends PagingAndSortingRepository<ContentPic, String>, JpaSpecificationExecutor<ContentPic> {

    ContentPic findByContentId(String contentId);
}
