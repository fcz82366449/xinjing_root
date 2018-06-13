package cn.easy.xinjing.api;

import cn.easy.base.bean.common.ApiResultBean;
import cn.easy.base.domain.User;
import cn.easy.base.service.UserService;
import cn.easy.xinjing.api.base.XjBaseApiController;
import cn.easy.xinjing.domain.UserDoctor;
import cn.easy.xinjing.domain.UserPatient;
import cn.easy.xinjing.service.UserDoctorService;
import cn.easy.xinjing.service.UserPatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by chenzhongyi on 2016/9/27.
 */
@Api(value = "test-api-controller", description = "测试相关API", position = 5)
@Controller
@RequestMapping("/api/v1/test")
public class TestApiController extends XjBaseApiController {
    @Autowired
    UserService userService;
    @Autowired
    UserDoctorService userDoctorService;
    @Autowired
    UserPatientService userPatientService;

    @ApiOperation(value = "删除个人信息", notes = "删除个人信息", position = 5)
    @ResponseBody
    @RequestMapping(value = {"/user_delete"}, method = RequestMethod.GET)
    public ApiResultBean infoGet(@RequestParam(value = "mobile") String mobile, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        User user = userService.getByUsername(mobile);
        if (user == null) {
            return toError("用户不存在", -1);
        }
        UserDoctor userDoctor = userDoctorService.getByUserId(user.getId());
        if (userDoctor != null) {
            userDoctorService.delete(userDoctor.getId());
        }

        UserPatient userPatient = userPatientService.getByUserId(user.getId());
        if (userPatient != null) {
            userPatientService.delete(userPatient.getId());
        }
        userService.delete(user.getId());

        return toSuccess("用户删除成功");
    }

    @ApiOperation(value = "helloword", notes = "helloword", position = 5)
    @ResponseBody
    @RequestMapping(value = {"/helloword"}, method = RequestMethod.GET)
    public ApiResultBean helloword(@RequestParam(value = "mas",required = false)  String mas, HttpServletRequest request) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {


        return toSuccess("接口调用成功",mas);
    }


}
