package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.VoideEfs;

public interface VoideEfsDao extends PagingAndSortingRepository<VoideEfs, String>, JpaSpecificationExecutor<VoideEfs> {

}
