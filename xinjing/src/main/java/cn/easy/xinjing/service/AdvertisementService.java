package cn.easy.xinjing.service;

import cn.easy.base.bean.PageBean;
import cn.easy.base.service.BaseService;
import cn.easy.xinjing.domain.Advertisement;
import cn.easy.xinjing.repository.AdvertisementDao;
import cn.easy.xinjing.utils.Constants;
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
public class AdvertisementService extends BaseService<Advertisement> {
    @Autowired
    private AdvertisementDao	advertisementDao;

    public Page<Advertisement> search(Map<String, Object> searchParams, PageBean pageBean) {
        return advertisementDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<Advertisement> search(Map<String, Object> searchParams) {
        return advertisementDao.findAll(spec(searchParams), new Sort(Direction.DESC, "createdAt"));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        advertisementDao.delete(id);
    }

    public Advertisement getOne(String id) {
        return advertisementDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Advertisement save(Advertisement advertisement) {
        return advertisementDao.save(advertisement);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void publish(Advertisement advertisement) {
        advertisement.setStatus(Constants.ADVERTISEMENT_STATUS_PUBLISH);
        advertisementDao.save(advertisement);
    }

}


