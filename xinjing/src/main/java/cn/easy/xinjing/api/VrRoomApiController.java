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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.utils.text.TextValidator;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenzhongyi on 2016/11/18.
 */
@Api(value = "VrRoom-api-controller", description = "VR室相关API", position = 5)
@Controller
@RequestMapping("/api/v1/vrRoom")
public class VrRoomApiController extends BaseApiController {
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
    private PrescriptionContentFeedbackService prescriptionContentFeedbackService;
    @Autowired
    private VrRoomAppointmentService vrRoomAppointmentService;
    @Autowired
    private UserVrRoomService userVrRoomService;
    @Autowired
    private VrRoomAppTaskService vrRoomAppTaskService;
    @Autowired
    private VrRoomService vrRoomService;

    @Autowired
    private EvaluatingService evaluatingService;

    @Autowired
    private SingleselRecordService singleselRecordService;

    @Autowired
    private SingleselService singleselService;

    @Autowired
    private GameEfsService gameEfsService;

    @Autowired
    private VoideEfsService voideEfsService;


    @Autowired
    private GameheadRecordService gameheadRecordService;
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
        if (userVrRoom == null) {
            return toError("账号类型不正确", -1);
        }
        if (userVrRoom.getType() != Constants.USER_VR_ROOM_TYPE_DESKTOP) {
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
        appToken.setUserType(Constants.USER_TYPE_VR_ROOM);
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
    @ApiOperation(value = "患者信息添加成功", notes = "患者信息添加成功", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/patient/add"}, method = RequestMethod.POST)
    public ApiResultBean addPatient(@RequestBody PatientBean patientBean, HttpServletRequest request) {
        User dbUser = userService.getByUsername(patientBean.getMobile());
        if (dbUser != null) {
            return toError("该手机号已经存在，请勿重复注册", -2);
        }

        User user = new User();
        UserPatient userPatient = new UserPatient();
        try {
            PropertyUtils.copyProperties(user, patientBean);
            PropertyUtils.copyProperties(userPatient, patientBean);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        user.setUsername(user.getMobile());
        user = userService.save(user);

        userPatient.setUserId(user.getId());
        userPatientService.save(userPatient);

        patientBean.setUserId(user.getId());

        return toSuccess("患者信息添加成功", patientBean);
    }

    @ApiAuth
    @ApiOperation(value = "获取病种类型", notes = "获取病种类型", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/disease"}, method = RequestMethod.POST)
    public ApiResultBean disease(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {
        return toSuccess("获取病种类型成功", diseaseService.findByStatus(Constants.DISEASE_STATUS_PUBLISH));
    }

    @ApiAuth
    @ApiOperation(value = "获取疗法类型", notes = "获取疗法类型", position = 10)
    @ResponseBody
    @RequestMapping(value = {"/therapy"}, method = RequestMethod.POST)
    public ApiResultBean therapy(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {
        return toSuccess("获取疗法类型成功", therapyService.findByStatus(Constants.THERAPY_STATUS_PUBLISH));
    }


    @ApiAuth
    @ApiOperation(value = "获取内容类型", notes = "获取内容类型", position = 10)
    @ResponseBody
    @RequestMapping(value = {"/contentType"}, method = RequestMethod.POST)
    public ApiResultBean contentType(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {
        return toSuccess("获取疗法类型成功", therapyService.findByStatus(Constants.THERAPY_STATUS_PUBLISH));
    }

    @ApiAuth
    @ApiOperation(value = "获取内容", notes = "获取内容", position = 7)
    @ResponseBody
    @RequestMapping(value = {"/content/search"}, method = RequestMethod.POST)
    public ApiResultBean contentSearch(@RequestBody ContentSearchBean contentSearchBean, HttpServletRequest request) {
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
            searchParams.put("EQ_type", contentSearchBean.getType());
        }
        if (StringUtils.isNotBlank(contentSearchBean.getKeyword())) {
            searchParams.put("LIKE_name", contentSearchBean.getKeyword());
        }

        Page<Content> page = contentService.findBySearchParams(searchParams, contentSearchBean.getPageBean());
        setConfigFieldValues(page, Constants.CONTENT_TYPE_KEY, "type");
        setConfigFieldValues(page, Constants.CONTENT_STATUS_KEY, "status");
        setConfigFieldValues(page, Global.IF_OR_NOT_ENUM_KEY, "isFree");
        return toSuccess("获取内容成功", page.getContent());
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
    @ApiOperation(value = "获取患者处方列表", notes = "获取患者处方列表", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/prescription/getByPatientId"}, method = RequestMethod.POST)
    public ApiResultBean<List<Map<String, Object>>> getPrescriptionByPatientId(@RequestBody PatientIdBean patientIdBean, HttpServletRequest request) {
        //根据VRID获取医院id
        VrRoom vrRoom = vrRoomService.getOne(patientIdBean.getVrRoomId());

        List<Prescription> prescriptionList = prescriptionService.findByPatientIdAndHospitalIdAndHiddenOrSource(patientIdBean.getPatientId(),(vrRoom==null?"":vrRoom.getHospital_id()),0,1);
        return toSuccess("获取患者处方列表成功", setFieldValues(prescriptionList, Hospital.class, "hospitalId", new String[]{"name"}));

    }

    @ApiAuth
    @ApiOperation(value = "获取患者处方内容列表", notes = "获取患者处方内容列表", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/prescriptionContent/getByPrescriptionId"}, method = RequestMethod.POST)
    public ApiResultBean getPrescriptionContentByPrescriptionId(@RequestBody PrescriptionIdBean prescriptionIdBean, HttpServletRequest request) {
        List<PrescriptionContent> prescriptionContentList = prescriptionContentService.findByPrescriptionId(prescriptionIdBean.getPrescriptionId());

        return toSuccess("获取患者处方内容列表成功", setFieldValues(prescriptionContentList, Content.class, "contentId", new String[]{"helpCode", "name", "type"}));
    }

    @ApiAuth
    @ApiOperation(value = "为患者开处方", notes = "为患者开处方", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/prescription/add"}, method = RequestMethod.POST)
    public ApiResultBean prescriptionAdd(@RequestBody PrescriptionBean prescriptionBean, HttpServletRequest request) {
        Prescription prescription;
        try {
            prescriptionBean.setSource(Constants.PRESCRIPTION_SOURCE_OFFLINE);
            prescription = prescriptionService.save(prescriptionBean);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return toError("开处方失败", -2);
        }

        return toSuccess("开处方成功", buildData("prescriptionId", prescription.getId()));
    }

    @ApiAuth
    @ApiOperation(value = "修改患者处方", notes = "修改患者处方", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/prescription/save"}, method = RequestMethod.POST)
    public ApiResultBean prescriptionSave(@RequestBody PrescriptionBean prescriptionBean, HttpServletRequest request) {
        Prescription prescription;
        try {
            prescriptionBean.setSource(Constants.PRESCRIPTION_SOURCE_OFFLINE);
            prescription = prescriptionService.save(prescriptionBean);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return toError("处方修改失败", -2);
        }

        return toSuccess("处方修改成功", buildData("prescriptionId", prescription.getId()));
    }

    @ApiAuth
    @ApiOperation(value = "中止患者处方", notes = "中止患者处方", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/prescription/suspend"}, method = RequestMethod.POST)
    public ApiResultBean prescriptionSuspend(@RequestBody PrescriptionIdBean prescriptionIdBean, HttpServletRequest request) {
        Prescription prescription = prescriptionService.suspend(prescriptionIdBean.getPrescriptionId());

        return toSuccess("处方中止成功", buildData("prescriptionId", prescription.getId()));
    }

    @ApiAuth
    @ApiOperation(value = "为患者开增加治疗记录", notes = "为患者开增加治疗记录", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/prescriptionContentFeedback/add"}, method = RequestMethod.POST)
    public ApiResultBean prescriptionContentFeedbackAdd(@RequestBody PrescriptionContentIdBean prescriptionContentIdBean, HttpServletRequest request) {
        prescriptionContentFeedbackService.add(prescriptionContentIdBean.getPrescriptionContentId());
        return toSuccess("为患者开增加治疗记录成功");
    }

    @ApiAuth
    @ApiOperation(value = "获取预约记录", notes = "获取预约记录", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/vrRoomAppointment/search"}, method = RequestMethod.POST)
    public ApiResultBean vrRoomAppointmentSearch(@RequestBody VrRoomAppointmentSearchBean vrRoomAppointmentSearchBean, HttpServletRequest request) {
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("EQ_vrRoomId", vrRoomAppointmentSearchBean.getVrRoomId());
        if (StringUtils.isNotBlank(vrRoomAppointmentSearchBean.getKeyword())) {
            searchParams.put("LIKE_bookingContact", vrRoomAppointmentSearchBean.getKeyword());
        }
        Page<VrRoomAppointment> page = vrRoomAppointmentService.search(searchParams, vrRoomAppointmentSearchBean.getPageBean());

        return toSuccess("获取预约记录成功", page.getContent());
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
    @ApiOperation(value = "获取评测信息", notes = "获取评测信息", position = 7)
    @ResponseBody
    @RequestMapping(value = {"/evaluatingInfo"}, method = RequestMethod.POST)
    public ApiResultBean evaluatingInfo(@RequestBody ApiBasePageBean apiBasePageBean, HttpServletRequest request) {
        Map<String, Object> searchParams = new HashMap<>();


        List<Evaluating> evaluatingList = evaluatingService.findByStatusAndHidden(2,0);
        Evaluating evaluating = new Evaluating();
        if(evaluatingList.size()>=1){
            evaluating = evaluatingList.get(0);
        }
        return toSuccess("获取获取评测信息成功", evaluating);
    }


    @ApiAuth
    @ApiOperation(value = "获取评测记录", notes = "获取评测记录", position = 7)
    @ResponseBody
    @RequestMapping(value = {"/singleselRecord"}, method = RequestMethod.POST)
    public ApiResultBean singleselRecord(@RequestBody SingleselRecordBean singleselRecordBean, HttpServletRequest request) {

        List<SingleselRecord> singleselRecordList = singleselRecordService
                .findByHiddenAndEvaluatingIdOrderByCreatedAtDesc(0,singleselRecordBean.getEvaluatingId());
        for (SingleselRecord singleselRecord : singleselRecordList) {
            Evaluating evaluating = evaluatingService.getOne(singleselRecord.getEvaluatingId());
            if(evaluating==null){
                singleselRecord.setEvaluatingId("");
            }else{
                singleselRecord.setEvaluatingId(evaluating.getName());
            }
        }
        return toSuccess("获取获取评测记录成功", singleselRecordList);
    }

    @ApiAuth
    @ApiOperation(value = "获取评测题目", notes = "获取评测题目", position = 7)
    @ResponseBody
    @RequestMapping(value = {"/singleselInfo"}, method = RequestMethod.POST)
    public ApiResultBean singleselInfo(@RequestBody SingleselBean singleselBean, HttpServletRequest request) {
        List<Singlesel> singleselList = singleselService.findByHiddenAndEvaluatingIdOrderByCreatedAtDesc(0,singleselBean.getEvaluatingId());

        return toSuccess("获取获取评测题目成功", singleselList);
    }


    @ApiAuth
    @ApiOperation(value = "保存评测记录", notes = "保存评测记录", position = 7)
    @ResponseBody
    @RequestMapping(value = {"/saveSsingleselRecord/add"}, method = RequestMethod.POST)
    public ApiResultBean saveSsingleselRecord(@RequestBody SingleselRecordBean singleselRecordBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        SingleselRecord singleselRecord = new SingleselRecord();
        PropertyUtils.copyProperties(singleselRecord, singleselRecordBean);
        singleselRecordService.save(singleselRecord);


        return toSuccess("添加任务成功");
    }

    @ApiAuth
    @ApiOperation(value = "获取内容新版", notes = "获取内容新版", position = 7)
    @ResponseBody
    @RequestMapping(value = {"/content/searchnew"}, method = RequestMethod.POST)
    public ApiResultBean contentSearchnew(@RequestBody ContentSearchBean contentSearchBean, HttpServletRequest request) {
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
            searchParams.put("EQ_type", contentSearchBean.getType());
        }
        if (StringUtils.isNotBlank(contentSearchBean.getKeyword())) {
            searchParams.put("LIKE_name", contentSearchBean.getKeyword());
        }

        Page<Content> page = contentService.findBySearchParams(searchParams, contentSearchBean.getPageBean());
        setConfigFieldValues(page, Constants.CONTENT_TYPE_KEY, "type");
        setConfigFieldValues(page, Constants.CONTENT_STATUS_KEY, "status");
        setConfigFieldValues(page, Global.IF_OR_NOT_ENUM_KEY, "isFree");
        return toSuccess("获取内容成功", page);
    }




    @ApiAuth
    @ApiOperation(value = "游戏加密验证（游戏端调用）", notes = "游戏加密验证（游戏端调用）", position = 5)
    @ResponseBody
    @RequestMapping(value = {"/gameEfs"}, method = RequestMethod.POST)
    public ApiResultBean gameEfs(@RequestBody GameEfsBean gameEfsBean,
                                 HttpServletRequest request) {
        UserVrRoom userVrRoom = userVrRoomService.getByUserId(gameEfsBean.getLoginuserid());
        if(userVrRoom==null){
            return toError("校验失败",-1);
        }
        Map<String, Object> searchParams = new HashedMap();
        searchParams.put("EQ_ip",gameEfsBean.getIp());
        searchParams.put("EQ_mac",gameEfsBean.getMac());
        searchParams.put("EQ_vruser",userVrRoom.getId());
        searchParams.put("EQ_hidden",0);
        List<GameEfs> gameefslist = gameEfsService.search(searchParams);
        if(gameefslist.size()>=1){
            return toSuccess("校验成功");
        }else{

            return toError("校验失败",-1);
        }

    }

    @ApiOperation(value = "游戏加密验证（VR室调用）", notes = "游戏加密验证（VR室调用）", position = 5)
    @ResponseBody
    @RequestMapping(value = {"/gameEfsVr"}, method = RequestMethod.POST)
    public ApiResultBean gameEfsVr(@RequestBody GameEfs gameEfs,
                                 HttpServletRequest request) {
        UserVrRoom userVrRoom = userVrRoomService.getByUserId(gameEfs.getVruser());
        if(userVrRoom==null){
            return toError("校验失败",-1);
        }
        Map<String, Object> searchParams = new HashedMap();
        searchParams.put("EQ_ip",gameEfs.getIp());
        searchParams.put("EQ_mac",gameEfs.getMac());
        searchParams.put("EQ_vruser",userVrRoom.getId());
        searchParams.put("EQ_hidden",0);
        List<GameEfs> gameefslist = gameEfsService.search(searchParams);
        if(gameefslist.size()>=1){
            return toSuccess("校验成功");
        }else{

            return toError("校验失败",-1);
        }

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
        Map<String, Object> searchParams = new HashedMap();
        searchParams.put("EQ_ip",gameEfsHeadBean.getIp());
        searchParams.put("EQ_mac",gameEfsHeadBean.getMac());
        searchParams.put("EQ_vruser",userVrRoom.getId());
        searchParams.put("EQ_hidden",0);
        List<GameEfs> gameefslist = gameEfsService.search(searchParams);
        if(gameefslist.size()>=1){
            GameheadRecord gameheadRecord = new GameheadRecord();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(gameEfsHeadBean.getClickdatatime());
            gameEfsHeadBean.setClickdatatime(null);
            PropertyUtils.copyProperties(gameheadRecord, gameEfsHeadBean);
            gameheadRecord.setClickdatatime(date);
            gameheadRecordService.save(gameheadRecord);

            List<GameEfsItemsBean> gameEfsItemsBeanList = gameEfsHeadBean.getItems();
            for (int i = 0; i < gameEfsItemsBeanList.size(); i++) {
                GameEfsItemsBean gameitem = gameEfsItemsBeanList.get(i);
                if("1".equals(gameitem.getGamephase())){
                    GameitemRecord01 gamitem01 = new GameitemRecord01();
                    PropertyUtils.copyProperties(gamitem01, gameitem);
                    gamitem01.setGameheadRecordId(gameheadRecord.getId());
                    gameitemRecord01Service.save(gamitem01);
                }else if("2".equals(gameitem.getGamephase())){
                    GameitemRecord02 gamitem02 = new GameitemRecord02();
                    PropertyUtils.copyProperties(gamitem02, gameitem);
                    gamitem02.setGameheadRecordId(gameheadRecord.getId());
                    gameitemRecord02Service.save(gamitem02);
                }else if("3".equals(gameitem.getGamephase())){
                    GameitemRecord03 gamitem03 = new GameitemRecord03();
                    PropertyUtils.copyProperties(gamitem03, gameitem);
                    gamitem03.setGameheadRecordId(gameheadRecord.getId());
                    gameitemRecord03Service.save(gamitem03);
                }else if("4".equals(gameitem.getGamephase())){
                    GameitemRecord04 gamitem04 = new GameitemRecord04();
                    PropertyUtils.copyProperties(gamitem04, gameitem);
                    gamitem04.setGameheadRecordId(gameheadRecord.getId());
                    gameitemRecord04Service.save(gamitem04);
                }else if("5".equals(gameitem.getGamephase())){
                    GameitemRecord05 gamitem05 = new GameitemRecord05();
                    PropertyUtils.copyProperties(gamitem05, gameitem);
                    gamitem05.setGameheadRecordId(gameheadRecord.getId());
                    gameitemRecord05Service.save(gamitem05);
                }
            }
        }else{
            return toError("校验失败",-1);
        }
        return toSuccess("接受成功");
    }




}
