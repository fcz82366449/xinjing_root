package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.SingleselRecord;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SingleselRecordDao extends PagingAndSortingRepository<SingleselRecord, String>, JpaSpecificationExecutor<SingleselRecord> {


    List<SingleselRecord> findByHiddenAndEvaluatingIdOrderByCreatedAtDesc(@Param("hidden") int hidden, @Param("evaluatingId")String evaluatingId);
    List<SingleselRecord> findByEvaluationPeople(@Param("evaluationPeople") String evaluationPeople);
    @Query(value = "select * from xj_singlesel_record where evaluation_people = :evaluationPeople and hidden=:hidden group by evaluating_id,evaluation_time",nativeQuery = true)
    SingleselRecord[] getRecord(@Param("evaluationPeople") String evaluationPeople,@Param("hidden") int hidden);
    @Query(value = "select sum(tmscore) from xj_singlesel_record where evaluation_people = :evaluationPeople and hidden=:hidden group by evaluating_id,evaluation_time",nativeQuery = true)
    BigDecimal[] getRecordScore(@Param("evaluationPeople") String evaluationPeople,@Param("hidden") int hidden);

}
