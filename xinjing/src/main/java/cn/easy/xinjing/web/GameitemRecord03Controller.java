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
import cn.easy.xinjing.domain.GameitemRecord03;
import cn.easy.xinjing.service.GameitemRecord03Service;

@Controller
@RequestMapping("/gameitemRecord03")
public class GameitemRecord03Controller extends BaseController {
    @Autowired
    private GameitemRecord03Service	gameitemRecord03Service;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        return "gameitemRecord03/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<GameitemRecord03> list(PageBean pageBean, HttpServletRequest request) {
        Page<GameitemRecord03> page = gameitemRecord03Service.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        if (isValidId(id)) {
            GameitemRecord03 gameitemRecord03 = gameitemRecord03Service.getOne(id);
            model.addAttribute("gameitemRecord03", gameitemRecord03);
        } else {
            model.addAttribute("gameitemRecord03", new GameitemRecord03());
        }
        return "gameitemRecord03/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(GameitemRecord03 gameitemRecord03, HttpServletRequest request) {
        gameitemRecord03Service.save(gameitemRecord03);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        gameitemRecord03Service.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/view")
    String view(@RequestParam(value = "id") String id, Model model) {
        GameitemRecord03 gameitemRecord03 = gameitemRecord03Service.getOne(id);
        model.addAttribute("gameitemRecord03", gameitemRecord03);
        return "gameitemRecord03/view";
    }

}


