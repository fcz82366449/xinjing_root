package cn.easy.xinjing.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.easy.base.domain.User;
import cn.easy.base.service.UserService;
import cn.easy.base.web.BaseController;
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
import cn.easy.xinjing.domain.UserPatient;
import cn.easy.xinjing.service.UserPatientService;

@Controller
@RequestMapping("/userPatient")
public class UserPatientController extends BaseController {
    @Autowired
    private UserPatientService	userPatientService;
    @Autowired
    private UserService userService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.userPatient.index");
        return "userPatient/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<UserPatient> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.userPatient.list");

        Page<UserPatient> page = userPatientService.search(searchParams(request), pageBean);
        setFieldValues(page, User.class, "userId", new String[]{"username", "realname", "mobile"});
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.userPatient.formGet");
        if (isValidId(id)) {
            UserPatient userPatient = userPatientService.getOne(id);
            model.addAttribute("userPatient", userPatient);
        } else {
            model.addAttribute("userPatient", new UserPatient());
        }
        return "userPatient/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(UserPatient userPatient, HttpServletRequest request) {
        increment("web.userPatient.formPost");
        userPatientService.save(userPatient);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.userPatient.delete");
        userPatientService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    String view(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.userPatient.view");
        UserPatient userPatient = userPatientService.getOne(id);
        User user = userService.findOne(userPatient.getUserId());
        model.addAttribute("userPatient", userPatient);
        model.addAttribute("user", user);
        return "userPatient/view";
    }
}


