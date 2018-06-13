package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.DoctorPatientcase;


public interface DoctorPatientcaseDao extends PagingAndSortingRepository<DoctorPatientcase, String>, JpaSpecificationExecutor<DoctorPatientcase> {


    DoctorPatientcase findByDoctorIdAndPrescriptioncaseId(String doctorId,String prescriptioncaseId);
}
