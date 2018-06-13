package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.GameitemRecord05;


public interface GameitemRecord05Dao extends PagingAndSortingRepository<GameitemRecord05, String>, JpaSpecificationExecutor<GameitemRecord05> {
    GameitemRecord05 findByHiddenAndGameheadRecordId(Integer hidden, String gameheadRecordId );
}
