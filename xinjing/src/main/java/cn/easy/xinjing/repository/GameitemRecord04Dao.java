package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.GameitemRecord04;


public interface GameitemRecord04Dao extends PagingAndSortingRepository<GameitemRecord04, String>, JpaSpecificationExecutor<GameitemRecord04> {
    GameitemRecord04 findByHiddenAndGameheadRecordId(Integer hidden, String gameheadRecordId );
}
