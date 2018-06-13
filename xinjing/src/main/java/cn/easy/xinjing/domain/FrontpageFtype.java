package cn.easy.xinjing.domain;

import cn.easy.base.domain.core.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "xj_frontpage_ftype")
public class FrontpageFtype extends IdEntity {
	/**头条分类Id*/
	private String frontpageTypeId;
	/**头条id*/
	private String frontpageId;
	public String getFrontpageTypeId() { return frontpageTypeId; }
	public void setFrontpageTypeId(String frontpageTypeId) { this.frontpageTypeId = frontpageTypeId; }
	public String getFrontpageId() { return frontpageId; }
	public void setFrontpageId(String frontpageId) { this.frontpageId = frontpageId; }

}
