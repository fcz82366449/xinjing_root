package cn.easy.xinjing.api;

import cn.easy.base.bean.PageBean;
import cn.easy.base.bean.common.ApiResultBean;
import cn.easy.xinjing.bean.api.DiseaseH5Bean;
import cn.easy.xinjing.bean.api.LoginBean;
import cn.easy.xinjing.bean.h5.ContentReturnH5Bean;
import cn.easy.xinjing.domain.Content;
import cn.easy.xinjing.domain.Disease;
import cn.easy.xinjing.domain.Therapy;
import cn.easy.xinjing.service.ContentService;
import cn.easy.xinjing.service.DiseaseService;
import cn.easy.xinjing.service.TherapyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by raytine on 2017/5/26.
 */

@Api(value = "h5r-api-controller", description = "H5相关API", position = 5)
@Controller
@RequestMapping("/api/v1/h5s")
public class H5ApiController {

    @Autowired
    private DiseaseService diseaseService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private TherapyService therapyService;

    @ApiOperation(value = "获取我的场景", notes = "获取我的场景", position = 5)
    @ResponseBody
    @RequestMapping(value = {"/content"}, method = RequestMethod.POST)
    public ContentReturnH5Bean[] login(@RequestParam("page") Integer page,@RequestParam("size") Integer size,
                                       @RequestParam( value = "checkbox1", required = false) String checkbox1,
                                       @RequestParam( value = "checkbox2", required = false) String checkbox2,ModelMap model) {
        PageBean pageBean = new PageBean();
        pageBean.setTotalPages(size);
        pageBean.setCurrentPage(page);
        Page<Content> contentPage = null;
        Map<String, Object> searchParams = new HashedMap();

        if((checkbox1!=null&&checkbox1.length()>4)){
            searchParams.put("EQ_diseaseId",checkbox1);
        }
        if((checkbox2!=null&&checkbox2.length()>4)){
            searchParams.put("EQ_therapyId",checkbox2);

        }
        if(searchParams.size()>=1){
            contentPage  = contentService.findBySearchParamsH5(searchParams,pageBean);
        }
        else {
            searchParams.put("EQ_status",2);
            searchParams.put("EQ_hidden",0);
            contentPage  = contentService.search(searchParams,pageBean);
        }
        List<ContentReturnH5Bean> contentReturnH5Beans= new ArrayList<ContentReturnH5Bean>();
        ContentReturnH5Bean contentReturnH5Bean = null;
        if(contentPage.getContent().size()>=1){
            for (Content content : contentPage.getContent()) {
                contentReturnH5Bean = new ContentReturnH5Bean();
                contentReturnH5Bean.setId(content.getId());
                contentReturnH5Bean.setName(content.getName());
                contentReturnH5Bean.setClicks(content.getClicks());
                contentReturnH5Bean.setPrice(content.getPrice());
                contentReturnH5Bean.setDuration(content.getDuration());
                contentReturnH5Bean.setIntroduce(content.getRemark());

                contentReturnH5Bean.setCoverPic(content.getCoverPic());
                contentReturnH5Beans.add(contentReturnH5Bean);
            }
        }
        ContentReturnH5Bean[]  ContentReturnH5Bean = contentReturnH5Beans.toArray(new ContentReturnH5Bean[contentReturnH5Beans.size()]);

        return ContentReturnH5Bean;
    }

    @ApiOperation(value = "获取病症", notes = "获取病症", position = 6)
    @ResponseBody
    @RequestMapping(value = {"/getdisease"}, method = RequestMethod.POST)
    public Disease[] getDisease(ModelMap model,@RequestParam( value = "checkbox1", required = false) String checkbox1) {
        Map<String, Object> searchParams = new HashedMap();
        searchParams.put("EQ_status",2);
        searchParams.put("EQ_hidden",0);
        List<Disease> diseaseList = diseaseService.search(searchParams);
        for (Disease disease : diseaseList) {
            if(checkbox1!=null&&checkbox1.length()>=1){
                checkbox1=checkbox1.replace("'","").replace("(","").replace(")","");
                String[] box = checkbox1.split(",");
                for (String s : box) {
                    if(disease.getId().equals(s)){
                        disease.setHelpCode("checked = 'checked'");
                        break;
                    }else{
                        disease.setHelpCode("");
                    }
                }
            }else{
                disease.setHelpCode("");
            }

        }

        Disease[] diseases =  diseaseList.toArray(new Disease[diseaseList.size()]);

        return diseases;
    }

    @ApiOperation(value = "获取疗法", notes = "获取疗法", position = 6)
    @ResponseBody
    @RequestMapping(value = {"/gettherapy"}, method = RequestMethod.POST)
    public Therapy[] getTherapy(ModelMap model, @RequestParam( value = "checkbox2", required = false) String checkbox2) {
        Map<String, Object> searchParams = new HashedMap();
        searchParams.put("EQ_status",2);
        searchParams.put("EQ_hidden",0);
        List<Therapy>  therapyList = therapyService.search(searchParams);
        for (Therapy therapy : therapyList) {
            if(checkbox2!=null&&checkbox2.length()>=1){
                checkbox2=checkbox2.replace("'","").replace("(","").replace(")","");
                String[] box = checkbox2.split(",");
                for (String s : box) {
                    if(therapy.getId().equals(s)){
                        therapy.setHelpCode("checked = 'checked'");
                        break;
                    }else{
                        therapy.setHelpCode("");
                    }
                }
            }else{
                therapy.setHelpCode("");
            }
        }

        Therapy[] ther =  therapyList.toArray(new Therapy[therapyList.size()]);

        return ther;
    }

    @RequestMapping(value = "/filterContent", method = RequestMethod.POST)
    @ResponseBody
    public ContentReturnH5Bean[]  filterContent(@RequestParam("student ") String student ) {
        PageBean pageBean = new PageBean();
        pageBean.setTotalPages(10);
        pageBean.setCurrentPage(1);
        Map<String, Object> searchParams = new HashedMap();
        Page<Content> contentPage = contentService.search(searchParams,pageBean);
        List<ContentReturnH5Bean> contentReturnH5Beans= new ArrayList<ContentReturnH5Bean>();
        ContentReturnH5Bean contentReturnH5Bean = null;
        if(contentPage.getContent().size()>=1){
            for (Content content : contentPage.getContent()) {
                contentReturnH5Bean = new ContentReturnH5Bean();
                contentReturnH5Bean.setId(content.getId());
                contentReturnH5Bean.setName(content.getName());
                contentReturnH5Bean.setClicks(content.getClicks());
                contentReturnH5Bean.setPrice(content.getPrice());
                contentReturnH5Bean.setDuration(content.getDuration());
                contentReturnH5Bean.setIntroduce(content.getRemark());

                contentReturnH5Bean.setCoverPic(content.getCoverPic());
                contentReturnH5Beans.add(contentReturnH5Bean);
            }
        }
        ContentReturnH5Bean[]  ContentReturnH5Bean = contentReturnH5Beans.toArray(new ContentReturnH5Bean[contentReturnH5Beans.size()]);

        return ContentReturnH5Bean;
    }
}
