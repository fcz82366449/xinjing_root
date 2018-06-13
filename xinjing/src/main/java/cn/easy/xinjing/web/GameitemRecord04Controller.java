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
import cn.easy.xinjing.domain.GameitemRecord04;
import cn.easy.xinjing.service.GameitemRecord04Service;

@Controller
@RequestMapping("/gameitemRecord04")
public class GameitemRecord04Controller extends BaseController {
    @Autowired
    private GameitemRecord04Service	gameitemRecord04Service;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        return "gameitemRecord04/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<GameitemRecord04> list(PageBean pageBean, HttpServletRequest request) {
        Page<GameitemRecord04> page = gameitemRecord04Service.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        if (isValidId(id)) {
            GameitemRecord04 gameitemRecord04 = gameitemRecord04Service.getOne(id);
            model.addAttribute("gameitemRecord04", gameitemRecord04);
        } else {
            model.addAttribute("gameitemRecord04", new GameitemRecord04());
        }
        return "gameitemRecord04/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(GameitemRecord04 gameitemRecord04, HttpServletRequest request) {
        gameitemRecord04Service.save(gameitemRecord04);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        gameitemRecord04Service.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/view")
    String view(@RequestParam(value = "id") String id, Model model) {
        GameitemRecord04 gameitemRecord04 = gameitemRecord04Service.getOne(id);
        model.addAttribute("gameitemRecord04", gameitemRecord04);
        return "gameitemRecord04/view";
    }

}


