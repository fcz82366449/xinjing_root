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
import cn.easy.xinjing.domain.Calllimit;
import cn.easy.xinjing.service.CalllimitService;

@Controller
@RequestMapping("/calllimit")
public class CalllimitController extends BaseController {
    @Autowired
    private CalllimitService	calllimitService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        return "calllimit/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<Calllimit> list(PageBean pageBean, HttpServletRequest request) {
        Page<Calllimit> page = calllimitService.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        if (isValidId(id)) {
            Calllimit calllimit = calllimitService.getOne(id);
            model.addAttribute("calllimit", calllimit);
        } else {
            model.addAttribute("calllimit", new Calllimit());
        }
        return "calllimit/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(Calllimit calllimit, HttpServletRequest request) {
        calllimitService.save(calllimit);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        calllimitService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/view")
    String view(@RequestParam(value = "id") String id, Model model) {
        Calllimit calllimit = calllimitService.getOne(id);
        model.addAttribute("calllimit", calllimit);
        return "calllimit/view";
    }

}


