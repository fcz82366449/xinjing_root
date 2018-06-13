package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.DoctorPlan;

public interface DoctorPlanDao extends PagingAndSortingRepository<DoctorPlan, String>, JpaSpecificationExecutor<DoctorPlan> {

}
