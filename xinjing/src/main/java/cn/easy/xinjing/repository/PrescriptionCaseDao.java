package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.PrescriptionCase;

import java.util.List;

public interface PrescriptionCaseDao extends PagingAndSortingRepository<PrescriptionCase, String>, JpaSpecificationExecutor<PrescriptionCase> {

    @Query("SELECT a FROM PrescriptionCase a WHERE a.hospitalId= ?1 AND a.hidden=?2 AND ( recordno=?3 OR mobile=?3) order by a.createdAt desc ")
    List<PrescriptionCase> findByPrescriptionCase(String hospitalId, Integer hidden, String keyword);

    PrescriptionCase findByUserIdAndHidden(String userid, Integer hidden);

    PrescriptionCase findByUserIdAndHospitalIdAndHidden(String userid,String hospitalId, Integer hidden);

}
