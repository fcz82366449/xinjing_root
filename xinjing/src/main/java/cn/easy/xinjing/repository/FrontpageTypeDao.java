package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.FrontpageType;

import java.util.List;

public interface FrontpageTypeDao extends PagingAndSortingRepository<FrontpageType, String>, JpaSpecificationExecutor<FrontpageType> {
    FrontpageType findByHelpCode(String helpCode);


    FrontpageType findByNameAndHidden(String name,int hidden);

    List<FrontpageType> findByPidAndHiddenOrderBySortAsc(String name, int hidden);
}
