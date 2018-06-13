package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.VrRoomAppointment;

public interface VrRoomAppointmentDao extends PagingAndSortingRepository<VrRoomAppointment, String>, JpaSpecificationExecutor<VrRoomAppointment> {

}
