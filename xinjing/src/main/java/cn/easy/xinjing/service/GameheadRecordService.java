package cn.easy.xinjing.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.List;

import cn.easy.base.service.BaseService;
import cn.easy.base.utils.jdbc.PaginationHelper;
import cn.easy.xinjing.domain.ClickRecord;
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
import cn.easy.xinjing.domain.GameheadRecord;
import cn.easy.xinjing.repository.GameheadRecordDao;

@Component
public class GameheadRecordService extends BaseService<GameheadRecord> {
    @Autowired
    private GameheadRecordDao	gameheadRecordDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public Page<GameheadRecord> search(Map<String, Object> searchParams, PageBean pageBean) {
        return gameheadRecordDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<GameheadRecord> search(Map<String, Object> searchParams, Sort... sort) {
        return gameheadRecordDao.findAll(spec(searchParams), sort == null || sort.length == 0  ? new Sort(Direction.DESC, "createdAt") : sort[0]);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        gameheadRecordDao.delete(id);
    }

    public GameheadRecord getOne(String id) {
        return gameheadRecordDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public GameheadRecord save(GameheadRecord gameheadRecord) {
        return gameheadRecordDao.save(gameheadRecord);
    }

    public Page<GameheadRecord> findBySearchParams(Map<String, Object> searchParams, PageBean pageBean) {
        List<Object> args = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder(" SELECT * FROM xj_gamehead_record c  ");

        if (isValidParam(searchParams, "EQ_contentName")) {
            sql.append(" Left join xj_content b on c.contentid = b.id ");
        }
        if (isValidParam(searchParams, "EQ_recordno")) {
            sql.append(" Left join xj_prescription_case d on c.patientid = d.id ");
        }
        if (isValidParam(searchParams, "EQ_uservrName")) {
            sql.append(" Left join xj_user_vr_room e on e.user_id = c.loginuserid ");
        }
        sql.append(" Where c.hidden=0 ");

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
            sql.append("AND c.loginuserid = ? ");
            args.add(searchParams.get("EQ_userId"));
        }
        if (isValidParam(searchParams, "EQ_patientcaseId")) {
            sql.append("AND c.patientid = ? ");
            args.add(searchParams.get("EQ_patientcaseId"));
        }
        if (isValidParam(searchParams, "EQ_hospitalId")) {
            sql.append("AND c.hospital_id = ? ");
            args.add(searchParams.get("EQ_hospitalId"));
        }
        if (isValidParam(searchParams, "GET_beginDate")) {
            sql.append("AND c.clickdatatime >= ? ");
            args.add(searchParams.get("GET_beginDate"));
        }

        if (isValidParam(searchParams, "GET_endDate")) {
            sql.append("AND c.clickdatatime <= ? ");
            args.add(searchParams.get("GET_endDate"));
        }
        sql.append("order by c.created_at desc");
        PaginationHelper<GameheadRecord> helper = new PaginationHelper<>();
        return helper.fetchPage(jdbcTemplate, sql.toString(), args.toArray(), pageBean, (rs, i) -> {
            GameheadRecord clickRecord = new GameheadRecord();
            clickRecord.setId(rs.getString("id"));
            clickRecord.setLoginuserid(rs.getString("loginuserid"));
            clickRecord.setPatientid(rs.getString("patientid"));
            clickRecord.setGameid(rs.getString("gameid"));
            clickRecord.setContentid(rs.getString("contentid"));
            clickRecord.setVrroomid(rs.getString("vrroomid"));
            clickRecord.setHospitalId(rs.getString("hospital_id"));
            clickRecord.setGamedatatime(rs.getString("gamedatatime"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = format.parse(rs.getString("clickdatatime"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            clickRecord.setClickdatatime(date);
            clickRecord.setHidden(rs.getInt("hidden"));

            return clickRecord;
        });
    }
    private boolean isValidParam(Map<String, Object> searchParams, String key) {
        return searchParams.containsKey(key) && StringUtils.isNotBlank(searchParams.get(key).toString());
    }
}


