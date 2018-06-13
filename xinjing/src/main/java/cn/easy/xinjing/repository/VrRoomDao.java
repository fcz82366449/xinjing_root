package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.VrRoom;

public interface VrRoomDao extends PagingAndSortingRepository<VrRoom, String>, JpaSpecificationExecutor<VrRoom> {

}
