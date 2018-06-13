package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.Advertisement;

public interface AdvertisementDao extends PagingAndSortingRepository<Advertisement, String>, JpaSpecificationExecutor<Advertisement> {

}
