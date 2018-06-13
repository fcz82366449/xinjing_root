package cn.easy.xinjing.web;

import cn.easy.base.bean.PageBean;
import cn.easy.base.bean.common.AjaxResultBean;
import cn.easy.base.domain.User;
import cn.easy.base.service.UserService;
import cn.easy.base.utils.BaseUtils;
import cn.easy.base.web.BaseController;
import cn.easy.xinjing.bean.api.DoctorRegisterBean;
import cn.easy.xinjing.domain.ApproveLog;
import cn.easy.xinjing.domain.Hospital;
import cn.easy.xinjing.domain.UserDoctor;
import cn.easy.xinjing.service.ApproveLogService;
import cn.easy.xinjing.service.HospitalService;
import cn.easy.xinjing.service.OssMtsService;
import cn.easy.xinjing.service.UserDoctorService;
import cn.easy.xinjing.utils.Constants;
import com.foxinmy.easemob4j.EasemobProxy;
import com.foxinmy.easemob4j.exception.EasemobException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Controller
@RequestMapping("/userDoctor")
public class UserDoctorController extends BaseController {
    @Autowired
    private UserDoctorService userDoctorService;
    @Autowired
    private UserService userService;
    @Autowired
    private ApproveLogService approveLogService;
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private OssMtsService ossMtsService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.userDoctor.index");
        return "userDoctor/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<UserDoctor> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.userDoctor.list");

        Page<UserDoctor> page = userDoctorService.search(searchParams(request), pageBean);
        setFieldValues(page, User.class, "userId", new String[]{"username", "realname", "mobile"});
        setFieldValues(page, Hospital.class, "hospital", new String[]{"name"});
        setConfigFieldValues(page, Constants.DOCTOR_STATUS_ENUM_KEY, "status");
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.userDoctor.formGet");
        if (isValidId(id)) {
            UserDoctor userDoctor = userDoctorService.getOne(id);
            Hospital hospital = hospitalService.getOne(userDoctor.getHospital());
            model.addAttribute("userDoctor", userDoctor);
            model.addAttribute("phone", userDoctor.getUser().getMobile());
            model.addAttribute("realname", userDoctor.getUser().getRealname());
            model.addAttribute("hospitalName", hospital==null?"":hospital.getName());
            model.addAttribute("entryDate", userDoctor.getEntryDate());
        } else {
            model.addAttribute("userDoctor", new UserDoctor());
            model.addAttribute("phone", "");
            model.addAttribute("realname", "");
            model.addAttribute("hospitalName", "");
        }


        return "userDoctor/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(UserDoctor userDoctor,
                            @RequestParam(value = "id") String id,
                            @RequestParam(value = "phone", required = false) String phone,
                            @RequestParam(value = "realname", required = false) String realname,
                            HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        increment("web.userDoctor.formPost");
        if (isValidId(id)) {
            UserDoctor userDoctor1 = userDoctorService.getOne(id);
            User user = userService.getOne(userDoctor1.getUserId());
            if(userDoctor1!=null&&user!=null){
//                DoctorRegisterBean doctorRegisterBean = new DoctorRegisterBean();
//                doctorRegisterBean.setRealname(realname);
                //生成加密密码
//                String encryptPw = BaseUtils.encodePassword("easy888");
//                doctorRegisterBean.setPassword(user.getPassword());
//                doctorRegisterBean.setUsername(phone);
//                User user1= new User();
//                PropertyUtils.copyProperties(user1, user);
                userDoctorService.updatexx(userDoctor1,userDoctor);
            }
        }else{
            //新增
            User dbUser = userService.getByUsername(phone.trim());
            if (dbUser != null) {
                return toError("该用户名已经存在，请勿重复注册");
            }
            //生成加密密码
            String encryptPw = BaseUtils.encodePassword("easy888");

            //创建环信IM账号
            if(!registerIMUser(phone, encryptPw)){
                //return toError("IM 账号创建失败", -3);
            }
            DoctorRegisterBean doctorRegisterBean = new DoctorRegisterBean();
            doctorRegisterBean.setRealname(realname);
            doctorRegisterBean.setPassword(encryptPw);
            doctorRegisterBean.setUsername(phone);
            userDoctorService.savexx(doctorRegisterBean,userDoctor,null);
        }



        return toSuccess("保存成功");
    }
    /**
     * 注册环信IM用户
     * @param userName
     * @param encryptPw
     * @return
     */
    protected boolean registerIMUser(String userName, String encryptPw){
//        try {
//            EasemobProxy easemobProxy = new EasemobProxy();
//            com.foxinmy.easemob4j.model.User user = new com.foxinmy.easemob4j.model.User();
//            user.setUname(userName);
//            user.setPassword(encryptPw);
//            easemobProxy.createUser(user);
//        } catch (EasemobException e) {
//            logger.warn("IM 账号创建失败, username: " + userName, e);
//            return false;
//        }
        return true;
    }
    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.userDoctor.delete");
        String userid = userDoctorService.getOne(id).getUserId();
        if(userid!=null&&userid.length()>=1){
            User user = userService.findOne(userid);
            userService.delete(user.getId());
            userDoctorService.delete(id);
        }

        return toSuccess("删除成功");
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    String view(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.userDoctor.view");
        UserDoctor userDoctor = userDoctorService.getOne(id);
        User user = userService.findOne(userDoctor.getUserId());

        List<ApproveLog> approveLogList = approveLogService.findByObjectIdAndObjectType(userDoctor.getId(), UserDoctor.class.getName());



        if(StringUtils.isNotEmpty(userDoctor.getPsychologicalConsultantImageUrl())) {
            if (userDoctor.getPsychologicalConsultantImageUrl().indexOf("http:") == -1) {
                //如果不包含则返回授权url
                String[] psychologicalConsultantImageUrls = userDoctor.getPsychologicalConsultantImageUrl().split(",");
                String psychologicalConsultantImageUr = "";
                for (String psychologicalConsultantImageUrl : psychologicalConsultantImageUrls) {
                    psychologicalConsultantImageUr += (ossMtsService.getImgAccessUrl(psychologicalConsultantImageUrl) + ",");
                }
                psychologicalConsultantImageUr = psychologicalConsultantImageUr.substring(0, psychologicalConsultantImageUr.length() - 1);
                userDoctor.setPsychologicalConsultantImageUrl(psychologicalConsultantImageUr);
            }
        }
        if(StringUtils.isNotEmpty(userDoctor.getEmployeeImageUrl())) {
            if (userDoctor.getEmployeeImageUrl().indexOf("http:") == -1) {
                //如果不包含则返回授权url
                String[] employeeImageUrl = userDoctor.getEmployeeImageUrl().split(",");
                String employeeImageUr = "";
                for (String employeeImageUrls : employeeImageUrl) {
                    employeeImageUr += (ossMtsService.getImgAccessUrl(employeeImageUrls) + ",");
                }
                employeeImageUr = employeeImageUr.substring(0, employeeImageUr.length() - 1);
                userDoctor.setEmployeeImageUrl(employeeImageUr);
            }
        }
        if(StringUtils.isNotEmpty(userDoctor.getDoctorProfessionImageUrl())) {
            if (userDoctor.getDoctorProfessionImageUrl().indexOf("http:") == -1) {
                //如果不包含则返回授权url
                String[] doctorProfessionImageUrls = userDoctor.getDoctorProfessionImageUrl().split(",");
                String doctorProfessionImageUrl = "";
                for (String doctorProfessionImageUrlo : doctorProfessionImageUrls) {
                    doctorProfessionImageUrl += (ossMtsService.getImgAccessUrl(doctorProfessionImageUrlo) + ",");
                }
                doctorProfessionImageUrl = doctorProfessionImageUrl.substring(0, doctorProfessionImageUrl.length() - 1);
                userDoctor.setDoctorProfessionImageUrl(doctorProfessionImageUrl);
            }
        }
        if(StringUtils.isNotEmpty(userDoctor.getProfessionalQualificationImageUrl())) {
            if(userDoctor.getProfessionalQualificationImageUrl().indexOf("http:")==-1){
                //如果不包含则返回授权url
                String[] getProfessionalQualificationImageUrl = userDoctor.getProfessionalQualificationImageUrl().split(",");
                String getProfessionalQualificationImageUr = "";
                for (String getProfessionalQualificationImageUro : getProfessionalQualificationImageUrl) {
                    getProfessionalQualificationImageUr += ( ossMtsService.getImgAccessUrl(getProfessionalQualificationImageUro)+",");
                }
                getProfessionalQualificationImageUr = getProfessionalQualificationImageUr.substring(0,getProfessionalQualificationImageUr.length() - 1);
                userDoctor.setProfessionalQualificationImageUrl(getProfessionalQualificationImageUr);
            }
        }


        model.addAttribute("userDoctor", userDoctor);
        model.addAttribute("user", user);
        model.addAttribute("approveLogList", setFieldValues(approveLogList, User.class, "creator", new String[]{"realname"}));
        return "userDoctor/view";
    }

    @RequestMapping("/stop")
    @ResponseBody
    AjaxResultBean stop(@RequestParam(value = "id") String id,
                        @RequestParam(value = "remark", required = false) String remark) {
        increment("web.userDoctor.stop");
        UserDoctor userDoctor = userDoctorService.getOne(id);
        userDoctor.setStatus(Constants.DOCTOR_STATUS_STOP);
        userDoctorService.save(userDoctor);

        //记录操作日志
        ApproveLog approveLog = new ApproveLog();
        approveLog.setObjectId(userDoctor.getId());
        approveLog.setObjectType(UserDoctor.class.getName());
        approveLog.setResult(getConfigValue(Constants.DOCTOR_STATUS_ENUM_KEY, String.valueOf(Constants.DOCTOR_STATUS_STOP)));
        approveLog.setRemark(remark);
        approveLogService.save(approveLog);
        return toSuccess("停诊成功");
    }

    @RequestMapping("/resume")
    @ResponseBody
    AjaxResultBean resume(@RequestParam(value = "id") String id,
                          @RequestParam(value = "remark", required = false) String remark) {
        increment("web.userDoctor.resume");
        UserDoctor userDoctor = userDoctorService.getOne(id);
        userDoctor.setStatus(Constants.DOCTOR_STATUS_NORMAL);
        userDoctorService.save(userDoctor);

        //记录操作日志
        ApproveLog approveLog = new ApproveLog();
        approveLog.setObjectId(userDoctor.getId());
        approveLog.setObjectType(UserDoctor.class.getName());
        approveLog.setResult(getConfigValue(Constants.DOCTOR_STATUS_ENUM_KEY, String.valueOf(Constants.DOCTOR_STATUS_NORMAL)));
        approveLog.setRemark(remark);
        approveLogService.save(approveLog);
        return toSuccess("恢复成功");
    }

    @RequestMapping("/select")
    public String select() {
        return "userDoctor/select";
    }
}


