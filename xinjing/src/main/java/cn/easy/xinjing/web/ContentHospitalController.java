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
import cn.easy.xinjing.domain.ContentHospital;
import cn.easy.xinjing.service.ContentHospitalService;

@Controller
@RequestMapping("/contentHospital")
public class ContentHospitalController extends BaseController {
    @Autowired
    private ContentHospitalService	contentHospitalService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        return "contentHospital/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<ContentHospital> list(PageBean pageBean, HttpServletRequest request) {
        Page<ContentHospital> page = contentHospitalService.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        if (isValidId(id)) {
            ContentHospital contentHospital = contentHospitalService.getOne(id);
            model.addAttribute("contentHospital", contentHospital);
        } else {
            model.addAttribute("contentHospital", new ContentHospital());
        }
        return "contentHospital/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(ContentHospital contentHospital, HttpServletRequest request) {
        contentHospitalService.save(contentHospital);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        contentHospitalService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/view")
    String view(@RequestParam(value = "id") String id, Model model) {
        ContentHospital contentHospital = contentHospitalService.getOne(id);
        model.addAttribute("contentHospital", contentHospital);
        return "contentHospital/view";
    }

}


