package cn.easy.xinjing.service;

import cn.easy.base.service.BaseService;
import cn.easy.base.utils.ExtractUtil;
import cn.easy.xinjing.domain.Disease;
import cn.easy.xinjing.domain.DiseaseTherapy;
import cn.easy.xinjing.domain.Therapy;
import cn.easy.xinjing.repository.DiseaseDao;
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
public class DiseaseService extends BaseService<Disease> {
    @Autowired
    private DiseaseDao	diseaseDao;
    @Autowired
    private DiseaseTherapyService diseaseTherapyService;
    @Autowired
    private TherapyService therapyService;

    public List<Disease> search(Map<String, Object> searchParams) {
        return diseaseDao.findAll(spec(searchParams), new Sort(Sort.Direction.ASC, "sort"));
    }

    public List<Disease> findByRemarkAndHidden(String remack,String hidden) {
        return diseaseDao.findByRemarkAndHidden(remack,hidden);
    }


    @CacheEvict(value = "diseaseCache", allEntries = true)
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        //删除对应的关联疗法的关联记录
        List<DiseaseTherapy> therapyList = diseaseTherapyService.findByDiseaseId(id);
        if (!therapyList.isEmpty()) {
            for (DiseaseTherapy diseaseTherapy : therapyList) {
                diseaseTherapyService.delete(diseaseTherapy.getId());
            }
        }
        diseaseDao.delete(id);
    }

    public Disease getOne(String id) {
        return diseaseDao.findOne(id);
    }

    @CachePut(value = "diseaseCache", key = "#disease.getId()")
    @Transactional(propagation = Propagation.REQUIRED)
    public Disease save(Disease disease) {
        return diseaseDao.save(disease);
    }

    /**
     * 保存病种以及关联的疗法信息
     * @param disease
     * @param therapyIds
     * @return
     */
    @CachePut(value = "diseaseCache", key = "#disease.getId()")
    @Transactional(propagation = Propagation.REQUIRED)
    public Disease saveDiseaseAndTherapys(Disease disease, String therapyIds) {
        disease= save(disease);
        //处理关联的疗法
        if(org.apache.commons.lang.StringUtils.isNotEmpty(therapyIds)){
            List<DiseaseTherapy> diseaseTherapies = diseaseTherapyService.findByDiseaseId(disease.getId());
            for(DiseaseTherapy diseaseTherapy : diseaseTherapies){
                diseaseTherapyService.delete(diseaseTherapy.getId());
            }
            String[] therapyIdArray = therapyIds.split(",");
            for(String id : therapyIdArray){
                DiseaseTherapy diseaseTherapy = new DiseaseTherapy();
                diseaseTherapy.setDiseaseId(disease.getId());
                diseaseTherapy.setTherapyId(id);
                diseaseTherapyService.save(diseaseTherapy);
            }
        }
        return disease;
    }

    @CachePut(value = "diseaseCache", key = "#disease.getId()")
    public void publish(Disease disease) {
        disease.setStatus(Constants.THERAPY_STATUS_PUBLISH);
        diseaseDao.save(disease);
    }

    @CacheEvict(value = "diseaseCache", allEntries = true)
    public void unPublish(Disease disease) {
        disease.setStatus(Constants.THERAPY_STATUS_NO_PUBLISH);
        diseaseDao.save(disease);
    }

    @CacheEvict(value = "diseaseCache", allEntries = true)
    public void moveDisease(String moveId, String desId) {
        Disease disease = getOne(moveId);
        if (disease != null) {
            disease.setPid(desId);
            save(disease);
        }
    }

    @Cacheable(value = "diseaseCache")
    public List<Disease> findAll() {
        return Lists.newArrayList(diseaseDao.findAll(new Sort(Sort.Direction.ASC, "sort")));
    }

    public String findNamesByIds(String ids){
        Map<String,Object> searchParams = new HashedMap();
        searchParams.put("IN_id", ids);
        List<Disease> list = search(searchParams);
        return StringUtils.join(ExtractUtil.extractToList(list,"name"),",");
    }

    @Cacheable(value = "diseaseCache")
    public List<Disease> findByStatus(int status) {
        return diseaseDao.findByStatusAndHiddenOrderBySortAsc(status,0);
    }
    @Cacheable(value = "diseaseCache")
    public List<Disease> findByStatusAndHidden(int status,int hidden) {
        return diseaseDao.findByStatusAndHidden(status,hidden);
    }


    /**
     * 获得关联的病种 ids 和 names
     * @return
     */
    public Map<String, String> getReTherapys(String diseaseId){
        List<DiseaseTherapy> diseaseTherapyList = diseaseTherapyService.findByDiseaseId(diseaseId);
        List<String> therapyIds = ExtractUtil.extractToList(diseaseTherapyList, "therapyId");

        if(therapyIds.isEmpty()){
            return new HashedMap();
        }
        Map<String, Object> searchParams = new HashedMap(){{
            put("IN_id", org.apache.commons.lang.StringUtils.join(therapyIds,","));
        }};

        List<Therapy> therapyList = therapyService.search(searchParams);
        List<String> therapyNames = ExtractUtil.extractToList(therapyList, "name");

        Map<String, String> resultMap = new HashedMap(){{
            put("therapyIds", org.apache.commons.lang.StringUtils.join(therapyIds, ","));
            put("therapyNames", org.apache.commons.lang.StringUtils.join(therapyNames, ","));
        }};

        return resultMap;
    }
}


