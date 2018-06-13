package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.VrRoomAppTask;

public interface VrRoomAppTaskDao extends PagingAndSortingRepository<VrRoomAppTask, String>, JpaSpecificationExecutor<VrRoomAppTask> {

}
