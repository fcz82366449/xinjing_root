package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.GameEfs;

public interface GameEfsDao extends PagingAndSortingRepository<GameEfs, String>, JpaSpecificationExecutor<GameEfs> {

}
