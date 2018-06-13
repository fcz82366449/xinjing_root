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
import cn.easy.xinjing.domain.PrescriptionContentFeedback;
import cn.easy.xinjing.service.PrescriptionContentFeedbackService;

@Controller
@RequestMapping("/prescriptionContentFeedback")
public class PrescriptionContentFeedbackController extends BaseController {
    @Autowired
    private PrescriptionContentFeedbackService	prescriptionContentFeedbackService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.prescriptionContentFeedback.index");
        return "prescriptionContentFeedback/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<PrescriptionContentFeedback> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.prescriptionContentFeedback.list");

        Page<PrescriptionContentFeedback> page = prescriptionContentFeedbackService.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.prescriptionContentFeedback.formGet");
        if (isValidId(id)) {
            PrescriptionContentFeedback prescriptionContentFeedback = prescriptionContentFeedbackService.getOne(id);
            model.addAttribute("prescriptionContentFeedback", prescriptionContentFeedback);
        } else {
            model.addAttribute("prescriptionContentFeedback", new PrescriptionContentFeedback());
        }
        return "prescriptionContentFeedback/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(PrescriptionContentFeedback prescriptionContentFeedback, HttpServletRequest request) {
        increment("web.prescriptionContentFeedback.formPost");
        prescriptionContentFeedbackService.save(prescriptionContentFeedback);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.prescriptionContentFeedback.delete");
        prescriptionContentFeedbackService.delete(id);
        return toSuccess("删除成功");
    }

}


