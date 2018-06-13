package cn.easy.xinjing.utils;


import cn.easy.base.SpringContext;
import cn.easy.base.service.ConfigService;
import cn.easy.xinjing.bean.api.ConfigBean;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chenzy on 2016/9/24.
 */
public class ProjectUtil {

    /**
     * 返回配置信息 API专用
     *
     * @param key
     * @return
     */
    public static List<ConfigBean> getConfigBeanList(String key) {
        Map<String, String> typeMap = SpringContext.getBean(ConfigService.class).getConfigMap(key);

        List<ConfigBean> result = new ArrayList<>();
        for (Map.Entry<String, String> entry : typeMap.entrySet()) {
            result.add(new ConfigBean(Integer.parseInt(entry.getKey()), entry.getValue()));
        }
        return result;
    }

    public static String[] str2Arr(String str) {
        if (StringUtils.isBlank(str)) {
            return new String[0];
        }
        if (str.indexOf(",") == -1) {
            return new String[]{str};
        }
        return str.split(",");
    }
}
