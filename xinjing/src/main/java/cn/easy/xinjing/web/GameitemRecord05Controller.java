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
import cn.easy.xinjing.domain.GameitemRecord05;
import cn.easy.xinjing.service.GameitemRecord05Service;

@Controller
@RequestMapping("/gameitemRecord05")
public class GameitemRecord05Controller extends BaseController {
    @Autowired
    private GameitemRecord05Service	gameitemRecord05Service;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        return "gameitemRecord05/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<GameitemRecord05> list(PageBean pageBean, HttpServletRequest request) {
        Page<GameitemRecord05> page = gameitemRecord05Service.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        if (isValidId(id)) {
            GameitemRecord05 gameitemRecord05 = gameitemRecord05Service.getOne(id);
            model.addAttribute("gameitemRecord05", gameitemRecord05);
        } else {
            model.addAttribute("gameitemRecord05", new GameitemRecord05());
        }
        return "gameitemRecord05/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(GameitemRecord05 gameitemRecord05, HttpServletRequest request) {
        gameitemRecord05Service.save(gameitemRecord05);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        gameitemRecord05Service.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/view")
    String view(@RequestParam(value = "id") String id, Model model) {
        GameitemRecord05 gameitemRecord05 = gameitemRecord05Service.getOne(id);
        model.addAttribute("gameitemRecord05", gameitemRecord05);
        return "gameitemRecord05/view";
    }

}


