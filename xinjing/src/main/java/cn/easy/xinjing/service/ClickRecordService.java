package cn.easy.xinjing.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import cn.easy.base.service.BaseService;
import cn.easy.base.utils.jdbc.PaginationHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.easy.base.bean.PageBean;
import cn.easy.xinjing.domain.ClickRecord;
import cn.easy.xinjing.repository.ClickRecordDao;

@Component
public class ClickRecordService extends BaseService<ClickRecord> {
    @Autowired
    private ClickRecordDao	clickRecordDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public Page<ClickRecord> search(Map<String, Object> searchParams, PageBean pageBean) {

        return clickRecordDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<ClickRecord> search(Map<String, Object> searchParams, Sort... sort) {
        return clickRecordDao.findAll(spec(searchParams), sort == null || sort.length == 0  ? new Sort(Direction.DESC, "createdAt") : sort[0]);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        clickRecordDao.delete(id);
    }

    public ClickRecord getOne(String id) {
        return clickRecordDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ClickRecord save(ClickRecord clickRecord) {
        return clickRecordDao.save(clickRecord);
    }

    private boolean isValidParam(Map<String, Object> searchParams, String key) {
        return searchParams.containsKey(key) && StringUtils.isNotBlank(searchParams.get(key).toString());
    }

    public Page<ClickRecord> findBySearchParams(Map<String, Object> searchParams, PageBean pageBean,Integer type) {

        List<Object> args = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder("select c.* from xj_click_record c  ");

        if (isValidParam(searchParams, "EQ_contentName")) {
            sql.append(" Left join xj_content b on c.content_id = b.id ");
        }
        if (isValidParam(searchParams, "EQ_recordno")) {
            sql.append(" Left join xj_prescription_case d on c.patientcase_id = d.id ");
        }
        if (isValidParam(searchParams, "EQ_uservrName")) {
            sql.append(" Left join xj_user_vr_room e on e.user_id = c.user_id ");
        }
        if(type!=null&&type==6){
            sql.append(" Where c.hidden=0 and IFNULL(c.TYPE,0)!=6 ");
        }else{
            sql.append(" Where c.hidden=0 ");
        }


        if (isValidParam(searchParams, "EQ_contentName")) {
            sql.append(" and b.name like ? and b.hidden=0");
            args.add("%" + searchParams.get("EQ_contentName") + "%");
        }
        if (isValidParam(searchParams, "EQ_recordno")) {
            sql.append(" and d.recordno = ? and d.hidden=0 ");
            args.add("" + searchParams.get("EQ_recordno") + "");
        }
        if (isValidParam(searchParams, "EQ_uservrName")) {
            sql.append(" and e.user_id in (select id from pb_user where realname like ? ) ");
            args.add("%" + searchParams.get("EQ_uservrName") + "%");
        }


        if (isValidParam(searchParams, "EQ_userId")) {
            sql.append("AND c.user_id = ? ");
            args.add(searchParams.get("EQ_userId"));
        }
        if (isValidParam(searchParams, "EQ_patientcaseId")) {
            sql.append("AND c.patientcase_id = ? ");
            args.add(searchParams.get("EQ_patientcaseId"));
        }
        if (isValidParam(searchParams, "EQ_hospitalId")) {
            sql.append("AND c.hospital_id = ? ");
            args.add(searchParams.get("EQ_hospitalId"));
        }
        if (isValidParam(searchParams, "GET_beginDate")) {
            sql.append("AND c.click_date >= ? ");
            args.add(searchParams.get("GET_beginDate"));
        }

        if (isValidParam(searchParams, "GET_endDate")) {
            sql.append("AND c.click_date <= ? ");
            args.add(searchParams.get("GET_endDate"));
        }
        sql.append("order by c.created_at desc");
        PaginationHelper<ClickRecord> helper = new PaginationHelper<>();
        return helper.fetchPage(jdbcTemplate, sql.toString(), args.toArray(), pageBean, (rs, i) -> {
            ClickRecord clickRecord = new ClickRecord();
            clickRecord.setId(rs.getString("id"));
            clickRecord.setContentId(rs.getString("content_id"));
            clickRecord.setClickDate(rs.getString("click_date"));
            clickRecord.setHidden(rs.getInt("hidden"));
            clickRecord.setUserId(rs.getString("user_id"));
            clickRecord.setPatientcaseId(rs.getString("patientcase_id"));
            clickRecord.setHospitalId(rs.getString("hospital_id"));
            return clickRecord;
        });
    }


}


