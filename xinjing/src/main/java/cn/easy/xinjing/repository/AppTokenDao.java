package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.AppToken;

public interface AppTokenDao extends PagingAndSortingRepository<AppToken, String>, JpaSpecificationExecutor<AppToken> {

}
