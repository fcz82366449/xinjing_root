package cn.easy.xinjing.web;

import cn.easy.base.bean.PageBean;
import cn.easy.base.bean.common.AjaxResultBean;
import cn.easy.base.core.mvc.FormModel;
import cn.easy.base.domain.User;
import cn.easy.base.service.UserService;
import cn.easy.base.utils.BaseUtils;
import cn.easy.base.web.BaseController;
import cn.easy.xinjing.domain.UserVrRoom;
import cn.easy.xinjing.domain.VrRoom;
import cn.easy.xinjing.service.UserVrRoomService;
import cn.easy.xinjing.service.VrRoomService;
import cn.easy.xinjing.utils.Constants;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/userVrRoom")
public class UserVrRoomController extends BaseController {
    @Autowired
    private UserVrRoomService	userVrRoomService;
    @Autowired
    private VrRoomService vrRoomService;
    @Autowired
    private UserService userService;

    @RequestMapping("")
    String index(Model model, HttpServletRequest request) {
        increment("web.userVrRoom.index");
        return "userVrRoom/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    Page<UserVrRoom> list(PageBean pageBean, HttpServletRequest request) {
        increment("web.userVrRoom.list");
        Map<String,Object> map =  searchParams(request);
        String realName=(String)map.get("realName");
        String vrRoomId =(String)map.get("vrRoomId");
        List<UserVrRoom> userVrRooms = userVrRoomService.findByRealNameAndVrRoomId(realName,vrRoomId);

//        Page<UserVrRoom> page = userVrRoomService.findAll(map,pageBean);


        Page<UserVrRoom> page= new PageImpl<UserVrRoom>(userVrRooms,pageBean.toPageRequest(new Sort(Sort.Direction.DESC,"id")),pageBean.getTotalPages());
        setFieldValues(page, User.class, "userId", new String[]{"username","realname"});
        setFieldValues(page, VrRoom.class, "vrRoomId", new String[]{"name"});
        setConfigFieldValues(page, Constants.USER_VR_ROOM_TYPE_KEY, "type");
        return page;
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    String formGet(@RequestParam(value = "id", required = false) String id, Model model, HttpServletRequest request) {
        increment("web.userVrRoom.formGet");
        UserVrRoom userVrRoom = new UserVrRoom();
        User reUser = new User();
        String vrRoomName = "";
        if (isValidId(id)) {
            userVrRoom = userVrRoomService.getOne(id);
            VrRoom vrRoom = vrRoomService.getOne(userVrRoom.getVrRoomId());
            reUser = userService.getOne(userVrRoom.getUserId());
            if(vrRoom != null){
                vrRoomName = vrRoom.getName();
            }
        }
        model.addAttribute("userVrRoom", userVrRoom);
        model.addAttribute("reUser", reUser);
        model.addAttribute("vrRoomName", vrRoomName);
        return "userVrRoom/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    AjaxResultBean formPost(UserVrRoom userVrRoom, @FormModel(value = "reUser") User reUser, HttpServletRequest request) {
        increment("web.userVrRoom.formPost");
        if (!isValidId(userVrRoom.getId())){
            if(userService.isExist(reUser)) {
                return toError("该用户名已存在,请勿重复添加");
            }
            reUser.setMobile(reUser.getUsername());
            reUser.setPassword(BaseUtils.encodePassword(cn.easy.base.utils.Constants.DEFAULT_PASSWORD));
            userService.save(reUser);
            userVrRoom.setUserId(reUser.getId());
        }
        userVrRoomService.save(userVrRoom);
        return toSuccess("保存成功");
    }

    @RequestMapping("/delete")
    @ResponseBody
    AjaxResultBean delete(@RequestParam(value = "id") String id) {
        increment("web.userVrRoom.delete");
        userVrRoomService.deleteUserVrRoom(id);
        return toSuccess("删除成功");
    }

    @RequestMapping("/select")
    String select(@RequestParam(value = "selectIds", required = false) String selectIds,
                  Model model, HttpServletRequest request) {
        increment("web.userVrRoom.select");
        List<Map<String,Object>>  userVrRooms = setFieldValues(userVrRoomService.getAll(), User.class, "userId", new String[]{"username"});
        setFieldValues(userVrRooms, VrRoom.class, "vrRoomId", new String[]{"name"});
        setConfigFieldValues(userVrRooms, Constants.USER_VR_ROOM_TYPE_KEY, "type");
        model.addAttribute("userVrRooms",userVrRooms);
//        model.addAttribute("selectIds", selectIds);
//        model.addAttribute("totalPrice", totalPrice);
        return "userVrRoom/select";
    }

}


