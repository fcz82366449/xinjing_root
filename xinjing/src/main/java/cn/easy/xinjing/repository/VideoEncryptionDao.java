package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.VideoEncryption;

public interface VideoEncryptionDao extends PagingAndSortingRepository<VideoEncryption, String>, JpaSpecificationExecutor<VideoEncryption> {

    VideoEncryption findByContentId(String contentId);

}
