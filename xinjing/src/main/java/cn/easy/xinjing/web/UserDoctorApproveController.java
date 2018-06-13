package cn.easy.xinjing.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.easy.base.domain.User;
import cn.easy.base.service.UserService;
import cn.easy.base.web.BaseController;
import cn.easy.xinjing.aop.ApiException;
import cn.easy.xinjing.bean.api.ConfigBean;
import cn.easy.xinjing.bean.api.DoctorAuthBean;
import cn.easy.xinjing.domain.ApproveLog;
import cn.easy.xinjing.domain.UserDoctor;
import cn.easy.xinjing.service.ApproveLogService;
import cn.easy.xinjing.service.OssMtsService;
import cn.easy.xinjing.service.UserDoctorService;
import cn.easy.xinjing.utils.Constants;
import cn.easy.xinjing.utils.ProjectUtil;
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

import cn.easy.base.bean.PageBean;
import cn.easy.base.bean.common.AjaxResultBean;
import cn.easy.xinjing.domain.UserDoctorApprove;
import cn.easy.xinjing.service.UserDoctorApproveService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/userDoctorApprove")
public class UserDoctorApproveController extends BaseController {
    @Autowired
    private UserDoctorApproveService	userDoctorApproveService;
    @Autowired
    private UserDoctorService userDoctorService;
    @Autowired
    private UserService userService;
    @Autowired
    private ApproveLogService approveLogService;
    @Autowired
    private OssMtsService ossMtsService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.userDoctorApprove.index");
        return "userDoctorApprove/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<UserDoctorApprove> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.userDoctorApprove.list");

        Page<UserDoctorApprove> page = userDoctorApproveService.search(searchParams(request), pageBean);

        setConfigFieldValues(page, Constants.USER_DOCTOR_APPROVE_STATUS_ENUM_KEY ,"status");
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.userDoctorApprove.formGet");
        if (isValidId(id)) {
            UserDoctorApprove userDoctorApprove = userDoctorApproveService.getOne(id);
            model.addAttribute("userDoctorApprove", userDoctorApprove);
        } else {
            model.addAttribute("userDoctorApprove", new UserDoctorApprove());
        }
        return "userDoctorApprove/form";
    }

    @RequestMapping(value = "/selectform", method = RequestMethod.GET)
    String selectform(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.userDoctorApprove.selectform");
        //根据id查询出 医生信息
        UserDoctor userDoctor = userDoctorService.getOne(id);
        if (userDoctor!=null) {
            UserDoctorApprove userDoctorApprove = new UserDoctorApprove();
            userDoctorApprove.setRealname(userDoctor.getUser().getRealname());
            userDoctorApprove.setUserId(userDoctor.getUserId());
            userDoctorApprove.setHospital(userDoctor.getHospital());
            List<ConfigBean> configBeanList = ProjectUtil.getConfigBeanList(Constants.PROFESSIONAL_TITLES_ENUM_KEY);
            model.addAttribute("userDoctorApprove", userDoctorApprove);
        } else {
            model.addAttribute("userDoctorApprove", new UserDoctorApprove());
        }

        model.addAttribute("psychologicalConsultantImageUrlList", new ArrayList<>());
        model.addAttribute("employeeImageUrlList", new ArrayList<>());
        model.addAttribute("doctorProfessionImageUrlList", new ArrayList<>());
        model.addAttribute("professionalQualificationImageUrlList", new ArrayList<>());






        return "userDoctorApprove/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(UserDoctorApprove userDoctorApprove, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        increment("web.userDoctorApprove.formPost");

        if(userDoctorApprove.getProfessionalTitleId()==5||userDoctorApprove.getProfessionalTitleId()==6){
            if(!StringUtils.isNotEmpty(userDoctorApprove.getPsychologicalConsultantImageUrl())){
                return toError("职称为心理咨询师二级或三级时，心理咨询师证证书图片必须上传!");
            }
            if(!StringUtils.isNotEmpty(userDoctorApprove.getEmployeeImageUrl())){
                return toError("职称为心理咨询师二级或三级时，工作证证书图片必须上传!");
            }
            if(!StringUtils.isNotEmpty(userDoctorApprove.getDoctorProfessionImageUrl())){
                return toError("职称为心理咨询师二级或三级时，医师职业证证书图片必须上传!");
            }
            if(!StringUtils.isNotEmpty(userDoctorApprove.getProfessionalQualificationImageUrl())){
                return toError("职称为心理咨询师二级或三级时，职称资格证证书图片必须上传!");
            }
        }
        if(userDoctorApprove.getWorkplaceType()==1){
            if(!StringUtils.isNotEmpty(userDoctorApprove.getDepartment())){
                return toError("场所为医院时，科室为必填项!!");
            }
        }
        if(userDoctorApprove.getWorkplaceType()==2){
            if(!StringUtils.isNotEmpty(userDoctorApprove.getPosition())){
                return toError("场所为医院时，职位为必填项!!");
            }
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获得文件
        Map<String,MultipartFile> coverPicFileMap = multipartRequest.getFileMap();
        //封面保存
        String coverPicUrl = ossMtsService.uploadStatic(coverPicFileMap.get("coverPicFile"));
        DoctorAuthBean doctorAuthBean = new DoctorAuthBean();
        PropertyUtils.copyProperties(doctorAuthBean, userDoctorApprove);
        if(StringUtils.isNotEmpty(coverPicUrl)){
            doctorAuthBean.setHeadPictureUrl(coverPicUrl);
        }
//        if(StringUtils.isNotEmpty(userDoctorApprove.getPsychologicalConsultantImageUrl())){
//            String[] psychologicalConsultantImageUrls = userDoctorApprove.getPsychologicalConsultantImageUrl().split(",");
//            String psychologicalConsultantImageUr = "";
//            for (String psychologicalConsultantImageUrl : psychologicalConsultantImageUrls) {
//                psychologicalConsultantImageUr += (psychologicalConsultantImageUrl+",");
//            }
//            psychologicalConsultantImageUr = psychologicalConsultantImageUr.substring(0,psychologicalConsultantImageUr.length() - 1);
//            doctorAuthBean.setPsychologicalConsultantImageUrl(psychologicalConsultantImageUr);
//        }

        userDoctorService.auth(doctorAuthBean, userDoctorApprove.getUserId());
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.userDoctorApprove.delete");
        userDoctorApproveService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping(value = "/approve", method = RequestMethod.GET)
    String approve(@RequestParam(value = "id", required = false) String id,
                   Model model, HttpServletRequest request) {
        increment("web.userDoctorApprove.approve");
        UserDoctorApprove userDoctorApprove = userDoctorApproveService.getOne(id);
        UserDoctor userDoctor = userDoctorService.getByUserId(userDoctorApprove.getUserId());
        User user = userService.findOne(userDoctorApprove.getUserId());
        model.addAttribute("userDoctorApprove", userDoctorApprove);
        model.addAttribute("userDoctor", userDoctor);
        model.addAttribute("user", user);
        return "userDoctorApprove/approve";
    }

    @RequestMapping("/approve")
    @ResponseBody
    AjaxResultBean resume(@RequestParam(value = "id") String id, int result,
                          @RequestParam(value = "remark", required = false) String remark) {
        increment("web.userDoctorApprove.resume");

        userDoctorApproveService.approve(id, result, remark, getCurrentUser());

        return toSuccess("审核成功");
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    String view(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.userDoctorApprove.view");

        UserDoctorApprove userDoctorApprove = userDoctorApproveService.getOne(id);
        if(StringUtils.isNotEmpty(userDoctorApprove.getPsychologicalConsultantImageUrl())) {
            if (userDoctorApprove.getPsychologicalConsultantImageUrl().indexOf("http:") == -1) {
                //如果不包含则返回授权url
                String[] psychologicalConsultantImageUrls = userDoctorApprove.getPsychologicalConsultantImageUrl().split(",");
                String psychologicalConsultantImageUr = "";
                for (String psychologicalConsultantImageUrl : psychologicalConsultantImageUrls) {
                    psychologicalConsultantImageUr += (ossMtsService.getImgAccessUrl(psychologicalConsultantImageUrl) + ",");
                }
                psychologicalConsultantImageUr = psychologicalConsultantImageUr.substring(0, psychologicalConsultantImageUr.length() - 1);
                userDoctorApprove.setPsychologicalConsultantImageUrl(psychologicalConsultantImageUr);
            }
        }
        if(StringUtils.isNotEmpty(userDoctorApprove.getEmployeeImageUrl())) {
            if (userDoctorApprove.getEmployeeImageUrl().indexOf("http:") == -1) {
                //如果不包含则返回授权url
                String[] employeeImageUrl = userDoctorApprove.getEmployeeImageUrl().split(",");
                String employeeImageUr = "";
                for (String employeeImageUrls : employeeImageUrl) {
                    employeeImageUr += (ossMtsService.getImgAccessUrl(employeeImageUrls) + ",");
                }
                employeeImageUr = employeeImageUr.substring(0, employeeImageUr.length() - 1);
                userDoctorApprove.setEmployeeImageUrl(employeeImageUr);
            }
        }
        if(StringUtils.isNotEmpty(userDoctorApprove.getDoctorProfessionImageUrl())) {
            if (userDoctorApprove.getDoctorProfessionImageUrl().indexOf("http:") == -1) {
                //如果不包含则返回授权url
                String[] doctorProfessionImageUrls = userDoctorApprove.getDoctorProfessionImageUrl().split(",");
                String doctorProfessionImageUrl = "";
                for (String doctorProfessionImageUrlo : doctorProfessionImageUrls) {
                    doctorProfessionImageUrl += (ossMtsService.getImgAccessUrl(doctorProfessionImageUrlo) + ",");
                }
                doctorProfessionImageUrl = doctorProfessionImageUrl.substring(0, doctorProfessionImageUrl.length() - 1);
                userDoctorApprove.setDoctorProfessionImageUrl(doctorProfessionImageUrl);
            }
        }
        if(StringUtils.isNotEmpty(userDoctorApprove.getProfessionalQualificationImageUrl())) {
            if(userDoctorApprove.getProfessionalQualificationImageUrl().indexOf("http:")==-1){
                //如果不包含则返回授权url
                String[] getProfessionalQualificationImageUrl = userDoctorApprove.getProfessionalQualificationImageUrl().split(",");
                String getProfessionalQualificationImageUr = "";
                for (String getProfessionalQualificationImageUro : getProfessionalQualificationImageUrl) {
                    getProfessionalQualificationImageUr += ( ossMtsService.getImgAccessUrl(getProfessionalQualificationImageUro)+",");
                }
                getProfessionalQualificationImageUr = getProfessionalQualificationImageUr.substring(0,getProfessionalQualificationImageUr.length() - 1);
                userDoctorApprove.setProfessionalQualificationImageUrl(getProfessionalQualificationImageUr);
            }
        }
        UserDoctor userDoctor = userDoctorService.getByUserId(userDoctorApprove.getUserId());
        User user = userService.findOne(userDoctorApprove.getUserId());
        model.addAttribute("userDoctorApprove", userDoctorApprove);
        model.addAttribute("userDoctor", userDoctor);
        model.addAttribute("user", user);

        return "userDoctorApprove/view";
    }



}


