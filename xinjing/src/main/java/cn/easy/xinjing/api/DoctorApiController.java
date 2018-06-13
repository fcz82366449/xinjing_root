package cn.easy.xinjing.api;

import cn.easy.base.bean.common.ApiResultBean;
import cn.easy.base.domain.User;
import cn.easy.base.service.AutoNoConfigService;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by chenzhongyi on 16/9/12.
 */
@Api(value = "doctor-api-controller", description = "医生相关API", position = 5)
@Controller
@RequestMapping("/api/v1/doctor")
public class DoctorApiController extends XjBaseApiController {
    @Autowired
    private FrontpageService	frontpageService;
    @Autowired
    private AppVersionService appVersionService;
    @Autowired
    private DoctorBankCardService doctorBankCardService;
    @Autowired
    private AdvertisementService advertisementService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserDoctorApproveService userDoctorApproveService;
    @Autowired
    private DiseaseTherapyService diseaseTherapyService;
    @Autowired
    private UserDoctorService userDoctorService;
    @Autowired
    private TherapyContentService therapyContentService;
    @Autowired
    private DiseaseContentService diseaseContentService;

    @Autowired
    private BanksService banksService;

    @Autowired
    private ChargesService chargesService;

    @Autowired
    private CalllimitService calllimitService;
    @Autowired
    private FrontpageCollectService frontpageCollectService;

    @ApiOperation(value = "登录", notes = "登录", position = 5)
    @ResponseBody
    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public ApiResultBean login(@RequestBody LoginBean loginBean, HttpServletRequest request) {
        return doLogin(loginBean, Constants.USER_TYPE_DOCTOR, request);
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
    @ApiOperation(value = "首页内容(个人信息)", notes = "首页内容(个人信息)", position = 33)
    @ResponseBody
    @RequestMapping(value = {"/homepage/contents"}, method = RequestMethod.POST)
    public ApiResultBean<Map<String, Object>> contents(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {
        //首页轮播图片,不限制数量
        Map<String, Object> searchMap = new HashedMap();
        List<Advertisement> advertisements = advertisementService.search(searchMap);
        //医生信息
        User user = userService.getOne(getCurrentUserId());
        UserDoctor userDoctor = userDoctorService.getByUserId(getCurrentUserId());
        UserDoctorApprove userDoctorApprove = userDoctorApproveService.findLastByUserId(getCurrentUserId());
        //关联信息
        List<DoctorPatient> doctorPatients = doctorPatientService.findByDoctorId(userDoctor.getId());
        searchMap.put("EQ_objectId", userDoctor.getId());
        searchMap.put("EQ_objectType", Constants.COMMENT_TYPE_DOCTOR);
        List<Comment> comments = commentService.search(searchMap);
        Map<String, Object> resultMap = new HashedMap() {{
            int status = userDoctor.getStatus();
            if(userDoctorApprove!=null&&userDoctorApprove.getStatus()==3){//如果此刻医生状态是认证不通过，则在调用个人首页时候给的状态就是认证不通过，而不是未审核
                status = userDoctorApprove.getStatus();
            }
            put("urls", ExtractUtil.extractToList(advertisements, "pic"));
            put("realname", user.getRealname());
            put("username", user.getUsername());
            put("status",status );
            put("headPictureUrl", userDoctor.getHeadPictureUrl());
            put("patientsNumber", doctorPatients.size());
            put("evaluationsNumber", comments.size());
            put("pointsNumber", userDoctor.getPoint());
        }};
        return toSuccess("获取成功", resultMap);
    }

    @ApiAuth
    @ApiOperation(value = "获取我的患者", notes = "获取我的患者", position = 34)
    @ResponseBody
    @RequestMapping(value = {"/myPatients"}, method = RequestMethod.POST)
    public ApiResultBean myPatients(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) throws PinyinException {
//        UserDoctor userDoctor = userDoctorService.getByUserId(getCurrentUserId());
        List<DoctorPatient> doctorPatients = doctorPatientService.findByDoctorId(getCurrentUserId());
        if(doctorPatients.isEmpty()){
            return toSuccess("该医生没有关联的患者", new ArrayList<Map<String, String>>() );
        }

        List<String> patientId = ExtractUtil.extractToList(doctorPatients, "patientId");
        Map<String, Object> searchMap = new HashedMap() {{
            put("IN_userId", StringUtils.join(patientId, ","));
        }};
        List<UserPatient> patients = userPatientService.search(searchMap);
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>() {{
            for (UserPatient patient : patients) {
                add(new HashedMap() {{
                    put("userId", patient.getUserId());
                    put("avatarUrl", patient.getHeadPictureUrl());
                    if( patient.getUser()!=null){
                        String realnameFirstSpell = StringUtils.isEmpty(patient.getUser().getRealname()) ? patient.getUser().getMobile() : patient.getUser().getRealname();
                        put("username", patient.getUser().getUsername());
                        put("realname",realnameFirstSpell);

                        if(realnameFirstSpell!=null&&realnameFirstSpell.length()>=1){
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
                        }else{
                            realnameFirstSpell="";
                        }
                        put("realnameFirstSpell",realnameFirstSpell);
                    }else{
                        put("username", "");
                        put("realname","");
                        put("realnameFirstSpell","");
                    }
                }});
            }
        }};
        return toSuccess("获取成功", dataList);
    }

    @ApiAuth
    @ApiOperation(value = "获取病症和病症对应的疗法", notes = "获取病症和病症对应的疗法", position = 35)
    @ResponseBody
    @RequestMapping(value = {"/content/diseasesAndTherapies"}, method = RequestMethod.POST)
    public ApiResultBean diseasesAndTherapies(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {

        return toSuccess("获取成功");
    }

    @ApiOperation(value = "注册", notes = "注册", position = 1)
    @ResponseBody
    @RequestMapping(value = {"/register"}, method = RequestMethod.POST)
    public ApiResultBean register(@RequestBody DoctorRegisterBean doctorRegisterBean, HttpServletRequest request) {
        if (!validateCaptcha(doctorRegisterBean.getCaptcha(), doctorRegisterBean.getUsername())) {
            logger.info("captcha=====" + doctorRegisterBean.getCaptcha() + ", session captcha====" + getCaptcha(doctorRegisterBean.getUsername()) + ", valid failed");
            return toError("验证码错误", -1);
        }

        User dbUser = userService.getByUsername(doctorRegisterBean.getUsername());
        if (dbUser != null) {
            return toError("该用户名已经存在，请勿重复注册", -2);
        }

        //生成加密密码
        String encryptPw = BaseUtils.encodePassword(doctorRegisterBean.getPassword());

        //创建环信IM账号
        if(!registerIMUser(doctorRegisterBean.getUsername(), encryptPw)){
            return toError("IM 账号创建失败", -3);
        }

        doctorRegisterBean.setPassword(encryptPw);
        userDoctorService.save(doctorRegisterBean);

        return toSuccess("注册成功");
    }

    @ApiOperation(value = "获取验证码", notes = "获取短信验证码", position = 2)
    @ResponseBody
    @RequestMapping(value = {"/sendCode"}, method = RequestMethod.POST)
    public ApiResultBean sendCode(@RequestBody SendCodeBean sendCodeBean, HttpServletRequest request) {
        return doSendCode(sendCodeBean.getMobile(), request);
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
    @ApiOperation(value = "全部职称", notes = "全部职称", position = 6)
    @ResponseBody
    @RequestMapping(value = {"/professionalTitles"}, method = RequestMethod.POST)
    public ApiResultBean<List<ConfigBean>> professionalTitles(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {
        return toSuccess("获取全部职称成功", ProjectUtil.getConfigBeanList(Constants.PROFESSIONAL_TITLES_ENUM_KEY));
    }

    @ApiAuth
    @ApiOperation(value = "认证", notes = "认证", position = 4)
    @ResponseBody
    @RequestMapping(value = {"/auth"}, method = RequestMethod.POST)
    public ApiResultBean auth(@RequestBody DoctorAuthBean doctorAuthBean, HttpServletRequest request) {
        try {
            userDoctorService.auth(doctorAuthBean, getCurrentUserId());
        } catch (ApiException e) {
            return toError(e.getMessgae(), e.getCode());
        }
        return toSuccess("认证信息提交成功，等待审核");
    }

    @ApiAuth
    @ApiOperation(value = "获取认证信息", notes = "获取认证信息", position = 4)
    @ResponseBody
    @RequestMapping(value = {"/authInfo"}, method = RequestMethod.POST)
    public ApiResultBean<DoctorAuthBean> authInfo(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {
        UserDoctorApprove userDoctorApprove = userDoctorApproveService.findLastByUserId(getCurrentUserId());
        //如果是已经认证通过，则返回医生管理中的状态
        if(userDoctorApprove!=null&&userDoctorApprove.getStatus()==4){
           UserDoctor userDoctor = userDoctorService.getByUserId(userDoctorApprove.getUserId());
            if(userDoctor!=null){
                userDoctorApprove.setStatus(userDoctor.getStatus());
            }
        }
        DoctorAuthBean doctorAuthBean = null;
        if (userDoctorApprove != null) {
            doctorAuthBean = new DoctorAuthBean();

            BeanUtils.copyProperties(userDoctorApprove, doctorAuthBean);

            String regionFullName = null;
            if (StringUtils.isNotBlank(doctorAuthBean.getRegion())) {
                regionFullName = areaService.getFullName(doctorAuthBean.getRegion());
            }
            doctorAuthBean.setRegionFullName(regionFullName);

            doctorAuthBean.setProfessionalTitle(getConfigValue(Constants.PROFESSIONAL_TITLES_ENUM_KEY, doctorAuthBean.getProfessionalTitleId()));
        }

        return toSuccess("获取认证信息成功", doctorAuthBean);
    }


    //TODO 改动到这里了

    @ApiAuth
    @ApiOperation(value = "我的银行卡", notes = "我的银行卡", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/bankCard/get"}, method = RequestMethod.POST)
    public ApiResultBean<DoctorBankReturnBean> bankCardGet(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
       System.out.println(getCurrentUserId());
        DoctorBankCard doctorBankCard = doctorBankCardService.findByDoctorIdAndhidden(getCurrentUserId(),0);
        DoctorBankReturnBean doctorBankReturnBean = new DoctorBankReturnBean();
        if(doctorBankCard!=null){
            Banks banks = banksService.getOne(doctorBankCard.getBankId());
            String backId = (banks ==null?"":banks.getId());
            String backName = (banks ==null?"":banks.getName());
            doctorBankReturnBean.setBankId(backId);
            doctorBankReturnBean.setBankName(backName);
            BeanUtils.copyProperties(doctorBankCard, doctorBankReturnBean);
        }

        return toSuccess("获取我的银行卡成功",doctorBankReturnBean);
    }

    @ApiAuth
    @ApiOperation(value = "保存银行卡", notes = "保存银行卡", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/bankCard/save"}, method = RequestMethod.POST)
    public ApiResultBean bankCardSave(@RequestBody DoctorBankCardBean doctorBankCardBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        System.out.println(getCurrentUserId());
        DoctorBankCard doctorBankCard = doctorBankCardService.findByDoctorIdAndhidden(getCurrentUserId(),0);
        Banks banks = banksService.getOne(doctorBankCardBean.getBankId());
        String backName = (banks ==null?"":banks.getName());
        if (doctorBankCard == null) {
            doctorBankCard = new DoctorBankCard();
            doctorBankCard.setDoctorId(getCurrentUserId());

        }
        if(banks==null){
            doctorBankCard.setBankName("");
            doctorBankCard.setBankId("");
        }else{
            doctorBankCard.setBankName(backName);
            doctorBankCard.setBankId(doctorBankCardBean.getBankId());
        }
//        BeanUtils.copyProperties(doctorBankCard, doctorBankCardBean);

        return toSuccess("保存银行卡成功", doctorBankCardService.save(doctorBankCard));
    }

    @ApiAuth
    @ApiOperation(value = "我的钱包", notes = "我的钱包", position = 16)
    @ResponseBody
    @RequestMapping(value = {"/myWallet"}, method = RequestMethod.POST)
    public ApiResultBean<MyWalletBean> myWallet(@RequestBody ApiBasePageBean apiBasePageBean, HttpServletRequest request) {
        return toSuccess("获取我的钱包成功", userDoctorService.getMyWalletInfo(getCurrentUserId(), apiBasePageBean.getPageBean()));
    }

    @ApiAuth
    @ApiOperation(value = "待收账单", notes = "待收账单", position = 17)
    @ResponseBody
    @RequestMapping(value = {"/receivableBills"}, method = RequestMethod.POST)
    public ApiResultBean<List<ReceivableBillsBean>> receivableBills(@RequestBody ApiBasePageBean apiBasePageBean, HttpServletRequest request) {
        return toSuccess("获取待收账单成功", userDoctorService.getReceivableBills(getCurrentUserId(), apiBasePageBean.getPageBean()));
    }

    @ApiAuth
    @ApiOperation(value = "图片上传", notes = "图片上传", position = 3)
    @ResponseBody
    @RequestMapping(value = {"/imageUpload"}, method = RequestMethod.POST)
    public ApiResultBean imageUpload(String token, String filename, String fileType, MultipartFile fileData, HttpServletRequest request) {
        String url = ossMtsService.uploadStatic(fileData);

        return toSuccess("图片上传成功", buildData("imageUrl", url));
    }

    @ApiOperation(value = "获取版本信息", notes = "获取版本信息", position = 1)
    @ResponseBody
    @RequestMapping(value = {"/getVersion"}, method = RequestMethod.POST)
    public ApiResultBean<AppVersion> getVersion(@RequestBody VersionBean versionBean) {
        String appCode = "";
        if (Constants.SYSTEM_VERSION_IOS.equals(versionBean.getSystemVersion())) {
            appCode = Constants.APP_CLIENT_DOCTOR_IOS;
        } else {
            appCode = Constants.APP_CLIENT_DOCTOR_ANDROID;
        }

        AppVersion appVersion = appVersionService.getByAppCode(appCode);
        return toSuccess("获取成功", appVersion);
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
    @ApiOperation(value = "获取内容类型", notes = "获取内容类型", position = 8)
    @ResponseBody
    @RequestMapping(value = {"/content/type"}, method = RequestMethod.POST)
    public ApiResultBean contentType(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {

        return toSuccess("获取内容类型成功", ProjectUtil.getConfigBeanList(Constants.CONTENT_TYPE_KEY));
    }

    @ApiAuth
    @ApiOperation(value = "获取病种类型", notes = "获取病种类型", position = 9)
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
    @ApiOperation(value = "获取疗法类型", notes = "获取疗法类型", position = 10)
    @ResponseBody
    @RequestMapping(value = {"/content/therapy"}, method = RequestMethod.POST)
    public ApiResultBean contentTherapy(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {
        List<Therapy> therapyList = therapyService.findAll();

        Map<String, List> mapList = new TreeMap<>(new Comparator<String>() {
            public int compare(String obj1, String obj2) {
                return obj1.compareTo(obj2);
            }
        });

        for (Therapy therapy : therapyList) {
            if (Constants.THERAPY_STATUS_PUBLISH != therapy.getStatus()) {
                continue;
            }
            try {
                String firstLetter = getFirstLetter(therapy.getName());
                if (mapList.containsKey(firstLetter)) {
                    mapList.get(firstLetter).add(therapy);
                } else {
                    List<Therapy> list = new ArrayList<>();
                    list.add(therapy);
                    mapList.put(firstLetter, list);
                }
            } catch (PinyinException e) {
                e.printStackTrace();
            }
        }
        return toSuccess("获取疗法类型成功", LetterToArrayBean.valueOf(mapList));
    }

    @ApiAuth
    @ApiOperation(value = "获取单个内容", notes = "获取单个内容", position = 11)
    @ResponseBody
    @RequestMapping(value = {"/content/info"}, method = RequestMethod.POST)
    public ApiResultBean contentInfo(@RequestBody ContentIdBean contentIdBean, HttpServletRequest request) {
        Content content = contentService.getOne(contentIdBean.getContentId());

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
    @ApiOperation(value = "获取患者信息", notes = "获取患者信息", position = 14)
    @ResponseBody
    @RequestMapping(value = {"/patient/info"}, method = RequestMethod.POST)
    public ApiResultBean patientInfo(@RequestBody PatientIdBean userIdBean, HttpServletRequest request) {
        PatientReturnBean patientReturnBean = null;
        try {
            patientReturnBean = userPatientService.getPatientReturnBean(userIdBean.getPatientId());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return toError("获取患者信息失败", -2);
        }

        if (patientReturnBean == null) {
            return toError("获取患者信息失败", -2);
        }

        return toSuccess("获取患者信息成功", patientReturnBean);
    }

    @ApiAuth
    @ApiOperation(value = "为患者开处方", notes = "为患者开处方", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/prescription/add"}, method = RequestMethod.POST)
    public ApiResultBean prescriptionAdd(@RequestBody PrescriptionBean prescriptionBean, HttpServletRequest request) {
        Prescription prescription;

        try {
            prescriptionBean.setSource(Constants.PRESCRIPTION_SOURCE_ONLINE);
            prescription = prescriptionService.save(prescriptionBean);
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
    @ApiOperation(value = "获取处方列表", notes = "获取处方列表", position = 16)
    @ResponseBody
    @RequestMapping(value = {"/prescription/search"}, method = RequestMethod.POST)
    public ApiResultBean prescriptionSearch(@RequestBody PrescriptionSearchBean prescriptionSearchBean, HttpServletRequest request) {
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("EQ_patientId", prescriptionSearchBean.getPatientId());
        searchParams.put("EQ_doctorId", prescriptionSearchBean.getDoctorId());

        Page<Prescription> page = prescriptionService.search(searchParams, prescriptionSearchBean.getPageBean());

        return toSuccess("获取处方列表成功", page.getContent());
    }

    @ApiAuth
    @ApiOperation(value = "获取单个处方", notes = "获取单个处方", position = 16)
    @ResponseBody
    @RequestMapping(value = {"/prescription/info"}, method = RequestMethod.POST)
    public ApiResultBean prescriptionInfo(@RequestBody PrescriptionIdBean prescriptionIdBean, HttpServletRequest request) {
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

        try {
            PatientBean patientBean = userPatientService.getPatientBean(prescription.getPatientId());
            data.put("patient", patientBean);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return toSuccess("获取单个处方失败", -1);
        }

        return toSuccess("获取单个处方成功", data);
    }

    @ApiAuth
    @ApiOperation(value = "获取个人信息", notes = "获取个人信息", position = 17)
    @ResponseBody
    @RequestMapping(value = {"/getInfo"}, method = RequestMethod.POST)
    public ApiResultBean getInfo(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        User user = userService.findOne(getCurrentUserId());
        UserDoctor userDoctor = userDoctorService.getByUserId(getCurrentUserId());

        DoctorBean doctorBean = new DoctorBean();
        BeanUtils.copyProperties(user, doctorBean);
        BeanUtils.copyProperties(userDoctor, doctorBean);

        String regionFullName = null;
        if (StringUtils.isNotBlank(userDoctor.getRegion())) {
            regionFullName = areaService.getFullName(userDoctor.getRegion());
        }
        doctorBean.setRegionFullName(regionFullName);
        doctorBean.setId(user.getId());

        return toSuccess("获取个人信息成功", doctorBean);
    }

    @ApiAuth
    @ApiOperation(value = "完善个人信息", notes = "完善个人信息", position = 17)
    @ResponseBody
    @RequestMapping(value = {"/fillInfo"}, method = RequestMethod.POST)
    public ApiResultBean fillInfo(@RequestBody DoctorFillInfoBean doctorFillInfoBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        UserDoctor userDoctor = userDoctorService.getByUserId(getCurrentUserId());

        userDoctor.setIntroduction(doctorFillInfoBean.getIntroduction());
        userDoctor.setExpertise(doctorFillInfoBean.getExpertise());
        userDoctor.setHeadPictureUrl(doctorFillInfoBean.getHeadPictureUrl());
        userDoctor.setSignature(doctorFillInfoBean.getSignature());
        userDoctor.setRegion(doctorFillInfoBean.getRegion());
        userDoctorService.save(userDoctor);

        return toSuccess("完善个人信息成功");
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
    @ApiOperation(value = "获取账户余额", notes = "获取账户余额", position = 24)
    @ResponseBody
    @RequestMapping(value = {"/getBalance"}, method = RequestMethod.POST)
    public ApiResultBean getBalance(@RequestBody DoctorStatusBean doctorStatusBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        UserDoctor userDoctor = userDoctorService.getByUserId(getCurrentUserId());

        return toSuccess("获取账户余额成功", buildData("balance", userDoctor.getBalance()));
    }

    @ApiAuth
    @ApiOperation(value = "获取账单记录", notes = "获取账单记录", position = 30)
    @ResponseBody
    @RequestMapping(value = {"/getBills"}, method = RequestMethod.POST)
    public ApiResultBean getBills(@RequestBody ApiBasePageBean apiBasePageBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("EQ_toUserId", getCurrentUserId());
        Page<Order> page = orderService.search(searchParams, apiBasePageBean.getPageBean());

        setFieldValues(page, User.class, "toUserId", new String[]{"username", "realname"});

        return toSuccess("获取账单记录成功", page.getContent());
    }

    @ApiAuth
    @ApiOperation(value = "设备问诊状态", notes = "设备问诊状态", position = 30)
    @ResponseBody
    @RequestMapping(value = {"/setStatus"}, method = RequestMethod.POST)
    public ApiResultBean setStatus(@RequestBody DoctorStatusBean doctorStatusBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (Constants.DOCTOR_STATUS_STOP != doctorStatusBean.getStatus() && Constants.DOCTOR_STATUS_NORMAL != doctorStatusBean.getStatus()) {
            return toError("状态不正确", -1);
        }
        UserDoctor userDoctor = userDoctorService.getByUserId(getCurrentUserId());
        userDoctor.setStatus(doctorStatusBean.getStatus());
        userDoctorService.save(userDoctor);
        return toSuccess("设备问诊状态成功");
    }


    @ApiAuth
    @ApiOperation(value = "获取地区列表", notes = "获取地区列表", position = 30)
    @ResponseBody
    @RequestMapping(value = {"/getRegion"}, method = RequestMethod.POST)
    public ApiResultBean getRegion(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {
        List<RegionBean> data = getRegionBean();
        return toSuccess("获取地区列表成功", data);
    }

    @ApiAuth
    @ApiOperation(value = "设置问诊最低价格", notes = "设置问诊最低价格", position = 30)
    @ResponseBody
    @RequestMapping(value = {"/setMinPrice"}, method = RequestMethod.POST)
    public ApiResultBean setMinPrice(@RequestBody MinPriceBean minPriceBean, HttpServletRequest request) {
        UserDoctor userDoctor = userDoctorService.getByUserId(getCurrentUserId());
        if (minPriceBean.getMinPrice() != null && minPriceBean.getMinPrice().doubleValue() >= 0) {
            userDoctor.setMinPrice(minPriceBean.getMinPrice());
            userDoctorService.save(userDoctor);
        }
        return toSuccess("设置问诊最低价格成功");
    }


    @ApiAuth
    @ApiOperation(value = "获取问诊最低价格", notes = "获取问诊最低价格", position = 31)
    @ResponseBody
    @RequestMapping(value = {"/getMinPrice"}, method = RequestMethod.POST)
    public ApiResultBean getMinPrice(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {
        UserDoctor userDoctor = userDoctorService.getByUserId(getCurrentUserId());
        return toSuccess("获取问诊最低价格成功", buildData("minPrice", userDoctor.getMinPrice()));
    }

    @ApiAuth
    @ApiOperation(value = "通过手机号查ID及姓名", notes = "通过手机号查ID及姓名", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/getUserIDAndName"}, method = RequestMethod.POST)
    public ApiResultBean getUserIDAndName(@RequestBody PhoneBean phoneBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return toSuccess("通过手机号查ID及姓名", getUserIdAndNameData(phoneBean));
    }

    @ApiAuth
    @ApiOperation(value = "行业新闻", notes = "获取行业新闻", position = 32)
    @ResponseBody
    @RequestMapping(value = {"/getFrontpages"}, method = RequestMethod.POST)
    public ApiResultBean getFrontpages(@RequestBody FrontpageSearchBean frontpageSearchBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // 查询为行业新闻的头条
        Map<String, Object> searchParams = new HashedMap();
        searchParams.put("nametype","行业新闻");
        Page<Frontpage> page = frontpageService.findBySearchParams(searchParams,frontpageSearchBean.getPageBean());
        FrontpageResultBean frontpageResultBean = null;
        List<FrontpageResultBean> frontpageResultBeanList = new ArrayList<FrontpageResultBean>();
        if (page.getContent().size() > 0) {
            for (int i = 0; i < page.getContent().size(); i++) {
                frontpageResultBean = new FrontpageResultBean();
                Frontpage frontpage = page.getContent().get(i);
                PropertyUtils.copyProperties(frontpageResultBean, frontpage);
                frontpageResultBeanList.add(frontpageResultBean);
            }
        }
        return toSuccess("获取行业新闻成功", frontpageResultBeanList);
    }

    @ApiAuth
    @ApiOperation(value = "获取历史处方", notes = "获取历史处方", position = 33)
    @ResponseBody
    @RequestMapping(value = {"/historicalPrescriptions"}, method = RequestMethod.POST)
    public ApiResultBean historicalPrescriptions(@RequestBody PrescriptionSearchBean prescriptionSearchBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("EQ_patientId", prescriptionSearchBean.getPatientId());
        searchParams.put("EQ_doctorId", prescriptionSearchBean.getDoctorId());

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
    @ApiOperation(value = "通过多个手机号查ID及姓名", notes = "通过多个手机号查ID及姓名", position = 34)
    @ResponseBody
    @RequestMapping(value = {"/getUserIDAndNames"}, method = RequestMethod.POST)
    public ApiResultBean getUserIDAndNames(@RequestBody PhoneBean phoneBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String[] phones = phoneBean.getPhone().split(",");
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        Map<String, String> data = null;
        for (int i = 0; i < phones.length; i++) {
            String phone = phones[i];
            User user = userService.getByUsername(phone);
            UserDoctor userDoctor = userDoctorService.getByUserId(user.getId());
            data = new HashMap<>();
            data.put("phone",phone);
            data.put("id", user.getId());
            data.put("realname", user.getRealname());
            data.put("headpictureurl",userDoctor==null?null:userDoctor.getHeadPictureUrl());
            list.add(data);
        }

        return toSuccess("通过手机号查ID及姓名", list);
    }

    @ApiAuth
    @ApiOperation(value = "获取银行档案", notes = "获取银行档案", position = 35)
    @ResponseBody
    @RequestMapping(value = {"/getBanks"}, method = RequestMethod.POST)
    public ApiResultBean getBanks(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<String, Object> searchParams = new HashedMap();
        List<Banks> banksList = banksService.search(searchParams);
        List<BanksBean> banksBeanList = new ArrayList<BanksBean>();
        BanksBean banksBean = null;
        for (int i = 0; i < banksList.size(); i++) {
            banksBean = new BanksBean();
            Banks bankss = banksList.get(i);
            PropertyUtils.copyProperties(banksBean, bankss);
            banksBeanList.add(banksBean);
        }
        return toSuccess("获取银行档案", banksBeanList);
    }

    @ApiAuth
    @ApiOperation(value = "咨询收费接口", notes = "咨询收费接口", position = 36)
    @ResponseBody
    @RequestMapping(value = {"/consultationCharge"}, method = RequestMethod.POST)
    public ApiResultBean consultationCharge(@RequestBody ChargesBean chargesBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        UserDoctor userDoctor = userDoctorService.getByUserId(getCurrentUserId());
        if(userDoctor==null){
            return toError("找不到该用户对应的医生",-1);
        }
        User user = userService.findOne(chargesBean.getPatientId());
        if (user == null) {
            return toError("找不到该患者的用户",-1);
        }
        UserPatient userPatient = userPatientService.getByUserId(user.getId());
        if(userPatient == null) {
            return toError("找不到该患者",-1);
        }

        Charges charges = new Charges();
        PropertyUtils.copyProperties(charges, chargesBean);
        charges.setChargesAmount(chargesBean.getFees());
        charges.setDoctorId(userDoctor.getId());
        charges.setDoctorName(userDoctor.getUser().getUsername());
        charges.setPatientName(userPatient.getUser().getUsername());
        charges.setPayStatus(1);
        charges.setRemark(chargesBean.getRemarks());
        Charges charges1 = chargesService.save(charges);
        ChargesReturnBean chargesReturnBean = new ChargesReturnBean();
        chargesReturnBean.setConsultationId(charges1.getId());
        return toSuccess("成功", chargesReturnBean);
    }

    @ApiAuth
    @ApiOperation(value = "获取咨询收费接口", notes = "获取咨询收费接口", position = 37)
    @ResponseBody
    @RequestMapping(value = {"/consultationDetail"}, method = RequestMethod.POST)
    public ApiResultBean consultationDetail (@RequestBody ChargesReturnBean chargesReturnBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if(chargesReturnBean.getConsultationId()==null){
            return toError("参数【ConsultationId】不能为空",-1);
        }
        Charges charges = chargesService.getOne(chargesReturnBean.getConsultationId());
        return toSuccess("获取成功", charges);
    }

    @ApiAuth
    @ApiOperation(value = "设置视频通话状态接口", notes = "设置视频通话状态接口", position = 38)
    @ResponseBody
    @RequestMapping(value = {"/resetVideoCallStatus"}, method = RequestMethod.POST)
    public ApiResultBean resetVideoCallStatus(@RequestBody CalllimitBean calllimitBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        UserDoctor userDoctor = userDoctorService.getByUserId(getCurrentUserId());
        if(userDoctor==null){
            return toError("找不到该用户对应的医生",-1);
        }
        User user = userService.findOne(calllimitBean.getPatientId());
        if (user == null) {
            return toError("找不到该患者的用户",-1);
        }
        UserPatient userPatient = userPatientService.getByUserId(user.getId());
        if(userPatient == null) {
            return toError("找不到该患者",-1);
        }
        if(calllimitBean.getStatus()==null||!(calllimitBean.getStatus()==1||calllimitBean.getStatus()==2)){
            return toError("参数status错误",-1);
        }
        //判断是更新还是新增
        Map<String, Object> searchParams = new HashedMap();
        searchParams.put("EQ_doctorId",userDoctor.getId());
        searchParams.put("EQ_patientId",userPatient.getId());
        searchParams.put("EQ_hidden",0);
        Calllimit calllimit = calllimitService.findOne(searchParams);
        Calllimit calllimit1 = new Calllimit();
        if(calllimit==null){
            Calllimit calllimits = new Calllimit();
            calllimits.setPayStatus(calllimitBean.getStatus());
            calllimits.setPatientId(userPatient.getId());
            calllimits.setPatientName(userPatient.getUser().getUsername());
            calllimits.setDoctorId(userDoctor.getId());
            calllimits.setDoctorName(userDoctor.getUser().getUsername());
            calllimit1 = calllimitService.save(calllimits);
        }else{
            calllimit.setPayStatus(calllimitBean.getStatus());
            calllimit1 = calllimitService.save(calllimit);
        }

        return toSuccess("成功", calllimit1);
    }

    @ApiAuth
    @ApiOperation(value = "获取视频通话状态接口", notes = "获取视频通话状态接口", position = 39)
    @ResponseBody
    @RequestMapping(value = {"/videoCallStatus"}, method = RequestMethod.POST)
    public ApiResultBean videoCallStatus(@RequestBody CalllimitBean calllimitBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if(calllimitBean.getPatientId()==null){
            return toError("参数【patientId】不能为空",-1);
        }
        UserDoctor userDoctor = userDoctorService.getByUserId(getCurrentUserId());
        if(userDoctor==null){
            return toError("找不到该用户对应的医生",-1);
        }
        User user = userService.findOne(calllimitBean.getPatientId());
        if (user == null) {
            return toError("找不到该患者的用户",-1);
        }
        UserPatient userPatient = userPatientService.getByUserId(user.getId());
        if(userPatient == null) {
            return toError("找不到该患者",-1);
        }
        Map<String, Object> searchParams = new HashedMap();
        searchParams.put("EQ_doctorId",userDoctor.getId());
        searchParams.put("EQ_patientId",userPatient.getId());
        searchParams.put("EQ_hidden",0);
        Calllimit calllimit = calllimitService.findOne(searchParams);
        CalllimitReturnBean calllimitReturnBean = new CalllimitReturnBean();
        if(calllimit==null){
            calllimitReturnBean.setDoctorId(userDoctor.getId());
            calllimitReturnBean.setPatientId(calllimitBean.getPatientId());
            calllimitReturnBean.setStatus(1);
        }else{
            calllimitReturnBean.setDoctorId(calllimit.getDoctorId());
            calllimitReturnBean.setPatientId(calllimit.getPatientId());
            calllimitReturnBean.setStatus(calllimit.getPayStatus());
        }
        return toSuccess("获取成功", calllimitReturnBean);
    }


    @ApiAuth
    @ApiOperation(value = "行业新闻、心景学院收藏（取消）接口", notes = "行业新闻、心景学院收藏（取消）接口", position = 40)
    @ResponseBody
    @RequestMapping(value = {"/collectNews"}, method = RequestMethod.POST)
    public ApiResultBean collectNews(@RequestBody FrontpageCollectBean frontpageCollectBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        UserDoctor userDoctor = userDoctorService.getByUserId(getCurrentUserId());
        if(userDoctor==null){
            return toError("找不到该用户对应的医生",-1);
        }
        if(frontpageCollectBean.getNewsId()==""||frontpageCollectBean.getNewsId()==null){
            return toError("参数【NewsId】不能为空",-1);
        }
        if(frontpageCollectBean.getIsCollect()==null||!(frontpageCollectBean.getIsCollect()==1||frontpageCollectBean.getIsCollect()==2)){
            return toError("参数IsCollect错误",-1);
        }
        Map<String, Object> searchParams = new HashedMap();
        searchParams.put("EQ_newsId",frontpageCollectBean.getNewsId());
        searchParams.put("EQ_userId",getCurrentUserId());
        searchParams.put("EQ_hidden",0);
        FrontpageCollect frontpageCollect =  frontpageCollectService.findOne(searchParams);
        FrontpageCollect frontpageCollectsave = new FrontpageCollect();
        if(frontpageCollect==null){
            PropertyUtils.copyProperties(frontpageCollectsave, frontpageCollectBean);
            frontpageCollectsave.setUserId(getCurrentUserId());
            frontpageCollectsave = frontpageCollectService.save(frontpageCollectsave);
        }else{
            frontpageCollect.setIsCollect(frontpageCollectBean.getIsCollect());
            frontpageCollectsave = frontpageCollectService.save(frontpageCollect);
        }

        return toSuccess("行业新闻、心景学院收藏或者取消成功", frontpageCollectsave);
    }
    @ApiAuth
    @ApiOperation(value = "获取行业新闻、心景学院收藏列表接口", notes = "获取行业新闻、心景学院收藏列表接口", position = 41)
    @ResponseBody
    @RequestMapping(value = {"/collectedNewsList"}, method = RequestMethod.POST)
    public ApiResultBean collectedNewsList(@RequestBody FrontpageCollectBean frontpageCollectBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        UserDoctor userDoctor = userDoctorService.getByUserId(getCurrentUserId());
        if(userDoctor==null){
            return toError("找不到该用户对应的医生",-1);
        }
        Map<String, Object> searchParams = new HashedMap();
        searchParams.put("EQ_userId",getCurrentUserId());
        searchParams.put("EQ_hidden",0);
        List<FrontpageCollect> frontpageCollectList = frontpageCollectService.findAll(searchParams);

        List<FrontpageCollectReturnBean> freturn = new ArrayList<>();
        List<Frontpage> frontpageList = null;
        if(frontpageCollectList.size()>=1){
            FrontpageCollectReturnBean frontpageCollectReturnBean = null;
            List<String> newsidList = ExtractUtil.extractToList(frontpageCollectList,"newsId");
            String[] newsids = newsidList.toArray(new String[newsidList.size()]);
            frontpageList = frontpageService.findByHiddenAndIdInOrderByReleaseTimeDesc(newsids);
            for (int i = 0; i < frontpageList.size(); i++) {
                frontpageCollectReturnBean = new FrontpageCollectReturnBean();
                Frontpage frontpage = frontpageList.get(i);
                PropertyUtils.copyProperties(frontpageCollectReturnBean, frontpage);
                for (int j = 0; j < frontpageCollectList.size(); j++) {
                    if(frontpage.getId().equals(frontpageCollectList.get(j).getNewsId())){
                        frontpageCollectReturnBean.setIsCollect(frontpageCollectList.get(j).getIsCollect());
                        freturn.add(frontpageCollectReturnBean);
                        break;
                    }
                }
            }
        }
        return toSuccess("行业新闻、心景学院收藏或者取消成功", freturn);
    }


}
