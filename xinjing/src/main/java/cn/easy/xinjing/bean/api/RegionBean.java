package cn.easy.xinjing.bean.api;

import cn.easy.base.domain.Area;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzhongyi on 2016/10/19.
 */
public class RegionBean extends Area{

    List<Area> array = new ArrayList<>();

    public static RegionBean valueOf(Area area) {
        RegionBean regionBean = new RegionBean();
        regionBean.setCode(area.getCode());
        regionBean.setName(area.getName());
        regionBean.setFullName(area.getFullName());
        return regionBean;
    }

    public void add(Area area) {
        array.add(area);
    }

    public List<Area> getArray() {
        return array;
    }

    public void setArray(List<Area> array) {
        this.array = array;
    }
}
