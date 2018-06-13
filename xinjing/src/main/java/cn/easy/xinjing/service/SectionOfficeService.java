package cn.easy.xinjing.service;

import cn.easy.base.bean.PageBean;
import cn.easy.base.service.BaseService;
import cn.easy.base.utils.ExtractUtil;
import cn.easy.xinjing.domain.Disease;
import cn.easy.xinjing.domain.SectionOffice;
import cn.easy.xinjing.domain.SectionOfficeDisease;
import cn.easy.xinjing.repository.SectionOfficeDao;
import cn.easy.xinjing.utils.Constants;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public class SectionOfficeService extends BaseService<SectionOffice> {
    @Autowired
    private SectionOfficeDao	sectionOfficeDao;
    @Autowired
    private SectionOfficeDiseaseService sectionOfficeDiseaseService;
    @Autowired
    private DiseaseService diseaseService;

    public List<SectionOffice> search(Map<String, Object> searchParams) {
        searchParams.put("EQ_hidden" , Constants.NO);
        return sectionOfficeDao.findAll(spec(searchParams));
    }

    public Page<SectionOffice> search(Map<String, Object> searchParams, PageBean pageBean) {
        return sectionOfficeDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    /**
     * 获得关联的病种 ids 和 names
     * @return
     */
    public Map<String, String> getReDiseases(String id){
        List<SectionOfficeDisease> officeDiseaseList = sectionOfficeDiseaseService.findBySectionOfficeId(id);
        List<String> diseaseIds = ExtractUtil.extractToList(officeDiseaseList, "diseaseId");
        Map<String, Object> searchParams = new HashedMap(){{
            put("IN_id", StringUtils.join(diseaseIds,","));
        }};

        List<Disease> diseaseList = diseaseService.search(searchParams);
        List<String> diseaseNames = ExtractUtil.extractToList(diseaseList, "name");

        Map<String, String> resultMap = new HashedMap(){{
          put("diseaseIds", StringUtils.join(diseaseIds, ","));
          put("diseaseNames", StringUtils.join(diseaseNames, ","));
        }};

        return resultMap;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        //删除对应的关联病种的关联记录
        List<SectionOfficeDisease> diseaseList = sectionOfficeDiseaseService.findBySectionOfficeId(id);
        if(!diseaseList.isEmpty()){
            for(SectionOfficeDisease officeDisease : diseaseList){
                sectionOfficeDiseaseService.delete(officeDisease.getId());
            }
        }
        sectionOfficeDao.delete(id);
    }

    public SectionOffice getOne(String id) {
        return sectionOfficeDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public SectionOffice save(SectionOffice sectionOffice) {
        return sectionOfficeDao.save(sectionOffice);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public SectionOffice saveOfficeAndDiseases(SectionOffice sectionOffice,String diseaseIds) {
        sectionOffice = save(sectionOffice);
        //处理关联的病种
        if(StringUtils.isNotEmpty(diseaseIds)){
            List<SectionOfficeDisease> diseaseList = sectionOfficeDiseaseService.findBySectionOfficeId(sectionOffice.getId());
            for(SectionOfficeDisease officeDisease : diseaseList){
                sectionOfficeDiseaseService.delete(officeDisease.getId());
            }
            String[] diseaseIdArray = diseaseIds.split(",");
            for(String id : diseaseIdArray){
                SectionOfficeDisease officeDisease = new SectionOfficeDisease();
                officeDisease.setSectionOfficeId(sectionOffice.getId());
                officeDisease.setDiseaseId(id);
                sectionOfficeDiseaseService.save(officeDisease);
            }
        }
        return sectionOffice;
    }

    public void publish(SectionOffice sectionOffice) {
        sectionOffice.setStatus(Constants.SECTION_OFFICE_STATUS_STATUS_PUBLISH);
        sectionOfficeDao.save(sectionOffice);
    }

    public void unPublish(SectionOffice sectionOffice) {
        sectionOffice.setStatus(Constants.SECTION_OFFICE_STATUS_STATUS_UN_PUBLISH);
        sectionOfficeDao.save(sectionOffice);
    }

    public void moveDisease(String moveId, String desId) {
        SectionOffice sectionOffice = getOne(moveId);
        if (sectionOffice != null) {
            sectionOffice.setPid(desId);
            save(sectionOffice);
        }
    }

    public List<SectionOffice> findByStatus(int status) {
        return sectionOfficeDao.findByStatus(status);
    }
}


