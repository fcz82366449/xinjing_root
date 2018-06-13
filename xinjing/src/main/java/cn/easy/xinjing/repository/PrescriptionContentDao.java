package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.PrescriptionContent;

import java.util.List;

public interface PrescriptionContentDao extends PagingAndSortingRepository<PrescriptionContent, String>, JpaSpecificationExecutor<PrescriptionContent> {

    List<PrescriptionContent> findByPrescriptionId(String prescriptionId);
}
