package cn.easy.xinjing.service;

import java.util.*;

import cn.easy.base.service.BaseService;
import cn.easy.xinjing.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.easy.base.bean.PageBean;
import cn.easy.xinjing.domain.AppToken;
import cn.easy.xinjing.repository.AppTokenDao;

@Component
public class AppTokenService extends BaseService<AppToken> {
    @Autowired
    private AppTokenDao	appTokenDao;

    public Page<AppToken> search(Map<String, Object> searchParams, PageBean pageBean) {
        return appTokenDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @CacheEvict(value = "appTokenCache", key = "#id")
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        appTokenDao.delete(id);
    }

    @CachePut(value = "appTokenCache", key = "#id")
    public AppToken getOne(String id) {
        return appTokenDao.findOne(id);
    }

    @CacheEvict(value = "appTokenCache", key = "#appToken.getId()")
    @Transactional(propagation = Propagation.REQUIRED)
    public AppToken save(AppToken appToken) {
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("EQ_userId", appToken.getUserId());
        searchParams.put("EQ_status", Constants.APP_TOKEN_STATUS_VALID);
        searchParams.put("EQ_userType", appToken.getUserType());
        List<AppToken> appTokenList = appTokenDao.findAll(spec(searchParams));
        if(!appTokenList.isEmpty()) {
            for (AppToken _appToken : appTokenList) {
                _appToken.setStatus(Constants.APP_TOKEN_STATUS_INVALID);
                _appToken.setLogoutAt(new Date());
            }
            appTokenDao.save(appTokenList);
        }

        return appTokenDao.save(appToken);
    }

}


