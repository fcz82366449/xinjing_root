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
import cn.easy.xinjing.domain.ClickRecord;
import cn.easy.xinjing.service.ClickRecordService;

@Controller
@RequestMapping("/clickRecord")
public class ClickRecordController extends BaseController {
    @Autowired
    private ClickRecordService	clickRecordService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        return "clickRecord/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<ClickRecord> list(PageBean pageBean, HttpServletRequest request) {
        Page<ClickRecord> page = clickRecordService.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        if (isValidId(id)) {
            ClickRecord clickRecord = clickRecordService.getOne(id);
            model.addAttribute("clickRecord", clickRecord);
        } else {
            model.addAttribute("clickRecord", new ClickRecord());
        }
        return "clickRecord/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(ClickRecord clickRecord, HttpServletRequest request) {
        clickRecordService.save(clickRecord);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        clickRecordService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/view")
    String view(@RequestParam(value = "id") String id, Model model) {
        ClickRecord clickRecord = clickRecordService.getOne(id);
        model.addAttribute("clickRecord", clickRecord);
        return "clickRecord/view";
    }

}


