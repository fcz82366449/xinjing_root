package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.GameitemRecord03;


public interface GameitemRecord03Dao extends PagingAndSortingRepository<GameitemRecord03, String>, JpaSpecificationExecutor<GameitemRecord03> {

    GameitemRecord03 findByHiddenAndGameheadRecordId(Integer hidden, String gameheadRecordId );
}
