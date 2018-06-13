package cn.easy.xinjing.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.easy.base.domain.User;
import cn.easy.base.service.UserService;
import cn.easy.base.web.BaseController;
import cn.easy.xinjing.domain.*;
import cn.easy.xinjing.service.*;
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

import java.util.List;

@Controller
@RequestMapping("/prescription")
public class PrescriptionController extends BaseController {
    @Autowired
    private PrescriptionService	prescriptionService;
    @Autowired
    private UserDoctorService   userDoctorService;
    @Autowired
    private UserPatientService userPatientService;
    @Autowired
    private UserService        userService;
    @Autowired
    private PrescriptionContentService prescriptionContentService;

    @Autowired
    private HospitalService hospitalService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.prescription.index");
        return "prescription/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<Prescription> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.prescription.list");

        Page<Prescription> page = prescriptionService.search(searchParams(request), pageBean);
        setConfigFieldValues(page, Constants.PRESCRIPTION_STATUS_KEY, "status");
        setConfigFieldValues(page, Constants.PRESCRIPTION_PAY_STATUS_KEY, "payStatus");
        setFieldValues(page, User.class, "doctorId", new String[]{"realname"});
        setFieldValues(page, User.class, "patientId", new String[]{"realname"});
        setFieldValues(page, Hospital.class, "hospitalId", new String[]{"name"});
        setFieldValues(page, PrescriptionCase.class, "patientcaseId", new String[]{"name"});
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.prescription.formGet");
        if (isValidId(id)) {
            Prescription prescription = prescriptionService.getOne(id);
            model.addAttribute("prescription", prescription);
        } else {
            model.addAttribute("prescription", new Prescription());
        }
        return "prescription/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(Prescription prescription, HttpServletRequest request) {
        increment("web.prescription.formPost");
        prescriptionService.save(prescription);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.prescription.delete");
        prescriptionService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    String view(String id, Model model, HttpServletRequest request) {
        increment("web.prescription.view");
        Prescription prescription = prescriptionService.getOne(id);
        User doctorUser = userService.getOne(prescription == null ? "" : prescription.getDoctorId());
        prescription.setDoctorId(doctorUser == null ? "" : doctorUser.getRealname());
        User patientUser = userService.getOne(prescription == null ? "" : prescription.getPatientId());
        prescription.setPatientId(patientUser == null ? "" : patientUser.getRealname());

        Hospital hospital = hospitalService.getOne(prescription == null ? "" : prescription.getHospitalId());
        prescription.setHospitalId(hospital == null ? "" : hospital.getName());
        model.addAttribute("prescription", prescription);

        return "prescription/view";
    }
}


