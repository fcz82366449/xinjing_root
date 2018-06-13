package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.PcVersion;

public interface PcVersionDao extends PagingAndSortingRepository<PcVersion, String>, JpaSpecificationExecutor<PcVersion> {

    PcVersion findByVersionCode(String versionCode);
}
