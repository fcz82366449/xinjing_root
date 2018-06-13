package cn.easy.xinjing.service;

import cn.easy.base.service.BaseService;
import cn.easy.base.utils.ExtractUtil;
import cn.easy.xinjing.domain.Therapy;
import cn.easy.xinjing.repository.TherapyDao;
import cn.easy.xinjing.utils.Constants;
import com.google.common.collect.Lists;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public class TherapyService extends BaseService<Therapy> {
    @Autowired
    private TherapyDao	therapyDao;

    public List<Therapy> search(Map<String, Object> searchParams) {
        return therapyDao.findAll(spec(searchParams), new Sort(Sort.Direction.ASC, "sort"));
    }

    @CacheEvict(value = "therapyCache", allEntries = true)
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        therapyDao.delete(id);
    }

    public Therapy getOne(String id) {
        return therapyDao.findOne(id);
    }

    @CacheEvict(value = "therapyCache", allEntries = true)
    @Transactional(propagation = Propagation.REQUIRED)
    public Therapy save(Therapy therapy) {
        return therapyDao.save(therapy);
    }

    @CacheEvict(value = "therapyCache", allEntries = true)
    public void publish(Therapy therapy) {
        therapy.setStatus(Constants.THERAPY_STATUS_PUBLISH);
        therapyDao.save(therapy);
    }

    @CacheEvict(value = "therapyCache", allEntries = true)
    public void unPublish(Therapy therapy) {
        therapy.setStatus(Constants.THERAPY_STATUS_NO_PUBLISH);
        therapyDao.save(therapy);
    }

    @CacheEvict(value = "therapyCache", allEntries = true)
    public void moveTherapy(String moveId, String desId) {
        Therapy therapy = getOne(moveId);
        if (therapy != null) {
            therapy.setPid(desId);
            save(therapy);
        }
    }

    @Cacheable(value = "therapyCache")
    public List<Therapy> findAll() {
        return Lists.newArrayList(therapyDao.findAll(new Sort(Sort.Direction.ASC, "sort")));
    }

    public String findNamesByIds(String ids){
        Map<String,Object> searchParams = new HashedMap();
        searchParams.put("IN_id", ids);
        List<Therapy> list = search(searchParams);
        return StringUtils.join(ExtractUtil.extractToList(list,"name"),",");
    }

    @Cacheable(value = "therapyCache", key = "#status")
    public List<Therapy> findByStatus(int status) {
        return therapyDao.findByStatusAndHiddenOrderBySortAsc(status,0);
    }


    public List<Object> findByContentStatus(List<String> contentid,int status){
        return therapyDao.findByContentStatus(contentid,status);
    }
}


