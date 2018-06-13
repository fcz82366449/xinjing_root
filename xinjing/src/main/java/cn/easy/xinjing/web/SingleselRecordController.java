package cn.easy.xinjing.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.easy.base.domain.User;
import cn.easy.base.web.BaseController;
import cn.easy.xinjing.bean.api.PrescriptionContentIdBean;
import cn.easy.xinjing.domain.Evaluating;
import cn.easy.xinjing.domain.PrescriptionCase;
import cn.easy.xinjing.domain.Singlesel;
import cn.easy.xinjing.service.EvaluatingService;
import cn.easy.xinjing.service.SingleselService;
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
import cn.easy.xinjing.domain.SingleselRecord;
import cn.easy.xinjing.service.SingleselRecordService;

@Controller
@RequestMapping("/singleselRecord")
public class SingleselRecordController extends BaseController {
    @Autowired
    private SingleselRecordService	singleselRecordService;
    @Autowired
    private EvaluatingService evaluatingService;
    @Autowired
    private SingleselService singleselService;
    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.singleselRecord.index");
        return "singleselRecord/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<SingleselRecord> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.singleselRecord.list");

        Page<SingleselRecord> page = singleselRecordService.search(searchParams(request), pageBean);
        setFieldValues(page, Evaluating.class, "evaluatingId", new String[]{"name"});
        setFieldValues(page, Singlesel.class, "singleselId", new String[]{"code"});
        setFieldValues(page, PrescriptionCase.class, "evaluationPeople", new String[]{"name"});
        setFieldValues(page, User.class,"creator", new String[]{"realname"});
        setFieldValues(page, User.class,"updator", new String[]{"realname"});
        setConfigFieldValues(page, "EVALUATING_STATUS_ENUM", "status");
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.singleselRecord.formGet");
        if (isValidId(id)) {
            SingleselRecord singleselRecord = singleselRecordService.getOne(id);
            if(singleselRecord!=null){
                Evaluating evaluating = evaluatingService.getOne(singleselRecord.getEvaluatingId());
                Singlesel singlesel = singleselService.getOne(singleselRecord.getSingleselId());

                model.addAttribute("singleselCode", singlesel==null?"":singlesel.getCode());
                model.addAttribute("singleselId", singlesel==null?"":singlesel.getId());

                model.addAttribute("evaluatingName", evaluating==null?"":evaluating.getName());
                model.addAttribute("evaluatingId", evaluating==null?"":evaluating.getId());
            }
            model.addAttribute("singleselRecord", singleselRecord);
        } else {
            model.addAttribute("singleselRecord", new SingleselRecord());
        }
        return "singleselRecord/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(SingleselRecord singleselRecord, HttpServletRequest request) {
        increment("web.singleselRecord.formPost");
        singleselRecordService.save(singleselRecord);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.singleselRecord.delete");
        singleselRecordService.delete(id);
        return toSuccess("删除成功");
    }

}


