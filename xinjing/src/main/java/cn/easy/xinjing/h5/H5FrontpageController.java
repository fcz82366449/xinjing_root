package cn.easy.xinjing.h5;

import cn.easy.xinjing.domain.Frontpage;
import cn.easy.xinjing.domain.FrontpageFtype;
import cn.easy.xinjing.domain.FrontpageType;
import cn.easy.xinjing.domain.Hospital;
import cn.easy.xinjing.service.FrontpageFtypeService;
import cn.easy.xinjing.service.FrontpageService;
import cn.easy.xinjing.service.FrontpageTypeService;
import cn.easy.xinjing.service.HospitalService;
import cn.easy.xinjing.utils.Constants;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chenzhongyi on 2017/5/8.
 */
@Controller
@RequestMapping("/h5/frontpage")
public class H5FrontpageController extends H5BaseController{

    @Autowired
    private FrontpageService frontpageService;
    @Autowired
    private FrontpageTypeService frontpageTypeService;
    @Autowired
    private FrontpageFtypeService frontpageFtypeService;
    @Autowired
    private HospitalService hospitalService;

    @RequestMapping("")
    String index01(Model model, HttpServletRequest request,
                   @RequestParam(value = "id", required = false) String id,
                   @RequestParam(value = "type", required = false) String ftype){
        List<Frontpage> frontpageList = new ArrayList<Frontpage>();
        List<Frontpage> carouselfrontpageList = new ArrayList<Frontpage>();

        //获取审核通过的头条或者新闻
        FrontpageType frontpageType = new FrontpageType();
        if("frontpage01".equals(ftype)){
             frontpageType = frontpageTypeService.findByName("行业新闻");
        }else if("frontpage02".equals(ftype)){
            frontpageType = frontpageTypeService.findByName("心景学院");
        }else if("frontpage03".equals(ftype)){
            frontpageType = frontpageTypeService.findByName("心景头条");
        }
        List<FrontpageType> frontpageTypeList = new ArrayList<>();
        String typename = "";
        if(frontpageType!=null){
           frontpageTypeList = frontpageTypeService.findByPidAndHiddenOrderBySortAsc(frontpageType.getId());

            List<String> pidlist = new ArrayList<String>();
            String pid = "";
            for (FrontpageType type : frontpageTypeList) {
                pidlist.add(type.getId());
                pid = frontpageTypeList.get(0).getId();
            }
            if(pidlist.size()>=1){//获取全部分类下的头条图片集合
                typename = frontpageTypeList.get(0).getName();
                String[] pids = pidlist.toArray(new String[pidlist.size()]);
                List<FrontpageFtype> frontpageFtypes = frontpageFtypeService.findByFrontpageTypeIdIn(pids);
                List<String> idlist = new ArrayList<String>();
                for (FrontpageFtype frontpa : frontpageFtypes) {
                    idlist.add(frontpa.getFrontpageId());
                }
                if(idlist.size()>=1){
                    String[] ids = idlist.toArray(new String[idlist.size()]);
                    carouselfrontpageList = frontpageService.findByHiddenAndIdInOrderByReleaseTimeDesc(ids);
                }
                List<FrontpageFtype> frontpageFtypes2 = new ArrayList<>();
                if(id!=null&&id.length()>0){
                    frontpageFtypes2  = frontpageFtypeService.findByFrontpageTypeId(id);
                    //更改选中id的状态
                    for (FrontpageType type : frontpageTypeList) {
                        if(id!=null&&id.length()>0){
                            if(id.equals(type.getId())){
                                type.setStatus(1);
                            }
                        }
                    }
                }else{
                    frontpageFtypes2  = frontpageFtypeService.findByFrontpageTypeId(pid);
                    //更改选中id的状态
                    for (FrontpageType type : frontpageTypeList) {
                        if(pid!=null&&pid.length()>0){
                            if(pid.equals(type.getId())){
                                type.setStatus(1);
                            }
                        }
                    }
                }



                List<String> idlist2 = new ArrayList<String>();
                for (FrontpageFtype frontpa : frontpageFtypes2) {
                    idlist2.add(frontpa.getFrontpageId());
                }
                if(idlist2.size()>=1){
                    String[] ids = idlist2.toArray(new String[idlist2.size()]);
                    frontpageList = frontpageService.findByHiddenAndIdInOrderByReleaseTimeDesc(ids);
                }

            }


        }
        model.addAttribute("frontpageTypeList", frontpageTypeList);
        model.addAttribute("typename", typename);
        model.addAttribute("carouselfrontpageList", carouselfrontpageList);
        model.addAttribute("frontpageList", frontpageList);
        model.addAttribute("type",ftype);
        return "h5/frontpage/index";
    }




    @RequestMapping("frontpage02")
    String index02(Model model, HttpServletRequest request) {

        return "h5/frontpage/index";
    }

    @RequestMapping("frontpage03")
    String index03(Model model, HttpServletRequest request) {

        return "h5/frontpage/index";
    }

    @RequestMapping("/info")
    String info(String type,String id, Model model, HttpServletRequest request) {
        Frontpage frontpage = frontpageService.getOne(id);
        model.addAttribute("frontpage", frontpage);
        model.addAttribute("isMobile", isMobile(request));
        model.addAttribute("type", type);
        return "h5/frontpage/info";
    }
}
