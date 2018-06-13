package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.DoctorBankCard;

import java.util.List;

public interface DoctorBankCardDao extends PagingAndSortingRepository<DoctorBankCard, String>, JpaSpecificationExecutor<DoctorBankCard> {

    List<DoctorBankCard> findByDoctorId(String doctorId);

    List<DoctorBankCard> findByDoctorIdAndHidden(String doctorId,Integer hidden);
}
