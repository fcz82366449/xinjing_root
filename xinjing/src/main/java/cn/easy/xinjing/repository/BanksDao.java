package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.Banks;

public interface BanksDao extends PagingAndSortingRepository<Banks, String>, JpaSpecificationExecutor<Banks> {

}
