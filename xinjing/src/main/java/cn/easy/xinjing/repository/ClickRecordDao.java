package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.ClickRecord;

public interface ClickRecordDao extends PagingAndSortingRepository<ClickRecord, String>, JpaSpecificationExecutor<ClickRecord> {

}
