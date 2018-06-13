package cn.easy.xinjing.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.easy.base.web.BaseController;
import cn.easy.xinjing.domain.UserDoctor;
import cn.easy.xinjing.domain.UserVrRoom;
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
import cn.easy.xinjing.domain.Charges;
import cn.easy.xinjing.service.ChargesService;

@Controller
@RequestMapping("/charges")
public class ChargesController extends BaseController {
    @Autowired
    private ChargesService	chargesService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        return "charges/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<Charges> list(PageBean pageBean, HttpServletRequest request) {
        Page<Charges> page = chargesService.search(searchParams(request), pageBean);

//        setFieldValues(page, UserDoctor.class,"doctorId", new String[]{"user"});
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        if (isValidId(id)) {
            Charges charges = chargesService.getOne(id);
            model.addAttribute("charges", charges);
        } else {
            model.addAttribute("charges", new Charges());
        }
        return "charges/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(Charges charges, HttpServletRequest request) {
        chargesService.save(charges);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        chargesService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/view")
    String view(@RequestParam(value = "id") String id, Model model) {
        Charges charges = chargesService.getOne(id);
        model.addAttribute("charges", charges);
        return "charges/view";
    }

}


