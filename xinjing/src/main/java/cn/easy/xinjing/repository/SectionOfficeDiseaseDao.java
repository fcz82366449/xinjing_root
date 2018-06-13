package cn.easy.xinjing.repository;

import cn.easy.xinjing.domain.SectionOfficeDisease;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SectionOfficeDiseaseDao extends PagingAndSortingRepository<SectionOfficeDisease, String>, JpaSpecificationExecutor<SectionOfficeDisease> {

    List<SectionOfficeDisease> findBySectionOfficeId(String sectionOfficeId);

    List<SectionOfficeDisease> findByDiseaseId(String diseaseId);
}
