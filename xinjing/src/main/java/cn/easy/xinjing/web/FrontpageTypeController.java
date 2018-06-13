package cn.easy.xinjing.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.easy.base.web.BaseController;
import cn.easy.xinjing.domain.Disease;
import cn.easy.xinjing.utils.Constants;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
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
import cn.easy.xinjing.domain.FrontpageType;
import cn.easy.xinjing.service.FrontpageTypeService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontpageType")
public class FrontpageTypeController extends BaseController {
    @Autowired
    private FrontpageTypeService	frontpageTypeService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.frontpageType.index");
        return "frontpageType/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Object list(PageBean pageBean, HttpServletRequest request) {
        increment("web.frontpageType.list");
        Map<String, Object> searchParams = searchParams(request);
        searchParams.put("EQ_hidden",Constants.NO);
        List<FrontpageType> list = frontpageTypeService.search(searchParams);
        List<Map<String, Object>> ListMap =  setConfigFieldValues(list, "FRONTPAGETYPE_STATUS_ENUM", "status");
       // Page<FrontpageType> page = frontpageTypeService.search(searchParams(request), pageBean);
        return toGridMap(ListMap);
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id,@RequestParam(value = "type", required = false) String type,  Model model, HttpServletRequest request) {
        increment("web.frontpageType.formGet");
        FrontpageType frontpageType = new FrontpageType();
        Map<String, String> reTherapyMap = new HashedMap();
        if (isValidId(id)) {
            if ("addSon".equals(type)) {
                frontpageType.setPid(id);
            } else {
                frontpageType = frontpageTypeService.getOne(id);
            }
        } else {
            frontpageType.setPid("0");
        }
        model.addAttribute("frontpageType", frontpageType);
        return "frontpageType/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(FrontpageType frontpageType, HttpServletRequest request) {
        increment("web.frontpageType.formPost");
        frontpageTypeService.save(frontpageType);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.frontpageType.delete");
        frontpageTypeService.delete(id);
        return toSuccess("删除成功");
    }

    /**
     * TO头条分类移动
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/move", method = RequestMethod.GET)
    String moveForm(@RequestParam(value = "moveId") String moveId, Model model, HttpServletRequest request) {
        increment("web.frontpageType.move");
        model.addAttribute("moveId", moveId);
        return "frontpageType/move";
    }

    @RequestMapping("/treeList")
    @ResponseBody
    List<FrontpageType> treeList(@RequestParam(value = "type", required = false) String type, HttpServletRequest request) {
        increment("web.frontpageType.list");
        Map<String, Object> searchParams = searchParams(request);
        searchParams.put("EQ_hidden", Constants.NO);
        List<FrontpageType> list = frontpageTypeService.search(searchParams);
        for(FrontpageType frontpageType : list) {
            if(StringUtils.isBlank(frontpageType.getPid())) {
                frontpageType.setPid("0");
            }
        }
        if ("moveTree".equals(type)) {
            FrontpageType disease = new FrontpageType();
            disease.setId("0");
            disease.setName("头条分类树");
            list.add(disease);
        }
        return list;
    }
    /**
     * 保存头条分类移动
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/move", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean moveForm(@RequestParam(value = "moveId") String moveId, @RequestParam(value = "desId") String desId,
                            Model model, HttpServletRequest request) {
        increment("web.frontpageType.move");
        frontpageTypeService.moveDisease(moveId, desId);
        return toSuccess("头条分类移动成功");
    }
}


