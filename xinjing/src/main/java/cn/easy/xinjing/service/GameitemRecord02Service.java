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
import cn.easy.xinjing.domain.GameitemRecord02;
import cn.easy.xinjing.repository.GameitemRecord02Dao;

@Component
public class GameitemRecord02Service extends BaseService<GameitemRecord02> {
    @Autowired
    private GameitemRecord02Dao	gameitemRecord02Dao;

    public Page<GameitemRecord02> search(Map<String, Object> searchParams, PageBean pageBean) {
        return gameitemRecord02Dao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<GameitemRecord02> search(Map<String, Object> searchParams, Sort... sort) {
        return gameitemRecord02Dao.findAll(spec(searchParams), sort == null || sort.length == 0  ? new Sort(Direction.DESC, "createdAt") : sort[0]);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        gameitemRecord02Dao.delete(id);
    }

    public GameitemRecord02 getOne(String id) {
        return gameitemRecord02Dao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public GameitemRecord02 save(GameitemRecord02 gameitemRecord02) {
        return gameitemRecord02Dao.save(gameitemRecord02);
    }
    public GameitemRecord02 findByHiddenAndGameheadRecordId(String gameheadRecordId) {
        return gameitemRecord02Dao.findByHiddenAndGameheadRecordId(0,gameheadRecordId);
    }
}


