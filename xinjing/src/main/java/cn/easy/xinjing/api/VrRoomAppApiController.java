package cn.easy.xinjing.api;

import cn.easy.base.api.BaseApiController;
import cn.easy.base.bean.PageBean;
import cn.easy.base.bean.common.ApiResultBean;
import cn.easy.base.domain.User;
import cn.easy.base.service.ConfigService;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.utils.text.TextValidator;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by caosk on 2017/02/07.
 */
@Api(value = "VrRoom-app-api-controller", description = "VR室APP相关API", position = 5)
@Controller
@RequestMapping("/api/v1/appVrRoom")
public class VrRoomAppApiController extends BaseApiController {
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
    private ConfigService configService;
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
    private SectionOfficeService sectionOfficeService;
    @Autowired
    private AppVersionService appVersionService;
    @Autowired
    private VrRoomAppTaskService vrRoomAppTaskService;
    @Autowired
    private VrRoomService vrRoomService;

    @Autowired
    private VoideEfsService voideEfsService;

    @ApiOperation(value = "获取版本信息", notes = "获取版本信息", position = 1)
    @ResponseBody
    @RequestMapping(value = {"/getVersion"}, method = RequestMethod.POST)
    public ApiResultBean<AppVersion> getVersion(@RequestBody VersionBean versionBean) {
        AppVersion appVersion = appVersionService.getByAppCode(Constants.APP_CLIENT_VR_ANDROID);
        return toSuccess("获取成功", appVersion);
    }

    @ApiOperation(value = "登录", notes = "登录", position = 3)
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
        if(userVrRoom.getType() != Constants.USER_VR_ROOM_TYPE_APP) {
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
        appToken.setUserType(Constants.USER_TYPE_APP_VR_ROOM);
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
    @ApiOperation(value = "退出登录", notes = "退出登录", position = 5)
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
    @ApiOperation(value = "修改密码", notes = "修改密码", position = 7)
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

    @ApiOperation(value = "获取字典值", notes = "获取字典值", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/config"}, method = RequestMethod.POST)
    public ApiResultBean<List<ConfigBean>> config(@RequestBody ConfigCodeBean configCodeBean, HttpServletRequest request) {

        return toSuccess("获取字典值成功", ProjectUtil.getConfigBeanList(configCodeBean.getCode()));
    }

    @ApiAuth
    @ApiOperation(value = "根据条件查询患者信息", notes = "根据条件查询患者信息", position = 11)
    @ResponseBody
    @RequestMapping(value = {"/patient/getByKeyword"}, method = RequestMethod.POST)
    public ApiResultBean<PatientBean> getPatientByKeyword(@RequestBody KeywordBean keywordBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
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

    @ApiOperation(value = "获取科室类型", notes = "获取科室类型", position = 13)
    @ResponseBody
    @RequestMapping(value = {"/sectionOffice"}, method = RequestMethod.POST)
    public ApiResultBean<List<SectionOffice>> sectionOffice(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {
        return toSuccess("获取科室类型成功", sectionOfficeService.findByStatus(Constants.SECTION_OFFICE_STATUS_PUBLISH));
    }

    @ApiOperation(value = "获取病种类型", notes = "获取病种类型", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/disease"}, method = RequestMethod.POST)
    public ApiResultBean<List<Disease>> disease(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {
        return toSuccess("获取病种类型成功", diseaseService.findByStatus(Constants.DISEASE_STATUS_PUBLISH));
    }

    @ApiOperation(value = "获取疗法类型", notes = "获取疗法类型", position = 17)
    @ResponseBody
    @RequestMapping(value = {"/therapy"}, method = RequestMethod.POST)
    public ApiResultBean<List<Therapy>> therapy(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {

        return toSuccess("获取疗法类型成功", therapyService.findByStatus(Constants.THERAPY_STATUS_PUBLISH));
    }

    @ApiAuth
    @ApiOperation(value = "获取内容", notes = "获取内容", position = 19)
    @ResponseBody
    @RequestMapping(value = {"/content/search"}, method = RequestMethod.POST)
    public ApiResultBean<List<Content>> contentSearch(@RequestBody ContentSearchBean contentSearchBean, HttpServletRequest request) {
        Map<String, Object> searchParams = new HashMap<>();
        if (StringUtils.isNotBlank(contentSearchBean.getDiseaseId())) {
            searchParams.put("EQ_diseaseId", contentSearchBean.getDiseaseId());
        }
        if (StringUtils.isNotBlank(contentSearchBean.getTherapyId())) {
            searchParams.put("EQ_therapyId", contentSearchBean.getTherapyId());
        }
        if(contentSearchBean.getType() == null || contentSearchBean.getType().intValue() <= 0) {
            searchParams.put("IN_type", new Integer[]{
                    Constants.CONTENT_TYPE_AUDIO,
                    Constants.CONTENT_TYPE_VIDEO,
                    Constants.CONTENT_TYPE_GAME,
                    Constants.CONTENT_TYPE_OUTSIDE_GAME});
        }
        else {
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
    @ApiOperation(value = "获取单个内容", notes = "获取单个内容", position = 21)
    @ResponseBody
    @RequestMapping(value = {"/content/info"}, method = RequestMethod.POST)
    public ApiResultBean<Map<String, Object>> contentInfo(@RequestBody ContentIdBean contentIdBean, HttpServletRequest request) {
        Content content = contentService.getOne(contentIdBean.getContentId());
        Map<String, Object> contentMap = ExtractUtil.object2Map(content);

        contentMap.put("ext", contentService.getExt(content.getId(), content.getType()));
        return toSuccess("获取单个内容成功", contentMap);
    }

    @ApiAuth
    @ApiOperation(value = "获取患者处方列表", notes = "获取患者处方列表", position = 23)
    @ResponseBody
    @RequestMapping(value = {"/prescription/getByPatientId"}, method = RequestMethod.POST)
    public ApiResultBean<List<Prescription>> getPrescriptionByPatientId(@RequestBody PatientIdBean patientIdBean, HttpServletRequest request) {
        List<Prescription> prescriptionList = prescriptionService.findByPatientIdAndHidden(patientIdBean.getPatientId(),0);


        return toSuccess("获取患者处方列表成功", prescriptionList);
    }

    @ApiAuth
    @ApiOperation(value = "获取患者处方内容列表", notes = "获取患者处方内容列表", position = 25)
    @ResponseBody
    @RequestMapping(value = {"/prescriptionContent/getByPrescriptionId"}, method = RequestMethod.POST)
    public ApiResultBean getPrescriptionContentByPrescriptionId(@RequestBody PrescriptionIdBean prescriptionIdBean, HttpServletRequest request) {
        List<PrescriptionContent> prescriptionContentList = prescriptionContentService.findByPrescriptionId(prescriptionIdBean.getPrescriptionId());


        return toSuccess("获取患者处方内容列表成功", setFieldValues(prescriptionContentList, Content.class, "contentId", new String[]{"helpCode", "name", "type"}));
    }

    @ApiAuth
    @ApiOperation(value = "为患者开增加治疗记录", notes = "为患者开增加治疗记录", position = 27)
    @ResponseBody
    @RequestMapping(value = {"/prescriptionContentFeedback/add"}, method = RequestMethod.POST)
    public ApiResultBean prescriptionContentFeedbackAdd(@RequestBody PrescriptionContentIdBean prescriptionContentIdBean, HttpServletRequest request) {
        prescriptionContentFeedbackService.add(prescriptionContentIdBean.getPrescriptionContentId());
        return toSuccess("为患者开增加治疗记录成功");
    }

    @ApiAuth
    @ApiOperation(value = "获取同步内容", notes = "获取同步内容", position = 29)
    @ResponseBody
    @RequestMapping(value = {"/content/sync"}, method = RequestMethod.POST)
    public ApiResultBean<List<Content>> contentSync(@RequestBody ContentSyncBean contentSyncBean, HttpServletRequest request) {
        Map<String, Object> searchParams = new HashMap<>();

        searchParams.put("IN_type", new Integer[]{
                Constants.CONTENT_TYPE_AUDIO,
                Constants.CONTENT_TYPE_VIDEO});

        if (StringUtils.isNotBlank(contentSyncBean.getLastSyncAt())) {
            searchParams.put("GTE_videoupdateAt", contentSyncBean.getLastSyncAt());
        }


        Page<Content> page = contentService.findBySearchParams(searchParams, contentSyncBean.getPageBean());
        return toSuccess("获取同步内容成功", page.getContent());
    }

    @ApiAuth
    @ApiOperation(value = "获取任务列表", notes = "获取任务列表", position = 29)
    @ResponseBody
    @RequestMapping(value = {"/task/list"}, method = RequestMethod.POST)
    public ApiResultBean<List<VrRoomAppTaskReturnBean>> taskList(@RequestBody ApiBaseBean apiBaseBean) {
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("EQ_userId", getCurrentUserId());
        searchParams.put("EQ_status", Constants.VR_ROOM_APP_TASK_STATUS_INIT);
        List<VrRoomAppTask> vrRoomAppTaskList = vrRoomAppTaskService.search(searchParams);
        List<VrRoomAppTaskReturnBean> vrRoomAppTaskReturnBeanList =  new ArrayList<VrRoomAppTaskReturnBean>();
        VoideEfs voideEfs = voideEfsService.getOne("8a8c7e235cc98126015cc9824cc40002");
        VrRoomAppTaskReturnBean vrRoomAppTaskReturnBean = null;
        //如果是播放指令，则将任务状态改为播放，其他类型改为已结束
        for(VrRoomAppTask vrRoomAppTask : vrRoomAppTaskList) {
            if(vrRoomAppTask.isPlayType()) {
                vrRoomAppTask.setStatus(Constants.VR_ROOM_APP_TASK_STATUS_PLAY);
//                prescriptionContentFeedbackService.add(vrRoomAppTask.getPrescriptionContentId());
            }
            else {
                vrRoomAppTask.setStatus(Constants.VR_ROOM_APP_TASK_STATUS_END);
            }
            vrRoomAppTaskReturnBean = new VrRoomAppTaskReturnBean();
            vrRoomAppTaskReturnBean.setVoidpassword(voideEfs==null?0:voideEfs.getPassword());
            BeanUtils.copyProperties(vrRoomAppTask, vrRoomAppTaskReturnBean);
            vrRoomAppTaskReturnBeanList.add(vrRoomAppTaskReturnBean);

        }
        vrRoomAppTaskService.save(vrRoomAppTaskList);

        return toSuccess("获取任务列表成功", vrRoomAppTaskReturnBeanList);
    }

    @ApiAuth
    @ApiOperation(value = "结束任务", notes = "结束任务", position = 29)
    @ResponseBody
    @RequestMapping(value = {"/task/end"}, method = RequestMethod.POST)
    public ApiResultBean taskEnd(@RequestBody VrRoomAppTaskIdBean vrRoomAppTaskIdBean) {
        VrRoomAppTask vrRoomAppTask = vrRoomAppTaskService.getOne(vrRoomAppTaskIdBean.getVrRoomAppTaskId());
        if(vrRoomAppTask == null) {
            return toError("找不到相应的任务", -1);
        }

        vrRoomAppTask.setStatus(Constants.VR_ROOM_APP_TASK_STATUS_END);
        vrRoomAppTaskService.save(vrRoomAppTask);
        return toSuccess("结束任务成功");
    }

    private String getCurrentUserId() {
        return SpringSecurityUtils.getCurrentUserId();
    }
}
