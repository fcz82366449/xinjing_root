package cn.easy.xinjing.service;

import java.util.Map;
import java.util.List;

import cn.easy.base.service.BaseService;
import cn.easy.xinjing.domain.ContentHospital;
import cn.easy.xinjing.domain.DoctorPlanContent;
import cn.easy.xinjing.repository.ContentHospitalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.easy.base.bean.PageBean;
import cn.easy.xinjing.domain.ContentDp;
import cn.easy.xinjing.repository.ContentDpDao;

@Component
public class ContentDpService extends BaseService<ContentDp> {
    @Autowired
    private ContentDpDao	contentDpDao;
    @Autowired
    private ContentHospitalDao	contentHospitalDao;
    public Page<ContentDp> search(Map<String, Object> searchParams, PageBean pageBean) {
        return contentDpDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<ContentDp> search(Map<String, Object> searchParams, Sort... sort) {
        return contentDpDao.findAll(spec(searchParams), sort == null || sort.length == 0  ? new Sort(Direction.DESC, "createdAt") : sort[0]);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        contentDpDao.delete(id);
        contentHospitalDao.delete(contentHospitalDao.findByContentDpId(id));
    }

    public ContentDp getOne(String id) {
        return contentDpDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ContentDp save(ContentDp contentDp) {
        return contentDpDao.save(contentDp);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ContentDp saveOnContentHospital(ContentDp contentDp,String contentid) {
        ContentDp contentDp1 = contentDpDao.save(contentDp);
        if(contentDp1!=null){
            contentHospitalDao.delete(contentHospitalDao.findByContentDpId(contentDp1.getId()));
            String[] contentIds = contentid.split(",");
            for(String id : contentIds){
                ContentHospital contentHospital = new ContentHospital();
                contentHospital.setContentDpId(contentDp1.getId());
                contentHospital.setContentId(id);
                contentHospitalDao.save(contentHospital);
            }

        }

        return contentDp1;
    }

}


