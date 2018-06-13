package cn.easy.xinjing.web;

import cn.easy.base.bean.PageBean;
import cn.easy.base.bean.common.AjaxResultBean;
import cn.easy.base.web.BaseController;
import cn.easy.xinjing.domain.Advertisement;
import cn.easy.xinjing.service.AdvertisementService;
import cn.easy.xinjing.service.OssMtsService;
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
import java.util.Map;

@Controller
@RequestMapping("/advertisement")
public class AdvertisementController extends BaseController {
    @Autowired
    private AdvertisementService	advertisementService;
    @Autowired
    private OssMtsService ossMtsService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.advertisement.index");
        return "advertisement/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<Advertisement> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.advertisement.list");

        Page<Advertisement> page = advertisementService.search(searchParams(request), pageBean);
        setConfigFieldValues(page, Constants.ADVERTISEMENT_TYPE_KEY, "type");
        setConfigFieldValues(page, Constants.ADVERTISEMENT_STATUS_KEY, "status");
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.advertisement.formGet");
        Advertisement advertisement = new Advertisement();
        if (isValidId(id)) {
            advertisement = advertisementService.getOne(id);
        }else{
            advertisement.setStatus(Constants.ADVERTISEMENT_STATUS_INIT);
        }
        model.addAttribute("advertisement", advertisement);
        return "advertisement/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(Advertisement advertisement, HttpServletRequest request) {
        increment("web.advertisement.formPost");

        //图片保存
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String,MultipartFile> coverPicFileMap = multipartRequest.getFileMap();
        MultipartFile file = coverPicFileMap.get("picFile");
        if(file != null){
            String picUrl = ossMtsService.uploadStatic(file);
            if(StringUtils.isNotEmpty(picUrl)){
                advertisement.setPic(picUrl);
            }
        }
        advertisementService.save(advertisement);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.advertisement.delete");
        advertisementService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/publish")
    @ResponseBody
    AjaxResultBean publish(@RequestParam(value = "id") String id) {
        increment("web.content.publish");
        Advertisement advertisement = advertisementService.getOne(id);
        if(Constants.CONTENT_STATUS_PUBLISH == advertisement.getStatus()){
            return toError("该广告已经发布");
        }
        advertisementService.publish(advertisement);
        return toSuccess("发布成功");
    }
}


