package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.GameheadRecord;

public interface GameheadRecordDao extends PagingAndSortingRepository<GameheadRecord, String>, JpaSpecificationExecutor<GameheadRecord> {

}
