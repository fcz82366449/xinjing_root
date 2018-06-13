package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.Calllimit;

public interface CalllimitDao extends PagingAndSortingRepository<Calllimit, String>, JpaSpecificationExecutor<Calllimit> {

}
