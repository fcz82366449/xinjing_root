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
import cn.easy.xinjing.domain.DoctorAppointment;
import cn.easy.xinjing.service.DoctorAppointmentService;

@Controller
@RequestMapping("/doctorAppointment")
public class DoctorAppointmentController extends BaseController {
    @Autowired
    private DoctorAppointmentService	doctorAppointmentService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.doctorAppointment.index");
        return "doctorAppointment/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<DoctorAppointment> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.doctorAppointment.list");

        Page<DoctorAppointment> page = doctorAppointmentService.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.doctorAppointment.formGet");
        if (isValidId(id)) {
            DoctorAppointment doctorAppointment = doctorAppointmentService.getOne(id);
            model.addAttribute("doctorAppointment", doctorAppointment);
        } else {
            model.addAttribute("doctorAppointment", new DoctorAppointment());
        }
        return "doctorAppointment/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(DoctorAppointment doctorAppointment, HttpServletRequest request) {
        increment("web.doctorAppointment.formPost");
        doctorAppointmentService.save(doctorAppointment);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.doctorAppointment.delete");
        doctorAppointmentService.delete(id);
        return toSuccess("删除成功");
    }

}


