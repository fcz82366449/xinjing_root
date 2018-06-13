package cn.easy.xinjing.bean.api;

import java.math.BigDecimal;

/**
 * Created by chenzy on 2016/10/31.
 */
public class MinPriceBean extends ApiBaseBean{
    private BigDecimal minPrice;

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }
}
