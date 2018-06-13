package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.UserPatient;

public interface UserPatientDao extends PagingAndSortingRepository<UserPatient, String>, JpaSpecificationExecutor<UserPatient> {

    UserPatient findByUserId(String userId);
}
