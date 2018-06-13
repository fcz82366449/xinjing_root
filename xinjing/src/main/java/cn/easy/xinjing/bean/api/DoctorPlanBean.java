package cn.easy.xinjing.bean.api;

/**
 * Created by raytine on 2017/8/21.
 */
public class DoctorPlanBean extends ApiBaseBean{

    private String planId;
    private String contentIds;
    private String name;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getContentIds() {
        return contentIds;
    }

    public void setContentIds(String contentIds) {
        this.contentIds = contentIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
