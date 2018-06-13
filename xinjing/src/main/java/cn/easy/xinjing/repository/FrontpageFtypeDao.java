package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.FrontpageFtype;

import java.util.List;

public interface FrontpageFtypeDao extends PagingAndSortingRepository<FrontpageFtype, String>, JpaSpecificationExecutor<FrontpageFtype> {

    List<FrontpageFtype> findByFrontpageId(String frontpageId);

    List<FrontpageFtype> findByFrontpageTypeIdIn(String[] frontpageId);
    List<FrontpageFtype> findByFrontpageTypeId(String frontpageTypeId);
}
