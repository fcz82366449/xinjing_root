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
import cn.easy.xinjing.domain.DoctorPatientcase;
import cn.easy.xinjing.service.DoctorPatientcaseService;

@Controller
@RequestMapping("/doctorPatientcase")
public class DoctorPatientcaseController extends BaseController {
    @Autowired
    private DoctorPatientcaseService	doctorPatientcaseService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        return "doctorPatientcase/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<DoctorPatientcase> list(PageBean pageBean, HttpServletRequest request) {
        Page<DoctorPatientcase> page = doctorPatientcaseService.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        if (isValidId(id)) {
            DoctorPatientcase doctorPatientcase = doctorPatientcaseService.getOne(id);
            model.addAttribute("doctorPatientcase", doctorPatientcase);
        } else {
            model.addAttribute("doctorPatientcase", new DoctorPatientcase());
        }
        return "doctorPatientcase/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(DoctorPatientcase doctorPatientcase, HttpServletRequest request) {
        doctorPatientcaseService.save(doctorPatientcase);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        doctorPatientcaseService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/view")
    String view(@RequestParam(value = "id") String id, Model model) {
        DoctorPatientcase doctorPatientcase = doctorPatientcaseService.getOne(id);
        model.addAttribute("doctorPatientcase", doctorPatientcase);
        return "doctorPatientcase/view";
    }

}


