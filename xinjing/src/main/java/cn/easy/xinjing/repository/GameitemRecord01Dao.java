package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.GameitemRecord01;


public interface GameitemRecord01Dao extends PagingAndSortingRepository<GameitemRecord01, String>, JpaSpecificationExecutor<GameitemRecord01> {

    GameitemRecord01 findByHiddenAndGameheadRecordId(Integer hidden, String gameheadRecordId );

}
