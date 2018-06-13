package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.DoctorPlanContent;

import java.util.List;

public interface DoctorPlanContentDao extends PagingAndSortingRepository<DoctorPlanContent, String>, JpaSpecificationExecutor<DoctorPlanContent> {

    List<DoctorPlanContent> findByDoctorPlanId(String doctorPlanId);
}
