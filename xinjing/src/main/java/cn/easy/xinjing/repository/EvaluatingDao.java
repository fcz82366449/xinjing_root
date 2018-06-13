package cn.easy.xinjing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.Evaluating;

import java.util.List;


public interface EvaluatingDao extends PagingAndSortingRepository<Evaluating, String>, JpaSpecificationExecutor<Evaluating> {

    List<Evaluating> findByStatusAndHidden(int status, int hidden);


    Page<Evaluating> findByHiddenAndStatus(int status, int hidden, Pageable pageable);
}
