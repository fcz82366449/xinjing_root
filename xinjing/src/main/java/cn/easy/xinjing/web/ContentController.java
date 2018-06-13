package cn.easy.xinjing.web;

import cn.easy.base.bean.PageBean;
import cn.easy.base.bean.common.AjaxResultBean;
import cn.easy.base.core.mvc.FormModel;
import cn.easy.base.domain.User;
import cn.easy.base.utils.ExtractUtil;
import cn.easy.base.utils.Global;
import cn.easy.base.web.BaseController;
import cn.easy.xinjing.bean.ContentComboBean;
import cn.easy.xinjing.domain.*;
import cn.easy.xinjing.service.*;
import cn.easy.xinjing.utils.Constants;
import org.apache.commons.collections.map.HashedMap;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/content")
public class ContentController extends BaseController {
    @Autowired
    private ContentService	contentService;
    @Autowired
    private ContentVideoService contentVideoService;
    @Autowired
    private ContentAudioService contentAudioService;
    @Autowired
    private ContentPicService contentPicService;
    @Autowired
    private ContentArticleService contentArticleService;
    @Autowired
    private ContentGameService contentGameService;
    @Autowired
    private ContentOutsideGameService contentOutsideGameService;
    @Autowired
    private OssMtsService ossMtsService;
    @Autowired
    private TherapyService therapyService;
    @Autowired
    private DiseaseService diseaseService;
    @Autowired
    private TherapyContentService therapyContentService;
    @Autowired
    private DiseaseContentService diseaseContentService;
    @Autowired
    private ContentComboService contentComboService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.content.index");
        return "content/index";
    }

    @RequestMapping("/select")
    String select(@RequestParam(value = "selectIds", required = false) String selectIds,
                  Model model, HttpServletRequest request) {
        increment("web.content.select");
        Map<String, Object> searchParams = new HashedMap(){{
            put("IN_id", selectIds);
        }};
        List<Content> reContentList = contentService.search(searchParams);
        BigDecimal totalPrice = BigDecimal.ZERO;
        if(!reContentList.isEmpty()){
            for(Content content : reContentList){
                totalPrice = totalPrice.add(content.getPrice());
            }
        }
        model.addAttribute("selectIds", selectIds);
        model.addAttribute("totalPrice", totalPrice);
        return "content/select";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<Content> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.content.list");

        Page<Content> page = contentService.search(searchParams(request), pageBean);
        setConfigFieldValues(page, Constants.CONTENT_TYPE_KEY,"type");
        setConfigFieldValues(page, Constants.CONTENT_STATUS_KEY,"status");
        setConfigFieldValues(page, Global.IF_OR_NOT_ENUM_KEY,"isFree");
        setFieldValues(page, User.class,"creator", new String[]{"realname"});
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.content.formGet");
        Content content = new Content();
        ContentVideo contentVideo = new ContentVideo();
        ContentAudio contentAudio = new ContentAudio();
        ContentPic contentPic = new ContentPic();
        ContentArticle contentArticle = new ContentArticle();
        ContentGame contentGame = new ContentGame();
        ContentOutsideGame contentOutsideGame = new ContentOutsideGame();
        ContentComboBean contentComboBean = new ContentComboBean();
        String vodUrl = "";
        String contentUrl = "";
        String therapyIds = "";
        String therapyNames = "";
        String diseaseIds = "";
        String diseaseNames = "";
        List<String> picList = new ArrayList<>();
        if (isValidId(id)) {
            content = contentService.getOne(id);
            if (Constants.CONTENT_TYPE_VIDEO == content.getType()) {
                contentVideo = contentVideoService.findByContentId(id);
                contentUrl = contentVideo.getContent();
                vodUrl = ossMtsService.getVodAccessUrl(contentUrl);
            } else if (Constants.CONTENT_TYPE_AUDIO == content.getType()) {
                contentAudio = contentAudioService.findByContentId(id);
                contentUrl = contentAudio.getContent();
                vodUrl = ossMtsService.getVodAccessUrl(contentUrl);
            } else if (Constants.CONTENT_TYPE_PIC == content.getType()) {
                contentPic = contentPicService.findByContentId(id);
                contentUrl = contentPic.getContent();
                String[] pics = contentPic.getContent().split(",");
                for(String pic : pics){
                   picList.add(ossMtsService.getImgAccessUrl(pic));
                }
            } else if (Constants.CONTENT_TYPE_GAME == content.getType()) {
                contentGame = contentGameService.findByContentId(id);
                contentUrl = contentGame.getContent();
            } else if (Constants.CONTENT_TYPE_OUTSIDE_GAME == content.getType()) {
                //外部游戏
                contentOutsideGame = contentOutsideGameService.findByContentId(id);
            } else if(Constants.CONTENT_TYPE_COMBO == content.getType()){
                //套餐
                contentComboBean = contentComboService.getContentComboBean(id);
            } else {
                //文章
                contentArticle = contentArticleService.findByContentId(id);
            }
            //获得关联的疗法,病种
            List<TherapyContent> therapyContents = therapyContentService.findByContentId(content.getId());
            therapyIds = StringUtils.join(ExtractUtil.extractToList(therapyContents,"therapyId"),",");
            therapyNames = therapyService.findNamesByIds(therapyIds);
            List<DiseaseContent> diseaseContents = diseaseContentService.findByContentId(content.getId());
            diseaseIds = StringUtils.join(ExtractUtil.extractToList(diseaseContents,"diseaseId"),",");
            if(diseaseIds!=null&&diseaseIds.length()>=1){
                diseaseNames = diseaseService.findNamesByIds(diseaseIds);
            }


        }
        else {
            content.setStatus(Constants.CONTENT_STATUS_INIT);
            content.setType(Constants.CONTENT_TYPE_VIDEO);
            content.setDuration(0);
        }
        model.addAttribute("content", content);
        model.addAttribute("contentVideo", contentVideo);
        model.addAttribute("contentAudio", contentAudio);
        model.addAttribute("contentPic", contentPic);
        model.addAttribute("contentArticle", contentArticle);
        model.addAttribute("contentGame", contentGame);
        model.addAttribute("contentOutsideGame", contentOutsideGame);
        model.addAttribute("contentComboBean", contentComboBean);
        model.addAttribute("contentTypeMap", getConfigMap(Constants.CONTENT_TYPE_KEY));
        model.addAttribute("isFreeMap", getConfigMap(Global.IF_OR_NOT_ENUM_KEY));
        model.addAttribute("vodUrl", vodUrl);
        model.addAttribute("contentUrl", contentUrl);
        model.addAttribute("picList", picList);
        model.addAttribute("coverPicUrl", content.getCoverPic());
        //疗法,病种
        model.addAttribute("therapyIds", therapyIds);
        model.addAttribute("therapyNames", therapyNames);
        model.addAttribute("diseaseIds", diseaseIds);
        model.addAttribute("diseaseNames", diseaseNames);
        return "content/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(Content content, HttpServletRequest request,
        @FormModel(value = "contentVideo") ContentVideo contentVideo,
        @FormModel(value = "contentAudio") ContentAudio contentAudio,
        @FormModel(value = "contentPic") ContentPic contentPic,
        @FormModel(value = "contentArticle") ContentArticle contentArticle,
        @FormModel(value = "contentGame") ContentGame contentGame,
                            @FormModel(value = "contentOutsideGame") ContentOutsideGame contentOutsideGame,
        @FormModel(value = "contentComboBean") ContentComboBean contentComboBean,
        @RequestParam(value = "contentUrl") String contentUrl,
        @RequestParam(value = "therapyIds") String therapyIds,
        @RequestParam(value = "diseaseIds") String diseaseIds
                            ) {
        increment("web.content.formPost");
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
            if(StringUtils.isNotEmpty(content.getId())){
                String picPath = contentService.getOne(content.getId()).getCoverPic();
                picPath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "/static/img/" + picPath.substring(picPath.lastIndexOf("/")+1);
                File pic = new File(picPath);
                if(pic.exists()){
                    pic.delete();
                }
            }
            content.setCoverPic(coverPicUrl);
        }
        content = contentService.saveContentAndTherapyAndDisease(content, therapyIds, diseaseIds);
        if (Constants.CONTENT_TYPE_VIDEO == content.getType()) {
            contentVideo.setContentId(content.getId());

            ContentVideo contentVideo1 = contentVideoService.findByContentId(content.getId());

            String contenturlto = contentVideo1==null?"":(contentVideo1.getContent()==null?"":contentVideo1.getContent());

            if((!((contentUrl==null?"":contentUrl).equals(contenturlto)))){
                content.setVideoupdateAt(content.getUpdatedAt());
                contentService.save(content);
            }


            if(StringUtils.isNotEmpty(contentUrl)){
                contentVideo.setContent(contentUrl);
            }
            contentVideoService.save(contentVideo);
        } else if (Constants.CONTENT_TYPE_AUDIO == content.getType()) {
            contentAudio.setContentId(content.getId());
            if(StringUtils.isNotEmpty(contentUrl)){
                contentAudio.setContent(contentUrl);
            }
            contentAudioService.save(contentAudio);
        } else if (Constants.CONTENT_TYPE_PIC == content.getType()) {
            contentPic.setContentId(content.getId());
            if(StringUtils.isNotEmpty(contentUrl)){
                contentPic.setContent(contentUrl);
            }
            contentPicService.save(contentPic);
        } else if(Constants.CONTENT_TYPE_ARTICLE == content.getType()){
            contentArticle.setContentId(content.getId());
            contentArticleService.save(contentArticle);
        } else if(Constants.CONTENT_TYPE_GAME == content.getType()){

            ContentGame contentGame1 = contentGameService.findByContentId(content.getId());

            String contenturlto = contentGame1==null?"":(contentGame1.getContent()==null?"":contentGame1.getContent());
            if((!((contentUrl==null?"":contentUrl).equals(contenturlto)))){
                content.setVideoupdateAt(content.getUpdatedAt());
                contentService.save(content);
            }
            contentGame.setContentId(content.getId());
            if(StringUtils.isNotEmpty(contentUrl)){
                contentGame.setContent(contentUrl);
            }
            contentGameService.save(contentGame);
        }
        //外部游戏
        else if(Constants.CONTENT_TYPE_OUTSIDE_GAME == content.getType()){
            contentOutsideGame.setContentId(content.getId());
            contentOutsideGameService.save(contentOutsideGame);
        }
        //套餐内容
        else if (Constants.CONTENT_TYPE_COMBO == content.getType()){
            contentComboBean.setContentId(content.getId());
            contentComboBean.setDiscountPrice(content.getPrice());
            contentComboService.save(contentComboBean);
        }
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.content.delete");
        String picPath = contentService.getOne(id).getCoverPic();
        picPath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "/static/img/" + picPath.substring(picPath.lastIndexOf("/")+1);
        File pic = new File(picPath);
        if(pic.exists()){
            pic.delete();
        }
        contentService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/publish")
    @ResponseBody
    AjaxResultBean publish(@RequestParam(value = "id") String id) {
        increment("web.content.publish");
        Content content = contentService.getOne(id);
        if(content.getStatus() != null && Constants.CONTENT_STATUS_PUBLISH == content.getStatus()){
            return toError("该内容已经发布");
        }
        contentService.publish(content);
        return toSuccess("发布成功");
    }

    @RequestMapping("/unPublish")
    @ResponseBody
    AjaxResultBean unPublish(@RequestParam(value = "id") String id) {
        increment("web.content.unPublish");
        Content content = contentService.getOne(id);
        if(Constants.CONTENT_STATUS_PUBLISH != content.getStatus()){
            return toError("该内容尚未发布");
        }
        contentService.unPublish(content);
        return toSuccess("下架成功");
    }

    /**
     * 获取上传资源的参数
     * @param type    IMG:图片，VOD:视音频
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/postObjectPolicy")
    @ResponseBody
    public Map<String, String> postObjectPolicy(@RequestParam("type") String type, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        return ossMtsService.getPostObjectPolicy(type);
    }
}


