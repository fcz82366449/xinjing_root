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
import cn.easy.xinjing.domain.DoctorPatientcase;
import cn.easy.xinjing.repository.DoctorPatientcaseDao;

@Component
public class DoctorPatientcaseService extends BaseService<DoctorPatientcase> {
    @Autowired
    private DoctorPatientcaseDao	doctorPatientcaseDao;

    public Page<DoctorPatientcase> search(Map<String, Object> searchParams, PageBean pageBean) {
        return doctorPatientcaseDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<DoctorPatientcase> search(Map<String, Object> searchParams, Sort... sort) {
        return doctorPatientcaseDao.findAll(spec(searchParams), sort == null || sort.length == 0  ? new Sort(Direction.DESC, "createdAt") : sort[0]);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        doctorPatientcaseDao.delete(id);
    }

    public DoctorPatientcase getOne(String id) {
        return doctorPatientcaseDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public DoctorPatientcase save(DoctorPatientcase doctorPatientcase) {
        return doctorPatientcaseDao.save(doctorPatientcase);
    }

    public DoctorPatientcase findByDoctorIdAndPrescriptioncaseId(String doctorId,String prescriptioncaseId) {
        return doctorPatientcaseDao.findByDoctorIdAndPrescriptioncaseId(doctorId,prescriptioncaseId);
    }


}


