package cn.easy.xinjing.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.easy.base.domain.core.AtEntity;

@Entity
@Table(name = "xj_video_encryption")
public class VideoEncryption extends AtEntity {
	/**内容id*/
	private String contentId;
	/**字节数*/
	private String bytes;

	private Integer jmvalues;

	public Integer getJmvalues() {
		return jmvalues;
	}

	public void setJmvalues(Integer jmvalues) {
		this.jmvalues = jmvalues;
	}

	public String getContentId() { return contentId; }
	public void setContentId(String contentId) { this.contentId = contentId; }
	public String getBytes() { return bytes; }
	public void setBytes(String bytes) { this.bytes = bytes; }

}
