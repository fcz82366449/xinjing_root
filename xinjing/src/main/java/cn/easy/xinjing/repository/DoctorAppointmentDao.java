package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.DoctorAppointment;

public interface DoctorAppointmentDao extends PagingAndSortingRepository<DoctorAppointment, String>, JpaSpecificationExecutor<DoctorAppointment> {

}
