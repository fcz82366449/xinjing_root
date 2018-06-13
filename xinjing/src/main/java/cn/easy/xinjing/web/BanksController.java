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
import cn.easy.xinjing.domain.Banks;
import cn.easy.xinjing.service.BanksService;

@Controller
@RequestMapping("/banks")
public class BanksController extends BaseController {
    @Autowired
    private BanksService	banksService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.banks.index");
        return "banks/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<Banks> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.banks.list");

        Page<Banks> page = banksService.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.banks.formGet");
        if (isValidId(id)) {
            Banks banks = banksService.getOne(id);
            model.addAttribute("banks", banks);
        } else {
            model.addAttribute("banks", new Banks());
        }
        return "banks/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(Banks banks, HttpServletRequest request) {
        increment("web.banks.formPost");
        banksService.save(banks);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.banks.delete");
        banksService.delete(id);
        return toSuccess("删除成功");
    }

}


