package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.Hospital;

import java.util.List;

public interface HospitalDao extends PagingAndSortingRepository<Hospital, String>, JpaSpecificationExecutor<Hospital> {

    List<Hospital> findByCarousel(String carousel);

    List<Hospital> findByIsdisplay(int isdisplay);
}
