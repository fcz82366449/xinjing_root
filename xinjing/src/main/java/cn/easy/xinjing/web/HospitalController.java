package cn.easy.xinjing.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.easy.base.domain.Area;
import cn.easy.base.domain.User;
import cn.easy.base.utils.Global;
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
import cn.easy.xinjing.domain.Hospital;
import cn.easy.xinjing.service.HospitalService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/hospital")
public class HospitalController extends BaseController {
    @Autowired
    private HospitalService	hospitalService;
    @Autowired
    private OssMtsService ossMtsService;
    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.hospital.index");
        return "hospital/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<Hospital> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.hospital.list");

        Page<Hospital> page = hospitalService.search(searchParams(request), pageBean);
        setFieldValues(page, Area.class, "region", "code", new String[] { "fullName" });
        setConfigFieldValues(page, Constants.USER_HOSPITAL_APPROVE_STATUS_ENUM, "status");
        setFieldValues(page, User.class,"creator", new String[]{"realname"});
        setFieldValues(page, User.class,"updator", new String[]{"realname"});
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.hospital.formGet");
        Hospital hospital;
        if (isValidId(id)) {
            hospital = hospitalService.getOne(id);
        } else {
            hospital = new Hospital();
            hospital.setRegion("330100");
        }
        String provinceCode = hospital.getRegion().substring(0,2) + "0000";
        String cityCode = hospital.getRegion().substring(0, 4) + "00";
        String areaCode = hospital.getRegion();
        model.addAttribute("provinceCode", provinceCode);
        model.addAttribute("cityCode", cityCode);
        model.addAttribute("areaCode", areaCode);
        model.addAttribute("hospital", hospital);
        model.addAttribute("coverPicUrl", hospital.getCoverPic());
        model.addAttribute("isFreeMap", getConfigMap(Global.IF_OR_NOT_ENUM_KEY));
        model.addAttribute("isDisplayMap", getConfigMap(Global.IF_OR_NOT_ENUM_KEY));
        model.addAttribute("coverPic", hospital.getCoverPic());

        return "hospital/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(Hospital hospital, HttpServletRequest request) {
        increment("web.hospital.formPost");
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
            if(StringUtils.isNotEmpty(hospital.getId())){
                String picPath = hospitalService.getOne(hospital.getId()).getCoverPic();
                picPath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "/static/img/" + picPath.substring(picPath.lastIndexOf("/")+1);
                File pic = new File(picPath);
                if(pic.exists()){
                    pic.delete();
                }
            }
            hospital.setCoverPic(coverPicUrl);
        }
        hospitalService.save(hospital);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.hospital.delete");
        String picPath = hospitalService.getOne(id).getCoverPic();
        picPath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "/static/img/" + picPath.substring(picPath.lastIndexOf("/")+1);
        File pic = new File(picPath);
        if(pic.exists()){
            pic.delete();
        }
        hospitalService.delete(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/select")
    String select(Model model, HttpServletRequest request) {
        increment("web.hospital.select");
        return "hospital/select";
    }

}


