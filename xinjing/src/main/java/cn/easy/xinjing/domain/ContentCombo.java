package cn.easy.xinjing.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.easy.base.domain.core.AtEntity;

@Entity
@Table(name = "xj_content_combo")
public class ContentCombo extends AtEntity {
	/**内容表ID*/
	private String contentId;
	/**套餐关联的内容表ID*/
	private String reContentId;
	/**单价*/
	private BigDecimal unitPrice;
	/**优惠价*/
	private BigDecimal discountPrice;
	/**排序*/
	private Integer sort;
	public String getContentId() { return contentId; }
	public void setContentId(String contentId) { this.contentId = contentId; }
	public String getReContentId() { return reContentId; }
	public void setReContentId(String reContentId) { this.reContentId = reContentId; }
	public BigDecimal getUnitPrice() { return unitPrice; }
	public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
	public BigDecimal getDiscountPrice() { return discountPrice; }
	public void setDiscountPrice(BigDecimal discountPrice) { this.discountPrice = discountPrice; }
	public Integer getSort() { return sort; }
	public void setSort(Integer sort) { this.sort = sort; }

}
