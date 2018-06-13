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
import cn.easy.xinjing.domain.VrRoomAppTask;
import cn.easy.xinjing.service.VrRoomAppTaskService;

@Controller
@RequestMapping("/vrRoomAppTask")
public class VrRoomAppTaskController extends BaseController {
    @Autowired
    private VrRoomAppTaskService	vrRoomAppTaskService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.vrRoomAppTask.index");
        return "vrRoomAppTask/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<VrRoomAppTask> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.vrRoomAppTask.list");

        Page<VrRoomAppTask> page = vrRoomAppTaskService.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.vrRoomAppTask.formGet");
        if (isValidId(id)) {
            VrRoomAppTask vrRoomAppTask = vrRoomAppTaskService.getOne(id);
            model.addAttribute("vrRoomAppTask", vrRoomAppTask);
        } else {
            model.addAttribute("vrRoomAppTask", new VrRoomAppTask());
        }
        return "vrRoomAppTask/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(VrRoomAppTask vrRoomAppTask, HttpServletRequest request) {
        increment("web.vrRoomAppTask.formPost");
        vrRoomAppTaskService.save(vrRoomAppTask);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.vrRoomAppTask.delete");
        vrRoomAppTaskService.delete(id);
        return toSuccess("删除成功");
    }

}


