package cn.easy.xinjing.repository;

import cn.easy.xinjing.domain.DiseaseTherapy;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DiseaseTherapyDao extends PagingAndSortingRepository<DiseaseTherapy, String>, JpaSpecificationExecutor<DiseaseTherapy> {

    List<DiseaseTherapy> findByTherapyId(String therapyId);

    List<DiseaseTherapy> findByDiseaseId(String diseaseId);
}
