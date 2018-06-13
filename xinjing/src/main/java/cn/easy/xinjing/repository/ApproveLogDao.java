package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.ApproveLog;

import java.util.List;

public interface ApproveLogDao extends PagingAndSortingRepository<ApproveLog, String>, JpaSpecificationExecutor<ApproveLog> {

    List<ApproveLog> findByObjectIdAndObjectTypeOrderByCreatedAtDesc(String objectId, String objectType);
}
