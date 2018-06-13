package cn.easy.xinjing.api;

import cn.easy.base.bean.common.ApiResultBean;
import cn.easy.base.domain.User;
import cn.easy.base.utils.BaseUtils;
import cn.easy.base.utils.ExtractUtil;
import cn.easy.weixin.service.WxMpExtService;
import cn.easy.xinjing.aop.ApiAuth;
import cn.easy.xinjing.api.base.XjBaseApiController;
import cn.easy.xinjing.bean.api.*;
import cn.easy.xinjing.domain.*;
import cn.easy.xinjing.service.AppVersionService;
import cn.easy.xinjing.service.FrontpageService;
import cn.easy.xinjing.utils.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
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
 * Created by chenzhongyi on 16/9/12.
 */
@Api(value = "patient-api-controller", description = "患者相关API", position = 5)
@Controller
@RequestMapping("/api/v1/patient")
public class PatientApiController extends XjBaseApiController {
    @Autowired
    private FrontpageService frontpageService;
    @Autowired
    private WxMpExtService wxMpExtService;
    @Autowired
    private AppVersionService appVersionService;

    @ApiOperation(value = "获取版本信息", notes = "获取版本信息", position = 1)
    @ResponseBody
    @RequestMapping(value = {"/getVersion"}, method = RequestMethod.POST)
    public ApiResultBean<AppVersion> getVersion(@RequestBody VersionBean versionBean) {
        String appCode = "";
        if(Constants.SYSTEM_VERSION_IOS.equals(versionBean.getSystemVersion())){
            appCode = Constants.APP_CLIENT_PATIENT_IOS;
        }else{
            appCode = Constants.APP_CLIENT_PATIENT_ANDROID;
        }

        AppVersion appVersion = appVersionService.getByAppCode(appCode);
        return toSuccess("获取成功", appVersion);
    }

    @ApiOperation(value = "注册", notes = "注册", position = 1)
    @ResponseBody
    @RequestMapping(value = {"/register"}, method = RequestMethod.POST)
    public ApiResultBean register(@RequestBody PatientRegisterBean patientRegisterBean, HttpServletRequest request) {
        if (!validateCaptcha(patientRegisterBean.getCaptcha(), patientRegisterBean.getUsername())) {
            logger.info("captcha=====" + patientRegisterBean.getCaptcha() + ", session captcha====" + getCaptcha(patientRegisterBean.getUsername()) + ", valid failed");
            return toError("验证码错误", -1);
        }

        User dbUser = userService.getByUsername(patientRegisterBean.getUsername());
        if (dbUser != null) {
            return toError("该用户名已经存在，请勿重复注册", -2);
        }

        //生成加密密码
        String encryptPw = BaseUtils.encodePassword(patientRegisterBean.getPassword());

        //创建环信IM账号
        if(!registerIMUser(patientRegisterBean.getUsername(), encryptPw)){
            return toError("IM 账号创建失败", -3);
        }

        patientRegisterBean.setPassword(encryptPw);
        userPatientService.save(patientRegisterBean);

        return toSuccess("注册成功");
    }

    @ApiOperation(value = "短信验证码", notes = "发送短信验证码", position = 2)
    @ResponseBody
    @RequestMapping(value = {"/sendCode"}, method = RequestMethod.POST)
    public ApiResultBean sendCode(@RequestBody SendCodeBean sendCodeBean, HttpServletRequest request) {
        return doSendCode(sendCodeBean.getMobile(), request);
    }

//    @ApiOperation(value = "登录", notes = "登录", position = 3)
//    @ResponseBody
//    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
//    public ApiResultBean login(@RequestBody LoginBean loginBean, HttpServletRequest request) {
//        return doLogin(loginBean, Constants.USER_TYPE_PATIENT, request);
//    }

    @ApiOperation(value = "登录", notes = "登录", position = 3)
    @ResponseBody
    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public ApiResultBean login(@RequestBody PationtLoginBean pationtLoginBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        LoginBean loginBean = new LoginBean();
        PropertyUtils.copyProperties(loginBean, pationtLoginBean);
        if (!validateCaptcha(pationtLoginBean.getCaptcha(), pationtLoginBean.getUsername())) {
            logger.info("captcha=====" + pationtLoginBean.getCaptcha() + ", session captcha====" + getCaptcha(pationtLoginBean.getUsername()) + ", valid failed");
            return toError("验证码错误", -1);
        }

        User dbUser = userService.getByUsername(pationtLoginBean.getUsername());
        if (dbUser != null) {
            return doLoginto(loginBean, Constants.USER_TYPE_PATIENT, request);
        }

        //生成加密密码
        String encryptPw = BaseUtils.encodePassword("easy888");

        //创建环信IM账号
        if(!registerIMUser(pationtLoginBean.getUsername(), encryptPw)){
            return toError("IM 账号创建失败", -3);
        }

        pationtLoginBean.setPassword(encryptPw);
        userPatientService.saveto(pationtLoginBean);

        return doLoginto(loginBean, Constants.USER_TYPE_PATIENT, request);
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
    @ApiOperation(value = "个人信息完善", notes = "个人信息完善", position = 5)
    @ResponseBody
    @RequestMapping(value = {"/fillInfo"}, method = RequestMethod.POST)
    public ApiResultBean infoPost(@RequestBody PatientBean patientBean, HttpServletRequest request) {
        User user = userService.findOne(getCurrentUserId());
        userPatientService.save(patientBean, user);
        return toSuccess("个人信息完善成功");
    }

    @ApiOperation(value = "获取地区列表", notes = "获取地区列表", position = 6)
    @ResponseBody
    @RequestMapping(value = {"/getRegion"}, method = RequestMethod.POST)
    public ApiResultBean getRegion(HttpServletRequest request) {
        Map<String, Object> searchParams = new HashedMap();
        searchParams.put("EQ_hidden",0);
        List<String> region02 = new ArrayList<String>();
        List<String> region05 = new ArrayList<String>();
        List<UserDoctor> userDoctorList = userDoctorService.findAll(searchParams);
        for (int i = 0; i < userDoctorList.size(); i++) {
            if(userDoctorList.get(i).getRegion()!=null&&userDoctorList.get(i).getRegion().length()>0&&userDoctorList.get(i).getRegion().length()<7){
                region02.add(userDoctorList.get(i).getRegion().substring(0,2));
                region05.add(userDoctorList.get(i).getRegion().substring(0,4)+"00");
            }
            
        }
        List<RegionBean> data = getRegionBean(region02,region05);
        return toSuccess("获取地区列表成功", data);
    }

    @ApiOperation(value = "获取病种类型", notes = "获取病种类型", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/content/disease"}, method = RequestMethod.POST)
    public ApiResultBean contentDisease(HttpServletRequest request) {
        return toSuccess("获取病种类型成功", getDisease());
    }

    @ApiOperation(value = "获取医生列表", notes = "获取医生列表", position = 7)
    @ResponseBody
    @RequestMapping(value = {"/doctor/search"}, method = RequestMethod.POST)
    public ApiResultBean doctorSearch(@RequestBody DoctorSearchBean doctorSearchBean, HttpServletRequest request) {
        Map<String, Object> searchParams = new HashMap<>();
        if(StringUtils.isNotBlank(doctorSearchBean.getRegion())) {
            searchParams.put("EQ_region", doctorSearchBean.getRegion());
        }

        if(doctorSearchBean.getIsVisitPatient() == 1) {
            searchParams.put("EQ_status", Constants.DOCTOR_STATUS_NORMAL);
        }
        else if(doctorSearchBean.getIsVisitPatient() == 0) {
            searchParams.put("EQ_status", Constants.DOCTOR_STATUS_STOP);
        }

        if(StringUtils.isNotBlank(doctorSearchBean.getDisease())) {
            searchParams.put("LIKE_expertise", doctorSearchBean.getDisease());
        }

        if(StringUtils.isNotBlank(doctorSearchBean.getKeyword())) {
            if (TextValidator.isMobileSimple(doctorSearchBean.getKeyword())) {
                searchParams.put("EQ_username", doctorSearchBean.getKeyword());
            } else {
                searchParams.put("EQ_realname", doctorSearchBean.getKeyword());
            }
        }

        Page<UserDoctor> page = userDoctorService.search(searchParams, doctorSearchBean.getPageBean());

        for(UserDoctor userDoctor : page.getContent()) { //TODO 待优化
            userDoctor.setRegion(areaService.getFullName(userDoctor.getRegion()));
        }

        return toSuccess("获取医生列表成功", userDoctor2DoctorBean(page.getContent()));
    }
    @ApiAuth
    @ApiOperation(value = "获取医生详情", notes = "获取医生详情", position = 8)
    @ResponseBody
    @RequestMapping(value = {"/doctor/info"}, method = RequestMethod.POST)
    public ApiResultBean doctorInfo(@RequestBody DoctorIdBeanWithoutToken doctorIdBean, HttpServletRequest request) {
        User user = userService.findOne(doctorIdBean.getDoctorId());
        UserDoctor userDoctor = userDoctorService.getByUserId(doctorIdBean.getDoctorId());
        String regionFullName = areaService.getFullName(userDoctor.getRegion());
        DoctorBean doctorBean = new DoctorBean();
        try {
            PropertyUtils.copyProperties(doctorBean, user);
            PropertyUtils.copyProperties(doctorBean, userDoctor);
            doctorBean.setRegion(regionFullName);
            doctorBean.setId(user.getId());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        Map<String, Object> data = ExtractUtil.object2Map(doctorBean);
        data.put("isVisitPatient", doctorBean.getIsVisitPatient());
        DoctorPatient doctorPatient = doctorPatientService.findByDoctorIdAndPatientId(doctorIdBean.getDoctorId(), getCurrentUserId());
        data.put("isCollect", doctorPatient != null);

        return toSuccess("获取医生详情成功", data);
    }

    @ApiAuth
    @ApiOperation(value = "收藏医生", notes = "收藏医生", position = 9)
    @ResponseBody
    @RequestMapping(value = {"/doctor/collect"}, method = RequestMethod.POST)
    public ApiResultBean contentCollect(@RequestBody DoctorIdBean doctorIdBean, HttpServletRequest request) {
        DoctorPatient doctorPatient = doctorPatientService.findByDoctorIdAndPatientId(doctorIdBean.getDoctorId(), getCurrentUserId());
        if (doctorPatient != null) {
            return toError("已经收藏该医生", -1);
        }
        doctorPatient = new DoctorPatient();
        doctorPatient.setPatientId(getCurrentUserId());
        doctorPatient.setDoctorId(doctorIdBean.getDoctorId());
        doctorPatientService.save(doctorPatient);
        return toSuccess("收藏医生成功");
    }

    @ApiAuth
    @ApiOperation(value = "取消收藏医生", notes = "取消收藏医生", position = 10)
    @ResponseBody
    @RequestMapping(value = {"/doctor/cancelCollect"}, method = RequestMethod.POST)
    public ApiResultBean contentCancelCollect(@RequestBody DoctorIdBean doctorIdBean, HttpServletRequest request) {
        DoctorPatient doctorPatient = doctorPatientService.findByDoctorIdAndPatientId(doctorIdBean.getDoctorId(), getCurrentUserId());
        if (doctorPatient != null) {
            doctorPatientService.delete(doctorPatient.getId());
        }
        return toSuccess("取消收藏医生成功");
    }

    @ApiAuth
    @ApiOperation(value = "获取处方列表", notes = "获取处方列表", position = 11)
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
    @ApiOperation(value = "获取单个处方", notes = "获取单个处方", position = 12)
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
    @ApiOperation(value = "获取单个内容", notes = "获取单个内容", position = 13)
    @ResponseBody
    @RequestMapping(value = {"/content/info"}, method = RequestMethod.POST)
    public ApiResultBean contentInfo(@RequestBody ContentIdBean contentIdBean, HttpServletRequest request) {
        Content content = contentService.getOne(contentIdBean.getContentId());
        Map<String, Object> contentMap = ExtractUtil.object2Map(content);

        ContentCollect contentCollect = contentCollectService.findByContentIdAndUserId(contentIdBean.getContentId(), getCurrentUserId());
        contentMap.put("isCollect", contentCollect != null);

        contentMap.put("ext", contentService.getExt(content.getId(), content.getType()));
        return toSuccess("获取单个内容成功", contentMap);
    }

    @ApiAuth
    @ApiOperation(value = "获取用户收藏的医生", notes = "获取用户收藏的医生", position = 14)
    @ResponseBody
    @RequestMapping(value = {"/doctor/collect/search"}, method = RequestMethod.POST)
    public ApiResultBean collectSearch(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {
        List<DoctorPatient> doctorPatientList = doctorPatientService.findByPatientId(getCurrentUserId());

        List<UserDoctor> userDoctorList = userDoctorService.findByUserId(ExtractUtil.extractToList(doctorPatientList, "doctorId"));

        return toSuccess("获取用户收藏的医生成功", userDoctor2DoctorBean(userDoctorList));
    }

    @ApiAuth
    @ApiOperation(value = "获取账单记录", notes = "获取账单记录", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/getBills"}, method = RequestMethod.POST)
    public ApiResultBean getBills(@RequestBody ApiBasePageBean apiBasePageBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("EQ_fromUserId", getCurrentUserId());
        Page<Order> page = orderService.search(searchParams, apiBasePageBean.getPageBean());

        setFieldValues(page, User.class, "toUserId", new String[]{"username", "realname"});

        return toSuccess("获取账单记录成功", page.getContent());
    }

    @ApiAuth
    @ApiOperation(value = "通过手机号查ID及姓名", notes = "通过手机号查ID及姓名", position = 15)
    @ResponseBody
    @RequestMapping(value = {"/getUserIDAndName"}, method = RequestMethod.POST)
    public ApiResultBean getUserIDAndName(@RequestBody PhoneBean phoneBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return toSuccess("通过手机号查ID及姓名", getUserIdAndNameData(phoneBean));
    }

    @ApiAuth
    @ApiOperation(value = "微信支付", notes = "微信支付", position = 16)
    @ResponseBody
    @RequestMapping(value = {"/wxGoPay"}, method = RequestMethod.POST)
    public ApiResultBean wxGoPay(@RequestBody PrescriptionIdBean prescriptionIdBean, HttpServletRequest request) {
        Prescription prescription = prescriptionService.getOne(prescriptionIdBean.getPrescriptionId());
        if(prescription == null){
            return toError("处方不存在", -1);
        }
        List<PrescriptionContent> pContentList = prescriptionContentService.findByPrescriptionId(prescriptionIdBean.getPrescriptionId());
        if(CollectionUtils.isEmpty(pContentList)){
            return toError("处方中未包含内容", -2);
        }
        PrescriptionContent pContent = pContentList.get(0);
        Content content = contentService.getOne(pContent.getContentId());
        String body = "心景_" + content.getName() + "等";

        Map<String, String> resultParams = wxMpExtService.goPayByAPP(request, body, null, prescription.getTotal().doubleValue(), "api/v1/wxPay/payNotify");
        Order order = new Order();
        order.setObjectId(prescriptionIdBean.getPrescriptionId());
        order.setObjectType(Constants.ORDER_OBJECT_TYPE_PRESCRIPTION);
        order.setOutTradeNo(resultParams.get("out_trade_no"));
        order.setStatus(Constants.ORDER_STATUS_PROCESSING);
        order.setPayWay(Constants.PAY_WAY_WEIXIN);
        order.setAmount(prescription.getTotal());
        order.setFromUserId(getCurrentUserId());
        order.setToUserId(prescription.getDoctorId());
        order = orderService.save(order);

        resultParams.put("orderId", order.getId());

        return toSuccess("预支付成功", resultParams);
    }

    @ApiAuth
    @ApiOperation(value = "取消微信支付", notes = "取消微信支付", position = 17)
    @ResponseBody
    @RequestMapping(value = {"/cancelWxPay"}, method = RequestMethod.POST)
    public ApiResultBean cancelWxPay(@RequestBody OrderIdBean orderIdBean) {
        Order order = orderService.getOne(orderIdBean == null ? "0" : orderIdBean.getOrderId());
        if(order == null) {
            toError("未查询到此订单", -1);
        }
        order.setStatus(Constants.ORDER_STATUS_FAIL);
        order.setOutTradeNo(null);
        orderService.save(order);
        return toSuccess("取消微信支付成功");
    }

    @ApiAuth
    @ApiOperation(value = "退出登录", notes = "退出登录", position = 30)
    @ResponseBody
    @RequestMapping(value = {"/logout"}, method = RequestMethod.POST)
    public ApiResultBean logout(@RequestBody ApiBaseBean apiBaseBean, HttpServletRequest request) {
        doLogout(apiBaseBean);
        return toSuccess("退出登录成功");
    }

    /**
     * 组装医生对象
     * @param userDoctorList
     * @return
     */
    private List<DoctorBean> userDoctor2DoctorBean(List<UserDoctor> userDoctorList) {
        if(userDoctorList == null) {
            return Collections.emptyList();
        }

        List<User> userList = userService.getAll(ExtractUtil.extractToList(userDoctorList, "userId"));
        Map<String, User> userMap = ExtractUtil.extractToMap(userList, "id");

        List<DoctorBean> result = new ArrayList<>();
        for(UserDoctor userDoctor : userDoctorList) {
            User user = userMap.containsKey(userDoctor.getUserId()) ? userMap.get(userDoctor.getUserId()) : new User();

            DoctorBean doctorBean = new DoctorBean();
            try {
                PropertyUtils.copyProperties(doctorBean, user);
                PropertyUtils.copyProperties(doctorBean, userDoctor);
                doctorBean.setId(user.getId());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            result.add(doctorBean);
        }
        return result;
    }


    @ApiAuth
    @ApiOperation(value = "心景头条", notes = "获取心景头条", position = 31)
    @ResponseBody
    @RequestMapping(value = {"/getFrontpages"}, method = RequestMethod.POST)
    public ApiResultBean getFrontpages(@RequestBody FrontpageSearchBean frontpageSearchBean, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // 查询为行业新闻的头条
        Map<String, Object> searchParams = new HashedMap();
        searchParams.put("nametype","心景头条");
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
    @ApiOperation(value = "通过多个手机号查ID及姓名", notes = "通过多个手机号查ID及姓名", position = 32)
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
}
