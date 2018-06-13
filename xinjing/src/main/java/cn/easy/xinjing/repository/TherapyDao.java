package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.Therapy;

import java.util.List;

public interface TherapyDao extends PagingAndSortingRepository<Therapy, String>, JpaSpecificationExecutor<Therapy> {

    List<Therapy> findByStatusAndHiddenOrderBySortAsc(int status,int hidden);

    @Query(" SELECT name,pid,id,sort FROM Therapy  WHERE id IN (SELECT DISTINCT therapyId FROM TherapyContent WHERE contentId IN ?1) AND hidden=0 AND STATUS =?2 order by sort desc")
    List<Object> findByContentStatus(List<String> contentid , int status);
}
