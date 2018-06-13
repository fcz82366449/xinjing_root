package cn.easy.xinjing.h5;

import cn.easy.xinjing.domain.Content;
import cn.easy.xinjing.domain.Hospital;
import cn.easy.xinjing.service.HospitalService;
import cn.easy.xinjing.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by chenzhongyi on 2017/5/8.
 */
@Controller
@RequestMapping("/h5/hospital")
public class H5HospitalController extends H5BaseController{

    @Autowired
    private HospitalService hospitalService;


    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        List<Hospital> carouselHospitalList = hospitalService.findByCarousel(String.valueOf(Constants.YES));

        List<Hospital> hospitalList = hospitalService.findByIsdisplay(Constants.YES);

        model.addAttribute("carouselHospitalList", carouselHospitalList);
        model.addAttribute("hospitalList", hospitalList);
        return "h5/hospital/index";
    }

    @RequestMapping("/info")
    String info(String id, Model model, HttpServletRequest request) {
        Hospital hospital = hospitalService.getOne(id);
        model.addAttribute("hospital", hospital);
        model.addAttribute("isMobile", isMobile(request));
        return "h5/hospital/info";
    }
}
