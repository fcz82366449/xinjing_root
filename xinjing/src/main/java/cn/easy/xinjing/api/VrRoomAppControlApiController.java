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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.utils.text.TextValidator;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by chenzhongyi on 2016/11/18.
 */
@Api(value = "VrRoom-app-control-api-controller", description = "VR室移动控制相关API", position = 5)
@Controller
@RequestMapping("/api/v1/appControlVrRoom")
public class VrRoomAppControlApiController extends BaseApiController {
    @Autowired
    protected TherapyService therapyService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserPatientService userPatientService;
    @Autowired
    private PrescriptionService prescriptionService;
    @Autowired
    private PrescriptionContentService prescriptionContentService;
    @Autowired
    private AppTokenService appTokenService;
    @Autowired
    private UserVrRoomService userVrRoomService;
    @Autowired
    private VrRoomAppTaskService vrRoomAppTaskService;
    @Autowired
    private AppVersionService appVersionService;
    @Autowired
    private VrRoomService vrRoomService;
    @Autowired
    private PrescriptionCaseService prescriptionCaseService;
    @Autowired
    private DiseaseService diseaseService;
    @Autowired
    private ContentService contentService;
    @Autowired
    private DiseaseTherapyService diseaseTherapyService;

    @ApiOperation(value = "获取版本信息", notes = "获取版本信息", position = 1)
    @ResponseBody
    @RequestMapping(value = {"/getVersion"}, method = RequestMethod.POST)
    public ApiResultBean<AppVersion> getVersion(@RequestBody VersionBean versionBean) {
        String appCode = "";
        if(Constants.SYSTEM_VERSION_IOS.equals(versionBean.getSystemVersion())){
            appCode = Constants.APP_CLIENT_CONTROL_VR_IOS;
        }else{
            appCode = Constants.APP_CLIENT_CONTROL_VR_ANDROID;
        }

        AppVersion appVersion = appVersionService.getByAppCode(appCode);
        return toSuccess("获取成功", appVersion);
    }

    @ApiOperation(value = "登录", notes = "登录", position = 5)
    @ResponseBody
    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public ApiResultBean<VrRoomLoginResultBean> login(@RequestBody LoginBean loginBean, HttpServletRequest request) {
        User user = userService.getByUsername(loginBean.getUsername());
        if (user == null) {
            return toError("用户未注册", -3);
        }

        if (!BaseUtils.matchesPassword(loginBean.getPassword(), user.getPassword())) {
            return toError("密码不正确", -2);
        }

        UserVrRoom userVrRoom = userVrRoomService.getByUserId(user.getId());
        if(userVrRoom == null) {
            return toError("账号类型不正确", -1);
        }
        if(userVrRoom.getType() != Constants.USER_VR_ROOM_TYPE_APP_CONTROL) {
            return toError("VR室账号类型不正确", -1);
        }

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
        appToken.setUserType(Constants.USER_TYPE_APP_CONTROL_VR_ROOM);
        appToken = appTokenService.save(appToken);

        VrRoomLoginResultBean resultBean = new VrRoomLoginResultBean();
        resultBean.setUserId(user.getId());
        resultBean.setUsername(user.getUsername());
        resultBean.setRealname(user.getRealname());
        resultBean.setToken(appToken.getId());
        resultBean.setVrRoomId(userVrRoom.getVrRoomId());

        VrRoom vrRoom = vrRoomService.getOne(userVrRoom.getVrRoomId());
        if(vrRoom != null) {
            resultBean.setVrRoomName(vrRoom.getName());
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

        if(!BaseUtils.matchesPassword(modifyPasswordBean.getOldPassword(), dbUser.getPassword())) {
            return toError("当前密码不正确", -1);
        }

        dbUser.setPassword(BaseUtils.encodePassword(modifyPasswordBean.getNewPassword()));
        userService.save(dbUser);

        return toSuccess("修改密码成功");
    }

    @ApiAuth
    @ApiOperation(value = "根据条件查询患者信息", notes = "根据条件查询患者信息", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/patient/getByKeyword"}, method = RequestMethod.POST)
    public ApiResultBean getPatientByKeyword(@RequestBody KeywordBean keywordBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<String, Object> searchParams = new HashMap<>();
        if (TextValidator.isMobileSimple(keywordBean.getKeyword())) {
            searchParams.put("EQ_username", keywordBean.getKeyword());
        } else {
            searchParams.put("EQ_realname", keywordBean.getKeyword());
        }

        PageBean pageBean = new PageBean();
        Page<User> page = userService.search(searchParams, pageBean);

        PatientBean patientBean = null;
        if (page.getTotalElements() > 0) {
            User user = page.getContent().get(0);
            UserPatient userPatient = userPatientService.getByUserId(user.getId());
            patientBean = PatientBean.valueOf(user, userPatient);
        }

        if (patientBean != null) {
            return toSuccess("根据条件查询患者信息成功", patientBean);
        }

        return toError("找不到匹配的患者信息", -1);
    }

    @ApiAuth
    @ApiOperation(value = "根据条件查询患者信息(病案号或手机号)", notes = "根据条件查询患者信息(病案号或手机号)", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/getPatientByKeywordCase"}, method = RequestMethod.POST)
    public ApiResultBean getPatientByKeywordCase(@RequestBody KeywordBean keywordBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if(keywordBean.getKeyword()==null||keywordBean.getKeyword().trim().length()<1){
            return toError("关键词不能为空！", -1);
        }
        //根据登陆的帐号获取医院
        UserVrRoom userVrRoom = userVrRoomService.getByUserId(getCurrentUserId());
        if(userVrRoom==null){
            return toError("登陆用户类型不正确", -1);
        }
        VrRoom vrRoom = vrRoomService.getOne(userVrRoom.getVrRoomId());
        if(vrRoom==null){
            return toError("登陆的用户没有维护VR室", -1);
        }
        List<PrescriptionCase> prescriptionCaseList = prescriptionCaseService.findByPrescriptionCase(vrRoom.getHospital_id(),0,keywordBean.getKeyword());
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
    @ApiOperation(value = "获取患者的内容(病案号版本)", notes = "获取患者的内容(病案号版本)", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/prescriptionCase/getContent"}, method = RequestMethod.POST)
    public ApiResultBean getContent(@RequestBody ContentAppReturnBean contentAppReturnBean, HttpServletRequest request) {
        if(contentAppReturnBean.getUserId()==null||contentAppReturnBean.getUserId().trim().length()<1){
            return toError("参数不能为空", -1);
        }
        //根据登陆的帐号获取医院
        UserVrRoom userVrRoom = userVrRoomService.getByUserId(getCurrentUserId());
        if(userVrRoom==null){
            return toError("登陆用户类型不正确", -1);
        }
        VrRoom vrRoom = vrRoomService.getOne(userVrRoom.getVrRoomId());
        if(vrRoom==null){
            return toError("登陆的用户没有维护VR室", -1);
        }
        PrescriptionCase prescriptionCase = prescriptionCaseService.getOne(contentAppReturnBean.getUserId());//.findByUserIdAndHospitalIdAndHidden(contentAppReturnBean.getUserId(),vrRoom.getHospital_id(),0);
        if(prescriptionCase==null){
            return toError("找不到对应的患者！", -1);
        }
        Map<String, Object> searchParams = new HashMap<>();
        if (StringUtils.isNotBlank(prescriptionCase.getIcd10())) {
            searchParams.put("EQ_diseaseId", prescriptionCase.getIcd10());
        }

            searchParams.put("IN_type", new Integer[]{
                    Constants.CONTENT_TYPE_AUDIO,
                    Constants.CONTENT_TYPE_VIDEO,
                    Constants.CONTENT_TYPE_GAME,
                    Constants.CONTENT_TYPE_OUTSIDE_GAME});


        searchParams.put("EQ_hidden", 0);
        Page<Content> page = contentService.findBySearchParamsto(searchParams, contentAppReturnBean.getPageBean());
        setConfigFieldValues(page, Constants.CONTENT_TYPE_KEY, "type");
        setConfigFieldValues(page, Constants.CONTENT_STATUS_KEY, "status");
        setConfigFieldValues(page, Global.IF_OR_NOT_ENUM_KEY, "isFree");
       List<Content> contentlist = page.getContent();
        List<PrescriptionContent> prescriptionContentList = new ArrayList<PrescriptionContent>();
        PrescriptionContent prescriptionContent = null;
        for (int i = 0; i < contentlist.size(); i++) {
            Map m = (Map) contentlist.get(i);
            prescriptionContent = new PrescriptionContent();
            prescriptionContent.setContentId(m.get("id").toString());
            prescriptionContentList.add(prescriptionContent);
        }

        return toSuccess("获取患者的内容列表成功",  setFieldValues(prescriptionContentList, Content.class, "contentId", new String[]{"helpCode", "name", "coverPic", "type"}));
    }

    @ApiAuth
    @ApiOperation(value = "获取患者处方列表", notes = "获取患者处方列表", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/prescription/getByPatientId"}, method = RequestMethod.POST)
    public ApiResultBean<List<PrescriptionReturnBean>> getPrescriptionByPatientId(@RequestBody PatientIdBean patientIdBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //根据登陆的帐号获取医院
        UserVrRoom userVrRoom = userVrRoomService.getByUserId(getCurrentUserId());
        if(userVrRoom==null){
            return toError("登陆用户类型不正确", -1);
        }
        VrRoom vrRoom = vrRoomService.getOne(userVrRoom.getVrRoomId());
        if(vrRoom==null){
            return toError("登陆的用户没有维护VR室", -1);
        }

        if(vrRoom.getHospital_id()==null||vrRoom.getHospital_id()==""){
            return toError("当前登陆的医生没有对应的医院，无法新增患者，请联系管理员进行维护！", -2);
        }
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("EQ_hospitalId", vrRoom.getHospital_id());
        searchParams.put("EQ_hidden", 0);

        List<Prescription> PrescriptionList = prescriptionService.findAll(searchParams);
        List<PrescriptionReturnBean> prescriptionReturnBeanList = new ArrayList<PrescriptionReturnBean>();
        PrescriptionReturnBean prescriptionReturnBean = null;
        if (PrescriptionList.size() > 0) {
            for (int i = 0; i < PrescriptionList.size(); i++) {
                Prescription prescription = PrescriptionList.get(i);
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
    @ApiOperation(value = "获取患者处方内容列表", notes = "获取患者处方内容列表", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/prescriptionContent/getByPrescriptionId"}, method = RequestMethod.POST)
    public ApiResultBean getPrescriptionContentByPrescriptionId(@RequestBody PrescriptionIdBean prescriptionIdBean, HttpServletRequest request) {
        Prescription prescription = prescriptionService.getOne(prescriptionIdBean.getPrescriptionId());
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

    private String getCurrentUserId() {
        return SpringSecurityUtils.getCurrentUserId();
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
        UserVrRoom userVrRoom = userVrRoomService.getByUserId(getCurrentUserId());
        if(userVrRoom==null){
            return toError("登陆用户类型不正确", -1);
        }
        VrRoom vrRoom = vrRoomService.getOne(userVrRoom.getVrRoomId());
        if(vrRoom==null){
            return toError("登陆的用户没有维护VR室", -1);
        }
        if(vrRoom.getHospital_id()==null||vrRoom.getHospital_id()==""){
            return toError("当前登陆的用户对应的VR室没有维护医院信息！", -2);
        }
        if(patientCaseAppBean.getClinichistoryNo()==null||patientCaseAppBean.getClinichistoryNo()==""){
            return toError("病历号不能为空", -2);
        }
        Map<String, Object> searchParams = new HashedMap();
        searchParams.put("EQ_hidden",0);
        searchParams.put("EQ_recordno",patientCaseAppBean.getClinichistoryNo());
        searchParams.put("EQ_hospitalId",vrRoom.getHospital_id());
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
        PrescriptionCase presriptionCaseSave = getPrescriptionCase(patientCaseAppBean,vrRoom);
        if (dbUser != null&&dbUser.getHidden()==0) {
            if(diseaseService.getOne(presriptionCaseSave.getIcd10())==null){
                return toError("病症不存在!", -2);
            }
            presriptionCaseSave.setUserId(dbUser.getId());
            prescriptionCaseService.save(presriptionCaseSave);
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
                    prescriptionCaseService.save(presriptionCaseSave);
                }
                if(user!=null&&user.getMobile()!=null&&user.getMobile().length()>=3){
                    //增加市场版本的患者信息表
                    UserPatient userPatient =  getUserPatient(patientCaseAppBean);
                    userPatient.setUserId(user.getId());
                    userPatientService.save(userPatient);
                }
            }else{
                presriptionCaseSave.setUserId("");
                prescriptionCaseService.save(presriptionCaseSave);
            }
        }
        return toSuccess("增加患者成功",presriptionCaseSave );
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
     * 构造PrescriptionCase
     * @param patientCaseAppBean
     * @return
     */
    public PrescriptionCase getPrescriptionCase(PatientCaseAppBean patientCaseAppBean,VrRoom vrRoom){
        PrescriptionCase presc = new PrescriptionCase();
        presc.setName(patientCaseAppBean.getName());
        presc.setRecordno(patientCaseAppBean.getClinichistoryNo());
        presc.setBornDate(patientCaseAppBean.getBirthday());
        presc.setGender(patientCaseAppBean.getSex());
        presc.setMobile(patientCaseAppBean.getPhone());
        presc.setEmergencyContactPhone(patientCaseAppBean.getPhone());
        presc.setEmergencyContact(patientCaseAppBean.getPhone());
        presc.setVrroomid(vrRoom.getId());
        presc.setHospitalId(vrRoom.getHospital_id());
        presc.setMarriage(patientCaseAppBean.getMaritalStatus()!=null?(patientCaseAppBean.getMaritalStatus()==1?"已婚":(patientCaseAppBean.getMaritalStatus()==2?"未婚":(patientCaseAppBean.getMaritalStatus()==0?"保密":""))):"");
        presc.setSchooling(patientCaseAppBean.getEducationDegree()!=null?(patientCaseAppBean.getEducationDegree()==1?"文盲":(patientCaseAppBean.getEducationDegree()==2?"小学":
                (patientCaseAppBean.getEducationDegree()==3?"初中":(patientCaseAppBean.getEducationDegree()==4?"高中":
                        (patientCaseAppBean.getEducationDegree()==5?"大学":(patientCaseAppBean.getEducationDegree()==6?"研究生及以上":"")))))):"");
        presc.setMedicareCardNumber(patientCaseAppBean.getMedicalInsuranceCardNo()==null?"":patientCaseAppBean.getMedicalInsuranceCardNo());
        presc.setIcd10(patientCaseAppBean.getDiseaseId());
        return presc;
    }

    @ApiAuth
    @ApiOperation(value = "获取病症和病症对应的疗法接口", notes = "获取病症和病症对应的疗法接口", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/disease"}, method = RequestMethod.POST)
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




}
