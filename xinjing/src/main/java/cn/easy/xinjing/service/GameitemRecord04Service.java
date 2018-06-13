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
import cn.easy.xinjing.domain.GameitemRecord04;
import cn.easy.xinjing.repository.GameitemRecord04Dao;

@Component
public class GameitemRecord04Service extends BaseService<GameitemRecord04> {
    @Autowired
    private GameitemRecord04Dao	gameitemRecord04Dao;

    public Page<GameitemRecord04> search(Map<String, Object> searchParams, PageBean pageBean) {
        return gameitemRecord04Dao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<GameitemRecord04> search(Map<String, Object> searchParams, Sort... sort) {
        return gameitemRecord04Dao.findAll(spec(searchParams), sort == null || sort.length == 0  ? new Sort(Direction.DESC, "createdAt") : sort[0]);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        gameitemRecord04Dao.delete(id);
    }

    public GameitemRecord04 getOne(String id) {
        return gameitemRecord04Dao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public GameitemRecord04 save(GameitemRecord04 gameitemRecord04) {
        return gameitemRecord04Dao.save(gameitemRecord04);
    }
    public GameitemRecord04 findByHiddenAndGameheadRecordId(String gameheadRecordId) {
        return gameitemRecord04Dao.findByHiddenAndGameheadRecordId(0,gameheadRecordId);
    }
}


