package cn.easy.xinjing.service;

import cn.easy.base.bean.PageBean;
import cn.easy.base.service.BaseService;
import cn.easy.base.utils.DateTimeUtil;
import cn.easy.xinjing.domain.Prescription;
import cn.easy.xinjing.domain.PrescriptionContent;
import cn.easy.xinjing.domain.PrescriptionContentFeedback;
import cn.easy.xinjing.repository.PrescriptionContentFeedbackDao;
import cn.easy.xinjing.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class PrescriptionContentFeedbackService extends BaseService<PrescriptionContentFeedback> {
    @Autowired
    private PrescriptionContentFeedbackDao	prescriptionContentFeedbackDao;
    @Autowired
    private PrescriptionContentService prescriptionContentService;
    @Autowired
    private PrescriptionService prescriptionService;

    public Page<PrescriptionContentFeedback> search(Map<String, Object> searchParams, PageBean pageBean) {
        return prescriptionContentFeedbackDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        prescriptionContentFeedbackDao.delete(id);
    }

    public PrescriptionContentFeedback getOne(String id) {
        return prescriptionContentFeedbackDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public PrescriptionContentFeedback save(PrescriptionContentFeedback prescriptionContentFeedback) {
        return prescriptionContentFeedbackDao.save(prescriptionContentFeedback);
    }

    public void add(String prescriptionContentId) {
        //一天内同一处方内容只记录一次
        Date now = new Date();
        Date startAt = DateTimeUtil.getStartDate(now);
        Date endAt = DateTimeUtil.getEndDate(now);
        PrescriptionContentFeedback dbFeedback = prescriptionContentFeedbackDao.findByPrescriptionContentIdAndUseAtBetween(prescriptionContentId, startAt, endAt);

        if (dbFeedback != null) { //已经存在反馈记录，则返回
            return;
        }

        PrescriptionContent prescriptionContent = prescriptionContentService.getOne(prescriptionContentId);

        prescriptionContent.setUseTimes(prescriptionContent.getUseTimes() + 1);
        prescriptionContent.setLastUseAt(new Date());

        if (prescriptionContent.getTimes() == prescriptionContent.getUseTimes()) {
            prescriptionContent.setStatus(Constants.PRESCRIPTION_CONTENT_STATUS_FINISH);
        } else {
            prescriptionContent.setStatus(Constants.PRESCRIPTION_CONTENT_STATUS_IN_PROGRESS);
        }

        prescriptionContent = prescriptionContentService.save(prescriptionContent);

        PrescriptionContentFeedback feedback = new PrescriptionContentFeedback();
        feedback.setPrescriptionId(prescriptionContent.getPrescriptionId());
        feedback.setContentId(prescriptionContent.getContentId());
        feedback.setPrescriptionContentId(prescriptionContent.getId());
        feedback.setUseAt(prescriptionContent.getLastUseAt());
        prescriptionContentFeedbackDao.save(feedback);


        //更新处方状态
        Prescription prescription = prescriptionService.getOne(prescriptionContent.getPrescriptionId());
        int prescriptionStatusToBe = Constants.PRESCRIPTION_STATUS_IN_PROGRESS;
        if (prescriptionContent.getStatus() == Constants.PRESCRIPTION_CONTENT_STATUS_FINISH) {
            List<PrescriptionContent> prescriptionContentList = prescriptionContentService.findByPrescriptionId(prescriptionContent.getPrescriptionId());
            boolean isFinish = true;
            for (PrescriptionContent _pc : prescriptionContentList) {
                if (_pc.getStatus() != Constants.PRESCRIPTION_CONTENT_STATUS_FINISH) {
                    isFinish = false;
                    break;
                }
            }
            if(isFinish) {
                prescriptionStatusToBe = Constants.PRESCRIPTION_STATUS_FINISH;
            }
        }

        if (prescription.getStatus() != prescriptionStatusToBe) {
            prescription.setStatus(prescriptionStatusToBe);
            prescriptionService.save(prescription);
        }

    }

}


