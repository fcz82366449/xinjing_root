package cn.easy.xinjing.web;

import cn.easy.base.bean.common.AjaxResultBean;
import cn.easy.base.web.BaseController;
import cn.easy.xinjing.domain.Therapy;
import cn.easy.xinjing.domain.TherapyContent;
import cn.easy.xinjing.service.TherapyContentService;
import cn.easy.xinjing.service.TherapyService;
import cn.easy.xinjing.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/therapy")
public class TherapyController extends BaseController {
    @Autowired
    private TherapyService	therapyService;
    @Autowired
    private TherapyContentService therapyContentService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.therapy.index");
        return "therapy/index";
    }

    @RequestMapping("/select")
    String select(Model model, String therapyIds, HttpServletRequest request) {
        increment("web.therapy.select");
        model.addAttribute("therapyIds", therapyIds);
        return "therapy/select";
    }

    @RequestMapping("/list")
    @ResponseBody
    Object list(HttpServletRequest request) {
        increment("web.therapy.list");
        Map<String, Object> searchParams = searchParams(request);
        searchParams.put("EQ_hidden",Constants.NO);
        List<Therapy> list = therapyService.search(searchParams);
        List<Map<String, Object>> ListMap =  setConfigFieldValues(list, Constants.THERAPY_STATUS_ENUM_KEY, "status");
        return toGridMap(ListMap);
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, @RequestParam(value = "type", required = false) String type, Model model, HttpServletRequest request) {
        increment("web.therapy.formGet");
        if (isValidId(id)) {
            if ("addSon".equals(type)) {
                Therapy therapy = new Therapy();
                therapy.setPid(id);
                model.addAttribute("therapy", therapy);
            } else {
                model.addAttribute("therapy", therapyService.getOne(id));
            }
        } else {
            Therapy therapy = new Therapy();
            therapy.setPid("0");
            model.addAttribute("therapy", new Therapy());
        }
        return "therapy/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(Therapy therapy, HttpServletRequest request) {
        increment("web.therapy.formPost");
        therapyService.save(therapy);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.therapy.delete");
        List<TherapyContent> therapyContents = therapyContentService.findByTherapyId(id);
        if(!therapyContents.isEmpty()){
            return toError("该疗法已经关联内容，不能删除");
        }
        therapyService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/publish")
    @ResponseBody
    AjaxResultBean publish(@RequestParam(value = "id") String id) {
        increment("web.therapy.publish");
        Therapy therapy = therapyService.getOne(id);
        if(therapy.getStatus() != null && Constants.THERAPY_STATUS_PUBLISH == therapy.getStatus()){
            return toError("该疗法已经发布");
        }
        therapyService.publish(therapy);
        return toSuccess("发布成功");
    }

    @RequestMapping("/unPublish")
    @ResponseBody
    AjaxResultBean unPublish(@RequestParam(value = "id") String id) {
        increment("web.therapy.unPublish");
        Therapy therapy = therapyService.getOne(id);
        if(Constants.THERAPY_STATUS_PUBLISH != therapy.getStatus()){
            return toError("该疗法未发布");
        }
        therapyService.unPublish(therapy);
        return toSuccess("下架成功");
    }

    @RequestMapping("/treeList")
    @ResponseBody
    List<Therapy> treeList(@RequestParam(value = "type", required = false) String type, HttpServletRequest request) {
        increment("web.therapy.list");
        Map<String, Object> searchParams = searchParams(request);
        searchParams.put("EQ_hidden",Constants.NO);
        List<Therapy> list = therapyService.search(searchParams);
        for(Therapy therapy : list) {
            if(StringUtils.isBlank(therapy.getPid())) {
                therapy.setPid("0");
            }
        }
        if ("moveTree".equals(type)) {
            Therapy therapy = new Therapy();
            therapy.setId("0");
            therapy.setName("疗法树");
            list.add(therapy);
        }
        return list;
    }

    /**
     * TO疗法移动
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/move", method = RequestMethod.GET)
    String moveForm(@RequestParam(value = "moveId") String moveId, Model model, HttpServletRequest request) {
        increment("web.therapy.move");
        model.addAttribute("moveId", moveId);
        return "therapy/move";
    }

    /**
     * 保存疗法移动
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/move", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean moveForm(@RequestParam(value = "moveId") String moveId, @RequestParam(value = "desId") String desId,
                            Model model, HttpServletRequest request) {
        increment("web.therapy.move");
        therapyService.moveTherapy(moveId, desId);
        return toSuccess("疗法移动成功");
    }
}


