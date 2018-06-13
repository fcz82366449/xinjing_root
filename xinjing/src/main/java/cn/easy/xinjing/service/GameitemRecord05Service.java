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
import cn.easy.xinjing.domain.GameitemRecord05;
import cn.easy.xinjing.repository.GameitemRecord05Dao;

@Component
public class GameitemRecord05Service extends BaseService<GameitemRecord05> {
    @Autowired
    private GameitemRecord05Dao	gameitemRecord05Dao;

    public Page<GameitemRecord05> search(Map<String, Object> searchParams, PageBean pageBean) {
        return gameitemRecord05Dao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<GameitemRecord05> search(Map<String, Object> searchParams, Sort... sort) {
        return gameitemRecord05Dao.findAll(spec(searchParams), sort == null || sort.length == 0  ? new Sort(Direction.DESC, "createdAt") : sort[0]);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        gameitemRecord05Dao.delete(id);
    }

    public GameitemRecord05 getOne(String id) {
        return gameitemRecord05Dao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public GameitemRecord05 save(GameitemRecord05 gameitemRecord05) {
        return gameitemRecord05Dao.save(gameitemRecord05);
    }
    public GameitemRecord05 findByHiddenAndGameheadRecordId(String gameheadRecordId) {
        return gameitemRecord05Dao.findByHiddenAndGameheadRecordId(0,gameheadRecordId);
    }
}


