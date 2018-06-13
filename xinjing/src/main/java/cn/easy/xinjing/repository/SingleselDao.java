package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.Singlesel;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SingleselDao extends PagingAndSortingRepository<Singlesel, String>, JpaSpecificationExecutor<Singlesel> {


    List<Singlesel> findByHiddenAndEvaluatingIdOrderByCreatedAtDesc( int hidden, String evaluatingId);

    List<Singlesel> findByEvaluatingIdAndHiddenAndStatus(String evaluatingId,int hidden,int status);
}
