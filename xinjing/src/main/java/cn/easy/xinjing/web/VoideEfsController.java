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
import cn.easy.xinjing.domain.VoideEfs;
import cn.easy.xinjing.service.VoideEfsService;

@Controller
@RequestMapping("/voideEfs")
public class VoideEfsController extends BaseController {
    @Autowired
    private VoideEfsService	voideEfsService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        return "voideEfs/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<VoideEfs> list(PageBean pageBean, HttpServletRequest request) {
        Page<VoideEfs> page = voideEfsService.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        if (isValidId(id)) {
            VoideEfs voideEfs = voideEfsService.getOne(id);
            model.addAttribute("voideEfs", voideEfs);
        } else {
            model.addAttribute("voideEfs", new VoideEfs());
        }
        return "voideEfs/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(VoideEfs voideEfs, HttpServletRequest request) {
        voideEfsService.save(voideEfs);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        voideEfsService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/view")
    String view(@RequestParam(value = "id") String id, Model model) {
        VoideEfs voideEfs = voideEfsService.getOne(id);
        model.addAttribute("voideEfs", voideEfs);
        return "voideEfs/view";
    }

}


