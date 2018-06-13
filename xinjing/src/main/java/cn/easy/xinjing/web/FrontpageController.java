package cn.easy.xinjing.web;

import cn.easy.base.bean.PageBean;
import cn.easy.base.bean.common.AjaxResultBean;
import cn.easy.base.domain.User;
import cn.easy.base.service.UserService;
import cn.easy.base.utils.ExtractUtil;
import cn.easy.base.utils.Global;
import cn.easy.base.web.BaseController;
import cn.easy.xinjing.domain.*;
import cn.easy.xinjing.service.*;
import cn.easy.xinjing.utils.Constants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontpage")
public class FrontpageController extends BaseController {
    @Autowired
    private FrontpageService	frontpageService;
    @Autowired
    private FrontpageTypeService frontpageTypeService;
    @Autowired
    private FrontpageFtypeService	frontpageFtypeService;
    @Autowired
    private OssMtsService ossMtsService;
    @Autowired
    private DoctorArticleService doctorArticleService;
    @Autowired
    private UserDoctorService userDoctorService;
    @Autowired
    private UserService userService;
    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.frontpage.index");
        return "frontpage/index";
    }
    @RequestMapping("/select")
    String select(Model model, HttpServletRequest request) {
        increment("web.vrRoom.select");
        return "userDoctor/select";
    }

    @RequestMapping("/select2")
    String select2(Model model, HttpServletRequest request) {
        increment("web.vrRoom.select");
        return "frontpageType/select";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<Frontpage> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.frontpage.list");

        Page<Frontpage> page = frontpageService.search(searchParams(request), pageBean);
        setFieldValues(page, User.class,"creator", new String[]{"realname"});
        setFieldValues(page, User.class,"updator", new String[]{"realname"});
        setConfigFieldValues(page, Constants.USER_FRONTPAGE_APPROVE_STATUS_ENUM, "status");
        setFieldValues(page, UserDoctor.class, "author", new String[]{"userId"});
        setFieldValues(page, User.class, "author_userId", new String[]{"realname"});
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.frontpage.formGet");
        String frontpageTypeId="";
        String frontpageTypeName="";
        if (isValidId(id)) {
            Frontpage frontpage = frontpageService.getOne(id);
            if(frontpage!=null){
               FrontpageType frontpageType = frontpageTypeService.getOne(frontpage.getFrontpageTypeId());
                model.addAttribute("frontpageTypeName", frontpageType==null?"":frontpageType.getName());
            }

            UserDoctor userDoctor = userDoctorService.getOne(frontpage.getAuthor());
            if(userDoctor!=null){
                User user = userService.getOne(userDoctor == null ? "" : userDoctor.getUserId());
                model.addAttribute("authorName", user == null ? "" : user.getRealname());
            }
            model.addAttribute("frontpage", frontpage);
            model.addAttribute("coverPic", frontpage.getCoverPic());
            model.addAttribute("coverPicUrl", frontpage.getCoverPic());

            List<FrontpageFtype> frontpageFtypes = frontpageFtypeService.findByFrontpageId(frontpage.getId());
            if(frontpageFtypes.size()>=1){
                frontpageTypeId = StringUtils.join(ExtractUtil.extractToList(frontpageFtypes,"frontpageTypeId"),",");
                if(frontpageTypeId!=null&&frontpageTypeId.length()>1){
                    frontpageTypeName = frontpageTypeService.findNamesByIds(frontpageTypeId);
                }else{
                    frontpageTypeName="";
                }
            }else{
                frontpageTypeName="";
            }





        } else {
            model.addAttribute("frontpage", new Frontpage());
        }
        model.addAttribute("isFreeMap", getConfigMap(Global.IF_OR_NOT_ENUM_KEY));
        model.addAttribute("frontpageTypeId",frontpageTypeId);
        model.addAttribute("frontpageTypeName", frontpageTypeName);
        return "frontpage/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(Frontpage frontpage, HttpServletRequest request, @RequestParam(value = "frontpageTypeId") String frontpageTypeId) {
        increment("web.frontpage.formPost");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获得文件
        Map<String,MultipartFile> coverPicFileMap = multipartRequest.getFileMap();
        //封面保存
        String coverPicUrl = ossMtsService.uploadStatic(coverPicFileMap.get("coverPicFile"));
        if(StringUtils.isNotEmpty(coverPicUrl)){
            frontpage.setCoverPic(coverPicUrl);
        }
        frontpageService.saveFrontpageAndType(frontpage, frontpageTypeId);

        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.frontpage.delete");
        frontpageService.delete(id);
        return toSuccess("删除成功");
    }

}


