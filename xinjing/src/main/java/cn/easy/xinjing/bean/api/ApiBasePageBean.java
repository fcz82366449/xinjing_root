package cn.easy.xinjing.bean.api;

import cn.easy.base.bean.PageBean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by chenzy on 2016/10/15.
 */
@ApiModel(value = "分页模型", description = "分页模型ApiBasePageBean")
public class ApiBasePageBean extends ApiBaseBean {
    @ApiModelProperty(value = "分页页码")
    private int paging;

    private String sortName;

    private String sortOrder;

    @JsonIgnore
    public PageBean getPageBean() {
        PageBean pageBean = new PageBean();
        pageBean.setCurrentPage(getPaging());
        return pageBean;
    }

    public int getPaging() {
        return paging;
    }

    public void setPaging(int paging) {
        this.paging = paging;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSortOrder() {
        return StringUtils.isBlank(sortOrder) ? "desc" :  sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
