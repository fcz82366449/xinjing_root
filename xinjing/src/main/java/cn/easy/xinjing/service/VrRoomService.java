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
import cn.easy.xinjing.domain.VrRoom;
import cn.easy.xinjing.repository.VrRoomDao;

@Component
public class VrRoomService extends BaseService<VrRoom> {
    @Autowired
    private VrRoomDao	vrRoomDao;

    public Page<VrRoom> search(Map<String, Object> searchParams, PageBean pageBean) {
        return vrRoomDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        vrRoomDao.delete(id);
    }

    public VrRoom getOne(String id) {
        return vrRoomDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public VrRoom save(VrRoom vrRoom) {
        return vrRoomDao.save(vrRoom);
    }

}


