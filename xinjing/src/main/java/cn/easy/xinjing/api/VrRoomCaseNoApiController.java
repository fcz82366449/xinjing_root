package cn.easy.xinjing.api;

import cn.easy.base.api.BaseApiController;
import cn.easy.base.bean.PageBean;
import cn.easy.base.bean.common.ApiResultBean;
import cn.easy.base.domain.User;
import cn.easy.base.service.UserService;
import cn.easy.base.utils.*;
import cn.easy.xinjing.aop.ApiAuth;
import cn.easy.xinjing.bean.api.*;
import cn.easy.xinjing.domain.*;
import cn.easy.xinjing.service.*;
import cn.easy.xinjing.utils.Constants;
import cn.easy.xinjing.utils.ProjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.utils.text.TextValidator;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by chenzhongyi on 2016/11/18.
 */
@Api(value = "VrRoomCaseNo-api-controller", description = "（病案号版本）VR室相关API", position = 5)
@Controller
@RequestMapping("/api/v1/VrRoomCaseNo")
public class VrRoomCaseNoApiController extends BaseApiController {
    @Autowired
    private PcVersionService pcVersionService;
    @Autowired
    private DiseaseService diseaseService;
    @Autowired
    private TherapyService therapyService;
    @Autowired
    private ContentService contentService;
    @Autowired
    private UserService userService;
    @Autowired
    private AppTokenService appTokenService;
    @Autowired
    private UserPatientService userPatientService;
    @Autowired
    private PrescriptionService prescriptionService;
    @Autowired
    private PrescriptionContentService prescriptionContentService;

    @Autowired
    private UserVrRoomService userVrRoomService;
    @Autowired
    private VrRoomAppTaskService vrRoomAppTaskService;
    @Autowired
    private VrRoomService vrRoomService;

    @Autowired
    private PrescriptionCaseService prescriptionCaseService;

    @Autowired
    private GameEfsService gameEfsService;
    @Autowired
    private PrescriptionContentFeedbackService prescriptionContentFeedbackService;
    @Autowired
    private VoideEfsService voideEfsService;
    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private GameitemRecord01Service gameitemRecord01Service;
    @Autowired
    private GameitemRecord02Service gameitemRecord02Service;
    @Autowired
    private GameitemRecord03Service gameitemRecord03Service;
    @Autowired
    private GameitemRecord04Service gameitemRecord04Service;
    @Autowired
    private GameitemRecord05Service gameitemRecord05Service;

    @Autowired
    private ClickRecordService clickRecordService;
    @Autowired
    private SingleselService singleselService;
    @Autowired
    private SingleselRecordService singleselRecordService;
    @Autowired
    private EvaluatingService evaluatingService;
    @Autowired
    protected UserDoctorService userDoctorService;
    @Autowired
    protected GameheadRecordService gameheadRecordService;




    @ApiOperation(value = "登录", notes = "登录", position = 5)
    @ResponseBody
    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public ApiResultBean<VrRoomLoginResultBean> login(@RequestBody LoginVRMCBean loginBean, HttpServletRequest request) {
        User user = userService.getByUsername(loginBean.getUsername());
        if (user == null) {
            return toError("用户未注册", -3);
        }

        if (!BaseUtils.matchesPassword(loginBean.getPassword(), user.getPassword())) {
            return toError("密码不正确", -2);
        }

        UserVrRoom userVrRoom = userVrRoomService.getByUserId(user.getId());
        if (userVrRoom == null) {
            return toError("账号类型不正确", -1);
        }
        if (userVrRoom.getType() != Constants.USER_VR_ROOM_TYPE_DESKTOP) {
            return toError("VR室账号类型不正确", -1);
        }


//        Map<String, Object> searchParams = new HashedMap();
//        searchParams.put("EQ_ip",loginBean.getIp());
//        searchParams.put("EQ_mac",loginBean.getMac());
//        searchParams.put("EQ_vruser",userVrRoom.getId());
//        searchParams.put("EQ_hidden",0);
//        List<GameEfs> gameefslist = gameEfsService.search(searchParams);
//        if(gameefslist.size()<1){
//            return toError("登陆异常",-4);
//        }

        //设置app auth信息
        AppToken appToken = new AppToken();
        appToken.setChannel(loginBean.getChannel());
        appToken.setAppId(loginBean.getAppId());
        appToken.setAppVersion(loginBean.getAppVersion());
        appToken.setDeviceModel(loginBean.getDeviceModel());
        appToken.setDeviceSystem(loginBean.getDeviceSystem());
        appToken.setDeviceVersion(loginBean.getDeviceVersion());

        appToken.setCreatedAt(new Date());
        appToken.setUserId(user.getId());
        appToken.setUserType(Constants.USER_TYPE_APP_VR_ROOM_CASE);
        appToken = appTokenService.save(appToken);

        VrRoomLoginResultBean resultBean = new VrRoomLoginResultBean();
        resultBean.setUserId(user.getId());
        resultBean.setUsername(user.getUsername());
        resultBean.setRealname(user.getRealname());
        resultBean.setToken(appToken.getId());
        resultBean.setVrRoomId(userVrRoom.getVrRoomId());

        VrRoom vrRoom = vrRoomService.getOne(userVrRoom.getVrRoomId());
        if (vrRoom != null) {
            resultBean.setVrRoomName(vrRoom.getName());

            if(vrRoom.getHospital_id()!=null){
                Hospital hospital =  hospitalService.getOne(vrRoom.getHospital_id());
                if(hospital!=null){
                    resultBean.setHospitalName(hospital.getName());
                    resultBean.setHospitalId(hospital.getId());
                }
            }
        }

        user.setLastLoginAt(DateTimeUtil.toTimestamp(new Date()));
        userService.save(user);

        return toSuccess("登录成功", resultBean);
    }

    @ApiAuth
    @ApiOperation(value = "退出登录", notes = "退出登录", position = 32)
    @ResponseBody
    @RequestMapping(value = {"/logout"}, method = RequestMethod.POST)
    public ApiResultBean logout(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {
        AppToken appToken = appTokenService.getOne(apiBaseBean.getToken());
        if (appToken != null) {
            appToken.setStatus(-1);
            appToken.setLogoutAt(new Date());
            appTokenService.save(appToken);
        }
        return toSuccess("退出登录成功");
    }

    @ApiAuth
    @ApiOperation(value = "修改密码", notes = "修改密码", position = 4)
    @ResponseBody
    @RequestMapping(value = {"/modifyPassword"}, method = RequestMethod.POST)
    public ApiResultBean modifyPassword(@RequestBody ModifyPassword4VrBean modifyPasswordBean, HttpServletRequest request) {
        User dbUser = userService.getOne(getCurrentUserId());

        if (!BaseUtils.matchesPassword(modifyPasswordBean.getOldPassword(), dbUser.getPassword())) {
            return toError("当前密码不正确", -1);
        }

        dbUser.setPassword(BaseUtils.encodePassword(modifyPasswordBean.getNewPassword()));
        userService.save(dbUser);

        return toSuccess("修改密码成功");
    }

    @ApiAuth
    @ApiOperation(value = "获取字典值", notes = "获取字典值", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/config"}, method = RequestMethod.POST)
    public ApiResultBean config(@RequestBody ConfigCodeBean configCodeBean, HttpServletRequest request) {
        return toSuccess("获取字典值成功", ProjectUtil.getConfigBeanList(configCodeBean.getCode()));
    }



    @ApiAuth
    @ApiOperation(value = "根据条件查询患者信息", notes = "根据条件查询患者信息", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/getPatientCase"}, method = RequestMethod.POST)
    public ApiResultBean getPatientByKeyword(@RequestBody PatientCaseBean patientCaseBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<String, Object> searchParams = new HashedMap();
        if(patientCaseBean.getName()!=null&&patientCaseBean.getName().length()>=1){
            searchParams.put("LIKE_name",patientCaseBean.getName());
        }
//
//
// if(patientCaseBean.getIcd10()!=null&&patientCaseBean.getIcd10().length()>=1){
//            searchParams.put("EQ_icd10",patientCaseBean.getIcd10());
//        }if(patientCaseBean.getRecordno()!=null&&patientCaseBean.getRecordno().length()>=1){
//            searchParams.put("LIKE_recordno",patientCaseBean.getRecordno());
//        }
        //根据登陆的帐号获取医院
        UserVrRoom userVrRoom = userVrRoomService.getByUserId(getCurrentUserId());
        if(userVrRoom==null){
            return toError("登陆用户类型不正确", -1);
        }
        VrRoom vrRoom = vrRoomService.getOne(userVrRoom.getVrRoomId());
        if(vrRoom==null){
            return toError("登陆的用户没有维护VR室", -1);
        }
        if(vrRoom.getHospital_id()!=null&&vrRoom.getHospital_id().length()>=1){
            searchParams.put("EQ_hospitalId",vrRoom.getHospital_id());
        }else{
            return toError("医院字段不能为空", -1);
        }

        searchParams.put("EQ_hidden",patientCaseBean.getHidden());
        PageBean pageBean = patientCaseBean.getPageBean();
        pageBean.setTotalPages(21);
        Page<PrescriptionCase>  prescriptionCasesPage =  prescriptionCaseService.searchto(searchParams,pageBean);
//        if(prescriptionCasesPage==null||prescriptionCasesPage.getContent().size()<1){
//            searchParams.remove("LIKE_recordno");
//            if(patientCaseBean.getRecordno()!=null||patientCaseBean.getRecordno().length()>=1){
//                searchParams.put("LIKE_recordno",patientCaseBean.getRecordno());
//            }
//            prescriptionCasesPage =  prescriptionCaseService.search(searchParams,pageBean);
//
//        }
        if(prescriptionCasesPage!=null&&prescriptionCasesPage.getContent().size()>=1){
            for (PrescriptionCase prescriptionCase : prescriptionCasesPage.getContent()) {
                Disease disease = diseaseService.getOne(prescriptionCase.getIcd10());
                prescriptionCase.setIcd10name(disease==null?"":disease.getName());
                Hospital hospital = hospitalService.getOne(prescriptionCase.getHospitalId());
                prescriptionCase.setHospitalName(hospital==null?"":hospital.getName());
            }

            return toSuccess("获取成功",prescriptionCasesPage);
        }
        return toError("找不到匹配的患者信息", -1);
    }


    /**
     * Updated by chenrujian on 2018/6/8.
     */
    @ApiAuth
    @ApiOperation(value = "患者信息添加成功", notes = "患者信息添加成功", position = 9)
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = {"/patient/add"}, method = RequestMethod.POST)
    public ApiResultBean addPatient(@RequestBody PatientCaseBean patientCaseBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        patientCaseBean.setTs(new Date());
        User dbUser = userService.getByUsername(patientCaseBean.getMobile());
        if(patientCaseBean.getHospitalId()==null||patientCaseBean.getHospitalId()==""){
            return toError("医院不能为空", -2);
        }
        if(patientCaseBean.getRecordno()==null||patientCaseBean.getRecordno()==""){
            return toError("病案号不能为空", -2);
        }
        Map<String, Object> searchParams = new HashedMap();
        searchParams.put("EQ_hidden",0);
        searchParams.put("EQ_recordno",patientCaseBean.getRecordno());
        searchParams.put("EQ_hospitalId",patientCaseBean.getHospitalId());
//        searchParams.put("EQ_mobile",patientCaseBean.getMobile());
        PrescriptionCase presriptionCase  = prescriptionCaseService.findOne(searchParams);
        if (presriptionCase != null) {
            return toError("该【病案号】在当前医院下已存在，请勿重复增加", -2);
        }
        if(patientCaseBean.getMobile()!=null&&patientCaseBean.getMobile().trim().length()>=1){
            searchParams.remove("EQ_recordno");
            searchParams.put("EQ_mobile",patientCaseBean.getMobile());
            presriptionCase  = prescriptionCaseService.findOne(searchParams);
            if (presriptionCase != null) {
                return toError("该【手机号】在当前医院下已存在，请勿重复增加", -2);
            }
        }
        if (dbUser != null&&dbUser.getHidden()==0) {
            if( userDoctorService.getByUserId(dbUser.getId())!=null){
                return toError("该手机号为医生手机号，无法注册，请重新填写手机号",-2);
            }
            PrescriptionCase presriptionCaseSave = new PrescriptionCase();
            PropertyUtils.copyProperties(presriptionCaseSave, patientCaseBean);
            presriptionCaseSave.setUserId(dbUser.getId());
            prescriptionCaseService.save(presriptionCaseSave);
        }else{
            PrescriptionCase presriptionCaseSave = new PrescriptionCase();
            PropertyUtils.copyProperties(presriptionCaseSave, patientCaseBean);
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
                    prescriptionCaseService.save(presriptionCaseSave);
                }
                if(user!=null&&user.getMobile()!=null&&user.getMobile().length()>=3){
                    //增加市场版本的患者信息表
                    UserPatient userPatient = new UserPatient();
                    PropertyUtils.copyProperties(userPatient, patientCaseBean);
                    userPatient.setUserId(user.getId());
                    userPatientService.save(userPatient);
                }
            }else{
                presriptionCaseSave.setUserId("");
                prescriptionCaseService.save(presriptionCaseSave);
            }
        }
        return toSuccess("患者信息添加成功", patientCaseBean);
    }

    @ApiAuth
    @ApiOperation(value = "患者信息修改成功", notes = "患者信息修改成功", position = 9)
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(value = {"/patient/update"}, method = RequestMethod.POST)
    public ApiResultBean updatePatient(@RequestBody PatientCaseBean patientCaseBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, ParseException {
        patientCaseBean.setTs(new Date());
        User dbUser = userService.getByUsername(patientCaseBean.getMobile());
        if(patientCaseBean.getHospitalId()==null||patientCaseBean.getHospitalId()==""){
            return toError("医院不能为空", -2);
        }
        if(patientCaseBean.getRecordno()==null||patientCaseBean.getRecordno()==""){
            return toError("病案号不能为空！", -2);

        }
        PrescriptionCase prescriptionCase = prescriptionCaseService.getOne(patientCaseBean.getId());
        if(prescriptionCase==null){
            return toError("修改失败，唯一id在系统无法识别到", -2);
        }else{
            //检测ts时间戳，判断是否已经被人修改，提示界面请刷新
            SimpleDateFormat upfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date up = upfm.parse(patientCaseBean.getTsdAt());
            if(prescriptionCase.getTs().getTime()>up.getTime()){
                return toError("数据已经被他人修改，请刷新!", -2);
            }
            if((!prescriptionCase.getRecordno().equals(patientCaseBean.getRecordno()))){
                Map<String, Object> searchParams = new HashedMap();
                searchParams.put("EQ_hidden",0);
                searchParams.put("EQ_recordno",patientCaseBean.getRecordno());
                searchParams.put("EQ_hospitalId",patientCaseBean.getHospitalId());
                PrescriptionCase presriptionCase  = prescriptionCaseService.findOne(searchParams);
                if (presriptionCase != null) {
                    return toError("该【病案号】在当前医院下已存在，请勿重复增加", -2);
                }
            }
            if((!prescriptionCase.getMobile().equals(patientCaseBean.getMobile()))){
                if(patientCaseBean.getMobile()!=null&&patientCaseBean.getMobile().trim().length()>=1){
                    Map<String, Object> searchParams = new HashedMap();
                    searchParams.put("EQ_hidden",0);
                    searchParams.put("EQ_hospitalId",patientCaseBean.getHospitalId());
                    searchParams.put("EQ_mobile",patientCaseBean.getMobile());
                    PrescriptionCase presriptionCase  = prescriptionCaseService.findOne(searchParams);
                    if (presriptionCase != null) {
                        return toError("该【手机号】在当前医院下已存在，请勿重复增加", -2);
                    }
                }
            }

        }
        PrescriptionCase presriptionCaseSave = new PrescriptionCase();
        PropertyUtils.copyProperties(presriptionCaseSave, patientCaseBean);
        if (dbUser != null&&dbUser.getHidden()==0) {//查看修改后的手机号是否已经是用户
            if( userDoctorService.getByUserId(dbUser.getId())!=null){
                return toError("该手机号为医生手机号，无法注册，请重新填写手机号",-2);
            }
            presriptionCaseSave.setUserId(dbUser.getId());
            prescriptionCaseService.save(presriptionCaseSave);
        }else{
            if((!prescriptionCase.getMobile().equals(patientCaseBean.getMobile()))){//是否修改了手机号码
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
                        prescriptionCaseService.save(presriptionCaseSave);
                        if(user.getMobile()!=null&&user.getMobile().length()>=3){
                            //增加市场版本的患者信息表
                            UserPatient userPatient = new UserPatient();
                            PropertyUtils.copyProperties(userPatient, patientCaseBean);
                            userPatient.setUserId(user.getId());
                            userPatientService.save(userPatient);
                        }
                    }
                }else{
                    presriptionCaseSave.setUserId("");
                    prescriptionCaseService.save(presriptionCaseSave);
                }
             }else{
                presriptionCaseSave.setUserId(prescriptionCase.getUserId());
                prescriptionCaseService.save(presriptionCaseSave);
            }
        }


        return toSuccess("患者信息修改成功", patientCaseBean);
    }

    @ApiAuth
    @ApiOperation(value = "删除患者", notes = "删除患者", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/patient/delete"}, method = RequestMethod.POST)
    public ApiResultBean deletePatient(@RequestBody PatientCaseBean patientCaseBean, HttpServletRequest request) {
        prescriptionCaseService.delete(patientCaseBean.getId());
        return toSuccess("删除患者成功");
    }

    @ApiAuth
    @ApiOperation(value = "获取病种类型", notes = "获取病种类型", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/disease"}, method = RequestMethod.POST)
    public ApiResultBean disease(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {
        List<Disease> diseaseList = diseaseService.findByStatus(Constants.DISEASE_STATUS_PUBLISH);
        for (Disease disease : diseaseList) {
            System.out.println(disease.getRemark());
        }
        return toSuccess("获取病种类型成功", diseaseService.findByStatus(Constants.DISEASE_STATUS_PUBLISH));
    }


    @ApiAuth
    @ApiOperation(value = "获取评测记录", notes = "获取评测记录", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/getSingleselRecords"}, method = RequestMethod.POST)
    public ApiResultBean getSingleselRecords(@RequestBody SingleselRecordBean singleselRecordBean, HttpServletRequest request) {
        String evaluationPeople =  singleselRecordBean.getEvaluationPeople();
        Map<String,Object> searchParams = new HashMap<>();
        searchParams.put("EQ_evaluationPeople",evaluationPeople);
        PageBean pageBean = singleselRecordBean.getPageBean();
        Page<SingleselRecord>  singleselRecordPage = singleselRecordService.search(searchParams,pageBean);
        SingleselRecord[] singleselRecords = singleselRecordService.getRecord(evaluationPeople);
        BigDecimal[] bigDecimals = singleselRecordService.getRecordScore(evaluationPeople);
        List<SingleselRecordReturnBean> singleselRecordReturnBeen = new ArrayList<>();
        for(int i=0;i<singleselRecords.length;i++){
            SingleselRecordReturnBean singleselRecordReturnBean = new SingleselRecordReturnBean();
            singleselRecordReturnBean.setEvaluationTime(singleselRecords[i].getEvaluationTime());
            singleselRecordReturnBean.setEvaluatingId(singleselRecords[i].getEvaluatingId());
            singleselRecordReturnBean.setTotalScore(bigDecimals[i]);
            singleselRecordReturnBeen.add(singleselRecordReturnBean);
        }
        List<Map<String, Object>> singleselRecordPageMap = setFieldValues(singleselRecordReturnBeen, Evaluating.class, "evaluatingId", new String[]{ "name"});

        return toSuccess("获取评测记录成功",singleselRecordPageMap);
    }

    @ApiAuth
    @ApiOperation(value = "获取评测信息", notes = "获取评测信息", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/getEvaluating"}, method = RequestMethod.POST)
    public ApiResultBean getEvaluating(@RequestBody EvaluatingBean evaluatingBean, HttpServletRequest request) {
        PageBean pageBean = evaluatingBean.getPageBean();
        pageBean.setCurrentPage(evaluatingBean.getPaging());
        Page<Evaluating>  evaluatingPage =  evaluatingService.findByHiddenAndStatus(0,Constants.THERAPY_STATUS_PUBLISH,pageBean.toPageRequest());
        return toSuccess("获取评测信息成功！",evaluatingPage );
    }

    @ApiAuth
    @ApiOperation(value = "获取评测题目", notes = "获取评测题目", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/getSinglesel"}, method = RequestMethod.POST)
    public ApiResultBean getSinglesel(@RequestBody SingleselBean singleselBean, HttpServletRequest request) {
        String evaluatingId = singleselBean.getEvaluatingId();
        List<Singlesel> singlesels = singleselService.findByEvaluatingId(evaluatingId);
        return toSuccess("获取评测题目成功！",singlesels);
    }

    @ApiAuth
    @ApiOperation(value = "获取评测题目", notes = "获取评测题目", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/saveSingleselRecord"}, method = RequestMethod.POST)
    public ApiResultBean saveSingleselRecord(@RequestBody SingleselRecordSaveBean singleselRecordSaveBean, HttpServletRequest request) {
    List<SingleselRecord> singleselRecords = singleselRecordSaveBean.getSingleselRecords();
    Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        for(SingleselRecord singleselRecord:singleselRecords){
            singleselRecord.setEvaluationTime(time);
            singleselRecordService.save(singleselRecord);
        }
        return toSuccess("获取评测题目成功！",singleselRecordSaveBean);
    }

    @ApiAuth
    @ApiOperation(value = "获取播放记录", notes = "获取播放记录", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/getClickRecords"}, method = RequestMethod.POST)
    public ApiResultBean getClickRecords(@RequestBody ClickRecordBean clickRecordBean, HttpServletRequest request) {

        Map<String, Object> searchParams = new  HashedMap();
        searchParams.put("EQ_hidden",0);
//        searchParams.put("EQ_userId",getCurrentUserId());
        if(clickRecordBean.getHospitalId()==null||clickRecordBean.getHospitalId().length()<1){
            return toError("医院参数不能为空！", -2);
        }
        if(clickRecordBean.getPatientcaseId()!=null&&clickRecordBean.getPatientcaseId().length()>0){
            searchParams.put("EQ_patientcaseId",clickRecordBean.getPatientcaseId());
        }

        searchParams.put("EQ_hospitalId",clickRecordBean.getHospitalId());

        if (StringUtils.isNotBlank(clickRecordBean.getBeginDate())) {
            searchParams.put("GET_beginDate", clickRecordBean.getBeginDate());
        }
        if (StringUtils.isNotBlank(clickRecordBean.getEndDate())) {
            searchParams.put("GET_endDate", clickRecordBean.getEndDate());
        }

        if(StringUtils.isNotBlank(clickRecordBean.getContentName())){
            searchParams.put("EQ_contentName", clickRecordBean.getContentName());
        }
        if(StringUtils.isNotBlank(clickRecordBean.getRecordno())){
            searchParams.put("EQ_recordno", clickRecordBean.getRecordno());
        }
        if(StringUtils.isNotBlank(clickRecordBean.getUservrName())){
            searchParams.put("EQ_uservrName", clickRecordBean.getUservrName());
        }

        PageBean pageBean = clickRecordBean.getPageBean();
        pageBean.setTotalPages(pageBean.getTotalPages());
        Page<ClickRecord>  clickpage =  clickRecordService.findBySearchParams(searchParams,pageBean,clickRecordBean.getType());
        List<Map<String, Object>> clickpageMap = setFieldValues(clickpage.getContent(), Content.class, "contentId", new String[]{ "name"});
        for (Map<String, Object> stringObjectMap : clickpageMap) {
            Hospital hospitals = hospitalService.getOne((stringObjectMap.get("hospitalId")==null?"":stringObjectMap.get("hospitalId").toString()));
            PrescriptionCase prescriptionCase =  prescriptionCaseService.getOne((stringObjectMap.get("patientcaseId")==null?"":stringObjectMap.get("patientcaseId").toString()));
            User user = userService.getOne((stringObjectMap.get("userId")==null?"":stringObjectMap.get("userId").toString()));
            stringObjectMap.put("hospitalName",hospitals==null?"":hospitals.getName());
            stringObjectMap.put("recordno",prescriptionCase==null?"":prescriptionCase.getRecordno());
            stringObjectMap.put("name",prescriptionCase==null?"":prescriptionCase.getName());
            stringObjectMap.put("vrname",user==null?"":user.getRealname());
        }
        ClickRecordBean cl = new ClickRecordBean();
        cl.setClickpageMap(clickpageMap);
        cl.setTotalElements(clickpage.getTotalElements());
        cl.setTotalPages(clickpage.getTotalPages());
        cl.setPaging(clickRecordBean.getPaging());
        return toSuccess("获取播放记录成功！",cl );
    }

    @ApiAuth
    @ApiOperation(value = "新增播放记录", notes = "新增播放记录", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/saveClickRecords"}, method = RequestMethod.POST)
    public ApiResultBean saveClickRecords(@RequestBody ClickRecordBean clickRecordBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if(clickRecordBean.getHospitalId()==null||clickRecordBean.getHospitalId().length()<1){
            return toError("医院参数不能为空！", -2);
        }
        if(clickRecordBean.getPatientcaseId()==null||clickRecordBean.getPatientcaseId().length()<1){
            return toError("患者参数不能为空！", -2);
        }
        ClickRecord clickRecord = new ClickRecord();
        PropertyUtils.copyProperties(clickRecord, clickRecordBean);
        clickRecord.setUserId(getCurrentUserId());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        clickRecord.setClickDate(df.format(new Date()));
        ClickRecord clickRecords = clickRecordService.save(clickRecord);
        return toSuccess("播放记录生成成功！", clickRecords);
    }


    @ApiAuth
    @ApiOperation(value = "获取病种类型", notes = "获取病种类型", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/diseaseOnid"}, method = RequestMethod.POST)
    public ApiResultBean diseaseOnid(@RequestBody DiseaseIdBean diseaseIdBean, HttpServletRequest request) {
        Disease disease = diseaseService.getOne(diseaseIdBean.getId());
        return toSuccess("获取病种成功", disease);
    }

    @ApiAuth
    @ApiOperation(value = "获取疗法类型", notes = "获取疗法类型", position = 10)
    @ResponseBody
    @RequestMapping(value = {"/therapy"}, method = RequestMethod.POST)
    public ApiResultBean therapy(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {
        return toSuccess("获取疗法类型成功", therapyService.findByStatus(Constants.THERAPY_STATUS_PUBLISH));
    }



    @ApiAuth
    @ApiOperation(value = "获取单个内容", notes = "获取单个内容", position = 13)
    @ResponseBody
    @RequestMapping(value = {"/content/info"}, method = RequestMethod.POST)
    public ApiResultBean contentInfo(@RequestBody ContentIdBean contentIdBean, HttpServletRequest request) {
        Content content = contentService.getOne(contentIdBean.getContentId());
        Map<String, Object> contentMap = ExtractUtil.object2Map(content);

        contentMap.put("ext", contentService.getExt(content.getId(), content.getType()));
        return toSuccess("获取单个内容成功", contentMap);
    }






    @ApiAuth
    @ApiOperation(value = "获取同步内容", notes = "获取同步内容", position = 7)
    @ResponseBody
    @RequestMapping(value = {"/content/sync"}, method = RequestMethod.POST)
    public ApiResultBean contentSync(@RequestBody ContentSyncBean contentSyncBean, HttpServletRequest request) {
        Map<String, Object> searchParams = new HashMap<>();

        searchParams.put("IN_type", new Integer[]{
                Constants.CONTENT_TYPE_AUDIO,
                Constants.CONTENT_TYPE_VIDEO,
                Constants.CONTENT_TYPE_GAME});

        if (StringUtils.isNotBlank(contentSyncBean.getLastSyncAt())) {
            searchParams.put("GTE_videoupdateAt", contentSyncBean.getLastSyncAt());
        }

        Page<Content> page = contentService.findBySearchParams(searchParams, contentSyncBean.getPageBean());
        return toSuccess("获取同步内容成功", page.getContent());
    }

    @ApiAuth
    @ApiOperation(value = "获取Vr室管理员", notes = "获取Vr室管理员", position = 7)
    @ResponseBody
    @RequestMapping(value = {"/userVrRoom/list"}, method = RequestMethod.POST)
    public ApiResultBean userVrRoomList(@RequestBody VrRoomIdBean vrRoomIdBean, HttpServletRequest request) {
        List<UserVrRoom> userVrRoomList = userVrRoomService.findByVrRoomIdAndType(vrRoomIdBean.getVrRoomId(), Constants.USER_VR_ROOM_TYPE_APP);

        return toSuccess("获取Vr室管理员成功", setFieldValues(userVrRoomList, User.class, "userId", new String[]{"username", "realname"}));
    }

    @ApiAuth
    @ApiOperation(value = "添加任务", notes = "添加任务", position = 7)
    @ResponseBody
    @RequestMapping(value = {"/task/add"}, method = RequestMethod.POST)
    public ApiResultBean taskAdd(@RequestBody VrRoomAppTaskBean vrRoomAppTaskBean, HttpServletRequest request) {
        vrRoomAppTaskBean.setPrescriptionContentId("");
        VrRoomAppTask vrRoomAppTask = new VrRoomAppTask();
        try {
            PropertyUtils.copyProperties(vrRoomAppTask, vrRoomAppTaskBean);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        vrRoomAppTask.setStatus(Constants.VR_ROOM_APP_TASK_STATUS_INIT);
        vrRoomAppTaskService.save(vrRoomAppTask);

        return toSuccess("添加任务成功");
    }

    @ApiAuth
    @ApiOperation(value = "任务情况", notes = "任务情况", position = 7)
    @ResponseBody
    @RequestMapping(value = {"/task/status"}, method = RequestMethod.POST)
    public ApiResultBean taskStatus(@RequestBody UserIdBean userIdBean, HttpServletRequest request) {
        List<VrRoomAppTask> vrRoomAppTaskList = vrRoomAppTaskService.findNotEnd(userIdBean.getUserId());

        if (!vrRoomAppTaskList.isEmpty()) {
            VrRoomAppTask lastVrRoomAppTask = vrRoomAppTaskList.get(vrRoomAppTaskList.size() - 1);
            if (lastVrRoomAppTask.isPlayType()) {
                return toSuccess("设备正在播放中", 1);
            } else if (lastVrRoomAppTask.isEndType()) {
                return toSuccess("设备等待停止", 2);
            }
        }
        return toSuccess("设备空闲");
    }

    private Map<String, Object> buildData(String key, Object value) {
        Map<String, Object> data = new HashMap<>();
        data.put(key, value);
        return data;
    }

    private String getCurrentUserId() {
        return SpringSecurityUtils.getCurrentUserId();
    }



    @ApiAuth
    @ApiOperation(value = "获取内容新版", notes = "获取内容新版", position = 7)
    @ResponseBody
    @RequestMapping(value = {"/content/searchnew"}, method = RequestMethod.POST)
    public ApiResultBean contentSearchnew(@RequestBody ContentSearchBean contentSearchBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<String, Object> searchParams = new HashMap<>();
        if (StringUtils.isNotBlank(contentSearchBean.getDiseaseId())) {
            searchParams.put("EQ_diseaseId", contentSearchBean.getDiseaseId());
        }
        if (StringUtils.isNotBlank(contentSearchBean.getTherapyId())) {
            searchParams.put("EQ_therapyId", contentSearchBean.getTherapyId());
        }
        if (contentSearchBean.getType() == null || contentSearchBean.getType().intValue() <= 0) {
            searchParams.put("IN_type", new Integer[]{
                    Constants.CONTENT_TYPE_AUDIO,
                    Constants.CONTENT_TYPE_VIDEO,
                    Constants.CONTENT_TYPE_GAME,
                    Constants.CONTENT_TYPE_OUTSIDE_GAME});
        } else {

            if(contentSearchBean.getType()==6){
                searchParams.put("IN_type", new Integer[]{
                        Constants.CONTENT_TYPE_GAME,
                        Constants.CONTENT_TYPE_OUTSIDE_GAME});
            }else{
                searchParams.put("EQ_type", contentSearchBean.getType());
            }

        }
        if (StringUtils.isNotBlank(contentSearchBean.getKeyword())) {
            searchParams.put("LIKE_name", contentSearchBean.getKeyword());
        }
        PageBean pageBean =contentSearchBean.getPageBean();
        pageBean.setTotalPages(8);
        Page<Content> page = contentService.findBySearchParamsto(searchParams,pageBean);


        setConfigFieldValues(page, Constants.CONTENT_TYPE_KEY, "type");
        setConfigFieldValues(page, Constants.CONTENT_STATUS_KEY, "status");
        setConfigFieldValues(page, Global.IF_OR_NOT_ENUM_KEY, "isFree");
        return toSuccess("获取内容成功", page);
    }

    @ApiAuth
    @ApiOperation(value = "获取内容对应的疗法", notes = "获取内容对应的疗法", position = 7)
    @ResponseBody
    @RequestMapping(value = {"/content/getTherapys"}, method = RequestMethod.POST)
    public ApiResultBean getTherapys(@RequestBody ContentSearchBean contentSearchBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<String, Object> searchParams = new HashMap<>();
        if (StringUtils.isNotBlank(contentSearchBean.getDiseaseId())) {
            searchParams.put("EQ_diseaseId", contentSearchBean.getDiseaseId());
        }
        if (StringUtils.isNotBlank(contentSearchBean.getTherapyId())) {
            searchParams.put("EQ_therapyId", contentSearchBean.getTherapyId());
        }
        if (contentSearchBean.getType() == null || contentSearchBean.getType().intValue() <= 0) {
            searchParams.put("IN_type", new Integer[]{
                    Constants.CONTENT_TYPE_AUDIO,
                    Constants.CONTENT_TYPE_VIDEO,
                    Constants.CONTENT_TYPE_GAME,
                    Constants.CONTENT_TYPE_OUTSIDE_GAME});
        } else {

            if(contentSearchBean.getType()==6){
                searchParams.put("IN_type", new Integer[]{
                        Constants.CONTENT_TYPE_GAME,
                        Constants.CONTENT_TYPE_OUTSIDE_GAME});
            }else{
                searchParams.put("EQ_type", contentSearchBean.getType());
            }

        }
        if (StringUtils.isNotBlank(contentSearchBean.getKeyword())) {
            searchParams.put("LIKE_name", contentSearchBean.getKeyword());
        }

        List<Content> contentlist = contentService.findBySearchParamsCase(searchParams);
        List<String> str = new ArrayList<String>();
        for (Content content : contentlist) {
            str.add(content.getId());
        }
        List<Therapy> thlist  = new ArrayList<Therapy>();
        if(str.size()>=1){
            List<Object> therapylist = therapyService.findByContentStatus(str,Constants.THERAPY_STATUS_PUBLISH);

            for (int i = 0; i < therapylist.size(); i++) {
                Object[] obj = (Object[])therapylist.get(i);
                Therapy th = new Therapy();
                th.setId(obj[2]==null?"":obj[2].toString());
                th.setPid(obj[1]==null?"":obj[1].toString());
                th.setSort(obj[3]==null?0:Integer.parseInt(obj[3].toString()));
                th.setName(obj[0]==null?"":obj[0].toString());
                thlist.add(th);
            }
        }


        return toSuccess("获取疗法成功",thlist );
    }


    @ApiAuth
    @ApiOperation(value = "游戏加密验证（游戏端调用）", notes = "游戏加密验证（游戏端调用）", position = 5)
    @ResponseBody
    @RequestMapping(value = {"/gameEfs"}, method = RequestMethod.POST)
    public ApiResultBean gameEfs(@RequestBody GameEfsBean gameEfsBean,
                                 HttpServletRequest request) {
//        UserVrRoom userVrRoom = userVrRoomService.getByUserId(gameEfsBean.getLoginuserid());
//        if(userVrRoom==null){
//            return toError("校验失败",-1);
//        }
//        Map<String, Object> searchParams = new HashedMap();
//        searchParams.put("EQ_ip",gameEfsBean.getIp());
//        searchParams.put("EQ_mac",gameEfsBean.getMac());
//        searchParams.put("EQ_vruser",userVrRoom.getId());
//        searchParams.put("EQ_hidden",0);
//        List<GameEfs> gameefslist = gameEfsService.search(searchParams);
//        if(gameefslist.size()>=1){
//            return toSuccess("校验成功");
//        }else{
//
//            return toError("校验失败",-1);
//        }
        return toSuccess("校验成功");
    }

    @ApiOperation(value = "游戏加密验证（VR室调用）", notes = "游戏加密验证（VR室调用）", position = 5)
    @ResponseBody
    @RequestMapping(value = {"/gameEfsVr"}, method = RequestMethod.POST)
    public ApiResultBean gameEfsVr(@RequestBody GameEfs gameEfs,
                                 HttpServletRequest request) {
//        UserVrRoom userVrRoom = userVrRoomService.getByUserId(gameEfs.getVruser());
//        if(userVrRoom==null){
//            return toError("校验失败",-1);
//        }
//        Map<String, Object> searchParams = new HashedMap();
//        searchParams.put("EQ_ip",gameEfs.getIp());
//        searchParams.put("EQ_mac",gameEfs.getMac());
//        searchParams.put("EQ_vruser",userVrRoom.getId());
//        searchParams.put("EQ_hidden",0);
//        List<GameEfs> gameefslist = gameEfsService.search(searchParams);
//        if(gameefslist.size()>=1){
//            return toSuccess("校验成功");
//        }else{
//
//            return toError("校验失败",-1);
//        }
        return toSuccess("校验成功");

    }



    @ApiAuth
    @ApiOperation(value = "获取视频密钥", notes = "获取视频密钥", position = 7)
    @ResponseBody
    @RequestMapping(value = {"/getvoideEfs"}, method = RequestMethod.POST)
    public ApiResultBean getvoideEfs(@RequestBody ApiBasePageBean apiBasePageBean, HttpServletRequest request) {
        VoideEfs voideEfs = voideEfsService.getOne("8a8c7e235cc98126015cc9824cc40002");
        VrRoomAppTaskReturnBean vrRoomAppTaskReturnBean = new VrRoomAppTaskReturnBean();
        vrRoomAppTaskReturnBean.setVoidpassword(voideEfs.getPassword());
        return toSuccess("成功",voideEfs);
    }

    @ApiAuth
    @ApiOperation(value = "接受游戏数据", notes = "接受游戏数据", position = 7)
    @ResponseBody
    @RequestMapping(value = {"/saveGameItems"}, method = RequestMethod.POST)
    public ApiResultBean saveGameItems(@RequestBody GameEfsHeadBean gameEfsHeadBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, ParseException {

        UserVrRoom userVrRoom = userVrRoomService.getByUserId(gameEfsHeadBean.getLoginuserid());
        if(userVrRoom==null){
            return toError("校验失败",-1);
        }
        GameheadRecord gameheadRecord = new GameheadRecord();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(gameEfsHeadBean.getClickdatatime());
        gameEfsHeadBean.setClickdatatime(null);
        PropertyUtils.copyProperties(gameheadRecord, gameEfsHeadBean);
        gameheadRecord.setClickdatatime(date);
        VrRoom vrRoom = vrRoomService.getOne(gameheadRecord.getVrroomid());
        gameheadRecord.setHospitalId(vrRoom==null?"":vrRoom.getHospital_id());
        gameheadRecordService.save(gameheadRecord);

        ClickRecord clickRecord = new ClickRecord();
        clickRecord.setContentId(gameheadRecord.getContentid());
        clickRecord.setHospitalId(gameheadRecord.getHospitalId());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        clickRecord.setClickDate(df.format(gameheadRecord.getClickdatatime()));
        clickRecord.setPatientcaseId(gameheadRecord.getPatientid());
        clickRecord.setUserId(gameheadRecord.getLoginuserid());
        clickRecord.setType(6);
        ClickRecord clickRecords = clickRecordService.save(clickRecord);
        DecimalFormat decimalFormat =new DecimalFormat("0.00");
        List<GameEfsItemsBean> gameEfsItemsBeanList = gameEfsHeadBean.getItems();
        for (int i = 0; i < gameEfsItemsBeanList.size(); i++) {
            GameEfsItemsBean gameitem = gameEfsItemsBeanList.get(i);
            if("1".equals(gameitem.getGamephase())){
                GameitemRecord01 gamitem01 = new GameitemRecord01();
                PropertyUtils.copyProperties(gamitem01, gameitem);
                gamitem01.setGameheadRecordId(gameheadRecord.getId());
                gamitem01.setCorrectrate(decimalFormat.format((float)((new Double(gameitem.getCorrectnumber())/new Double(gameitem.getTotalnumber()))*100)));//正确率
                gamitem01.setInaccuracyrate(decimalFormat.format((float)((new Double(gameitem.getInaccuracynumber())/new Double(gameitem.getTotalnumber()))*100)));//错误率
                gameitemRecord01Service.save(gamitem01);
            }else if("2".equals(gameitem.getGamephase())){
                GameitemRecord02 gamitem02 = new GameitemRecord02();
                PropertyUtils.copyProperties(gamitem02, gameitem);
                gamitem02.setGameheadRecordId(gameheadRecord.getId());
                gamitem02.setCorrectrate(decimalFormat.format((float)((new Double(gameitem.getCorrectnumber())/new Double(gameitem.getTotalnumber()))*100)));//正确率
                gamitem02.setInaccuracyrate(decimalFormat.format((float)((new Double(gameitem.getInaccuracynumber())/new Double(gameitem.getTotalnumber()))*100)));//错误率
                gameitemRecord02Service.save(gamitem02);
            }else if("3".equals(gameitem.getGamephase())){
                GameitemRecord03 gamitem03 = new GameitemRecord03();
                PropertyUtils.copyProperties(gamitem03, gameitem);
                gamitem03.setGameheadRecordId(gameheadRecord.getId());
                gamitem03.setCorrectrate(decimalFormat.format((float)((new Double(gameitem.getCorrectnumber())/new Double(gameitem.getTotalnumber()))*100)));//正确率
                gamitem03.setInaccuracyrate(decimalFormat.format((float)((new Double(gameitem.getInaccuracynumber())/new Double(gameitem.getTotalnumber()))*100)));//错误率
                gameitemRecord03Service.save(gamitem03);
            }else if("4".equals(gameitem.getGamephase())){
                GameitemRecord04 gamitem04 = new GameitemRecord04();
                PropertyUtils.copyProperties(gamitem04, gameitem);
                gamitem04.setGameheadRecordId(gameheadRecord.getId());
                gamitem04.setCorrectrate(decimalFormat.format((float)((new Double(gameitem.getCorrectnumber())/new Double(gameitem.getTotalnumber()))*100)));//正确率
                gamitem04.setInaccuracyrate(decimalFormat.format((float)((new Double(gameitem.getInaccuracynumber())/new Double(gameitem.getTotalnumber()))*100)));//错误率
                gameitemRecord04Service.save(gamitem04);
            }else if("5".equals(gameitem.getGamephase())){
                GameitemRecord05 gamitem05 = new GameitemRecord05();
                PropertyUtils.copyProperties(gamitem05, gameitem);
                gamitem05.setGameheadRecordId(gameheadRecord.getId());
                gamitem05.setCorrectrate(decimalFormat.format((float)((new Double(gameitem.getCorrectnumber())/new Double(gameitem.getTotalnumber()))*100)));//正确率
                gamitem05.setInaccuracyrate(decimalFormat.format((float)((new Double(gameitem.getInaccuracynumber())/new Double(gameitem.getTotalnumber()))*100)));//错误率
                gameitemRecord05Service.save(gamitem05);
            }
        }
        return toSuccess("接受成功");
    }

    @ApiAuth
    @ApiOperation(value = "获取患者处方列表", notes = "获取患者处方列表", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/prescription/getByPatientId"}, method = RequestMethod.POST)
    public ApiResultBean<List<Map<String, Object>>> getPrescriptionByPatientId(@RequestBody PatientIdPageBean patientIdPageBean, HttpServletRequest request) {
        //根据VRID获取医院id
        VrRoom vrRoom = vrRoomService.getOne(patientIdPageBean.getVrRoomId());
        Map<String, Object> searchParams = new HashedMap();
        searchParams.put("EQ_hidden",0);
        searchParams.put("EQ_patientcaseId",patientIdPageBean.getPatientId());
        if(patientIdPageBean.getDoctorId()!=null&&patientIdPageBean.getDoctorId().length()>0){
            searchParams.put("EQ_doctorId",patientIdPageBean.getDoctorId());
        }
        if(patientIdPageBean.getStatus()!=null){
           searchParams.put("EQ_status",patientIdPageBean.getStatus());
        }
        searchParams.put("EQ_hospitalId",(vrRoom==null?"":vrRoom.getHospital_id()));
        Page<Prescription> prescriptionListPage = prescriptionService.search(searchParams,patientIdPageBean.getPageBean());
        if(prescriptionListPage.getContent().size()<1){
            return toSuccess("获取患者处方列表成功", null);
        }
        List<Map<String, Object>>  prescriptionListMap = setFieldValues(prescriptionListPage.getContent(), Hospital.class, "hospitalId", new String[]{"name"});
        for (Map<String, Object> stringObjectMap : prescriptionListMap) {
            if(stringObjectMap.get("disease")!=null&&stringObjectMap.get("disease").toString().length()>0){
                Disease disease = diseaseService.getOne(stringObjectMap.get("disease").toString());
                stringObjectMap.put("diseaseName",disease==null?"":disease.getName());
            }else{
                stringObjectMap.put("diseaseName","");
            }
        }
        return toSuccess("获取患者处方列表成功", prescriptionListMap);

    }

    @ApiAuth
    @ApiOperation(value = "获取患者处方内容列表", notes = "获取患者处方内容列表", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/prescriptionContent/getByPrescriptionId"}, method = RequestMethod.POST)
    public ApiResultBean getPrescriptionContentByPrescriptionId(@RequestBody PrescriptionIdBean prescriptionIdBean, HttpServletRequest request) {
        List<PrescriptionContent> prescriptionContentList = prescriptionContentService.findByPrescriptionId(prescriptionIdBean.getPrescriptionId());

        return toSuccess("获取患者处方内容列表成功", setFieldValues(prescriptionContentList, Content.class, "contentId", new String[]{"helpCode", "name", "type","coverPic","name","remark","duration"}));
    }


    @ApiAuth
    @ApiOperation(value = "为患者开增加治疗记录", notes = "为患者开增加治疗记录", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/prescriptionContentFeedback/add"}, method = RequestMethod.POST)
    public ApiResultBean prescriptionContentFeedbackAdd(@RequestBody PrescriptionContentIdBean prescriptionContentIdBean, HttpServletRequest request) {
        prescriptionContentFeedbackService.add(prescriptionContentIdBean.getPrescriptionContentId());
        return toSuccess("为患者开增加治疗记录成功");
    }

    @ApiOperation(value = "获取PC版本信息成功", notes = "获取PC版本信息成功", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/getSysInformation"}, method = RequestMethod.POST)
    public ApiResultBean getSysInformation(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {
        PcVersion pcVersion = pcVersionService.finByVersionCode("PC1.0.0");
        return toSuccess("获取PC版本信息成功",pcVersion);
    }

    @ApiAuth
    @ApiOperation(value = "修改治疗师名称", notes = "修改治疗师名称", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/upUservrName"}, method = RequestMethod.POST)
    public ApiResultBean upUservrName(@RequestBody UserVrRoomBean userVrRoomBean, HttpServletRequest request) {
        User user = userService.getOne(userVrRoomBean.getUserId());
        if(user!=null){
//            user.setUsername(userVrRoomBean.getUsername());
            user.setRealname(userVrRoomBean.getUsername());
            userService.update(user);
            return toSuccess("修改治疗师名称成功",1);
        }
        return toError("修改治疗师名称失败",0);
    }

    @ApiAuth
    @ApiOperation(value = "获取游戏点播记录", notes = "获取游戏点播记录", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/getClickGameRecords"}, method = RequestMethod.POST)
    public ApiResultBean getClickGameRecords(@RequestBody ClickRecordBean clickRecordBean, HttpServletRequest request) {

        Map<String, Object> searchParams = new  HashedMap();
        searchParams.put("EQ_hidden",0);
//        searchParams.put("EQ_userId",getCurrentUserId());
        if(clickRecordBean.getHospitalId()==null||clickRecordBean.getHospitalId().length()<1){
            return toError("医院参数不能为空！", -2);
        }
        if(clickRecordBean.getPatientcaseId()!=null&&clickRecordBean.getPatientcaseId().length()>0){
            searchParams.put("EQ_patientcaseId",clickRecordBean.getPatientcaseId());
        }

        searchParams.put("EQ_hospitalId",clickRecordBean.getHospitalId());

        if (StringUtils.isNotBlank(clickRecordBean.getBeginDate())) {
            searchParams.put("GET_beginDate", clickRecordBean.getBeginDate());
        }
        if (StringUtils.isNotBlank(clickRecordBean.getEndDate())) {
            searchParams.put("GET_endDate", clickRecordBean.getEndDate());
        }

        if(StringUtils.isNotBlank(clickRecordBean.getContentName())){
            searchParams.put("EQ_contentName", clickRecordBean.getContentName());
        }
        if(StringUtils.isNotBlank(clickRecordBean.getRecordno())){
            searchParams.put("EQ_recordno", clickRecordBean.getRecordno());
        }
        if(StringUtils.isNotBlank(clickRecordBean.getUservrName())){
            searchParams.put("EQ_uservrName", clickRecordBean.getUservrName());
        }

        PageBean pageBean = clickRecordBean.getPageBean();
        pageBean .setTotalPages(clickRecordBean.getTotalPages());

        Page<GameheadRecord>  clickpage =  gameheadRecordService.findBySearchParams(searchParams,pageBean);
        List<Map<String, Object>> clickpageMap = setFieldValues(clickpage.getContent(), Content.class, "contentid", new String[]{ "name"});
        double correctnumbertotal = 0;//总正确个数
        double inaccuracynumbertotal = 0;//总错误个数
        double timeconsumingtotal = 0;//总用时
        double totalnumber = 0;//总个数
        DecimalFormat decimalFormat =new DecimalFormat("0.00");
        for (Map<String, Object> stringObjectMap : clickpageMap) {
            Hospital hospitals = hospitalService.getOne((stringObjectMap.get("hospitalId")==null?"":stringObjectMap.get("hospitalId").toString()));
            PrescriptionCase prescriptionCase =  prescriptionCaseService.getOne((stringObjectMap.get("patientid")==null?"":stringObjectMap.get("patientid").toString()));
            User user = userService.getOne((stringObjectMap.get("loginuserid")==null?"":stringObjectMap.get("loginuserid").toString()));
            GameitemRecord01 gameitemRecord01 = gameitemRecord01Service.findByHiddenAndGameheadRecordId(stringObjectMap.get("id")==null?"":stringObjectMap.get("id").toString());//
            stringObjectMap.put("correctrate01",gameitemRecord01==null?"":gameitemRecord01.getCorrectrate());//正确率
            stringObjectMap.put("correctnumber01",gameitemRecord01==null?"":gameitemRecord01.getCorrectnumber());//正确的个数
            stringObjectMap.put("inaccuracyrate01",gameitemRecord01==null?"":gameitemRecord01.getInaccuracyrate());//错误率
            stringObjectMap.put("inaccuracynumber01",gameitemRecord01==null?"":gameitemRecord01.getInaccuracynumber());//错误的个数
            stringObjectMap.put("timeconsuming01",gameitemRecord01==null?"":gameitemRecord01.getTimeconsuming());
            stringObjectMap.put("totalnumber01",gameitemRecord01==null?"":gameitemRecord01.getTotalnumber());
            GameitemRecord02 gameitemRecord02 = gameitemRecord02Service.findByHiddenAndGameheadRecordId(stringObjectMap.get("id")==null?"":stringObjectMap.get("id").toString());//

            stringObjectMap.put("correctrate02",gameitemRecord02==null?"":gameitemRecord02.getCorrectrate());//正确率
            stringObjectMap.put("correctnumber02",gameitemRecord02==null?"":gameitemRecord02.getCorrectnumber());//正确的个数
            stringObjectMap.put("inaccuracyrate02",gameitemRecord02==null?"":gameitemRecord02.getInaccuracyrate());//错误率
            stringObjectMap.put("inaccuracynumber02",gameitemRecord02==null?"":gameitemRecord02.getInaccuracynumber());//错误的个数
            stringObjectMap.put("timeconsuming02",gameitemRecord02==null?"":gameitemRecord02.getTimeconsuming());
            stringObjectMap.put("totalnumber02",gameitemRecord02==null?"":gameitemRecord02.getTotalnumber());
            GameitemRecord03 gameitemRecord03 = gameitemRecord03Service.findByHiddenAndGameheadRecordId(stringObjectMap.get("id")==null?"":stringObjectMap.get("id").toString());//
            stringObjectMap.put("correctrate03",gameitemRecord03==null?"":gameitemRecord03.getCorrectrate());//正确率
            stringObjectMap.put("correctnumber03",gameitemRecord03==null?"":gameitemRecord03.getCorrectnumber());//正确的个数
            stringObjectMap.put("inaccuracyrate03",gameitemRecord03==null?"":gameitemRecord03.getInaccuracyrate());//错误率
            stringObjectMap.put("inaccuracynumber03",gameitemRecord03==null?"":gameitemRecord03.getInaccuracynumber());//错误的个数
            stringObjectMap.put("timeconsuming03",gameitemRecord03==null?"":gameitemRecord03.getTimeconsuming());
            stringObjectMap.put("totalnumber03",gameitemRecord03==null?"":gameitemRecord03.getTotalnumber());


            correctnumbertotal = ((gameitemRecord01==null?new Double(0):new Double(gameitemRecord01.getCorrectnumber()))
                    +(gameitemRecord02==null?new Double(0):new Double(gameitemRecord02.getCorrectnumber()))
                    +(gameitemRecord03==null?new Double(0):new Double(gameitemRecord03.getCorrectnumber())));
            inaccuracynumbertotal = ((gameitemRecord01==null?new Double(0):new Double(gameitemRecord01.getInaccuracynumber()))
                    +(gameitemRecord02==null?new Double(0):new Double(gameitemRecord02.getInaccuracynumber()))
                    +(gameitemRecord03==null?new Double(0):new Double(gameitemRecord03.getInaccuracynumber())));
            timeconsumingtotal = ((gameitemRecord01==null?new Double(0):new Double(gameitemRecord01.getTimeconsuming()))
                    +(gameitemRecord02==null?new Double(0):new Double(gameitemRecord02.getTimeconsuming()))
                    +(gameitemRecord03==null?new Double(0):new Double(gameitemRecord03.getTimeconsuming())));
            totalnumber = ((gameitemRecord01==null?new Double(0):new Double(gameitemRecord01.getTotalnumber()))
                    +(gameitemRecord02==null?new Double(0):new Double(gameitemRecord02.getTotalnumber()))
                    +(gameitemRecord03==null?new Double(0):new Double(gameitemRecord03.getTotalnumber())));
            if(totalnumber==0){
                stringObjectMap.put("correctratetotal",0);//正确率
                stringObjectMap.put("inaccuracyratetotal",0);//错误率
            }else{
                if(correctnumbertotal<=0){
                    stringObjectMap.put("correctratetotal",0);//正确率
                }else{
                    stringObjectMap.put("correctratetotal",decimalFormat.format(correctnumbertotal/(correctnumbertotal+inaccuracynumbertotal)*100));//正确率
                }
                if(inaccuracynumbertotal<=0){
                    stringObjectMap.put("inaccuracyratetotal",0);//错误率
                }else{
                    stringObjectMap.put("inaccuracyratetotal",decimalFormat.format(inaccuracynumbertotal/(correctnumbertotal+inaccuracynumbertotal)*100));//错误率
                }
            }

            stringObjectMap.put("correctnumbertotal",correctnumbertotal);//正确的个数

            stringObjectMap.put("inaccuracynumbertotal",inaccuracynumbertotal);//总错误的个数
            stringObjectMap.put("timeconsumingtotal",timeconsumingtotal);//总用时
            stringObjectMap.put("totalnumber",totalnumber);//总个数

            stringObjectMap.put("hospitalName",hospitals==null?"":hospitals.getName());
            stringObjectMap.put("recordno",prescriptionCase==null?"":prescriptionCase.getRecordno());
            stringObjectMap.put("name",prescriptionCase==null?"":prescriptionCase.getName());
            stringObjectMap.put("vrname",user==null?"":user.getRealname());
        }
        ClickRecordBean cl = new ClickRecordBean();
        cl.setClickpageMap(clickpageMap);
        cl.setTotalElements(clickpage.getTotalElements());
        cl.setTotalPages(clickpage.getTotalPages());
        cl.setPaging(clickRecordBean.getPaging());
        return toSuccess("获取播放记录成功！",cl );
    }


    @ApiAuth
    @ApiOperation(value = "根据Id患者信息", notes = "根据Id患者信息", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/findByIdPatientCase"}, method = RequestMethod.POST)
    public ApiResultBean findByIdPatientCase(@RequestBody PatientCaseBean patientCaseBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        PrescriptionCase prescriptionCase = prescriptionCaseService.getOne(patientCaseBean.getId());
        if(prescriptionCase!=null){
            Map<String, Object> patmap = new HashedMap();
            Hospital hospitals = hospitalService.getOne(prescriptionCase.getHospitalId());
            Disease disease = diseaseService.getOne(prescriptionCase.getIcd10());
            patmap.put("gender",(prescriptionCase.getGender()==0?"保密":(prescriptionCase.getGender()==1?"男":(prescriptionCase.getGender()==2?"女":""))));
            patmap.put("mobile",prescriptionCase.getMobile());
            patmap.put("vrname","");
            patmap.put("bornDate",prescriptionCase.getBornDate());
            patmap.put("name",prescriptionCase.getName());
            patmap.put("hospitalName",hospitals==null?"":hospitals.getName());
            patmap.put("icd10",disease==null?"":disease.getName());

            return toSuccess("获取患者信息成功", patmap);
        }
        return toError("找不到匹配的患者信息", -1);
    }
}
