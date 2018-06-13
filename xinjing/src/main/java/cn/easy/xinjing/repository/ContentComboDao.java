package cn.easy.xinjing.repository;

import cn.easy.xinjing.domain.ContentCombo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ContentComboDao extends PagingAndSortingRepository<ContentCombo, String>, JpaSpecificationExecutor<ContentCombo> {

     List<ContentCombo> findByContentId(String contentId);
}
