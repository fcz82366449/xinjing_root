package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.Charges;

public interface ChargesDao extends PagingAndSortingRepository<Charges, String>, JpaSpecificationExecutor<Charges> {

}
