package cn.easy.xinjing.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.easy.base.web.BaseController;
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
import cn.easy.xinjing.domain.AppVersion;
import cn.easy.xinjing.service.AppVersionService;

@Controller
@RequestMapping("/appVersion")
public class AppVersionController extends BaseController {
    @Autowired
    private AppVersionService	appVersionService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.appVersion.index");
        return "appVersion/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<AppVersion> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.appVersion.list");

        Page<AppVersion> page = appVersionService.search(searchParams(request), pageBean);
        setConfigFieldValues(page, Constants.APP_CLIENT_KEY, "appCode");
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.appVersion.formGet");
        if (isValidId(id)) {
            AppVersion appVersion = appVersionService.getOne(id);
            model.addAttribute("appVersion", appVersion);
        } else {
            model.addAttribute("appVersion", new AppVersion());
        }
        return "appVersion/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(AppVersion appVersion, HttpServletRequest request) {
        increment("web.appVersion.formPost");
        appVersionService.save(appVersion);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.appVersion.delete");
        appVersionService.delete(id);
        return toSuccess("删除成功");
    }

}


