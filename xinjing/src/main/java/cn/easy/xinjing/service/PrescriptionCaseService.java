package cn.easy.xinjing.service;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.*;

import cn.easy.base.domain.User;
import cn.easy.base.service.BaseService;
import cn.easy.base.service.UserService;
import cn.easy.base.utils.jdbc.PaginationHelper;
import cn.easy.xinjing.bean.api.PatientCaseAppBean;
import cn.easy.xinjing.domain.DoctorPatientcase;
import cn.easy.xinjing.domain.UserDoctor;
import cn.easy.xinjing.domain.UserPatient;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.easy.base.bean.PageBean;
import cn.easy.xinjing.domain.PrescriptionCase;
import cn.easy.xinjing.repository.PrescriptionCaseDao;

@Component
public class PrescriptionCaseService extends BaseService<PrescriptionCase> {
    @Autowired
    private PrescriptionCaseDao	prescriptionCaseDao;
    @Autowired
    private UserDoctorService	userDoctorService;
    @Autowired
    private PrescriptionCaseService	prescriptionCaseService;
    @Autowired
    private DoctorPatientcaseService doctorPatientcaseService;
    @Autowired
    private DiseaseService diseaseService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserPatientService userPatientService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * 自定义查询
     * @param searchParams
     * @param pageBean
     * @return
     */
    public Page<PrescriptionCase> searchto(Map<String, Object> searchParams, PageBean pageBean) {

        List<Object> args = new ArrayList<>();
        StringBuilder sql = new StringBuilder(" select * from xj_prescription_case a where a.hidden=0 ");

        if (isValidParam(searchParams, "LIKE_name")) {
            sql.append(" AND (NAME LIKE '%"+searchParams.get("LIKE_name")+"%' OR recordno LIKE '%"+searchParams.get("LIKE_name")+"%') ");
        }
        if (isValidParam(searchParams, "SORT_name")) {
            sql.append("order by a." + searchParams.get("SORT_name") + " " + searchParams.get("SORT_order"));
        } else {
            sql.append("order by a.created_at desc");
        }


        PaginationHelper<PrescriptionCase> helper = new PaginationHelper<>();
        return helper.fetchPage(jdbcTemplate, sql.toString(), args.toArray(), pageBean, (rs, i) -> {
            PrescriptionCase bean = new PrescriptionCase();
            bean.setId((rs.getString("id")));
            bean.setRecordno(rs.getString("recordno"));//病案号
            bean.setHospitalId(rs.getString("hospital_id"));//医院id
            bean.setAge(rs.getInt("age"));//年龄
            bean.setSchooling(rs.getString("schooling"));//学历
            bean.setGender(rs.getInt("gender"));//患者性别
            bean.setName(rs.getString("name"));//患者姓名
            bean.setMarriage(rs.getString("marriage"));//婚姻状态
            bean.setCreator(rs.getString("creator"));//创建者id
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = format.parse(rs.getString("created_at"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            bean.setCreatedAt(date);
            date = null;
            try {
                date = format.parse(rs.getString("updated_at"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            bean.setUpdatedAt(date);
            bean.setUpdator(rs.getString("updator"));
            return bean;
        });


    }

    private boolean isValidParam(Map<String, Object> searchParams, String key) {
        return searchParams.containsKey(key) && StringUtils.isNotBlank(searchParams.get(key).toString());
    }

    public Page<PrescriptionCase> search(Map<String, Object> searchParams, PageBean pageBean) {
        return prescriptionCaseDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }
    public PrescriptionCase findOne(Map<String, Object> searchParams) {
        return prescriptionCaseDao.findOne(spec(searchParams));
    }
    public List<PrescriptionCase> search(Map<String, Object> searchParams, Sort... sort) {
        return prescriptionCaseDao.findAll(spec(searchParams), sort == null || sort.length == 0  ? new Sort(Direction.DESC, "createdAt") : sort[0]);
    }
    public List<PrescriptionCase> findByPrescriptionCase(String hospitalId, Integer hidden, String keyword) {
        return prescriptionCaseDao.findByPrescriptionCase(hospitalId,hidden,keyword);//spec(searchParams), sort == null || sort.length == 0  ? new Sort(Direction.DESC, "createdAt") : sort[0]);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        prescriptionCaseDao.delete(id);
    }

    public PrescriptionCase getOne(String id) {
        return prescriptionCaseDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public PrescriptionCase save(PrescriptionCase prescriptionCase) {
        return prescriptionCaseDao.save(prescriptionCase);
    }
    public PrescriptionCase findByUseridAndHidden(String userid,int hidden) {
        return prescriptionCaseDao.findByUserIdAndHidden(userid,hidden);
    }

    public PrescriptionCase findByUserIdAndHospitalIdAndHidden(String userid,String hospitalId,int hidden) {
        return prescriptionCaseDao.findByUserIdAndHospitalIdAndHidden(userid,hospitalId,hidden);

    }


    @Transactional(propagation = Propagation.REQUIRED)
    public PrescriptionCase update(PatientCaseAppBean patientCaseAppBean, String CurrentUserId)throws Exception {

        //根据登陆的帐号获取医院
        UserDoctor userDoctor = userDoctorService.getByUserId(CurrentUserId);
        if(userDoctor==null){
            throw  new Exception("登陆用户类型不正确");
        }
        if(userDoctor.getHospital()==null||userDoctor.getHospital()==""){
            throw new Exception("当前登陆的医生没有对应的医院，无法新增患者，请联系管理员进行维护！");
        }
        if(patientCaseAppBean.getClinichistoryNo()==null||patientCaseAppBean.getClinichistoryNo()==""){
            throw new Exception("病历号不能为空");
        }
        if(patientCaseAppBean.getClinichistoryNo().equals(patientCaseAppBean.getPhone())){
            throw  new Exception("病例号和手机号不能一样");
        }
        //1.检测要修改的患者id是否存在
        PrescriptionCase prescriptionCase = prescriptionCaseService.getOne(patientCaseAppBean.getPatientId());
        if(prescriptionCase==null){
            throw new Exception("该患者不存在，请检查!");
        }
        if(!prescriptionCase.getCreator().equals(CurrentUserId)){
            //更新备注
            DoctorPatientcase doctorPatientcase = doctorPatientcaseService.findByDoctorIdAndPrescriptioncaseId(CurrentUserId,patientCaseAppBean.getPatientId());
            if(doctorPatientcase==null){
                doctorPatientcase = new DoctorPatientcase();
                doctorPatientcase.setDoctorId(CurrentUserId);
                doctorPatientcase.setPrescriptioncaseId(prescriptionCase.getId());
                doctorPatientcase.setRemark(patientCaseAppBean.getRemark());
                doctorPatientcase.setHospitalId(userDoctor.getHospital());
                doctorPatientcaseService.save(doctorPatientcase);
            }else{
                doctorPatientcase.setRemark(patientCaseAppBean.getRemark());
                doctorPatientcaseService.save(doctorPatientcase);
            }

             return prescriptionCase;
        }
        Map<String, Object> searchParams = new HashedMap();
        searchParams.put("EQ_hidden",0);
        searchParams.put("EQ_recordno",patientCaseAppBean.getClinichistoryNo());
        searchParams.put("EQ_hospitalId",userDoctor.getHospital());
        PrescriptionCase presriptionCase  = prescriptionCaseService.findOne(searchParams);
        if(!prescriptionCase.getRecordno().equals(patientCaseAppBean.getClinichistoryNo())){
            if (presriptionCase != null) {
                throw  new Exception("该【病案号】在当前医院下已存在，请勿重复增加");
            }
        }
        //2.检测时间戳，来判断是否已经被修改过
        if(patientCaseAppBean.getTs()==null){
            throw new Exception("参数ts不能为空!");
        }
        if(prescriptionCase.getTs().getTime()>patientCaseAppBean.getTs().getTime()){
            throw new Exception("该患者已被他人修改，请重新刷新详情信息，再修改！");
        }
        //被修改后的数据
        DoctorPatientcase doctorPatientcase = new DoctorPatientcase();
        //获取修改的数据
        PrescriptionCase presriptionCaseSave = getPrescriptionCaseUpdate(patientCaseAppBean,userDoctor);
        if(diseaseService.getOne(presriptionCaseSave.getIcd10())==null){
            throw  new Exception("病症不存在!");
        }
        //3.修改患者基本信息,1.检测是否修改手机号
        String moblie = prescriptionCase.getMobile()==null?"":prescriptionCase.getMobile();
        String phone = patientCaseAppBean.getPhone()==null?"":patientCaseAppBean.getPhone();
        PrescriptionCase prescriptionCasesave = null;
        if(!moblie.equals(phone)){
            if(patientCaseAppBean.getPhone()!=null&&patientCaseAppBean.getPhone().trim().length()>=1){
                searchParams.remove("EQ_recordno");
                searchParams.put("EQ_mobile",patientCaseAppBean.getPhone());
                presriptionCase  = prescriptionCaseService.findOne(searchParams);
                if (presriptionCase != null) {
                    throw new Exception("该【手机号】在当前医院下已存在，请勿重复增加");
                }
                User dbUser = userService.getByUsername(patientCaseAppBean.getPhone());
                String userid = "";
                if(dbUser==null){
                    //new user()
                    User user = new User();
                    user.setUsername(presriptionCaseSave.getMobile().trim());
                    user.setMobile(presriptionCaseSave.getMobile().trim());
                    user.setRealname(presriptionCaseSave.getName());
                    user.setEmail(presriptionCaseSave.getEmail());
                    user.setGender(presriptionCaseSave.getGender());
                    user = userService.save(user);
                    userid = user.getId();
                    //增加市场版本的患者信息表
                    UserPatient userPatient =  getUserPatient(patientCaseAppBean);
                    userPatient.setUserId(user.getId());
                    userPatientService.save(userPatient);
                }else{
                    if( userDoctorService.getByUserId(dbUser.getId())!=null){
                        throw  new Exception("该手机号为医生手机号，无法注册，请重新填写手机号");
                    }
                    userid = dbUser.getId();
                }
                presriptionCaseSave.setUserId(userid);
                prescriptionCasesave = prescriptionCaseService.save(presriptionCaseSave);
                if(prescriptionCasesave!=null){//如果保存成功就更新备注
                    doctorPatientcase = doctorPatientcaseService.findByDoctorIdAndPrescriptioncaseId(CurrentUserId,prescriptionCasesave.getId());
                    doctorPatientcase.setRemark(patientCaseAppBean.getRemark());
                    doctorPatientcaseService.save(doctorPatientcase);
                }
            }else{
                presriptionCaseSave.setUserId("");
                //修改患者信息，userid为空
                prescriptionCasesave = prescriptionCaseService.save(presriptionCaseSave);
                if(prescriptionCasesave!=null){//如果保存成功就更新备注
                    doctorPatientcase = doctorPatientcaseService.findByDoctorIdAndPrescriptioncaseId(CurrentUserId,prescriptionCasesave.getId());
                    doctorPatientcase.setRemark(patientCaseAppBean.getRemark());
                    doctorPatientcaseService.save(doctorPatientcase);
                }
            }
        }else{
            presriptionCaseSave.setUserId(prescriptionCase.getUserId());
            //修改患者信息，userid为空
            prescriptionCasesave = prescriptionCaseService.save(presriptionCaseSave);
            if(prescriptionCasesave!=null){//如果保存成功就更新备注
                doctorPatientcase = doctorPatientcaseService.findByDoctorIdAndPrescriptioncaseId(CurrentUserId,prescriptionCasesave.getId());
                doctorPatientcase.setRemark(patientCaseAppBean.getRemark());
                doctorPatientcaseService.save(doctorPatientcase);
            }
        }
        return  prescriptionCasesave;
    }
    /**
     * 构造UserPatient
     * @param patientCaseAppBean
     * @return
     */
    public UserPatient getUserPatient(PatientCaseAppBean patientCaseAppBean){
        UserPatient userPatient = new UserPatient();
        userPatient.setMedicareCardNumber(patientCaseAppBean.getMedicalInsuranceCardNo());
        return userPatient;
    }
    /**
     * 构造修改后的数据
     * @param patientCaseAppBean
     * @param userDoctor
     * @return
     */
    private PrescriptionCase getPrescriptionCaseUpdate(PatientCaseAppBean patientCaseAppBean, UserDoctor userDoctor) {
        PrescriptionCase presc = new PrescriptionCase();
        presc.setName(patientCaseAppBean.getName());
        presc.setRecordno(patientCaseAppBean.getClinichistoryNo());
        presc.setBornDate(patientCaseAppBean.getBirthday());
        presc.setGender(patientCaseAppBean.getSex());
        presc.setMobile(patientCaseAppBean.getPhone());
        presc.setEmergencyContactPhone(patientCaseAppBean.getPhone());
        presc.setEmergencyContact(patientCaseAppBean.getPhone());
        presc.setHospitalId(userDoctor.getHospital());
        presc.setMarriage(patientCaseAppBean.getMaritalStatus()!=null?(patientCaseAppBean.getMaritalStatus()==1?"已婚":(patientCaseAppBean.getMaritalStatus()==2?"未婚":(patientCaseAppBean.getMaritalStatus()==0?"保密":""))):"");
        presc.setSchooling(patientCaseAppBean.getEducationDegree()!=null?(patientCaseAppBean.getEducationDegree()==1?"文盲":(patientCaseAppBean.getEducationDegree()==2?"小学":
                (patientCaseAppBean.getEducationDegree()==3?"初中":(patientCaseAppBean.getEducationDegree()==4?"高中":
                        (patientCaseAppBean.getEducationDegree()==5?"大学":(patientCaseAppBean.getEducationDegree()==6?"研究生及以上":"")))))):"");
        presc.setMedicareCardNumber(patientCaseAppBean.getMedicalInsuranceCardNo()==null?"":patientCaseAppBean.getMedicalInsuranceCardNo());
        presc.setIcd10(patientCaseAppBean.getDiseaseId());
        presc.setId(patientCaseAppBean.getPatientId());
        presc.setTs(patientCaseAppBean.getTs());
        return presc;
    }


}


