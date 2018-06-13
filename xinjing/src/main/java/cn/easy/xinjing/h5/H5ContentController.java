package cn.easy.xinjing.h5;

import cn.easy.xinjing.domain.Content;
import cn.easy.xinjing.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenzhongyi on 2017/5/2.
 */
@Controller
@RequestMapping("/h5/content")
public class H5ContentController extends H5BaseController {

    @Autowired
    private ContentService contentService;

    @RequestMapping("/info")
    String info(String id, Model model, HttpServletRequest request) {
        Content content = contentService.getOne(id);
        model.addAttribute("content", content);
        return "h5/content/info";
    }


}
