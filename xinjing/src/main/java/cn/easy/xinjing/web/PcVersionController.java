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
import cn.easy.xinjing.domain.PcVersion;
import cn.easy.xinjing.service.PcVersionService;

@Controller
@RequestMapping("/pcVersion")
public class PcVersionController extends BaseController {
    @Autowired
    private PcVersionService	pcVersionService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        return "pcVersion/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<PcVersion> list(PageBean pageBean, HttpServletRequest request) {
        Page<PcVersion> page = pcVersionService.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        if (isValidId(id)) {
            PcVersion pcVersion = pcVersionService.getOne(id);
            model.addAttribute("pcVersion", pcVersion);
        } else {
            model.addAttribute("pcVersion", new PcVersion());
        }
        return "pcVersion/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(PcVersion pcVersion, HttpServletRequest request) {
        pcVersionService.save(pcVersion);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        pcVersionService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/view")
    String view(@RequestParam(value = "id") String id, Model model) {
        PcVersion pcVersion = pcVersionService.getOne(id);
        model.addAttribute("pcVersion", pcVersion);
        return "pcVersion/view";
    }

}


