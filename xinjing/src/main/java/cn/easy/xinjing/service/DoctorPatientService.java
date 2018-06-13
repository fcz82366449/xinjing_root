package cn.easy.xinjing.service;

import cn.easy.base.bean.PageBean;
import cn.easy.base.service.BaseService;
import cn.easy.xinjing.domain.DoctorPatient;
import cn.easy.xinjing.repository.DoctorPatientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public class DoctorPatientService extends BaseService<DoctorPatient> {
    @Autowired
    private DoctorPatientDao	doctorPatientDao;

    public Page<DoctorPatient> search(Map<String, Object> searchParams, PageBean pageBean) {
        return doctorPatientDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        doctorPatientDao.delete(id);
    }

    public DoctorPatient getOne(String id) {
        return doctorPatientDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public DoctorPatient save(DoctorPatient doctorPatient) {
        return doctorPatientDao.save(doctorPatient);
    }

    public DoctorPatient findByDoctorIdAndPatientId(String doctorId, String patientId) {
        return doctorPatientDao.findByDoctorIdAndPatientId(doctorId, patientId);
    }

    public List<DoctorPatient> findByDoctorId(String doctorId) {
        return doctorPatientDao.findByDoctorId(doctorId);
    }

    public List<DoctorPatient> findByPatientId(String patientId) {
        return doctorPatientDao.findByPatientId(patientId);
    }
}


