package cn.easy.xinjing.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.easy.base.domain.User;
import cn.easy.base.service.UserService;
import cn.easy.base.web.BaseController;
import cn.easy.xinjing.domain.ApproveLog;
import cn.easy.xinjing.domain.UserDoctor;
import cn.easy.xinjing.service.ApproveLogService;
import cn.easy.xinjing.service.UserDoctorService;
import cn.easy.xinjing.utils.Constants;
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
import cn.easy.xinjing.domain.DoctorArticle;
import cn.easy.xinjing.service.DoctorArticleService;

@Controller
@RequestMapping("/doctorArticle")
public class DoctorArticleController extends BaseController {
    @Autowired
    private DoctorArticleService	doctorArticleService;
    @Autowired
    private ApproveLogService       approveLogService;
    @Autowired
    private UserDoctorService       userDoctorService;
    @Autowired
    private UserService             userService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.doctorArticle.index");
        return "doctorArticle/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<DoctorArticle> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.doctorArticle.list");

        Page<DoctorArticle> page = doctorArticleService.search(searchParams(request), pageBean);
        setConfigFieldValues(page, Constants.DOCTOR_ARTICLE_STATUS_ENUM_KEY, "status");
        setFieldValues(page, UserDoctor.class, "doctorId", new String[]{"userId"});
        setFieldValues(page, User.class, "doctor_userId", new String[]{"realname"});
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.doctorArticle.formGet");
        if (isValidId(id)) {
            DoctorArticle doctorArticle = doctorArticleService.getOne(id);
            UserDoctor userDoctor = userDoctorService.getOne(doctorArticle == null ? "" : doctorArticle.getDoctorId());
            User user = userService.getOne(userDoctor == null ? "" : userDoctor.getUserId());
            model.addAttribute("doctorArticle", doctorArticle);
            model.addAttribute("doctorName", user == null ? "" : user.getRealname());
        } else {
            model.addAttribute("doctorArticle", new DoctorArticle());
        }
        return "doctorArticle/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(DoctorArticle doctorArticle, HttpServletRequest request) {
        increment("web.doctorArticle.formPost");
        doctorArticleService.save(doctorArticle);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.doctorArticle.delete");
        doctorArticleService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping(value = "/approve", method = RequestMethod.GET)
    String approve(@RequestParam(value = "id", required = false) String id,
                   Model model, HttpServletRequest request) {
        increment("web.doctorArticle.approve");
        DoctorArticle doctorArticle = doctorArticleService.getOne(id);
        model.addAttribute("doctorArticle", doctorArticle);
        return "doctorArticle/approve";
    }

    @RequestMapping("/approve")
    @ResponseBody
    AjaxResultBean approve(@RequestParam(value = "id") String id, int result,
                          @RequestParam(value = "remark", required = false) String remark) {
        increment("web.doctorArticle.resume");
        DoctorArticle doctorArticle = doctorArticleService.getOne(id);
        if(result == 1) {
            doctorArticle.setStatus(Constants.DOCTOR_ARTICLE_STATUS_PASSED);
        }
        else {
            doctorArticle.setStatus(Constants.DOCTOR_ARTICLE_STATUS_NO_PASS);
        }
        doctorArticleService.save(doctorArticle);

        //记录操作日志
        ApproveLog approveLog = new ApproveLog();
        approveLog.setObjectId(doctorArticle.getId());
        approveLog.setObjectType(DoctorArticle.class.getName());
        approveLog.setResult(getConfigValue(Constants.DOCTOR_ARTICLE_STATUS_ENUM_KEY, String.valueOf(doctorArticle.getStatus())));
        approveLog.setRemark(remark);
        approveLogService.save(approveLog);
        return toSuccess("审核成功");
    }

}


