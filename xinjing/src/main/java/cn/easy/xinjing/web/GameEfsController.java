package cn.easy.xinjing.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.easy.base.domain.User;
import cn.easy.base.web.BaseController;
import cn.easy.xinjing.domain.UserVrRoom;
import cn.easy.xinjing.domain.VrRoom;
import cn.easy.xinjing.service.UserVrRoomService;
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
import cn.easy.xinjing.domain.GameEfs;
import cn.easy.xinjing.service.GameEfsService;

@Controller
@RequestMapping("/gameEfs")
public class GameEfsController extends BaseController {
    @Autowired
    private GameEfsService	gameEfsService;

    @Autowired
    private UserVrRoomService userVrRoomService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        return "gameEfs/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<GameEfs> list(PageBean pageBean, HttpServletRequest request) {
        Page<GameEfs> page = gameEfsService.search(searchParams(request), pageBean);
        setFieldValues(page, UserVrRoom.class,"vruser", new String[]{"user"});
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        if (isValidId(id)) {
            GameEfs gameEfs = gameEfsService.getOne(id);
            UserVrRoom userVrRoom = userVrRoomService.getOne(gameEfs.getVruser());

            if(userVrRoom!=null){
                model.addAttribute("vruser", userVrRoom.getId());
                model.addAttribute("vrusername", userVrRoom.getUser().getUsername());
            }
            model.addAttribute("gameEfs", gameEfs);
        } else {
            model.addAttribute("gameEfs", new GameEfs());
        }
        return "gameEfs/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(GameEfs gameEfs, HttpServletRequest request) {
        gameEfsService.save(gameEfs);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        gameEfsService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/view")
    String view(@RequestParam(value = "id") String id, Model model) {
        GameEfs gameEfs = gameEfsService.getOne(id);
        model.addAttribute("gameEfs", gameEfs);
        return "gameEfs/view";
    }

}


