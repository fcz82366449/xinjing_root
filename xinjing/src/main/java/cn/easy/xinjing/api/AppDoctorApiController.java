package cn.easy.xinjing.api;

import cn.easy.base.bean.common.ApiResultBean;
import cn.easy.base.domain.User;
import cn.easy.base.utils.BaseUtils;
import cn.easy.base.utils.ExtractUtil;
import cn.easy.xinjing.aop.ApiAuth;
import cn.easy.xinjing.aop.ApiException;
import cn.easy.xinjing.api.base.XjBaseApiController;
import cn.easy.xinjing.bean.api.*;
import cn.easy.xinjing.domain.*;
import cn.easy.xinjing.service.*;
import cn.easy.xinjing.utils.Constants;
import cn.easy.xinjing.utils.JudgeUtity;
import cn.easy.xinjing.utils.ProjectUtil;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chenzhongyi on 16/9/12.
 */
@Api(value = "doctor-api-controller", description = "(线下)医生相关API", position = 5)
@Controller
@RequestMapping("/api/v1/appControlDoctor")
public class AppDoctorApiController extends XjBaseApiController {
    @Autowired
    private PrescriptionCaseService prescriptionCaseService;
    @Autowired
    private DoctorPatientcaseService doctorPatientcaseService;
    @Autowired
    private DiseaseTherapyService diseaseTherapyService;
    @Autowired
    private TherapyContentService therapyContentService;
    @Autowired
    private DiseaseContentService diseaseContentService;

    @Autowired
    private AppVersionService appVersionService;


    @ApiOperation(value = "登录", notes = "登录", position = 5)
    @ResponseBody
    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public ApiResultBean login(@RequestBody LoginBean loginBean, HttpServletRequest request) {
        return doLogin(loginBean, Constants.USER_TYPE_CONTROLDOCTOR_APP, request);
    }

    @ApiAuth
    @ApiOperation(value = "退出登录", notes = "退出登录", position = 32)
    @ResponseBody
    @RequestMapping(value = {"/logout"}, method = RequestMethod.POST)
    public ApiResultBean logout(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {
        doLogout(apiBaseBean);
        return toSuccess("退出登录成功");
    }

    @ApiAuth
    @ApiOperation(value = "获取病症列表", notes = "获取病症列表", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/diseaseList"}, method = RequestMethod.POST)
    public ApiResultBean disease(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {
        List<Disease> diseaseList = diseaseService.findByStatusAndHidden(Constants.DISEASE_STATUS_PUBLISH,0);
        List<DiseaseReturnBean> dislist = new ArrayList<DiseaseReturnBean>();
        DiseaseReturnBean diseaseReturnBean = null;
        for (Disease disease : diseaseList) {
            diseaseReturnBean = new DiseaseReturnBean();
            diseaseReturnBean.setDiseaseId(disease.getId());
            diseaseReturnBean.setDiseaseName(disease.getName());
            dislist.add(diseaseReturnBean);
        }

        return toSuccess("获取获取病症列表成功",dislist );
    }

    @ApiAuth
    @ApiOperation(value = "增加患者", notes = "增加患者", position = 9)
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = {"/addPatient"}, method = RequestMethod.POST)
    public ApiResultBean addPatient(@RequestBody PatientCaseAppBean patientCaseAppBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //根据登陆的帐号获取医院
        UserDoctor userDoctor = userDoctorService.getByUserId(getCurrentUserId());
        if(userDoctor==null){
            return toError("登陆用户类型不正确", -1);
        }
        if(userDoctor.getHospital()==null||userDoctor.getHospital()==""){
            return toError("当前登陆的医生没有对应的医院，无法新增患者，请联系管理员进行维护！", -2);
        }
        if(patientCaseAppBean.getClinichistoryNo()==null||patientCaseAppBean.getClinichistoryNo()==""){
            return toError("病历号不能为空", -2);
        }
        if(patientCaseAppBean.getClinichistoryNo().equals(patientCaseAppBean.getPhone())){
            return toError("病例号和手机号不能一样", -2);
        }

        Map<String, Object> searchParams = new HashedMap();
        searchParams.put("EQ_hidden",0);
        searchParams.put("EQ_recordno",patientCaseAppBean.getClinichistoryNo());
        searchParams.put("EQ_hospitalId",userDoctor.getHospital());
        PrescriptionCase presriptionCase  = prescriptionCaseService.findOne(searchParams);
        if (presriptionCase != null) {
            return toError("该【病案号】在当前医院下已存在，请勿重复增加", -2);
        }
        if(patientCaseAppBean.getPhone()!=null&&patientCaseAppBean.getPhone().trim().length()>=1){
            searchParams.remove("EQ_recordno");
            searchParams.put("EQ_mobile",patientCaseAppBean.getPhone());
            presriptionCase  = prescriptionCaseService.findOne(searchParams);
            if (presriptionCase != null) {
                return toError("该【手机号】在当前医院下已存在，请勿重复增加", -2);
            }
        }

        User dbUser = userService.getByUsername(patientCaseAppBean.getPhone());
        PrescriptionCase presriptionCaseSave = getPrescriptionCase(patientCaseAppBean,userDoctor);
        if(diseaseService.getOne(presriptionCaseSave.getIcd10())==null){
            return toError("病症不存在!", -2);
        }
        if (dbUser != null&&dbUser.getHidden()==0) {
            if( userDoctorService.getByUserId(dbUser.getId())!=null){
                return toError("该手机号为医生手机号，无法注册，请重新填写手机号",-2);
            }
            presriptionCaseSave.setUserId(dbUser.getId());
            PrescriptionCase prescriptionCase = prescriptionCaseService.save(presriptionCaseSave);
            if(prescriptionCase!=null){//如果保存成功则生成与医生对应"病案号患者表id"的关系表
                DoctorPatientcase doctorPatientcase = new DoctorPatientcase();
                doctorPatientcase.setDoctorId(getCurrentUserId());
                doctorPatientcase.setPrescriptioncaseId(prescriptionCase.getId());
                doctorPatientcase.setRemark(patientCaseAppBean.getRemark());
                doctorPatientcase.setHospitalId(userDoctor.getHospital());
                doctorPatientcaseService.save(doctorPatientcase);
            }
        }else{
            //构造user数据
            User user = new User();
            if(presriptionCaseSave.getMobile()!=null&&presriptionCaseSave.getMobile().trim().length()>=1){
                user.setUsername(presriptionCaseSave.getMobile().trim());
                user.setMobile(presriptionCaseSave.getMobile().trim());
                user.setRealname(presriptionCaseSave.getName());
                user.setEmail(presriptionCaseSave.getEmail());
                user.setGender(presriptionCaseSave.getGender());
                user = userService.save(user);
                if(user!=null){
                    presriptionCaseSave.setUserId(user.getId());
                    PrescriptionCase prescriptionCase = prescriptionCaseService.save(presriptionCaseSave);
                    if(prescriptionCase!=null){//如果保存成功则生成与医生对应"病案号患者表id"的关系表
                        DoctorPatientcase doctorPatientcase = new DoctorPatientcase();
                        doctorPatientcase.setDoctorId(getCurrentUserId());
                        doctorPatientcase.setPrescriptioncaseId(prescriptionCase.getId());
                        doctorPatientcase.setRemark(patientCaseAppBean.getRemark());
                        doctorPatientcase.setHospitalId(userDoctor.getHospital());
                        doctorPatientcaseService.save(doctorPatientcase);
                    }
                }
                if(user!=null&&user.getMobile()!=null&&user.getMobile().length()>=3){
                    //增加市场版本的患者信息表
                    UserPatient userPatient =  prescriptionCaseService.getUserPatient(patientCaseAppBean);
                    userPatient.setUserId(user.getId());
                    userPatientService.save(userPatient);
                }
            }else{
                presriptionCaseSave.setUserId("");
                PrescriptionCase prescriptionCase = prescriptionCaseService.save(presriptionCaseSave);
                if(prescriptionCase!=null){//如果保存成功则生成与医生对应"病案号患者表id"的关系表
                    DoctorPatientcase doctorPatientcase = new DoctorPatientcase();
                    doctorPatientcase.setDoctorId(getCurrentUserId());
                    doctorPatientcase.setPrescriptioncaseId(prescriptionCase.getId());
                    doctorPatientcase.setRemark(patientCaseAppBean.getRemark());
                    doctorPatientcase.setHospitalId(userDoctor.getHospital());
                    doctorPatientcaseService.save(doctorPatientcase);
                }
            }
        }
        return toSuccess("增加患者成功",presriptionCaseSave );
    }



    /**
     * 构造PrescriptionCase
     * @param patientCaseAppBean
     * @return
     */
    public PrescriptionCase getPrescriptionCase(PatientCaseAppBean patientCaseAppBean, UserDoctor userDoctor ){
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
        presc.setTs(new Date());
        return presc;
    }

    @ApiOperation(value = "找回密码", notes = "找回密码", position = 6)
    @ResponseBody
    @RequestMapping(value = {"/resetPassword"}, method = RequestMethod.POST)
    public ApiResultBean resetPassword(@RequestBody ResetPasswordBean resetPasswordBean, HttpServletRequest request) {
        return doResetPassword(resetPasswordBean, request);
    }

    @ApiAuth
    @ApiOperation(value = "修改密码", notes = "修改密码", position = 6)
    @ResponseBody
    @RequestMapping(value = {"/modifyPassword"}, method = RequestMethod.POST)
    public ApiResultBean modifyPassword(@RequestBody ModifyPasswordBean modifyPasswordBean, HttpServletRequest request) {
        return doModifyPassword(modifyPasswordBean, request);
    }


    @ApiAuth
    @ApiOperation(value = "获取我的患者", notes = "获取我的患者", position = 6)
    @ResponseBody
    @RequestMapping(value = {"/myPatients"}, method = RequestMethod.POST)
    public ApiResultBean myPatients(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) throws PinyinException {
        //根据登陆的帐号获取医院
        UserDoctor userDoctor = userDoctorService.getByUserId(getCurrentUserId());
        if(userDoctor==null){
            return toError("登陆用户类型不正确", -1);
        }
        if(userDoctor.getHospital()==null||userDoctor.getHospital()==""){
            return toError("当前登陆的医生没有对应的医院，无法新增患者，请联系管理员进行维护！", -2);
        }
        Map<String, Object> searchParams = new HashedMap();
        searchParams.put("EQ_doctorId",getCurrentUserId());
        searchParams.put("EQ_hospitalId",userDoctor.getHospital());
        List<DoctorPatientcase> doctorPatientcaseList = doctorPatientcaseService.search(searchParams,new Sort(Sort.Direction.DESC, "createdAt") );
        List< Map<String, Object>> mypatientslist = new ArrayList< Map<String, Object>>();
        for (DoctorPatientcase doctorPatientcase : doctorPatientcaseList) {
            Map<String, Object> data = new HashMap<>();
            PrescriptionCase prcase = prescriptionCaseService.getOne(doctorPatientcase.getPrescriptioncaseId());
            if(prcase==null){
                continue;
            }
            String name = prcase.getName();
            if(name==null||name.length()<1){
                return toError("患者数据中存在患者名称为空的数据，程序异常",-1);
            }
            String remark =doctorPatientcase.getRemark();
            String realnameFirstSpell = StringUtils.isEmpty(remark) ? name: remark;
            realnameFirstSpell = realnameFirstSpell.subSequence(0, 1).toString();
            if(JudgeUtity.isNumeric(realnameFirstSpell)){
                realnameFirstSpell="#";
            }else{
                realnameFirstSpell = PinyinHelper.getShortPinyin(realnameFirstSpell).toUpperCase();
                if(realnameFirstSpell.length()>=1){
                    realnameFirstSpell = realnameFirstSpell.subSequence(0, 1).toString();
                }else{
                    realnameFirstSpell="";
                }
            }
            data.put("id", doctorPatientcase.getPrescriptioncaseId());
            data.put("name",name);
            data.put("remark", doctorPatientcase.getRemark());
            data.put("realnameFirstSpell", realnameFirstSpell);
            mypatientslist.add(data);
        }
        return toSuccess("获取我的患者", mypatientslist);
    }


    @ApiAuth
    @ApiOperation(value = "获取患者详情信息", notes = "获取患者详情信息", position = 6)
    @ResponseBody
    @RequestMapping(value = {"/patientInformations"}, method = RequestMethod.POST)
    public ApiResultBean patientInformations(@RequestBody PatientAppBean patientAppBean, HttpServletRequest request) throws PinyinException {

        Map<String, Object> data = new HashMap<>();
        PrescriptionCase prcase = prescriptionCaseService.getOne(patientAppBean.getPatientId());

        PatientCaseAppBean patbean = getPatientCaseAppBean(prcase);
        if(prcase.getCreator()==null||getCurrentUserId()==null||(!prcase.getCreator().equals(getCurrentUserId()))){
            patbean.setCanModify(2);
        }else{
            patbean.setCanModify(1);
        }
        return toSuccess("获取患者详情信息成功", patbean);
    }

    /**
     * 构造
     * @param prcase
     * @return
     */
    public PatientCaseAppBean getPatientCaseAppBean(PrescriptionCase prcase ){
        PatientCaseAppBean patientCaseAppBean = new PatientCaseAppBean();
        patientCaseAppBean.setName(prcase.getName());
        patientCaseAppBean.setClinichistoryNo(prcase.getRecordno());
        patientCaseAppBean.setBirthday(prcase.getBornDate());
        patientCaseAppBean.setSex(prcase.getGender());
        patientCaseAppBean.setPhone(prcase.getMobile());
        patientCaseAppBean.setMaritalStatus(prcase.getMarriage()!=null?(prcase.getMarriage().equals("已婚")?1:(prcase.getMarriage().equals("未婚")?2:(prcase.getMarriage().equals("保密")?0:0))):0);

        patientCaseAppBean.setEducationDegree(prcase.getSchooling()!=null?(prcase.getSchooling().equals("文盲")?1:(prcase.getSchooling().equals("小学")?2:
                (prcase.getSchooling().equals("初中")?3:(prcase.getSchooling().equals("高中")?4:(prcase.getSchooling().equals("大学")?5:(prcase.getSchooling().equals("研究生及以上")?6:
                        0)))))):0);
        patientCaseAppBean.setMedicalInsuranceCardNo(prcase.getMedicareCardNumber()==null?"":prcase.getMedicareCardNumber());
        patientCaseAppBean.setDiseaseId(prcase.getIcd10());
        Map<String, Object> searchParams = new HashedMap();

        searchParams.put("EQ_doctorId",getCurrentUserId());
        searchParams.put("EQ_hospitalId",prcase.getHospitalId());
        searchParams.put("EQ_prescriptioncaseId",prcase.getId());
        List<DoctorPatientcase> doctorPatientcaseList = doctorPatientcaseService.search(searchParams,new Sort(Sort.Direction.DESC, "createdAt") );
        if(doctorPatientcaseList.size()==1){
            patientCaseAppBean.setRemark(doctorPatientcaseList.get(0).getRemark());//备注
        }

        patientCaseAppBean.setTs(prcase.getTs());
        if(prcase.getIcd10()!=null&&prcase.getIcd10().length()>=1){
            Disease disease = diseaseService.getOne(prcase.getIcd10());
            patientCaseAppBean.setDisease(disease==null?"":disease.getName());
        }



        return patientCaseAppBean;
    }



    @ApiAuth
    @ApiOperation(value = "修改患者基本信息接口", notes = "修改患者基本信息接口", position = 6)
    @ResponseBody
    @RequestMapping(value = {"/modifyPatientInformations"}, method = RequestMethod.POST)
    public ApiResultBean modifyPatientInformations(@RequestBody PatientCaseAppBean patientCaseAppBean, HttpServletRequest request) throws PinyinException {

        try {
            Date date = new Date();
            patientCaseAppBean.setTs(date);
            PrescriptionCase prescriptionCase =  prescriptionCaseService.update(patientCaseAppBean,getCurrentUserId());
            if(prescriptionCase!=null){
                Map<String, Object> data = new HashMap<>();
                data.put("ts", date);
                return toSuccess("修改成功", data);
            }else{
                return toError("修改失败null", -1);
            }
        } catch (Exception e) {
            return toError("修改失败"+e.getMessage(), -1);

        }



    }




    @ApiAuth
    @ApiOperation(value = "获取病症和病症对应的疗法接口", notes = "获取病症和病症对应的疗法接口", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/content/disease"}, method = RequestMethod.POST)
    public ApiResultBean contentDisease(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {
        //获得所有的疗法
        List<Therapy> therapyList = therapyService.findAll();
        List<Map<String, String>> therapyMapList = new ArrayList<Map<String, String>>();
        Map<String, String> therapyNameMap = new HashedMap();
        for (Therapy therapy : therapyList) {
            Map<String, String> therapyMap = new HashedMap();
            therapyMap.put("therapyId", therapy.getId());
            therapyMap.put("therapyName", therapy.getName());
            therapyNameMap.put(therapy.getId(), therapy.getName());
            therapyMapList.add(therapyMap);
        }
        //第一行,所有病症对应所有疗法
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        dataList.add(new HashedMap() {{
            put("diseaseId", "");
            put("diseaseName", "所有病症");
            put("therapiesArray", therapyMapList);
        }});
        //组装病种跟对应多个疗法的关联Map
        List<DiseaseTherapy> allDiseaseTherapies = diseaseTherapyService.findAll();
        String diseaseKey = "";
        List<Map<String, String>> existMapList = new ArrayList<>();
        List<Disease> allDiseases = diseaseService.findAll();
        Map<String, String> diseaseNameMap = ExtractUtil.extractToMap(allDiseases, "id", "name");
        Map<String, Map<String, Object>> reDiseaseTherapyMap = new HashedMap();
        for (int i = 0; i < allDiseaseTherapies.size(); i++) {
            DiseaseTherapy diseaseTherapy = allDiseaseTherapies.get(i);
            //diseaseKey变化或者最后一行
            if (!diseaseTherapy.getDiseaseId().equals(diseaseKey) || i == allDiseaseTherapies.size() - 1) {
                if (StringUtils.isNotEmpty(diseaseKey)) {
                    Map<String, Object> diseaseTherapyMap = new HashedMap();
                    diseaseTherapyMap.put("diseaseId", diseaseKey);
                    diseaseTherapyMap.put("diseaseName", diseaseNameMap.get(diseaseKey));
                    diseaseTherapyMap.put("therapiesArray", existMapList);
                    reDiseaseTherapyMap.put(diseaseKey, diseaseTherapyMap);
                }
                diseaseKey = diseaseTherapy.getDiseaseId();
                existMapList = new ArrayList<>();
            }
            Map<String, String> innerTherapyMap = new HashedMap() {{
                put("therapyId", diseaseTherapy.getTherapyId());
                put("therapyName", therapyNameMap.get(diseaseTherapy.getTherapyId()));
            }};
            existMapList.add(innerTherapyMap);
        }
        //处理最后一个
        Map<String, Object> diseaseTherapyMap = new HashedMap();
        diseaseTherapyMap.put("diseaseId", diseaseKey);
        diseaseTherapyMap.put("diseaseName", diseaseNameMap.get(diseaseKey));
        diseaseTherapyMap.put("therapiesArray", existMapList);
        reDiseaseTherapyMap.put(diseaseKey, diseaseTherapyMap);

        for (Disease disease : allDiseases) {
            Map<String, Object> resultMap = reDiseaseTherapyMap.get(disease.getId());
            if (resultMap == null) {
                resultMap = new HashedMap() {{
                    put("diseaseId", disease.getId());
                    put("diseaseName", disease.getName());
                    put("therapiesArray", new ArrayList<>());
                }};
            }
            dataList.add(resultMap);
        }
        return toSuccess("获取病种类型成功", dataList);
    }


    @ApiAuth
    @ApiOperation(value = "为患者开处方", notes = "为患者开处方", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/prescription/add"}, method = RequestMethod.POST)
    public ApiResultBean prescriptionAdd(@RequestBody PrescriptionBean prescriptionBean, HttpServletRequest request) {
        //根据登陆的帐号获取医院
        UserDoctor userDoctor = userDoctorService.getByUserId(getCurrentUserId());
        if(userDoctor==null){
            return toError("登陆用户类型不正确", -1);
        }
        if(userDoctor.getHospital()==null||userDoctor.getHospital()==""){
            return toError("当前登陆的医生没有对应的医院，无法新增患者，请联系管理员进行维护！", -2);
        }
//       Disease disease = diseaseService.getOne(prescriptionBean.getDisease());
//        if(disease==null){
//            return toError("病症不正确，请检查！", -2);
//        }
        Prescription prescription;
        try {
            prescriptionBean.setSource(Constants.PRESCRIPTION_SOURCE_OFFLINE2);
            prescriptionBean.setHospital(userDoctor.getHospital());
            prescriptionBean.setDoctorId(getCurrentUserId());
            prescription = prescriptionService.save(prescriptionBean);
            //如果该医生和患者没有关系则新增一条
            //更新备注
            DoctorPatientcase doctorPatientcase = doctorPatientcaseService.findByDoctorIdAndPrescriptioncaseId(getCurrentUserId(),prescriptionBean.getUserId());
            if(doctorPatientcase==null){
                doctorPatientcase = new DoctorPatientcase();
                doctorPatientcase.setDoctorId(getCurrentUserId());
                doctorPatientcase.setPrescriptioncaseId(prescriptionBean.getUserId());
                doctorPatientcase.setRemark("");
                doctorPatientcase.setHospitalId(userDoctor.getHospital());
                doctorPatientcaseService.save(doctorPatientcase);
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return toError("开处方失败", -2);
        }
        Map<String, Object> builmap = new HashMap<String, Object>();
        builmap.put("prescriptionId", prescription.getId());
        builmap.put("billno",prescription.getBillno());
        return toSuccess("开处方成功", builmap);
    }


    @ApiAuth
    @ApiOperation(value = "获取内容", notes = "获取内容", position = 7)
    @ResponseBody
    @RequestMapping(value = {"/content/search"}, method = RequestMethod.POST)
    public ApiResultBean<List<ContentSearchResultBean>> contentSearch(@RequestBody ContentSearchBean contentSearchBean, HttpServletRequest request) {
        Map<String, Object> searchParams = new HashMap<>();
        if (StringUtils.isNotBlank(contentSearchBean.getDiseaseId())) {
            searchParams.put("EQ_diseaseId", contentSearchBean.getDiseaseId());
        }
        if (StringUtils.isNotBlank(contentSearchBean.getTherapyId())) {
            searchParams.put("EQ_therapyId", contentSearchBean.getTherapyId());
        }
        if (contentSearchBean.getType() != null && contentSearchBean.getType() > 0) {
            searchParams.put("EQ_type", contentSearchBean.getType());
        }
        if (StringUtils.isNotBlank(contentSearchBean.getKeyword())) {
            searchParams.put("LIKE_name", contentSearchBean.getKeyword());
        }
        if (StringUtils.isNotBlank(contentSearchBean.getSortName())) {
            searchParams.put("SORT_name", contentSearchBean.getSortName());
            searchParams.put("SORT_order", contentSearchBean.getSortOrder());
        }

        Page<Content> page = contentService.findBySearchParams(searchParams, contentSearchBean.getPageBean());
        setConfigFieldValues(page, Constants.CONTENT_TYPE_KEY, "type");
        List<ContentSearchResultBean> resultBeanList = new ArrayList<>();
        if (page.getContent().size() > 0) {
            for (int i = 0; i < page.getContent().size(); i++) {
                Map<String, Object> map = (Map<String, Object>) page.getContent().get(i);
                ContentSearchResultBean resultBean = new ContentSearchResultBean();
                resultBean.setId(map.get("id").toString());
                resultBean.setContentType(Constants.CONTENT_TYPE_COMBO == (Integer) (map.get("type")) ? 0 : 1);
                resultBean.setType((Integer) (map.get("type")));
                resultBean.setTypeName(map.get("config_type").toString());
                resultBean.setPrice(new BigDecimal(map.get("price").toString()));
                resultBean.setName(map.get("name").toString());
                resultBean.setCoverPic(map.get("coverPic").toString());
                resultBean.setClicks((Integer) map.get("clicks"));
                resultBean.setDuration((Integer) map.get("duration"));

                ContentCollect contentCollect = contentCollectService.findByContentIdAndUserId(map.get("id").toString(), getCurrentUserId());
                resultBean.setIsCollected(contentCollect != null ? 1 : 0);

                resultBeanList.add(resultBean);
            }
        }
        return toSuccess("获取内容成功", resultBeanList);
    }

    @ApiAuth
    @ApiOperation(value = "获取单个内容", notes = "获取单个内容", position = 11)
    @ResponseBody
    @RequestMapping(value = {"/content/info"}, method = RequestMethod.POST)
    public ApiResultBean contentInfo(@RequestBody ContentIdBean contentIdBean, HttpServletRequest request) {
        Content content = contentService.getOne(contentIdBean.getContentId());
        content.setDescription("h5/myshow/appContent?contentid="+contentIdBean.getContentId());
        Map<String, Object> contentMap = ExtractUtil.object2Map(content);

        ContentCollect contentCollect = contentCollectService.findByContentIdAndUserId(contentIdBean.getContentId(), getCurrentUserId());
        contentMap.put("isCollected", contentCollect != null?"1":"0");
        //获得关联的疗法,病种
        List<TherapyContent> therapyContents = therapyContentService.findByContentId(content.getId());
        String therapyIds = org.apache.commons.lang.StringUtils.join(ExtractUtil.extractToList(therapyContents,"therapyId"),",");
        String  therapyNames = therapyService.findNamesByIds(therapyIds);
        List<DiseaseContent> diseaseContents = diseaseContentService.findByContentId(content.getId());
        String  diseaseIds = org.apache.commons.lang.StringUtils.join(ExtractUtil.extractToList(diseaseContents,"diseaseId"),",");
        String diseaseNames = diseaseService.findNamesByIds(diseaseIds);
        contentMap.put("typeName",content.getType()==1?"视频":(content.getType()==2?"音频":(content.getType()==3?"图片集":(content.getType()==4?"文章":(content.getType()==5?"电子表格":"")))));
        contentMap.put("disease", diseaseNames);
        contentMap.put("therapy", therapyNames);
        contentMap.remove("creator");
        contentMap.remove("hidden");
        contentMap.remove("remark");
        contentMap.remove("helpCode");
        contentMap.remove("isFree");
        contentMap.remove("updator");
        contentMap.remove("status");
        contentMap.remove("updatedAt");
        ContentExtBean contentExtBean =  contentService.getExt(content.getId(), content.getType());
        contentMap.put("ext", contentExtBean);
        return toSuccess("获取单个内容成功", contentMap);
    }

    @ApiAuth
    @ApiOperation(value = "收藏内容", notes = "收藏内容", position = 12)
    @ResponseBody
    @RequestMapping(value = {"/content/collect"}, method = RequestMethod.POST)
    public ApiResultBean contentCollect(@RequestBody ContentIdBean contentIdBean, HttpServletRequest request) {
        ContentCollect contentCollect = contentCollectService.findByContentIdAndUserId(contentIdBean.getContentId(), getCurrentUserId());
        if (contentCollect != null) {
            return toError("已经收藏该内容", -1);
        }
        contentCollect = new ContentCollect();
        contentCollect.setUserId(getCurrentUserId());
        contentCollect.setContentId(contentIdBean.getContentId());
        contentCollectService.save(contentCollect);
        return toSuccess("收藏内容成功");
    }

    @ApiAuth
    @ApiOperation(value = "取消收藏内容", notes = "取消收藏内容", position = 13)
    @ResponseBody
    @RequestMapping(value = {"/content/cancelCollect"}, method = RequestMethod.POST)
    public ApiResultBean contentCancelCollect(@RequestBody ContentIdBean contentIdBean, HttpServletRequest request) {
        ContentCollect contentCollect = contentCollectService.findByContentIdAndUserId(contentIdBean.getContentId(), getCurrentUserId());
        if (contentCollect != null) {
            contentCollectService.delete(contentCollect.getId());
        }
        return toSuccess("取消收藏内容成功");
    }


    @ApiAuth
    @ApiOperation(value = "获取收藏列表", notes = "获取收藏列表", position = 18)
    @ResponseBody
    @RequestMapping(value = {"/contentCollect/search"}, method = RequestMethod.POST)
    public ApiResultBean collectSearch(@RequestBody ApiBasePageBean apiBasePageBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("EQ_userId", getCurrentUserId());
        Page<ContentCollect> page = contentCollectService.search(searchParams, apiBasePageBean.getPageBean());
        List<String> idList = ExtractUtil.extractToList(page.getContent(), "contentId");

        List<Content> contentList = contentService.findAll(idList);
        List<ContentIsCollected> contentIsCollecteds = new ArrayList<ContentIsCollected>();
        ContentIsCollected contentIsCollected = null;
        for (int i = 0; i < contentList.size(); i++) {
            Content content = contentList.get(i);
            contentIsCollected = new ContentIsCollected();
            try {
                PropertyUtils.copyProperties(contentIsCollected, content);
                contentIsCollected.setIsCollected(1);
                contentIsCollecteds.add(contentIsCollected);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }


        return toSuccess("获取收藏列表成功", contentIsCollecteds);
    }

    @ApiAuth
    @ApiOperation(value = "获取历史处方", notes = "获取历史处方", position = 33)
    @ResponseBody
    @RequestMapping(value = {"/historicalPrescriptions"}, method = RequestMethod.POST)
    public ApiResultBean historicalPrescriptions(@RequestBody PrescriptionSearchBean prescriptionSearchBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //根据登陆的帐号获取医院
        UserDoctor userDoctor = userDoctorService.getByUserId(getCurrentUserId());
        if(userDoctor==null){
            return toError("登陆用户类型不正确", -1);
        }
        if(userDoctor.getHospital()==null||userDoctor.getHospital()==""){
            return toError("当前登陆的医生没有对应的医院，无法新增患者，请联系管理员进行维护！", -2);
        }
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("EQ_patientcaseId", prescriptionSearchBean.getPatientId());
        searchParams.put("EQ_doctorId", userDoctor.getUserId());
        searchParams.put("EQ_hidden", 0);

        Page<Prescription> page = prescriptionService.search(searchParams, prescriptionSearchBean.getPageBean());
        List<PrescriptionReturnBean> prescriptionReturnBeanList = new ArrayList<PrescriptionReturnBean>();
        PrescriptionReturnBean prescriptionReturnBean = null;
        if (page.getContent().size() > 0) {
            for (int i = 0; i < page.getContent().size(); i++) {
                Prescription prescription = page.getContent().get(i);
                prescriptionReturnBean =  new PrescriptionReturnBean();
                PropertyUtils.copyProperties(prescriptionReturnBean, prescription);
                List<PrescriptionContent> prescriptionContentList = prescriptionContentService.findByPrescriptionId(prescription.getId());
                List<Content> contentList = contentService.findAll(ExtractUtil.extractToList(prescriptionContentList, "contentId"));
                prescriptionReturnBean.setContents(contentList);
                prescriptionReturnBeanList.add(prescriptionReturnBean);
            }
        }


        return toSuccess("获取处方列表成功", prescriptionReturnBeanList);
    }


    @ApiAuth
    @ApiOperation(value = "获取单个处方", notes = "获取单个处方", position = 16)
    @ResponseBody
    @RequestMapping(value = {"/prescription/info"}, method = RequestMethod.POST)
    public ApiResultBean prescriptionInfo(@RequestBody PrescriptionIdBean prescriptionIdBean, HttpServletRequest request) {
        Prescription prescription = prescriptionService.getOne(prescriptionIdBean.getPrescriptionId());
        prescription.setPatientId(prescription.getPatientcaseId()==null?"":prescription.getPatientcaseId());
        Map<String, Object> data = ExtractUtil.object2Map(prescription);

        List<PrescriptionContent> prescriptionContentList = prescriptionContentService.findByPrescriptionId(prescription.getId());
        List<Content> contentList = contentService.findAll(ExtractUtil.extractToList(prescriptionContentList, "contentId"));
        Map<String, Content> contentMap = ExtractUtil.extractToMap(contentList, "id");

        List<ContentBean> contentBeanList = new ArrayList<>();
        for (PrescriptionContent prescriptionContent : prescriptionContentList) {
            Content content = contentMap.get(prescriptionContent.getContentId());
            if (content == null) {
                continue;
            }
            ContentBean contentBean = new ContentBean();
            contentBean.setId(content.getId());
            contentBean.setHelpCode(content.getHelpCode());//记助吗
            contentBean.setName(content.getName());//名称
            contentBean.setType(content.getType());//类型
            contentBean.setIsFree(content.getIsFree());//是否免费
            contentBean.setPrice(content.getPrice());//价格
            contentBean.setDescription(content.getDescription());//
            contentBean.setRemark(content.getRemark());//备注
            contentBean.setStatus(content.getStatus());//状态
            contentBean.setCoverPic(content.getCoverPic());//封面
            contentBean.setHidden(content.getHidden());//是否隐藏
            contentBean.setClicks(content.getClicks());//点击量
            contentBean.setDuration(content.getDuration());//时长
            contentBean.setFrequency(prescriptionContent.getFrequency());//频率
            contentBean.setPeriod(prescriptionContent.getPeriod());//
            contentBean.setPeriodUnit(prescriptionContent.getPeriodUnit());
            contentBean.setTimes(prescriptionContent.getTimes());
            contentBean.setUseTimes(prescriptionContent.getUseTimes());
            contentBeanList.add(contentBean);
        }
        data.put("contents", contentBeanList);

//        try {
//            PatientBean patientBean = userPatientService.getPatientBean(prescription.getPatientId());
//            data.put("patient", patientBean);
//        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
//            e.printStackTrace();
//            return toSuccess("获取单个处方失败", -1);
//        }

        return toSuccess("获取单个处方成功", data);
    }


    @ApiAuth
    @ApiOperation(value = "根据条件查询患者信息(病案号或手机号)", notes = "根据条件查询患者信息(病案号或手机号)", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/getPatientByKeywordCase"}, method = RequestMethod.POST)
    public ApiResultBean getPatientByKeywordCase(@RequestBody KeywordBean keywordBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if(keywordBean.getKeyword()==null||keywordBean.getKeyword().trim().length()<1){
            return toError("关键词不能为空！", -1);
        }
        UserDoctor userDoctor = userDoctorService.getByUserId(getCurrentUserId());
        if(userDoctor==null){
            return toError("登陆用户类型不正确", -1);
        }
        if(userDoctor.getHospital()==null||userDoctor.getHospital()==""){
            return toError("当前登陆的医生没有对应的医院，无法新增患者，请联系管理员进行维护！", -2);
        }
        List<PrescriptionCase> prescriptionCaseList = prescriptionCaseService.findByPrescriptionCase(userDoctor.getHospital(),0,keywordBean.getKeyword());
        if(prescriptionCaseList.size()==1){
            PatientAppReturnBean patientBean = new PatientAppReturnBean();
            PropertyUtils.copyProperties(patientBean, prescriptionCaseList.get(0));
            patientBean.setRealname(prescriptionCaseList.get(0).getName());

            patientBean.setUserId(prescriptionCaseList.get(0).getId());


            return toSuccess("根据条件查询患者信息成功", patientBean);
        }

        return toError("找不到匹配的患者信息", -1);
    }


    @ApiAuth
    @ApiOperation(value = "获取版本信息", notes = "获取版本信息", position = 1)
    @ResponseBody
    @RequestMapping(value = {"/getVersion"}, method = RequestMethod.POST)
    public ApiResultBean<AppVersion> getVersion(@RequestBody VersionBean versionBean) {
        String appCode = "";
        if (Constants.SYSTEM_VERSION_IOS.equals(versionBean.getSystemVersion())) {
            appCode = Constants.VR_CONTROL_DOCTORIOS;
        } else {
            appCode = Constants.VR_CONTROL_DOCTORANDROID;
        }
        AppVersion appVersion = appVersionService.getByAppCode(appCode);
        return toSuccess("获取成功", appVersion);
    }
}
