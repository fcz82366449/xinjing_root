package cn.easy.xinjing.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.easy.base.web.BaseController;
import cn.easy.xinjing.domain.Content;
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
import cn.easy.xinjing.domain.PrescriptionContent;
import cn.easy.xinjing.service.PrescriptionContentService;

@Controller
@RequestMapping("/prescriptionContent")
public class PrescriptionContentController extends BaseController {
    @Autowired
    private PrescriptionContentService	prescriptionContentService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.prescriptionContent.index");
        return "prescriptionContent/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<PrescriptionContent> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.prescriptionContent.list");

        Page<PrescriptionContent> page = prescriptionContentService.search(searchParams(request), pageBean);
        setFieldValues(page, Content.class, "contentId", new String[]{"name"});
        setConfigFieldValues(page, Constants.PRESCRIPTION_CONTENT_STATUS_KEY, "status");
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.prescriptionContent.formGet");
        if (isValidId(id)) {
            PrescriptionContent prescriptionContent = prescriptionContentService.getOne(id);
            model.addAttribute("prescriptionContent", prescriptionContent);
        } else {
            model.addAttribute("prescriptionContent", new PrescriptionContent());
        }
        return "prescriptionContent/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(PrescriptionContent prescriptionContent, HttpServletRequest request) {
        increment("web.prescriptionContent.formPost");
        prescriptionContentService.save(prescriptionContent);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.prescriptionContent.delete");
        prescriptionContentService.delete(id);
        return toSuccess("删除成功");
    }

}


