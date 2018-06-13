package cn.easy.xinjing.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.easy.base.domain.User;
import cn.easy.base.utils.Global;
import cn.easy.base.web.BaseController;
import cn.easy.xinjing.domain.Evaluating;
import cn.easy.xinjing.service.EvaluatingService;
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
import cn.easy.xinjing.domain.Singlesel;
import cn.easy.xinjing.service.SingleselService;

@Controller
@RequestMapping("/singlesel")
public class SingleselController extends BaseController {
    @Autowired
    private SingleselService	singleselService;

    @Autowired
    private EvaluatingService evaluatingService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.singlesel.index");
        return "singlesel/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<Singlesel> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.singlesel.list");

        Page<Singlesel> page = singleselService.search(searchParams(request), pageBean);
        setFieldValues(page, Evaluating.class, "evaluatingId", new String[]{"name"});
        setFieldValues(page, User.class,"creator", new String[]{"realname"});
        setFieldValues(page, User.class,"updator", new String[]{"realname"});
        setConfigFieldValues(page, "EVALUATING_STATUS_ENUM", "status");
        setConfigFieldValues(page, "EVALUATINGTYPE_STATUS_ENUM", "singtype");
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.singlesel.formGet");
        if (isValidId(id)) {
            Singlesel singlesel = singleselService.getOne(id);
            if(singlesel!=null){
                Evaluating evaluating = evaluatingService.getOne(singlesel.getEvaluatingId());
                model.addAttribute("evaluatingName", evaluating==null?"":evaluating.getName());
                model.addAttribute("evaluatingId", evaluating==null?"":evaluating.getId());
            }
            model.addAttribute("singlesel", singlesel);
        } else {
            model.addAttribute("singlesel", new Singlesel());
        }
        model.addAttribute("isFreeMap", getConfigMap("EVALUATINGTYPE_STATUS_ENUM"));
        return "singlesel/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(Singlesel singlesel, HttpServletRequest request) {
        increment("web.singlesel.formPost");
        if(singlesel!=null){
            if(singlesel.getSingtype()!=1){
                singlesel.setOptions01("");
                singlesel.setOptions02("");
                singlesel.setOptions03("");
                singlesel.setOptions04("");
                singlesel.setOptions05("");
                singlesel.setOptions06("");
            }
        }
        singleselService.save(singlesel);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.singlesel.delete");
        singleselService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/select")
    String select(Model model, HttpServletRequest request) {
        increment("web.vrRoom.select");
        return "singlesel/select";
    }

}


