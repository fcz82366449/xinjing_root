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
import cn.easy.xinjing.domain.GameitemRecord02;
import cn.easy.xinjing.service.GameitemRecord02Service;

@Controller
@RequestMapping("/gameitemRecord02")
public class GameitemRecord02Controller extends BaseController {
    @Autowired
    private GameitemRecord02Service	gameitemRecord02Service;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        return "gameitemRecord02/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<GameitemRecord02> list(PageBean pageBean, HttpServletRequest request) {
        Page<GameitemRecord02> page = gameitemRecord02Service.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        if (isValidId(id)) {
            GameitemRecord02 gameitemRecord02 = gameitemRecord02Service.getOne(id);
            model.addAttribute("gameitemRecord02", gameitemRecord02);
        } else {
            model.addAttribute("gameitemRecord02", new GameitemRecord02());
        }
        return "gameitemRecord02/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(GameitemRecord02 gameitemRecord02, HttpServletRequest request) {
        gameitemRecord02Service.save(gameitemRecord02);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        gameitemRecord02Service.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/view")
    String view(@RequestParam(value = "id") String id, Model model) {
        GameitemRecord02 gameitemRecord02 = gameitemRecord02Service.getOne(id);
        model.addAttribute("gameitemRecord02", gameitemRecord02);
        return "gameitemRecord02/view";
    }

}


