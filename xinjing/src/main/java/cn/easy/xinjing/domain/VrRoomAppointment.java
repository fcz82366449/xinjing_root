package cn.easy.xinjing.domain;

import cn.easy.base.core.repository.annotation.Hiddenable;
import cn.easy.base.domain.core.AtEntity;
import cn.easy.base.domain.core.IHiddenEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Hiddenable
@Table(name = "xj_vr_room_appointment")
public class VrRoomAppointment extends AtEntity implements IHiddenEntity {
    private String vrRoomId;
    /**
     * 开始时间
     */
    private Timestamp startAt;
    /**
     * 结束时间
     */
    private Timestamp endAt;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 预约人
     */
    private String bookingPerson;
    /**
     * 预约人Id
     */
    private String bookingPersonId;
    /**
     * 预约人联系方式
     */
    private String bookingContact;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否隐藏
     */
    private Integer hidden = 0;

    public String getVrRoomId() {
        return vrRoomId;
    }

    public void setVrRoomId(String vrRoomId) {
        this.vrRoomId = vrRoomId;
    }

    public Timestamp getStartAt() {
        return startAt;
    }

    public void setStartAt(Timestamp startAt) {
        this.startAt = startAt;
    }

    public Timestamp getEndAt() {
        return endAt;
    }

    public void setEndAt(Timestamp endAt) {
        this.endAt = endAt;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBookingPerson() {
        return bookingPerson;
    }

    public void setBookingPerson(String bookingPerson) {
        this.bookingPerson = bookingPerson;
    }

    public String getBookingPersonId() {
        return bookingPersonId;
    }

    public void setBookingPersonId(String bookingPersonId) {
        this.bookingPersonId = bookingPersonId;
    }

    public String getBookingContact() {
        return bookingContact;
    }

    public void setBookingContact(String bookingContact) {
        this.bookingContact = bookingContact;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public Integer getHidden() {
        return hidden;
    }

    @Override
    public void setHidden(Integer hidden) {
        this.hidden = hidden;
    }

}
