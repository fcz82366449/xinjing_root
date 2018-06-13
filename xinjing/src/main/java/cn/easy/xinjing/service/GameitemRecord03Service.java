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
import cn.easy.xinjing.domain.GameitemRecord03;
import cn.easy.xinjing.repository.GameitemRecord03Dao;

@Component
public class GameitemRecord03Service extends BaseService<GameitemRecord03> {
    @Autowired
    private GameitemRecord03Dao	gameitemRecord03Dao;

    public Page<GameitemRecord03> search(Map<String, Object> searchParams, PageBean pageBean) {
        return gameitemRecord03Dao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<GameitemRecord03> search(Map<String, Object> searchParams, Sort... sort) {
        return gameitemRecord03Dao.findAll(spec(searchParams), sort == null || sort.length == 0  ? new Sort(Direction.DESC, "createdAt") : sort[0]);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        gameitemRecord03Dao.delete(id);
    }

    public GameitemRecord03 getOne(String id) {
        return gameitemRecord03Dao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public GameitemRecord03 save(GameitemRecord03 gameitemRecord03) {
        return gameitemRecord03Dao.save(gameitemRecord03);
    }

    public GameitemRecord03 findByHiddenAndGameheadRecordId(String gameheadRecordId) {
        return gameitemRecord03Dao.findByHiddenAndGameheadRecordId(0,gameheadRecordId);
    }

}


