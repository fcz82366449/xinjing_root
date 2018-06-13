package cn.easy.xinjing.repository;

import cn.easy.xinjing.domain.SectionOffice;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SectionOfficeDao extends PagingAndSortingRepository<SectionOffice, String>, JpaSpecificationExecutor<SectionOffice> {

    List<SectionOffice> findByStatus(int status);
}
