package cn.easy.xinjing.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import cn.easy.xinjing.domain.AppToken;
import cn.easy.xinjing.service.AppTokenService;

@Controller
@RequestMapping("/appToken")
public class AppTokenController extends BaseController {
    @Autowired
    private AppTokenService	appTokenService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.appToken.index");
        return "appToken/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<AppToken> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.appToken.list");

        Page<AppToken> page = appTokenService.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.appToken.formGet");
        if (isValidId(id)) {
            AppToken appToken = appTokenService.getOne(id);
            model.addAttribute("appToken", appToken);
        } else {
            model.addAttribute("appToken", new AppToken());
        }
        return "appToken/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(AppToken appToken, HttpServletRequest request) {
        increment("web.appToken.formPost");
        appTokenService.save(appToken);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.appToken.delete");
        appTokenService.delete(id);
        return toSuccess("删除成功");
    }

}


