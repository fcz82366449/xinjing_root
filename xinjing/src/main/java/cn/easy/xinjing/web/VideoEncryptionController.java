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
import cn.easy.xinjing.domain.VideoEncryption;
import cn.easy.xinjing.service.VideoEncryptionService;

@Controller
@RequestMapping("/videoEncryption")
public class VideoEncryptionController extends BaseController {
    @Autowired
    private VideoEncryptionService	videoEncryptionService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        return "videoEncryption/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<VideoEncryption> list(PageBean pageBean, HttpServletRequest request) {
        Page<VideoEncryption> page = videoEncryptionService.search(searchParams(request), pageBean);
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        if (isValidId(id)) {
            VideoEncryption videoEncryption = videoEncryptionService.getOne(id);
            model.addAttribute("videoEncryption", videoEncryption);
        } else {
            model.addAttribute("videoEncryption", new VideoEncryption());
        }
        return "videoEncryption/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(VideoEncryption videoEncryption, HttpServletRequest request) {
        videoEncryptionService.save(videoEncryption);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        videoEncryptionService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/view")
    String view(@RequestParam(value = "id") String id, Model model) {
        VideoEncryption videoEncryption = videoEncryptionService.getOne(id);
        model.addAttribute("videoEncryption", videoEncryption);
        return "videoEncryption/view";
    }

}


