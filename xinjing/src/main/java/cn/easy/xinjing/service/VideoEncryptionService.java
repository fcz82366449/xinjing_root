package cn.easy.xinjing.service;

import java.util.Map;
import java.util.List;

import cn.easy.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.easy.base.bean.PageBean;
import cn.easy.xinjing.domain.VideoEncryption;
import cn.easy.xinjing.repository.VideoEncryptionDao;

@Component
public class VideoEncryptionService extends BaseService<VideoEncryption> {
    @Autowired
    private VideoEncryptionDao	videoEncryptionDao;

    public Page<VideoEncryption> search(Map<String, Object> searchParams, PageBean pageBean) {
        return videoEncryptionDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<VideoEncryption> search(Map<String, Object> searchParams, Sort... sort) {
        return videoEncryptionDao.findAll(spec(searchParams), sort == null || sort.length == 0  ? new Sort(Direction.DESC, "createdAt") : sort[0]);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        videoEncryptionDao.delete(id);
    }

    public VideoEncryption getOne(String id) {
        return videoEncryptionDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public VideoEncryption save(VideoEncryption videoEncryption) {
        return videoEncryptionDao.save(videoEncryption);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public VideoEncryption upSave(VideoEncryption videoEncryption) {
        //如已有，则先删除，再新增
        VideoEncryption videoEncryption1 = videoEncryptionDao.findByContentId(videoEncryption.getContentId());
        if(videoEncryption1!=null){
            videoEncryptionDao.delete(videoEncryption1.getId());
        }
        return videoEncryptionDao.save(videoEncryption);
    }

    public VideoEncryption findByContentId(String contentId) {
        return videoEncryptionDao.findByContentId(contentId);
    }

}


