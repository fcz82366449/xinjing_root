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
import cn.easy.xinjing.domain.DoctorAppointment;
import cn.easy.xinjing.repository.DoctorAppointmentDao;

@Component
public class DoctorAppointmentService extends BaseService<DoctorAppointment> {
    @Autowired
    private DoctorAppointmentDao	doctorAppointmentDao;

    public Page<DoctorAppointment> search(Map<String, Object> searchParams, PageBean pageBean) {
        return doctorAppointmentDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        doctorAppointmentDao.delete(id);
    }

    public DoctorAppointment getOne(String id) {
        return doctorAppointmentDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public DoctorAppointment save(DoctorAppointment doctorAppointment) {
        return doctorAppointmentDao.save(doctorAppointment);
    }

}


