package cn.easy.xinjing.web;

import cn.easy.base.bean.common.AjaxResultBean;
import cn.easy.base.web.BaseController;
import cn.easy.xinjing.domain.Disease;
import cn.easy.xinjing.domain.DiseaseContent;
import cn.easy.xinjing.domain.SectionOfficeDisease;
import cn.easy.xinjing.service.DiseaseContentService;
import cn.easy.xinjing.service.DiseaseService;
import cn.easy.xinjing.service.SectionOfficeDiseaseService;
import cn.easy.xinjing.utils.Constants;
import org.apache.commons.collections.map.HashedMap;
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
@RequestMapping("/disease")
public class DiseaseController extends BaseController {
    @Autowired
    private DiseaseService	diseaseService;
    @Autowired
    private DiseaseContentService diseaseContentService;
    @Autowired
    private SectionOfficeDiseaseService sectionOfficeDiseaseService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.disease.index");
        return "disease/index";
    }

    @RequestMapping("/select")
    String select(Model model, String diseaseIds, HttpServletRequest request) {
        increment("web.disease.select");
        model.addAttribute("diseaseIds", diseaseIds);
        return "disease/select";
    }

    @RequestMapping("/list")
    @ResponseBody
    Object list(HttpServletRequest request) {
        increment("web.disease.list");
        Map<String, Object> searchParams = searchParams(request);
        searchParams.put("EQ_hidden",Constants.NO);
        List<Disease> list = diseaseService.search(searchParams);
        List<Map<String, Object>> ListMap =  setConfigFieldValues(list, Constants.DISEASE_STATUS_ENUM_KEY, "status");
        return toGridMap(ListMap);
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, @RequestParam(value = "type", required = false) String type, Model model, HttpServletRequest request) {
        increment("web.disease.formGet");
        Disease disease = new Disease();
        Map<String, String> reTherapyMap = new HashedMap();
        if (isValidId(id)) {
            if ("addSon".equals(type)) {
                disease.setPid(id);
            } else {
                disease = diseaseService.getOne(id);
                reTherapyMap = diseaseService.getReTherapys(id);
            }
        } else {
            disease.setPid("0");
        }
        model.addAttribute("disease", disease);
        model.addAttribute("therapyIds", reTherapyMap.isEmpty() ? "" : reTherapyMap.get("therapyIds"));
        model.addAttribute("therapyNames", reTherapyMap.isEmpty() ? "" : reTherapyMap.get("therapyNames"));
        return "disease/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(Disease disease,
        @RequestParam(value = "therapyIds") String therapyIds, HttpServletRequest request) {
        increment("web.disease.formPost");
        diseaseService.saveDiseaseAndTherapys(disease, therapyIds);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.disease.delete");
        List<DiseaseContent> diseaseContents = diseaseContentService.findByDiseaseId(id);
        if(!diseaseContents.isEmpty()){
            return toError("该病种已经关联内容，不能删除");
        }
        List<SectionOfficeDisease> officeDiseases = sectionOfficeDiseaseService.findByDiseaseId(id);
        if(!officeDiseases.isEmpty()){
            return toError("该病种已经关联科室，不能删除");
        }
        diseaseService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/publish")
    @ResponseBody
    AjaxResultBean publish(@RequestParam(value = "id") String id) {
        increment("web.disease.publish");
        Disease disease = diseaseService.getOne(id);
        if(disease.getStatus() != null && Constants.THERAPY_STATUS_PUBLISH == disease.getStatus()){
            return toError("该病种已经发布");
        }
        diseaseService.publish(disease);
        return toSuccess("发布成功");
    }

    @RequestMapping("/unPublish")
    @ResponseBody
    AjaxResultBean unPublish(@RequestParam(value = "id") String id) {
        increment("web.disease.unPublish");
        Disease disease = diseaseService.getOne(id);
        if(Constants.THERAPY_STATUS_PUBLISH != disease.getStatus()){
            return toError("该病种未发布");
        }
        diseaseService.unPublish(disease);
        return toSuccess("发布成功");
    }

    @RequestMapping("/treeList")
    @ResponseBody
    List<Disease> treeList(@RequestParam(value = "type", required = false) String type, HttpServletRequest request) {
        increment("web.disease.list");
        Map<String, Object> searchParams = searchParams(request);
        searchParams.put("EQ_hidden",Constants.NO);
        List<Disease> list = diseaseService.search(searchParams);
        for(Disease disease : list) {
            if(StringUtils.isBlank(disease.getPid())) {
                disease.setPid("0");
            }
        }
        if ("moveTree".equals(type)) {
            Disease disease = new Disease();
            disease.setId("0");
            disease.setName("病种树");
            list.add(disease);
        }
        return list;
    }

    /**
     * TO病种移动
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/move", method = RequestMethod.GET)
    String moveForm(@RequestParam(value = "moveId") String moveId, Model model, HttpServletRequest request) {
        increment("web.disease.move");
        model.addAttribute("moveId", moveId);
        return "disease/move";
    }

    /**
     * 保存病种移动
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/move", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean moveForm(@RequestParam(value = "moveId") String moveId, @RequestParam(value = "desId") String desId,
                            Model model, HttpServletRequest request) {
        increment("web.disease.move");
        diseaseService.moveDisease(moveId, desId);
        return toSuccess("病种移动成功");
    }
}


