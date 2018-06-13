package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.UserDoctor;

import java.util.List;

public interface UserDoctorDao extends PagingAndSortingRepository<UserDoctor, String>, JpaSpecificationExecutor<UserDoctor> {

    UserDoctor findByUserId(String userId);

    List<UserDoctor> findByUserIdIn(List<String> doctorIdList);
}
