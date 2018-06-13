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
import cn.easy.xinjing.domain.ContentCollect;
import cn.easy.xinjing.service.ContentCollectService;

@Controller
@RequestMapping("/contentCollect")
public class ContentCollectController extends BaseController {
    @Autowired
    private ContentCollectService	contentCollectService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.contentCollect.index");
        return "contentCollect/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<ContentCollect> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.contentCollect.list");

        Page<ContentCollect> page = contentCollectService.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.contentCollect.formGet");
        if (isValidId(id)) {
            ContentCollect contentCollect = contentCollectService.getOne(id);
            model.addAttribute("contentCollect", contentCollect);
        } else {
            model.addAttribute("contentCollect", new ContentCollect());
        }
        return "contentCollect/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(ContentCollect contentCollect, HttpServletRequest request) {
        increment("web.contentCollect.formPost");
        contentCollectService.save(contentCollect);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.contentCollect.delete");
        contentCollectService.delete(id);
        return toSuccess("删除成功");
    }

}


