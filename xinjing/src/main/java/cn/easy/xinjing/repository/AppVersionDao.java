package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.AppVersion;

public interface AppVersionDao extends PagingAndSortingRepository<AppVersion, String>, JpaSpecificationExecutor<AppVersion> {
    AppVersion getByAppCode(String appCode);
}
