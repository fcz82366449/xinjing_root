package cn.easy.xinjing.bean.api;

import java.math.BigDecimal;

/**
 * Created by chenrujian on 2017/8/13.
 */
public class SingleselBean extends ApiBasePageBean {

    /**题目内容*/
    private String name;
    /**问题编号*/
    private String code;
    /**评测编码*/
    private String evaluatingId;
    /**问题类型*/
    private Integer singtype;
    /**选项1*/
    private String options01;
    /**选项2*/
    private String options02;
    /**选项3*/
    private String options03;
    /**选项4*/
    private String options04;
    /**选项5*/
    private String options05;
    /**选项6*/
    private String options06;
    /**题目分值*/
    private BigDecimal tmscore;
    /**备注*/
    private String remark;
    /**状态*/
    private Integer status;
    /**是否隐藏*/
    private Integer hidden = 0;

    /**评测内容介绍*/
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getEvaluatingId() { return evaluatingId; }
    public void setEvaluatingId(String evaluatingId) { this.evaluatingId = evaluatingId; }
    public Integer getSingtype() { return singtype; }
    public void setSingtype(Integer singtype) { this.singtype = singtype; }
    public String getOptions01() { return options01; }
    public void setOptions01(String options01) { this.options01 = options01; }
    public String getOptions02() { return options02; }
    public void setOptions02(String options02) { this.options02 = options02; }
    public String getOptions03() { return options03; }
    public void setOptions03(String options03) { this.options03 = options03; }
    public String getOptions04() { return options04; }
    public void setOptions04(String options04) { this.options04 = options04; }
    public String getOptions05() { return options05; }
    public void setOptions05(String options05) { this.options05 = options05; }
    public String getOptions06() { return options06; }
    public void setOptions06(String options06) { this.options06 = options06; }
    public BigDecimal getTmscore() { return tmscore; }
    public void setTmscore(BigDecimal tmscore) { this.tmscore = tmscore; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getHidden() { return hidden; }
    public void setHidden(Integer hidden) { this.hidden = hidden; }


}
