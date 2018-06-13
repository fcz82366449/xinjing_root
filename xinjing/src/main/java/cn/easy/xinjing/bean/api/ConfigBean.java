package cn.easy.xinjing.bean.api;

/**
 * Created by chenzhongyi on 2016/10/12.
 */
public class ConfigBean {
    private int id;
    private String name;

    public ConfigBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
