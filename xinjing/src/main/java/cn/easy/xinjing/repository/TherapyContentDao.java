package cn.easy.xinjing.repository;

import cn.easy.xinjing.domain.TherapyContent;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TherapyContentDao extends PagingAndSortingRepository<TherapyContent, String>, JpaSpecificationExecutor<TherapyContent> {

    List<TherapyContent> findByContentId(String contentId);

    List<TherapyContent> findByTherapyId(String therapyId);
}
