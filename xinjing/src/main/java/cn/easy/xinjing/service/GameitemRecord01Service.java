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
import cn.easy.xinjing.domain.GameitemRecord01;
import cn.easy.xinjing.repository.GameitemRecord01Dao;

@Component
public class GameitemRecord01Service extends BaseService<GameitemRecord01> {
    @Autowired
    private GameitemRecord01Dao	gameitemRecord01Dao;

    public Page<GameitemRecord01> search(Map<String, Object> searchParams, PageBean pageBean) {
        return gameitemRecord01Dao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<GameitemRecord01> search(Map<String, Object> searchParams, Sort... sort) {
        return gameitemRecord01Dao.findAll(spec(searchParams), sort == null || sort.length == 0  ? new Sort(Direction.DESC, "createdAt") : sort[0]);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        gameitemRecord01Dao.delete(id);
    }

    public GameitemRecord01 getOne(String id) {
        return gameitemRecord01Dao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public GameitemRecord01 save(GameitemRecord01 gameitemRecord01) {
        return gameitemRecord01Dao.save(gameitemRecord01);
    }

    public GameitemRecord01 findByHiddenAndGameheadRecordId(String gameheadRecordId) {
        return gameitemRecord01Dao.findByHiddenAndGameheadRecordId(0,gameheadRecordId);
    }


}


