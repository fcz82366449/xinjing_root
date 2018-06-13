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
import cn.easy.xinjing.domain.AppVersion;
import cn.easy.xinjing.repository.AppVersionDao;

@Component
public class AppVersionService extends BaseService<AppVersion> {
    @Autowired
    private AppVersionDao	appVersionDao;

    public Page<AppVersion> search(Map<String, Object> searchParams, PageBean pageBean) {
        return appVersionDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        appVersionDao.delete(id);
    }

    public AppVersion getOne(String id) {
        return appVersionDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AppVersion save(AppVersion appVersion) {
        return appVersionDao.save(appVersion);
    }

    public AppVersion getByAppCode(String appCode){
        return appVersionDao.getByAppCode(appCode);
    }
}


