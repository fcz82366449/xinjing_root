package cn.easy.xinjing.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.easy.base.service.BaseService;
import cn.easy.base.utils.ExtractUtil;
import cn.easy.base.utils.jdbc.PaginationHelper;
import cn.easy.xinjing.domain.Content;
import cn.easy.xinjing.domain.Disease;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.easy.base.bean.PageBean;
import cn.easy.xinjing.domain.FrontpageType;
import cn.easy.xinjing.repository.FrontpageTypeDao;

@Component
public class FrontpageTypeService extends BaseService<FrontpageType> {
    @Autowired
    private FrontpageTypeDao	frontpageTypeDao;

    public Page<FrontpageType> search(Map<String, Object> searchParams, PageBean pageBean) {
        return frontpageTypeDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<FrontpageType> search(Map<String, Object> searchParams) {
        return frontpageTypeDao.findAll(spec(searchParams));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        frontpageTypeDao.delete(id);
    }

    public FrontpageType getOne(String id) {
        return frontpageTypeDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public FrontpageType save(FrontpageType frontpageType) {
        return frontpageTypeDao.save(frontpageType);
    }

    @CacheEvict(value = "frontpageTypeCache", allEntries = true)
    public void moveDisease(String moveId, String desId) {
        FrontpageType frontpageType = getOne(moveId);
        if (frontpageType != null) {
            frontpageType.setPid(desId);
            save(frontpageType);
        }
    }

    public String findNamesByIds(String ids){
        Map<String,Object> searchParams = new HashedMap();
        searchParams.put("IN_id", ids);
        List<FrontpageType> list = search(searchParams);
        return StringUtils.join(ExtractUtil.extractToList(list,"name"),",");
    }

    public FrontpageType findByHelpCode(String helpCode){
            return frontpageTypeDao.findByHelpCode(helpCode);
    }


    public FrontpageType findByName(String name){
        return frontpageTypeDao.findByNameAndHidden(name,0);
    }
    public List<FrontpageType> findByPidAndHiddenOrderBySortAsc(String pid){
        return frontpageTypeDao.findByPidAndHiddenOrderBySortAsc(pid,0);
    }



}


