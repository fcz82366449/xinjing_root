package cn.easy.xinjing.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.easy.base.domain.Area;
import cn.easy.base.utils.Global;
import cn.easy.base.web.BaseController;
import cn.easy.xinjing.service.HospitalService;
import cn.easy.xinjing.service.OssMtsService;
import cn.easy.xinjing.utils.Constants;
import org.apache.commons.lang3.StringUtils;
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
import cn.easy.xinjing.domain.VrRoom;
import cn.easy.xinjing.service.VrRoomService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/vrRoom")
public class VrRoomController extends BaseController {
    @Autowired
    private VrRoomService	vrRoomService;
    //--being--fanchegnzhi---添加医院的参照逻辑---------------------//
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private OssMtsService ossMtsService;
    //--end--fanchegnzhi------------------------------------------//
    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.vrRoom.index");
        return "vrRoom/index";
    }

    @RequestMapping("/select")
    String select(Model model, HttpServletRequest request) {
        increment("web.vrRoom.select");
        return "vrRoom/select";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<VrRoom> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.vrRoom.list");

        Page<VrRoom> page = vrRoomService.search(searchParams(request), pageBean);
        setFieldValues(page, Area.class, "region", "code", new String[] { "fullName" });
        setConfigFieldValues(page, Constants.VR_ROOM_STATUS_ENUM_KEY, "status");
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.vrRoom.formGet");
        VrRoom vrRoom;
        //--being--fanchegnzhi---添加医院的参照逻辑---------------------//
        String hospitalName="";
        //--end--------------------------------------------------------//
        if (isValidId(id)) {
            vrRoom = vrRoomService.getOne(id);
            //--being--fanchegnzhi---添加医院的参照逻辑---------------------//
            if(isValidId(vrRoom.getHospital_id())){
                hospitalName = hospitalService.getOne(vrRoom.getHospital_id()).getName();
            }
            //--end--------------------------------------------------------//
        } else {
            vrRoom = new VrRoom();
            vrRoom.setRegion("330100");
        }

        String provinceCode = vrRoom.getRegion().substring(0,2) + "0000";
        String cityCode = vrRoom.getRegion().substring(0, 4) + "00";
        String areaCode = vrRoom.getRegion();

        model.addAttribute("vrRoom", vrRoom);
        model.addAttribute("provinceCode", provinceCode);
        model.addAttribute("cityCode", cityCode);
        model.addAttribute("areaCode", areaCode);
        model.addAttribute("isFreeMap", getConfigMap(Global.IF_OR_NOT_ENUM_KEY));
        //--being--fanchegnzhi---添加医院的参照逻辑---------------------//
        model.addAttribute("hospitalName", hospitalName);
        model.addAttribute("coverPicUrl", vrRoom.getCoverPic());
        model.addAttribute("coverPic", vrRoom.getCoverPic());
        //--end--------------------------------------------------------//
        return "vrRoom/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(VrRoom vrRoom, HttpServletRequest request) {

        increment("web.vrRoom.formPost");
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
            if(StringUtils.isNotEmpty(vrRoom.getId())){
                String picPath = vrRoomService.getOne(vrRoom.getId()).getCoverPic();
                picPath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "/static/img/" + picPath.substring(picPath.lastIndexOf("/")+1);
                File pic = new File(picPath);
                if(pic.exists()){
                    pic.delete();
                }
            }
            vrRoom.setCoverPic(coverPicUrl);
        }
        vrRoomService.save(vrRoom);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.vrRoom.delete");
        String picPath = vrRoomService.getOne(id).getCoverPic();
        picPath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "/static/img/" + picPath.substring(picPath.lastIndexOf("/")+1);
        File pic = new File(picPath);
        if(pic.exists()){
            pic.delete();
        }
        vrRoomService.delete(id);
        return toSuccess("删除成功");
    }

}


