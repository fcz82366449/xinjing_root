package cn.easy.xinjing.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import cn.easy.xinjing.domain.PrescriptionContentFeedback;

import java.util.Date;

public interface PrescriptionContentFeedbackDao extends PagingAndSortingRepository<PrescriptionContentFeedback, String>, JpaSpecificationExecutor<PrescriptionContentFeedback> {

    PrescriptionContentFeedback findByPrescriptionContentIdAndUseAtBetween(String prescriptionContentId, Date startAt, Date endAt);
}
