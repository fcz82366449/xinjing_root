package cn.easy.xinjing.bean;

import java.math.BigDecimal;

/**
 * 内容-套餐 中间 Bean
 * Created by 001978 on 2017/2/8.
 */
public class ContentComboBean {
    //内容id
    private String contentId;
    //关联的套餐内容ids
    private String reContentIds;
    //关联的套餐内容names
    private String reContentNames;
    //合计价格
    private BigDecimal totalPrice;
    //优惠价格
    private BigDecimal discountPrice;
    //折扣率
    private BigDecimal discountRate;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getReContentIds() {
        return reContentIds;
    }

    public void setReContentIds(String reContentIds) {
        this.reContentIds = reContentIds;
    }

    public String getReContentNames() {
        return reContentNames;
    }

    public void setReContentNames(String reContentNames) {
        this.reContentNames = reContentNames;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getDiscountRate() {
        return BigDecimal.ZERO.equals(totalPrice) ? BigDecimal.ZERO : this.discountPrice.divide(this.totalPrice, 2, BigDecimal.ROUND_HALF_UP);
    }
}
