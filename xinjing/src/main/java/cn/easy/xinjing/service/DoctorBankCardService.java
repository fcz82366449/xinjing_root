package cn.easy.xinjing.service;

import java.util.List;
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
import cn.easy.xinjing.domain.DoctorBankCard;
import cn.easy.xinjing.repository.DoctorBankCardDao;

import javax.print.Doc;

@Component
public class DoctorBankCardService extends BaseService<DoctorBankCard> {
    @Autowired
    private DoctorBankCardDao	doctorBankCardDao;

    public Page<DoctorBankCard> search(Map<String, Object> searchParams, PageBean pageBean) {
        return doctorBankCardDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        doctorBankCardDao.delete(id);
    }

    public DoctorBankCard getOne(String id) {
        return doctorBankCardDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public DoctorBankCard save(DoctorBankCard doctorBankCard) {
        return doctorBankCardDao.save(doctorBankCard);
    }

    public DoctorBankCard findByDoctorId(String doctorId) {
        List<DoctorBankCard> doctorBankCardList = doctorBankCardDao.findByDoctorId(doctorId);
        return doctorBankCardList.isEmpty() ? null : doctorBankCardList.get(0);
    }

    public DoctorBankCard findByDoctorIdAndhidden(String doctorId,Integer hidden) {
        List<DoctorBankCard> doctorBankCardList = doctorBankCardDao.findByDoctorIdAndHidden(doctorId,hidden);
        return doctorBankCardList.isEmpty() ? null : doctorBankCardList.get(0);
    }
}


