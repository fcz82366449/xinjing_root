package cn.easy.xinjing.web;

import cn.easy.base.bean.PageBean;
import cn.easy.base.bean.common.AjaxResultBean;
import cn.easy.base.web.BaseController;
import cn.easy.xinjing.domain.SectionOffice;
import cn.easy.xinjing.service.SectionOfficeService;
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
@RequestMapping("/sectionOffice")
public class SectionOfficeController extends BaseController {
    @Autowired
    private SectionOfficeService	sectionOfficeService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.sectionOffice.index");
        return "sectionOffice/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Object list(PageBean pageBean, HttpServletRequest request) {
        increment("web.sectionOffice.list");
        Map<String, Object> searchParams = searchParams(request);
        searchParams.put("EQ_hidden",Constants.NO);
        List<SectionOffice> list = sectionOfficeService.search(searchParams);
        List<Map<String, Object>> ListMap =  setConfigFieldValues(list, Constants.SECTION_OFFICE_STATUS_ENUM_KEY, "status");
        return toGridMap(ListMap);
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, @RequestParam(value = "type", required = false) String type, Model model, HttpServletRequest request) {
        increment("web.sectionOffice.formGet");
        SectionOffice sectionOffice = new SectionOffice();
        Map<String, String> reDiseaseMap = new HashedMap();
        if (isValidId(id)) {
            if ("addSon".equals(type)) {
                sectionOffice.setPid(id);
            } else {
                sectionOffice = sectionOfficeService.getOne(id);
                reDiseaseMap = sectionOfficeService.getReDiseases(id);
            }
        } else {
            sectionOffice.setPid("0");
        }
        model.addAttribute("sectionOffice", sectionOffice);
        model.addAttribute("diseaseIds", reDiseaseMap.isEmpty() ? "" : reDiseaseMap.get("diseaseIds"));
        model.addAttribute("diseaseNames", reDiseaseMap.isEmpty() ? "" : reDiseaseMap.get("diseaseNames"));
        return "sectionOffice/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(SectionOffice sectionOffice,
        @RequestParam(value = "diseaseIds") String diseaseIds, HttpServletRequest request) {
        increment("web.sectionOffice.formPost");
        sectionOfficeService.saveOfficeAndDiseases(sectionOffice, diseaseIds);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.sectionOffice.delete");
        sectionOfficeService.delete(id);
        return toSuccess("删除成功");
    }
    
    @RequestMapping("/publish")
    @ResponseBody
    AjaxResultBean publish(@RequestParam(value = "id") String id) {
        increment("web.sectionOffice.publish");
        SectionOffice sectionOffice = sectionOfficeService.getOne(id);
        if(sectionOffice.getStatus() != null && Constants.SECTION_OFFICE_STATUS_STATUS_PUBLISH == sectionOffice.getStatus()){
            return toError("该科室已经发布");
        }
        sectionOfficeService.publish(sectionOffice);
        return toSuccess("发布成功");
    }

    @RequestMapping("/unPublish")
    @ResponseBody
    AjaxResultBean unPublish(@RequestParam(value = "id") String id) {
        increment("web.sectionOffice.unPublish");
        SectionOffice sectionOffice = sectionOfficeService.getOne(id);
        if(Constants.SECTION_OFFICE_STATUS_STATUS_PUBLISH != sectionOffice.getStatus()){
            return toError("该科室未发布");
        }
        sectionOfficeService.unPublish(sectionOffice);
        return toSuccess("发布成功");
    }

    @RequestMapping("/treeList")
    @ResponseBody
    List<SectionOffice> treeList(@RequestParam(value = "type", required = false) String type, HttpServletRequest request) {
        increment("web.sectionOffice.list");
        Map<String, Object> searchParams = searchParams(request);
        searchParams.put("EQ_hidden",Constants.NO);
        List<SectionOffice> officeList = sectionOfficeService.search(searchParams);
        for(SectionOffice sectionOffice : officeList) {
            if(StringUtils.isBlank(sectionOffice.getPid())) {
                sectionOffice.setPid("0");
            }
        }
        if ("moveTree".equals(type)) {
            SectionOffice sectionOffice = new SectionOffice();
            sectionOffice.setId("0");
            sectionOffice.setName("科室树");
            officeList.add(sectionOffice);
        }
        return officeList;
    }

    /**
     * TO科室移动
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/move", method = RequestMethod.GET)
    String moveForm(@RequestParam(value = "moveId") String moveId, Model model, HttpServletRequest request) {
        increment("web.sectionOffice.move");
        model.addAttribute("moveId", moveId);
        return "sectionOffice/move";
    }

    /**
     * 保存科室 移动
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/move", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean moveForm(@RequestParam(value = "moveId") String moveId, @RequestParam(value = "desId") String desId,
                            Model model, HttpServletRequest request) {
        increment("web.sectionOffice.move");
        sectionOfficeService.moveDisease(moveId, desId);
        return toSuccess("科室移动成功");
    }
}


