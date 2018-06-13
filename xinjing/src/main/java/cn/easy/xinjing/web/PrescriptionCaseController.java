package cn.easy.xinjing.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.easy.base.web.BaseController;
import cn.easy.xinjing.domain.VrRoom;
import cn.easy.xinjing.service.VrRoomService;
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
import cn.easy.xinjing.domain.PrescriptionCase;
import cn.easy.xinjing.service.PrescriptionCaseService;

@Controller
@RequestMapping("/prescriptionCase")
public class PrescriptionCaseController extends BaseController {
    @Autowired
    private PrescriptionCaseService	prescriptionCaseService;
    @Autowired
    private VrRoomService vrRoomService;
    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        return "prescriptionCase/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<PrescriptionCase> list(PageBean pageBean, HttpServletRequest request) {
        Page<PrescriptionCase> page = prescriptionCaseService.search(searchParams(request), pageBean);
        setFieldValues(page, VrRoom.class, "vrroomid", new String[]{"name"});
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        String vrRoomName ="";
        if (isValidId(id)) {
            PrescriptionCase prescriptionCase = prescriptionCaseService.getOne(id);
            VrRoom vrRoom = vrRoomService.getOne(prescriptionCase.getVrroomid());
            if(vrRoom != null){
                vrRoomName = vrRoom.getName();
            }
            model.addAttribute("prescriptionCase", prescriptionCase);
        } else {
            model.addAttribute("prescriptionCase", new PrescriptionCase());
        }
        model.addAttribute("vrRoomName", vrRoomName);
        return "prescriptionCase/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(PrescriptionCase prescriptionCase, HttpServletRequest request) {
        prescriptionCaseService.save(prescriptionCase);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        prescriptionCaseService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/view")
    String view(@RequestParam(value = "id") String id, Model model) {
        PrescriptionCase prescriptionCase = prescriptionCaseService.getOne(id);
        model.addAttribute("prescriptionCase", prescriptionCase);
        return "prescriptionCase/view";
    }

}


