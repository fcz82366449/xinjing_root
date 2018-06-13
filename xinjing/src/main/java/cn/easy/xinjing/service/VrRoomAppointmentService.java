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
import cn.easy.xinjing.domain.VrRoomAppointment;
import cn.easy.xinjing.repository.VrRoomAppointmentDao;

@Component
public class VrRoomAppointmentService extends BaseService<VrRoomAppointment> {
    @Autowired
    private VrRoomAppointmentDao	vrRoomAppointmentDao;

    public Page<VrRoomAppointment> search(Map<String, Object> searchParams, PageBean pageBean) {
        return vrRoomAppointmentDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        vrRoomAppointmentDao.delete(id);
    }

    public VrRoomAppointment getOne(String id) {
        return vrRoomAppointmentDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public VrRoomAppointment save(VrRoomAppointment vrRoomAppointment) {
        return vrRoomAppointmentDao.save(vrRoomAppointment);
    }

}


