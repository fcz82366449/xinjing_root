package cn.easy.xinjing.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.easy.base.web.BaseController;
import cn.easy.xinjing.domain.Content;
import cn.easy.xinjing.domain.VrRoom;
import cn.easy.xinjing.service.ContentService;
import cn.easy.xinjing.service.VrRoomService;
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
import cn.easy.xinjing.domain.VrRoomAppointment;
import cn.easy.xinjing.service.VrRoomAppointmentService;

@Controller
@RequestMapping("/vrRoomAppointment")
public class VrRoomAppointmentController extends BaseController {
    @Autowired
    private VrRoomAppointmentService	vrRoomAppointmentService;
    @Autowired
    private VrRoomService vrRoomService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.vrRoomAppointment.index");
        return "vrRoomAppointment/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<VrRoomAppointment> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.vrRoomAppointment.list");

        Page<VrRoomAppointment> page = vrRoomAppointmentService.search(searchParams(request), pageBean);
        setConfigFieldValues(page, Constants.VR_ROOM_APPOINTMENT_TYPE_ENUM_KEY, "type");
        setConfigFieldValues(page, Constants.VR_ROOM_APPOINTMENT_STATUS_KEY, "status");
        setFieldValues(page, VrRoom.class,"vrRoomId",new String[]{"name"});
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.vrRoomAppointment.formGet");
        VrRoomAppointment vrRoomAppointment = new VrRoomAppointment();
        String vrRoomName = "";
        if (isValidId(id)) {
            vrRoomAppointment = vrRoomAppointmentService.getOne(id);
            VrRoom vrRoom = vrRoomService.getOne(vrRoomAppointment.getVrRoomId());
            vrRoomName = vrRoom.getName();
        }
        model.addAttribute("vrRoomAppointment", vrRoomAppointment);
        model.addAttribute("vrRoomName", vrRoomName);
        return "vrRoomAppointment/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(VrRoomAppointment vrRoomAppointment, HttpServletRequest request) {
        increment("web.vrRoomAppointment.formPost");
        vrRoomAppointmentService.save(vrRoomAppointment);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.vrRoomAppointment.delete");
        vrRoomAppointmentService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/order")
    @ResponseBody
    AjaxResultBean order(@RequestParam(value = "id") String id) {
        increment("web.vrRoomAppointment.order");
        VrRoomAppointment vrRoomAppointment = vrRoomAppointmentService.getOne(id);
        vrRoomAppointment.setStatus(Constants.VR_ROOM_APPOINTMENT_STATUS_ORDER);
        vrRoomAppointmentService.save(vrRoomAppointment);
        return toSuccess("预约成功");
    }

    @RequestMapping("/cancelOrder")
    @ResponseBody
    AjaxResultBean cancelOrder(@RequestParam(value = "id") String id) {
        increment("web.vrRoomAppointment.cancelOrder");
        VrRoomAppointment vrRoomAppointment = vrRoomAppointmentService.getOne(id);
        vrRoomAppointment.setStatus(Constants.VR_ROOM_APPOINTMENT_STATUS_CANCEL_ORDER);
        vrRoomAppointmentService.save(vrRoomAppointment);
        return toSuccess("取消预约成功");
    }

}


