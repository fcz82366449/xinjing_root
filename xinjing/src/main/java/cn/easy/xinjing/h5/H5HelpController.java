package cn.easy.xinjing.h5;

import cn.easy.xinjing.domain.Disease;
import cn.easy.xinjing.domain.Therapy;
import cn.easy.xinjing.service.DiseaseService;
import cn.easy.xinjing.service.TherapyService;
import cn.easy.xinjing.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenzhongyi on 2017/5/2.
 */
@Controller
@RequestMapping("/h5/help")
public class H5HelpController extends H5BaseController{
    @Autowired
    private TherapyService therapyService;
    @Autowired
    private DiseaseService diseaseService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        model.addAttribute("therapyList", therapyService.findByStatus(Constants.THERAPY_STATUS_PUBLISH));
        model.addAttribute("diseaseList", diseaseService.findByStatus(Constants.DISEASE_STATUS_PUBLISH));
        return "h5/help/index";
    }
    @RequestMapping("/therapy")
    String therapy(String id, Model model, HttpServletRequest request) {
        Therapy therapy = therapyService.getOne(id);
        model.addAttribute("therapy", therapy);
        model.addAttribute("isMobile", isMobile(request));
        return "h5/help/therapy";
    }

    @RequestMapping("/disease")
    String disease(String id, Model model, HttpServletRequest request) {
        Disease disease = diseaseService.getOne(id);
        model.addAttribute("disease", disease);
        model.addAttribute("isMobile", isMobile(request));
        return "h5/help/disease";
    }


    @RequestMapping("/apptherapy")
    String apptherapy(String id, Model model, HttpServletRequest request) {
        Therapy therapy = therapyService.getOne(id);
        //替换场景超链接h5/myshow/content 为 h5/myshow/appContent
        if(therapy.getDescription()!=null&&therapy.getDescription().length()>=1){
            String thdes = therapy.getDescription().replaceAll("h5/myshow/content", "h5/myshow/appContent");
            therapy.setDescription(thdes);
        }
        model.addAttribute("therapy", therapy);
        model.addAttribute("isMobile", isMobile(request));
        return "h5/help/therapy";
    }

    @RequestMapping("/appdisease")
    String appdisease(String id, Model model, HttpServletRequest request) {
        Disease disease = diseaseService.getOne(id);
        if(disease.getDescription()!=null&&disease.getDescription().length()>=1){
            String thdes = disease.getDescription().replaceAll("h5/myshow/content", "h5/myshow/appContent");
            disease.setDescription(thdes);
        }
        model.addAttribute("disease", disease);
        model.addAttribute("isMobile", isMobile(request));
        return "h5/help/disease";
    }


}
