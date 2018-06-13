package cn.easy.xinjing.service;

import java.util.Map;

import cn.easy.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.easy.base.bean.PageBean;
import cn.easy.xinjing.domain.ContentAudio;
import cn.easy.xinjing.repository.ContentAudioDao;

@Component
public class ContentAudioService extends BaseService<ContentAudio> {
    @Autowired
    private ContentAudioDao	contentAudioDao;

    public Page<ContentAudio> search(Map<String, Object> searchParams, PageBean pageBean) {
        return contentAudioDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        contentAudioDao.delete(id);
    }

    public ContentAudio getOne(String id) {
        return contentAudioDao.findOne(id);
    }

    public ContentAudio findByContentId(String contentId) {
        return contentAudioDao.findByContentId(contentId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ContentAudio save(ContentAudio contentAudio) {
        return contentAudioDao.save(contentAudio);
    }

}


