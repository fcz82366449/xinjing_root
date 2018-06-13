package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.Disease;

import java.util.List;

public interface DiseaseDao extends PagingAndSortingRepository<Disease, String>, JpaSpecificationExecutor<Disease> {

    List<Disease> findByIdInAndStatus(List<String> diseaseIdList, int status);

    List<Disease> findByStatusAndHiddenOrderBySortAsc(int status,int hidden);

    List<Disease> findByStatusAndHidden(int status,int hidden);

    List<Disease> findByRemarkAndHidden(String remark,String hidden);
}
