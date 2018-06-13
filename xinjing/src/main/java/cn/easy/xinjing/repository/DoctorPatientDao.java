package cn.easy.xinjing.repository;

import cn.easy.xinjing.domain.DoctorPatient;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DoctorPatientDao extends PagingAndSortingRepository<DoctorPatient, String>, JpaSpecificationExecutor<DoctorPatient> {

    DoctorPatient findByDoctorIdAndPatientId(String doctorId, String patientId);

    List<DoctorPatient> findByPatientId(String patientId);

    List<DoctorPatient> findByDoctorId(String doctorId);
}
