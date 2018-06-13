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
import cn.easy.xinjing.domain.FrontpageCollect;
import cn.easy.xinjing.service.FrontpageCollectService;

@Controller
@RequestMapping("/frontpageCollect")
public class FrontpageCollectController extends BaseController {
    @Autowired
    private FrontpageCollectService	frontpageCollectService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        return "frontpageCollect/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<FrontpageCollect> list(PageBean pageBean, HttpServletRequest request) {
        Page<FrontpageCollect> page = frontpageCollectService.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        if (isValidId(id)) {
            FrontpageCollect frontpageCollect = frontpageCollectService.getOne(id);
            model.addAttribute("frontpageCollect", frontpageCollect);
        } else {
            model.addAttribute("frontpageCollect", new FrontpageCollect());
        }
        return "frontpageCollect/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(FrontpageCollect frontpageCollect, HttpServletRequest request) {
        frontpageCollectService.save(frontpageCollect);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        frontpageCollectService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/view")
    String view(@RequestParam(value = "id") String id, Model model) {
        FrontpageCollect frontpageCollect = frontpageCollectService.getOne(id);
        model.addAttribute("frontpageCollect", frontpageCollect);
        return "frontpageCollect/view";
    }

}


