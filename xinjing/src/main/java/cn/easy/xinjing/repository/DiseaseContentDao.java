package cn.easy.xinjing.repository;

import cn.easy.xinjing.domain.DiseaseContent;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DiseaseContentDao extends PagingAndSortingRepository<DiseaseContent, String>, JpaSpecificationExecutor<DiseaseContent> {

    List<DiseaseContent> findByContentId(String contentId);

    List<DiseaseContent> findByDiseaseId(String diseaseId);
}
