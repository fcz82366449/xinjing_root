package cn.easy.xinjing.h5;

import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.LiteDeviceResolver;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenzhongyi on 2017/5/8.
 */
public class H5BaseController {

    final DeviceResolver deviceResolver = new LiteDeviceResolver();

    protected boolean isMobile(HttpServletRequest request) {
        return deviceResolver.resolveDevice(request).isMobile();
    }
}
