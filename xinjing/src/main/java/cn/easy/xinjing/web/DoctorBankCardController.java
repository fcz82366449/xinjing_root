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
import cn.easy.xinjing.domain.DoctorBankCard;
import cn.easy.xinjing.service.DoctorBankCardService;

@Controller
@RequestMapping("/doctorBankCard")
public class DoctorBankCardController extends BaseController {
    @Autowired
    private DoctorBankCardService	doctorBankCardService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.doctorBankCard.index");
        return "doctorBankCard/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<DoctorBankCard> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.doctorBankCard.list");

        Page<DoctorBankCard> page = doctorBankCardService.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.doctorBankCard.formGet");
        if (isValidId(id)) {
            DoctorBankCard doctorBankCard = doctorBankCardService.getOne(id);
            model.addAttribute("doctorBankCard", doctorBankCard);
        } else {
            model.addAttribute("doctorBankCard", new DoctorBankCard());
        }
        return "doctorBankCard/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(DoctorBankCard doctorBankCard, HttpServletRequest request) {
        increment("web.doctorBankCard.formPost");
        doctorBankCardService.save(doctorBankCard);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.doctorBankCard.delete");
        doctorBankCardService.delete(id);
        return toSuccess("删除成功");
    }

}


