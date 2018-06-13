package cn.easy.xinjing.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.easy.base.service.BaseService;
import cn.easy.base.utils.jdbc.PaginationHelper;
import cn.easy.xinjing.domain.*;
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
import cn.easy.xinjing.repository.FrontpageDao;

@Component
public class FrontpageService extends BaseService<Frontpage> {
    @Autowired
    private FrontpageDao	frontpageDao;
    @Autowired
    private FrontpageFtypeService frontpageFtypeService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public Page<Frontpage> search(Map<String, Object> searchParams, PageBean pageBean) {
        return frontpageDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }


    public List<Frontpage> findAll(Map<String, Object> searchParams) {
        return frontpageDao.findAll(spec(searchParams));
    }

    public Page<Frontpage> searchOn( PageBean pageBean) {
        return frontpageDao.findAll( pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<Frontpage> findByHiddenAndIdInOrderByReleaseTimeDesc(String[] id) {
        return frontpageDao.findByHiddenAndIdInOrderByReleaseTimeDesc(0,id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        frontpageDao.delete(id);
    }

    public Frontpage getOne(String id) {
        return frontpageDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Frontpage save(Frontpage frontpage) {
        return frontpageDao.save(frontpage);
    }


    /**
     * 保存头条及头条分类
     * @param frontpage
     * @param frontpageTypeId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Frontpage saveFrontpageAndType(Frontpage frontpage, String frontpageTypeId) {
        frontpage.setFrontpageTypeId(null);
        frontpage = frontpageDao.save(frontpage);
        //处理关联的头条分类
        if(StringUtils.isNotEmpty(frontpageTypeId)){
            List<FrontpageFtype> frontpageFtypeList = frontpageFtypeService.findByFrontpageId(frontpage.getId());
            for(FrontpageFtype frontpageFtype : frontpageFtypeList){
                frontpageFtypeService.delete(frontpageFtype.getId());
            }
            String[] frontpageFtypeidArray = frontpageTypeId.split(",");
            for(String id : frontpageFtypeidArray){
                FrontpageFtype frontpageFtype = new FrontpageFtype();
                frontpageFtype.setFrontpageId(frontpage.getId());
                frontpageFtype.setFrontpageTypeId(id);
                frontpageFtypeService.save(frontpageFtype);
            }
        }

        return frontpage;
    }

    public Page<Frontpage> findBySearchParams(Map<String, Object> searchParams, PageBean pageBean) {
        List<Object> args = new ArrayList<>();
        StringBuilder sql = new StringBuilder(" " +
                "SELECT a.* FROM xj_frontpage a WHERE a.status=2 AND a.hidden=0 " +
                "AND ((SELECT COUNT(*) FROM xj_frontpage_type c WHERE c.hidden=0 AND c.id IN (SELECT frontpage_type_id FROM xj_frontpage_ftype b WHERE b.frontpage_id=a.id ) " +
                "AND c.pid=(SELECT id FROM xj_frontpage_type WHERE hidden=0 AND NAME ='"+searchParams.get("nametype")+"'))) >=1 ");
        PaginationHelper<Frontpage> helper = new PaginationHelper<>();
        return helper.fetchPage(jdbcTemplate, sql.toString(), args.toArray(), pageBean, (rs, i) -> {
            Frontpage bean = new Frontpage();
            bean.setId(rs.getString("ID"));
            bean.setThemes(rs.getString("THEMES"));//主题
            bean.setAbstracts(rs.getString("ABSTRACTS"));//摘要
            bean.setAuthor(rs.getString("AUTHOR"));//作者
            bean.setCoverPic(rs.getString("COVER_PIC"));//缩略图
            bean.setLinkurl(rs.getString("LINKURL"));//链接
            bean.setReleaseTime(rs.getTimestamp("RELEASE_TIME"));
            bean.setRemark(rs.getString("REMARK"));
            bean.setStatus(rs.getInt("STATUS"));
            bean.setCreator(rs.getString("CREATOR"));
            bean.setCreatedAt(rs.getDate("CREATED_AT"));
            bean.setUpdator(rs.getString("UPDATOR"));
            bean.setUpdatedAt(rs.getDate("UPDATED_AT"));
            bean.setCoverPic(rs.getString("COVER_PIC"));
            return bean;
        });
    }


    public List<Frontpage> findByHiddenAndStatusAndFrontpageTypeIdIn(Integer hidden,Integer status,String[] FrontpageTypeId) {
        return frontpageDao.findByHiddenAndStatusAndFrontpageTypeIdIn(hidden,status,FrontpageTypeId);
    }


}


