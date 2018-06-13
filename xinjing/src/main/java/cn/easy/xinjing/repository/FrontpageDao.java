package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.Frontpage;

import java.util.List;

public interface FrontpageDao extends PagingAndSortingRepository<Frontpage, String>, JpaSpecificationExecutor<Frontpage> {

    List<Frontpage> findByHiddenAndIdInOrderByReleaseTimeDesc(Integer hidden,String[] id );



    /**根据分类id查找新闻数据**/
    List<Frontpage> findByHiddenAndStatusAndFrontpageTypeIdIn(Integer hidden,Integer status,String[] FrontpageTypeId);
}
