package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.Prescription;

import java.util.List;

public interface PrescriptionDao extends PagingAndSortingRepository<Prescription, String>, JpaSpecificationExecutor<Prescription> {

    List<Prescription> findByPatientIdOrderByCreatedAtDesc(String patientId);

    List<Prescription> findByPatientIdAndHiddenOrderByCreatedAtDesc(String patientId,Integer hidden);

    @Query("SELECT a FROM Prescription a WHERE (a.hospitalId= ?2 OR a.source=?4 ) AND  a.hidden=?3 AND a.patientId=?1 order by a.createdAt desc ")
    List<Prescription> findByPatientIdAndHospitalIdAndHiddenOrSourceOrderByCreatedAtDesc(String patientId,String hospitalId,Integer hidden,Integer source);


    /**
     * 根据患者（病案号）和对应的医院查找处方
     * @param patientcaseId
     * @param hospitalId
     * @param hidden
     * @return
     */
    List<Prescription> findByPatientcaseIdAndHospitalIdAndHiddenOrderByCreatedAtDesc(String patientcaseId,String hospitalId,Integer hidden);
}