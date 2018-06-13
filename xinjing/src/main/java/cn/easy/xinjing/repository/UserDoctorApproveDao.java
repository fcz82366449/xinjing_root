package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.UserDoctorApprove;

public interface UserDoctorApproveDao extends PagingAndSortingRepository<UserDoctorApprove, String>, JpaSpecificationExecutor<UserDoctorApprove> {

    @Query(value = "select * from xj_user_doctor_approve where user_id = ? order by created_at desc limit 1",nativeQuery = true)
    UserDoctorApprove findLastByUserId(String userId);
}
