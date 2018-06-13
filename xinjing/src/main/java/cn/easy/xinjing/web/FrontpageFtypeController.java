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
import cn.easy.xinjing.domain.FrontpageFtype;
import cn.easy.xinjing.service.FrontpageFtypeService;

@Controller
@RequestMapping("/frontpageFtype")
public class FrontpageFtypeController extends BaseController {
    @Autowired
    private FrontpageFtypeService	frontpageFtypeService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.frontpageFtype.index");
        return "frontpageFtype/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<FrontpageFtype> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.frontpageFtype.list");

        Page<FrontpageFtype> page = frontpageFtypeService.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.frontpageFtype.formGet");
        if (isValidId(id)) {
            FrontpageFtype frontpageFtype = frontpageFtypeService.getOne(id);
            model.addAttribute("frontpageFtype", frontpageFtype);
        } else {
            model.addAttribute("frontpageFtype", new FrontpageFtype());
        }
        return "frontpageFtype/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(FrontpageFtype frontpageFtype, HttpServletRequest request) {
        increment("web.frontpageFtype.formPost");
        frontpageFtypeService.save(frontpageFtype);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.frontpageFtype.delete");
        frontpageFtypeService.delete(id);
        return toSuccess("删除成功");
    }

}


