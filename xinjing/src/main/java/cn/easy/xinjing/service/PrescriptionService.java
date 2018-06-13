package cn.easy.xinjing.service;

import cn.easy.base.bean.PageBean;
import cn.easy.base.domain.User;
import cn.easy.base.service.AutoNoConfigService;
import cn.easy.base.service.BaseService;
import cn.easy.base.service.UserService;
import cn.easy.base.utils.ExtractUtil;
import cn.easy.xinjing.bean.api.PrescriptionBean;
import cn.easy.xinjing.domain.*;
import cn.easy.xinjing.repository.PrescriptionDao;
import cn.easy.xinjing.utils.Constants;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class PrescriptionService extends BaseService<Prescription> {
    @Autowired
    private PrescriptionDao	prescriptionDao;
    @Autowired
    private PrescriptionContentService prescriptionContentService;
    @Autowired
    private UserDoctorService userDoctorService;
    @Autowired
    private UserService userService;
    @Autowired
    private ContentService contentService;
    @Autowired
    private AutoNoConfigService autoNoConfigService;
    @Autowired
    private VrRoomService vrRoomService;
    public Page<Prescription> search(Map<String, Object> searchParams, PageBean pageBean) {
        return prescriptionDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }
    public List<Prescription> findAll(Map<String, Object> searchParams) {
        return prescriptionDao.findAll(spec(searchParams), new Sort(Direction.DESC, "createdAt"));
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        prescriptionDao.delete(id);
    }

    public Prescription getOne(String id) {
        return prescriptionDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Prescription save(Prescription prescription) {
        if(prescription.getId()==null||prescription.getId().length()<1){//如果id为空表示是新增，则需要添加订单号的生成逻辑
            Calendar now = Calendar.getInstance();
            DecimalFormat decimalFormat = new DecimalFormat("00");
            DecimalFormat billnoprformat = new DecimalFormat("0000");
            String prefix = (prescription.getSource()==1||prescription.getSource()==3)?"X":(prescription.getSource()==2?"Y":"");
            String billno = prefix+new SimpleDateFormat("yy", Locale.CHINESE).format(Calendar.getInstance().getTime())+
                    decimalFormat.format((now.get(Calendar.MONTH) + 1))+
                    decimalFormat.format((now.get(Calendar.DAY_OF_MONTH)))+
                    decimalFormat.format(now.get(Calendar.HOUR_OF_DAY))+
                    decimalFormat.format(now.get(Calendar.MINUTE))+billnoprformat.format(Integer.parseInt(autoNoConfigService.getNoByCode("billnopr", 1)));
            prescription.setBillno(billno);
        }
        return prescriptionDao.save(prescription);
    }

    //TODO 后续将新增与修改进行业务拆分；
    @Transactional(propagation = Propagation.REQUIRED)
    public Prescription save(PrescriptionBean prescriptionBean) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Prescription prescription = new Prescription();
        if (prescriptionBean.getSource() == Constants.PRESCRIPTION_SOURCE_OFFLINE) {
            //根据VR室id查找出关联的医院
            VrRoom vrRoom = vrRoomService.getOne(prescriptionBean.getVrRoomId());
            if (vrRoom != null) {
                prescription.setHospitalId(vrRoom.getHospital_id());
            }
        }
        PropertyUtils.copyProperties(prescription, prescriptionBean);
        if(prescription.isNew()) {
            prescription.setStatus(Constants.PRESCRIPTION_STATUS_INIT);
            if(prescriptionBean.getSource() == Constants.PRESCRIPTION_SOURCE_OFFLINE2) {//如果是院内线下医生版本
                prescription.setPayStatus(Constants.PRESCRIPTION_PAY_STATUS_PAIDOFF);
                prescription.setHospitalId(prescriptionBean.getHospital()==null?"":prescriptionBean.getHospital());
            }else {
                prescription.setPayStatus(Constants.PRESCRIPTION_PAY_STATUS_UNPAY);
            }

        }

        if(prescriptionBean.getSource() == Constants.PRESCRIPTION_SOURCE_OFFLINE2){//如果是院内线下医生版本
            prescription.setPatientcaseId(prescriptionBean.getUserId());
        }else{
            prescription.setPatientId(prescriptionBean.getUserId());
        }
        //线上处方
        if(StringUtils.isNotBlank(prescriptionBean.getDoctorId())) {
            UserDoctor userDoctor = userDoctorService.getByUserId(prescriptionBean.getDoctorId());
            if(userDoctor != null) {
                User doctorUser = userService.findOne(userDoctor.getUserId());
                prescription.setDoctorName(doctorUser.getRealname());
                if (prescription.isNew()) {
                    userDoctor.setConsultationTimes(userDoctor.getConsultationTimes() + 1);
                }
                userDoctorService.save(userDoctor);
            }
        }

        if(!prescription.isNew()) {
            //删除处方内容
            List<PrescriptionContent> dbPrescriptionContentList = prescriptionContentService.findByPrescriptionId(prescription.getId());
            List<String> prescriptionContentIdList = ExtractUtil.extractToList(prescriptionBean.getPrescriptionContentList(), "id");
            for(PrescriptionContent dbPrescriptionContent : dbPrescriptionContentList) {
                if(!prescriptionContentIdList.contains(dbPrescriptionContent.getId())) {
                    prescriptionContentService.delete(dbPrescriptionContent.getId());
                }
            }
        }

        prescription = save(prescription);

        BigDecimal total = BigDecimal.ZERO;
        if (prescriptionBean.getPrescriptionContentList() != null && !prescriptionBean.getPrescriptionContentList().isEmpty()) {
            for (PrescriptionContent prescriptionContent : prescriptionBean.getPrescriptionContentList()) {
                Content content = contentService.getOne(prescriptionContent.getContentId());

                prescriptionContent.setPrescriptionId(prescription.getId());
                if (content.getPrice() == null) {
                    prescriptionContent.setUnitPrice(BigDecimal.ZERO);
                    prescriptionContent.setPrice(BigDecimal.ZERO);
                } else {
                    prescriptionContent.setUnitPrice(content.getPrice());
                    prescriptionContent.setPrice(content.getPrice().multiply(new BigDecimal(prescriptionContent.getTimes())));
                }
                prescriptionContent.setTimes(prescriptionContent.getFrequency() * prescriptionContent.getPeriod());
                prescriptionContent.setUseTimes(0);
                total =  total.add(prescriptionContent.getPrice());
            }
            prescriptionContentService.save(prescriptionBean.getPrescriptionContentList());
        }

        prescription.setTotal(total);
        save(prescription);


        return prescription;
    }

    public List<Prescription> findByPatientId(String patientId) {
        return prescriptionDao.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    public List<Prescription> findByPatientIdAndHidden(String patientId,Integer hidden) {
        return prescriptionDao.findByPatientIdAndHiddenOrderByCreatedAtDesc(patientId,hidden);
    }
    public List<Prescription> findByPatientIdAndHospitalIdAndHiddenOrSource(String patientId,String hospitalId,Integer hidden,Integer source) {
        return prescriptionDao.findByPatientIdAndHospitalIdAndHiddenOrSourceOrderByCreatedAtDesc(patientId,hospitalId,hidden,source);
    }

    /**
     * 根据患者（病案号）和对应的医院查找处方
     * @param patientcaseId
     * @param hospitalId
     * @param hidden
     * @return
     */
    public List<Prescription> findByPatientcaseIdAndHospitalIdAndHiddenOrderByCreatedAtDesc(String patientcaseId,String hospitalId,Integer hidden) {
        return prescriptionDao.findByPatientcaseIdAndHospitalIdAndHiddenOrderByCreatedAtDesc(patientcaseId,hospitalId,hidden);
    }


    public Prescription suspend(String prescriptionId) {
        Prescription prescription = getOne(prescriptionId);
        prescription.setStatus(Constants.PRESCRIPTION_STATUS_SUSPEND);
        save(prescription);

        List<PrescriptionContent> prescriptionContentList = prescriptionContentService.findByPrescriptionId(prescriptionId);
        for(PrescriptionContent prescriptionContent : prescriptionContentList) {
            if(prescriptionContent.getStatus() == Constants.PRESCRIPTION_CONTENT_STATUS_FINISH) {
                continue;
            }
            prescriptionContent.setStatus(Constants.PRESCRIPTION_CONTENT_STATUS_SUSPEND);
        }
        prescriptionContentService.save(prescriptionContentList);
        return prescription;
    }
}


