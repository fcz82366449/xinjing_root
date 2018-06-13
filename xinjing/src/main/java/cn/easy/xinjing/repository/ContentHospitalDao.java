package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.ContentHospital;

import java.util.List;

public interface ContentHospitalDao extends PagingAndSortingRepository<ContentHospital, String>, JpaSpecificationExecutor<ContentHospital> {

    List<ContentHospital> findByContentDpId(String contentDpId);
}
