package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.GameitemRecord02;


public interface GameitemRecord02Dao extends PagingAndSortingRepository<GameitemRecord02, String>, JpaSpecificationExecutor<GameitemRecord02> {

    GameitemRecord02 findByHiddenAndGameheadRecordId(Integer hidden, String gameheadRecordId );

}
