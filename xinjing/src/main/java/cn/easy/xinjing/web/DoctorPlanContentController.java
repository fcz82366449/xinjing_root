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
import cn.easy.xinjing.domain.DoctorPlanContent;
import cn.easy.xinjing.service.DoctorPlanContentService;

@Controller
@RequestMapping("/doctorPlanContent")
public class DoctorPlanContentController extends BaseController {
    @Autowired
    private DoctorPlanContentService	doctorPlanContentService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        return "doctorPlanContent/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<DoctorPlanContent> list(PageBean pageBean, HttpServletRequest request) {
        Page<DoctorPlanContent> page = doctorPlanContentService.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        if (isValidId(id)) {
            DoctorPlanContent doctorPlanContent = doctorPlanContentService.getOne(id);
            model.addAttribute("doctorPlanContent", doctorPlanContent);
        } else {
            model.addAttribute("doctorPlanContent", new DoctorPlanContent());
        }
        return "doctorPlanContent/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(DoctorPlanContent doctorPlanContent, HttpServletRequest request) {
        doctorPlanContentService.save(doctorPlanContent);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        doctorPlanContentService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/view")
    String view(@RequestParam(value = "id") String id, Model model) {
        DoctorPlanContent doctorPlanContent = doctorPlanContentService.getOne(id);
        model.addAttribute("doctorPlanContent", doctorPlanContent);
        return "doctorPlanContent/view";
    }

}


