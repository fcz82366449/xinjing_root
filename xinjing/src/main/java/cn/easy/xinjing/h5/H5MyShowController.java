package cn.easy.xinjing.h5;

import cn.easy.base.bean.common.AjaxResultBean;
import cn.easy.base.utils.ExtractUtil;
import cn.easy.xinjing.aop.ApiAuth;
import cn.easy.xinjing.bean.h5.ContentDetailsBean;
import cn.easy.xinjing.bean.h5.ContentReturnH5Bean;
import cn.easy.xinjing.domain.*;
import cn.easy.xinjing.service.*;
import cn.easy.xinjing.utils.Constants;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chenzhongyi on 2017/5/8.
 */
@Controller
@RequestMapping("/h5/myshow")
public class H5MyShowController extends H5BaseController{
    @Autowired
    private ContentService contentService;

    @Autowired
    private TherapyContentService therapyContentService;

    @Autowired
    private DiseaseContentService diseaseContentService;

    @Autowired
    private DiseaseService diseaseService;

    @Autowired
    private TherapyService therapyService;




    @RequestMapping("index")
    String index(Model model, HttpServletRequest request) {
        String  url = request.getScheme() +"://" + request.getServerName()+ ":" +request.getServerPort();
        model.addAttribute("url",url);
        return "h5/myshow/index";
    }

    @RequestMapping("indexPc")
    String indexPc(Model model, HttpServletRequest request) {
        String  url = request.getScheme() +"://" + request.getServerName()+ ":" +request.getServerPort();
        model.addAttribute("url",url);
        return "h5/myshow/indexPc";
    }

    @RequestMapping("/index02")
    String index02(Model model, HttpServletRequest request) {
        String  url = request.getScheme() +"://" + request.getServerName()+ ":" +request.getServerPort();
        model.addAttribute("url",url);
        return "h5/myshow/index02";
    }

    @RequestMapping("/contents")
    String contents(@RequestParam("contentid") String contentid,Model model, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        System.out.println(contentid);
        String  url = request.getScheme() +"://" + request.getServerName()+ ":" +request.getServerPort();
        Content content = contentService.getOne(contentid);
        List<Therapy> therapyList =  new ArrayList<Therapy>();
        List<Disease> diseaseList = new ArrayList<Disease>();
        //获取疗法
        List<TherapyContent> therapyContentList = therapyContentService.findByContentId(content.getId());
        if(therapyContentList.size()>=1){
            String therapyIds = org.apache.commons.lang.StringUtils.join(ExtractUtil.extractToList(therapyContentList,"therapyId"),",");
            Map<String,Object> searchParams = new HashedMap();
            searchParams.put("IN_id", therapyIds);
            therapyList = therapyService.search(searchParams);
            for (int i = 0; i < therapyList.size(); i++) {
                therapyList.get(i).setHelpCode(url+"/h5/help/therapy?id="+ therapyList.get(i).getId()+"");
            }
        }

        List<DiseaseContent> diseaseContentList = diseaseContentService.findByContentId(content.getId());
        if(diseaseContentList.size()>=1){
            String diseaseIds = org.apache.commons.lang.StringUtils.join(ExtractUtil.extractToList(diseaseContentList,"diseaseId"),",");
            Map<String,Object> searchParams2 = new HashedMap();
            searchParams2.put("IN_id", diseaseIds);
            diseaseList = diseaseService.search(searchParams2);
            for (int i = 0; i < diseaseList.size(); i++) {
                diseaseList.get(i).setHelpCode(url+"/h5/help/disease?id="+ diseaseList.get(i).getId()+"");
            }
        }


        ContentDetailsBean contentDetailsBean = new ContentDetailsBean();
        PropertyUtils.copyProperties(contentDetailsBean, content);
        contentDetailsBean.setTherapyList(therapyList);
        contentDetailsBean.setDiseaseList(diseaseList);
        model.addAttribute("content", contentDetailsBean);
        return "h5/myshow/contents";
    }

    @RequestMapping("/content")
    String content(@RequestParam("contentid") String contentid,Model model, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        System.out.println(contentid);
        String  url = request.getScheme() +"://" + request.getServerName()+ ":" +request.getServerPort();
        Content content = contentService.getOne(contentid);
        List<Therapy> therapyList =  new ArrayList<Therapy>();
        List<Disease> diseaseList = new ArrayList<Disease>();
        //获取疗法
        List<TherapyContent> therapyContentList = therapyContentService.findByContentId(content.getId());
        if(therapyContentList.size()>=1){
            String therapyIds = org.apache.commons.lang.StringUtils.join(ExtractUtil.extractToList(therapyContentList,"therapyId"),",");
            Map<String,Object> searchParams = new HashedMap();
            searchParams.put("IN_id", therapyIds);
            therapyList = therapyService.search(searchParams);
            for (int i = 0; i < therapyList.size(); i++) {
                therapyList.get(i).setHelpCode(url+"/h5/help/therapy?id="+ therapyList.get(i).getId()+"");
            }
        }

        List<DiseaseContent> diseaseContentList = diseaseContentService.findByContentId(content.getId());
        if(diseaseContentList.size()>=1){
            String diseaseIds = org.apache.commons.lang.StringUtils.join(ExtractUtil.extractToList(diseaseContentList,"diseaseId"),",");
            Map<String,Object> searchParams2 = new HashedMap();
            searchParams2.put("IN_id", diseaseIds);
            diseaseList = diseaseService.search(searchParams2);
            for (int i = 0; i < diseaseList.size(); i++) {
                diseaseList.get(i).setHelpCode(url+"/h5/help/disease?id="+ diseaseList.get(i).getId()+"");
            }
        }


        ContentDetailsBean contentDetailsBean = new ContentDetailsBean();
        PropertyUtils.copyProperties(contentDetailsBean, content);
        contentDetailsBean.setTherapyList(therapyList);
        contentDetailsBean.setDiseaseList(diseaseList);
        model.addAttribute("content", contentDetailsBean);
        return "h5/myshow/content";
    }


    @RequestMapping("/appContent")
    String appContent(@RequestParam("contentid") String contentid,Model model, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        System.out.println(contentid);
        String  url = request.getScheme() +"://" + request.getServerName()+ ":" +request.getServerPort();
        Content content = contentService.getOne(contentid);
        List<Therapy> therapyList =  new ArrayList<Therapy>();
        List<Disease> diseaseList = new ArrayList<Disease>();
        //获取疗法
        List<TherapyContent> therapyContentList = therapyContentService.findByContentId(content.getId());
        if(therapyContentList.size()>=1){
            String therapyIds = org.apache.commons.lang.StringUtils.join(ExtractUtil.extractToList(therapyContentList,"therapyId"),",");
            Map<String,Object> searchParams = new HashedMap();
            searchParams.put("IN_id", therapyIds);
            therapyList = therapyService.search(searchParams);
            for (int i = 0; i < therapyList.size(); i++) {
                therapyList.get(i).setHelpCode(url+"/h5/help/apptherapy?id="+ therapyList.get(i).getId()+"");
            }
        }

        List<DiseaseContent> diseaseContentList = diseaseContentService.findByContentId(content.getId());
        if(diseaseContentList.size()>=1){
            String diseaseIds = org.apache.commons.lang.StringUtils.join(ExtractUtil.extractToList(diseaseContentList,"diseaseId"),",");
            Map<String,Object> searchParams2 = new HashedMap();
            searchParams2.put("IN_id", diseaseIds);
            diseaseList = diseaseService.search(searchParams2);
            for (int i = 0; i < diseaseList.size(); i++) {
                diseaseList.get(i).setHelpCode(url+"/h5/help/appdisease?id="+ diseaseList.get(i).getId()+"");
            }
        }


        ContentDetailsBean contentDetailsBean = new ContentDetailsBean();
        PropertyUtils.copyProperties(contentDetailsBean, content);
        contentDetailsBean.setTherapyList(therapyList);
        contentDetailsBean.setDiseaseList(diseaseList);
        model.addAttribute("content", contentDetailsBean);
        return "h5/myshow/appContent";
    }

//    @RequestMapping(value = "/content", method = RequestMethod.POST)
//    @ResponseBody
//    String contentPost(ContentReturnH5Bean contentReturnH5Bean, HttpServletRequest request) {
//        System.out.println("");
//        request.getSe
//        return "redirect:content.html";
//    }


    @RequestMapping("/filterContent")//对应jsp页面中的deleteBatch()请求
   String filterContent(String checkbox1,String checkbox2,Model model, HttpServletRequest request){
       model.addAttribute("checkbox1", checkbox1);
       model.addAttribute("checkbox2", checkbox2);
        return "h5/myshow/index02";
    }

}
