package cn.easy.xinjing.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.easy.base.domain.User;
import cn.easy.base.web.BaseController;
import cn.easy.xinjing.service.OssMtsService;
import cn.easy.xinjing.utils.Constants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.easy.base.bean.PageBean;
import cn.easy.base.bean.common.AjaxResultBean;
import cn.easy.xinjing.domain.Evaluating;
import cn.easy.xinjing.service.EvaluatingService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/evaluating")
public class EvaluatingController extends BaseController {
    @Autowired
    private EvaluatingService	evaluatingService;
    @Autowired
    private OssMtsService ossMtsService;
    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.evaluating.index");
        return "evaluating/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<Evaluating> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.evaluating.list");

        Page<Evaluating> page = evaluatingService.search(searchParams(request), pageBean);
        setConfigFieldValues(page, "EVALUATING_STATUS_ENUM", "status");
        setFieldValues(page, User.class,"creator", new String[]{"realname"});
        setFieldValues(page, User.class,"updator", new String[]{"realname"});
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.evaluating.formGet");
        Evaluating evaluating ;
        if (isValidId(id)) {
            evaluating = evaluatingService.getOne(id);
        } else {
            evaluating = new Evaluating();
        }
        model.addAttribute("evaluating", evaluating);
        model.addAttribute("evaluating", evaluating);
        model.addAttribute("coverPicUrl", evaluating.getCoverPic());
        model.addAttribute("coverPic", evaluating.getCoverPic());

        return "evaluating/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(Evaluating evaluating, HttpServletRequest request) throws IOException {
        increment("web.evaluating.formPost");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获得文件
        Map<String,MultipartFile> coverPicFileMap = multipartRequest.getFileMap();
        //封面保存
        MultipartFile file = coverPicFileMap.get("coverPicFile");
        String coverPicUrl = "";
        if(file!=null) {
            String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            String fileName = "prod" + System.currentTimeMillis() + "." + extName;
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "/static/img";
            File dir = new File(path);
            if(!dir.exists()){
                dir.mkdirs();
            }
            try {
                file.transferTo(new File(path+"/"+fileName));
            } catch (IOException e) {
                throw new RuntimeException("fail@uploadLocalFile", e);
            }
            coverPicUrl = "http://localhost:8080/img/" + fileName;
        }
//        String coverPicUrl = ossMtsService.uploadStatic(coverPicFileMap.get("coverPicFile"));
        if(StringUtils.isNotEmpty(coverPicUrl)){
            if(StringUtils.isNotEmpty(evaluating.getId())){
                String picPath = evaluatingService.getOne(evaluating.getId()).getCoverPic();
                picPath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "/static/img/" + picPath.substring(picPath.lastIndexOf("/")+1);
                File pic = new File(picPath);
                if(pic.exists()){
                    pic.delete();
                }
            }
            evaluating.setCoverPic(coverPicUrl);
        }
        evaluatingService.save(evaluating);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.evaluating.delete");
        evaluatingService.delete(id);
        String picPath = evaluatingService.getOne(id).getCoverPic();
        picPath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "/static/img/" + picPath.substring(picPath.lastIndexOf("/")+1);
        File pic = new File(picPath);
        if(pic.exists()){
            pic.delete();
        }
        return toSuccess("删除成功");
    }

    @RequestMapping("/select")
    String select(Model model, HttpServletRequest request) {
        increment("web.vrRoom.select");
        return "evaluating/select";
    }

}


