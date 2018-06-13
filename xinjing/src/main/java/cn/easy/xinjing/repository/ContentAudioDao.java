package cn.easy.xinjing.repository;

import cn.easy.xinjing.domain.ContentAudio;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ContentAudioDao extends PagingAndSortingRepository<ContentAudio, String>, JpaSpecificationExecutor<ContentAudio> {

    ContentAudio findByContentId(String contentId);
}
