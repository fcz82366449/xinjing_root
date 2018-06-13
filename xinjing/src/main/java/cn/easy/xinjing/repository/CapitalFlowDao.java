package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.CapitalFlow;

import java.util.List;

public interface CapitalFlowDao extends PagingAndSortingRepository<CapitalFlow, String>, JpaSpecificationExecutor<CapitalFlow> {
}
