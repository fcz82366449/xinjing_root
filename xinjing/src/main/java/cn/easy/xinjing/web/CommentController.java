package cn.easy.xinjing.web;

import cn.easy.base.bean.PageBean;
import cn.easy.base.bean.common.AjaxResultBean;
import cn.easy.base.domain.User;
import cn.easy.base.web.BaseController;
import cn.easy.xinjing.domain.Comment;
import cn.easy.xinjing.service.CommentService;
import cn.easy.xinjing.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController {
    @Autowired
    private CommentService	commentService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.comment.index");
        return "comment/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<Comment> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.comment.list");
        Map<String,Object> map = searchParams(request);
        Page<Comment> page = commentService.search(map, pageBean);
        setConfigFieldValues(page, Constants.COMMENT_TYPE_KEY,"objectType");
        setConfigFieldValues(page, Constants.COMMENT_STATUS_KEY,"status");
        setFieldValues(page, User.class,"creator", new String[]{"realname"});
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.comment.formGet");
        if (isValidId(id)) {
            Comment comment = commentService.getOne(id);
            model.addAttribute("comment", comment);
        } else {
            model.addAttribute("comment", new Comment());
        }
        return "comment/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(Comment comment, HttpServletRequest request) {
        increment("web.comment.formPost");
        commentService.save(comment);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.comment.delete");
        commentService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/allCanSee")
    @ResponseBody
    AjaxResultBean allCanSee(@RequestParam(value = "id") String id) {
        increment("web.comment.allCanSee");
        commentService.allCanSee(id);
        return toSuccess("设置成功");
    }

    @RequestMapping("/noneCanSee")
    @ResponseBody
    AjaxResultBean noneCanSee(@RequestParam(value = "id") String id) {
        increment("web.comment.noneCanSee");
        commentService.noneCanSee(id);
        return toSuccess("设置成功");
    }
}


