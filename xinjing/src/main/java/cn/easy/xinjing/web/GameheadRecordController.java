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
import cn.easy.xinjing.domain.GameheadRecord;
import cn.easy.xinjing.service.GameheadRecordService;

@Controller
@RequestMapping("/gameheadRecord")
public class GameheadRecordController extends BaseController {
    @Autowired
    private GameheadRecordService	gameheadRecordService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        return "gameheadRecord/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<GameheadRecord> list(PageBean pageBean, HttpServletRequest request) {
        Page<GameheadRecord> page = gameheadRecordService.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        if (isValidId(id)) {
            GameheadRecord gameheadRecord = gameheadRecordService.getOne(id);
            model.addAttribute("gameheadRecord", gameheadRecord);
        } else {
            model.addAttribute("gameheadRecord", new GameheadRecord());
        }
        return "gameheadRecord/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(GameheadRecord gameheadRecord, HttpServletRequest request) {
        gameheadRecordService.save(gameheadRecord);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        gameheadRecordService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/view")
    String view(@RequestParam(value = "id") String id, Model model) {
        GameheadRecord gameheadRecord = gameheadRecordService.getOne(id);
        model.addAttribute("gameheadRecord", gameheadRecord);
        return "gameheadRecord/view";
    }

}


