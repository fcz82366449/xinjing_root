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
import cn.easy.xinjing.domain.GameitemRecord01;
import cn.easy.xinjing.service.GameitemRecord01Service;

@Controller
@RequestMapping("/gameitemRecord01")
public class GameitemRecord01Controller extends BaseController {
    @Autowired
    private GameitemRecord01Service	gameitemRecord01Service;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        return "gameitemRecord01/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<GameitemRecord01> list(PageBean pageBean, HttpServletRequest request) {
        Page<GameitemRecord01> page = gameitemRecord01Service.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        if (isValidId(id)) {
            GameitemRecord01 gameitemRecord01 = gameitemRecord01Service.getOne(id);
            model.addAttribute("gameitemRecord01", gameitemRecord01);
        } else {
            model.addAttribute("gameitemRecord01", new GameitemRecord01());
        }
        return "gameitemRecord01/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(GameitemRecord01 gameitemRecord01, HttpServletRequest request) {
        gameitemRecord01Service.save(gameitemRecord01);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        gameitemRecord01Service.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/view")
    String view(@RequestParam(value = "id") String id, Model model) {
        GameitemRecord01 gameitemRecord01 = gameitemRecord01Service.getOne(id);
        model.addAttribute("gameitemRecord01", gameitemRecord01);
        return "gameitemRecord01/view";
    }

}


