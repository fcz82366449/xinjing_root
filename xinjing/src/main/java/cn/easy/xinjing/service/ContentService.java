package cn.easy.xinjing.service;

import cn.easy.base.bean.PageBean;
import cn.easy.base.service.BaseService;
import cn.easy.base.utils.jdbc.PaginationHelper;
import cn.easy.xinjing.bean.api.ContentExtBean;
import cn.easy.xinjing.domain.*;
import cn.easy.xinjing.repository.ContentDao;
import cn.easy.xinjing.utils.Constants;
import com.aliyun.oss.model.OSSObject;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ContentService extends BaseService<Content> {
    @Autowired
    private ContentDao contentDao;
    @Autowired
    private ContentVideoService contentVideoService;
    @Autowired
    private ContentAudioService contentAudioService;
    @Autowired
    private ContentPicService contentPicService;
    @Autowired
    private ContentArticleService contentArticleService;
    @Autowired
    private ContentGameService contentGameService;
    @Autowired
    private ContentOutsideGameService contentOutsideGameService;
    @Autowired
    private OssMtsService ossMtsService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TherapyContentService therapyContentService;
    @Autowired
    private DiseaseContentService diseaseContentService;

    public Page<Content> search(Map<String, Object> searchParams, PageBean pageBean) {
        return contentDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    public List<Content> search(Map<String, Object> searchParams) {
        return contentDao.findAll(spec(searchParams));
    }

    private boolean isValidParam(Map<String, Object> searchParams, String key) {
        return searchParams.containsKey(key) && StringUtils.isNotBlank(searchParams.get(key).toString());
    }

    /**
     * 根据searchParams关联查询content信息
     * 关联表：xj_content，xj_disease_content，xj_therapy_content
     *
     * @param searchParams
     * @param pageBean
     * @return
     */
    public Page<Content> findBySearchParams(Map<String, Object> searchParams, PageBean pageBean) {
        List<Object> args = new ArrayList<>();
        StringBuilder sql = new StringBuilder("select c.* from xj_content c  ");

        if (isValidParam(searchParams, "EQ_diseaseId")) {
            sql.append("LEFT JOIN xj_disease_content dc on c.id = dc.content_id ");
        }

        if (isValidParam(searchParams, "EQ_therapyId")) {
            sql.append("LEFT JOIN xj_therapy_content tc on c.id = tc.content_id ");
        }

        sql.append("where hidden = 0 AND STATUS=2 ");

        if (isValidParam(searchParams, "EQ_diseaseId")) {
            sql.append("AND dc.disease_id=? ");
            args.add(searchParams.get("EQ_diseaseId"));
        }

        if (isValidParam(searchParams, "EQ_therapyId")) {
            sql.append("AND tc.therapy_id=? ");
            args.add(searchParams.get("EQ_therapyId"));
        }

        if (isValidParam(searchParams, "EQ_type")) {
            sql.append("AND c.type=? ");
            args.add(searchParams.get("EQ_type"));
        }
        if (isValidParam(searchParams, "IN_type")) {
            sql.append("AND c.type IN (" + StringUtils.join((Integer[]) searchParams.get("IN_type"), ",") + ") ");
        }

        if (isValidParam(searchParams, "LIKE_name")) {
            sql.append("AND c.name like ? ");
            args.add("%" + searchParams.get("LIKE_name") + "%");
        }

        if (isValidParam(searchParams, "GTE_updatedAt")) {
            sql.append("AND c.updated_at >= ? ");
            args.add(searchParams.get("GTE_updatedAt"));
        }
        if (isValidParam(searchParams, "GTE_videoupdateAt")) {
            sql.append("AND c.videoupdate_at >= ? ");
            args.add(searchParams.get("GTE_videoupdateAt"));
        }


        if (isValidParam(searchParams, "SORT_name")) {
            sql.append("order by c." + searchParams.get("SORT_name") + " " + searchParams.get("SORT_order"));
        } else {
            sql.append("order by c.created_at desc");
        }

        PaginationHelper<Content> helper = new PaginationHelper<>();


        return helper.fetchPage(jdbcTemplate, sql.toString(), args.toArray(), pageBean, (rs, i) -> {
            Content bean = new Content();
            bean.setId(rs.getString("ID"));
            bean.setHelpCode(rs.getString("HELP_CODE"));
            bean.setName(rs.getString("NAME"));
            bean.setType(rs.getInt("TYPE"));
            bean.setIsFree(rs.getInt("IS_FREE"));
            bean.setPrice(rs.getBigDecimal("PRICE"));
            bean.setRemark(rs.getString("REMARK"));
            bean.setStatus(rs.getInt("STATUS"));
            bean.setCreator(rs.getString("CREATOR"));
            bean.setCreatedAt(rs.getDate("CREATED_AT"));
            bean.setUpdator(rs.getString("UPDATOR"));
            bean.setUpdatedAt(rs.getDate("UPDATED_AT"));
            bean.setCoverPic(rs.getString("COVER_PIC"));
            bean.setClicks(rs.getInt("CLICKS"));
            bean.setDuration(rs.getInt("DURATION"));
            return bean;
        });
    }

    /**
     * 根据searchParams关联查询content信息[病案号版本（带分页）]
     * 关联表：xj_content，xj_disease_content，xj_therapy_content
     *
     * @param searchParams
     * @param pageBean
     * @return
     */
    public Page<Content> findBySearchParamsto(Map<String, Object> searchParams, PageBean pageBean) {
        List<Object> args = new ArrayList<>();
        StringBuilder sql = new StringBuilder("select c.* from xj_content c  ");

        if (isValidParam(searchParams, "EQ_diseaseId")) {
            sql.append("LEFT JOIN xj_disease_content dc on c.id = dc.content_id ");
        }

        if (isValidParam(searchParams, "EQ_therapyId")) {
            sql.append("LEFT JOIN xj_therapy_content tc on c.id = tc.content_id ");
        }

        sql.append("where hidden = 0 AND STATUS=2 ");

        if (isValidParam(searchParams, "EQ_diseaseId")) {
            sql.append("AND (dc.disease_id=? OR dc.disease_id IS NULL) ");
            args.add(searchParams.get("EQ_diseaseId"));
        }

        if (isValidParam(searchParams, "EQ_therapyId")) {
            sql.append("AND tc.therapy_id=? ");
            args.add(searchParams.get("EQ_therapyId"));
        }

        if (isValidParam(searchParams, "EQ_type")) {
            sql.append("AND c.type=? ");
            args.add(searchParams.get("EQ_type"));
        }
        if (isValidParam(searchParams, "IN_type")) {
            sql.append("AND c.type IN (" + StringUtils.join((Integer[]) searchParams.get("IN_type"), ",") + ") ");
        }

        if (isValidParam(searchParams, "LIKE_name")) {
            sql.append("AND c.name like ? ");
            args.add("%" + searchParams.get("LIKE_name") + "%");
        }

        if (isValidParam(searchParams, "GTE_updatedAt")) {
            sql.append("AND c.updated_at >= ? ");
            args.add(searchParams.get("GTE_updatedAt"));
        }
        if (isValidParam(searchParams, "GTE_videoupdateAt")) {
            sql.append("AND c.videoupdate_at >= ? ");
            args.add(searchParams.get("GTE_videoupdateAt"));
        }


        if (isValidParam(searchParams, "SORT_name")) {
            sql.append("order by c." + searchParams.get("SORT_name") + " " + searchParams.get("SORT_order"));
        } else {
            sql.append("order by c.created_at desc");
        }

        PaginationHelper<Content> helper = new PaginationHelper<>();

        return helper.fetchPage(jdbcTemplate, sql.toString(), args.toArray(), pageBean, (rs, i) -> {
            Content bean = new Content();
            bean.setId(rs.getString("ID"));
            bean.setHelpCode(rs.getString("HELP_CODE"));
            bean.setName(rs.getString("NAME"));
            bean.setType(rs.getInt("TYPE"));
            bean.setIsFree(rs.getInt("IS_FREE"));
            bean.setPrice(rs.getBigDecimal("PRICE"));
            bean.setRemark(rs.getString("REMARK"));
            bean.setStatus(rs.getInt("STATUS"));
            bean.setCreator(rs.getString("CREATOR"));
            bean.setCreatedAt(rs.getDate("CREATED_AT"));
            bean.setUpdator(rs.getString("UPDATOR"));
            bean.setUpdatedAt(rs.getDate("UPDATED_AT"));
            bean.setCoverPic(rs.getString("COVER_PIC"));
            bean.setClicks(rs.getInt("CLICKS"));
            bean.setDuration(rs.getInt("DURATION"));
            return bean;
        });
    }



    /**
     * 根据searchParams关联查询content信息[病案号版本（不带分页）]
     * 关联表：xj_content，xj_disease_content，xj_therapy_content
     *
     * @param searchParams
     * @return
     */
    public  List<Content> findBySearchParamsCase(Map<String, Object> searchParams) {
        List<Object> args = new ArrayList<>();
        StringBuilder sql = new StringBuilder("select c.* from xj_content c  ");

        if (isValidParam(searchParams, "EQ_diseaseId")) {
            sql.append("LEFT JOIN xj_disease_content dc on c.id = dc.content_id ");
        }

        if (isValidParam(searchParams, "EQ_therapyId")) {
            sql.append("LEFT JOIN xj_therapy_content tc on c.id = tc.content_id ");
        }

        sql.append("where hidden = 0 AND STATUS=2 ");

        if (isValidParam(searchParams, "EQ_diseaseId")) {
            sql.append("AND (dc.disease_id=? OR dc.disease_id IS NULL) ");
            args.add(searchParams.get("EQ_diseaseId"));
        }

        if (isValidParam(searchParams, "EQ_therapyId")) {
            sql.append("AND tc.therapy_id=? ");
            args.add(searchParams.get("EQ_therapyId"));
        }

        if (isValidParam(searchParams, "EQ_type")) {
            sql.append("AND c.type=? ");
            args.add(searchParams.get("EQ_type"));
        }
        if (isValidParam(searchParams, "IN_type")) {
            sql.append("AND c.type IN (" + StringUtils.join((Integer[]) searchParams.get("IN_type"), ",") + ") ");
        }

        if (isValidParam(searchParams, "LIKE_name")) {
            sql.append("AND c.name like ? ");
            args.add("%" + searchParams.get("LIKE_name") + "%");
        }

        if (isValidParam(searchParams, "GTE_updatedAt")) {
            sql.append("AND c.updated_at >= ? ");
            args.add(searchParams.get("GTE_updatedAt"));
        }
        if (isValidParam(searchParams, "GTE_videoupdateAt")) {
            sql.append("AND c.videoupdate_at >= ? ");
            args.add(searchParams.get("GTE_videoupdateAt"));
        }


        if (isValidParam(searchParams, "SORT_name")) {
            sql.append("order by c." + searchParams.get("SORT_name") + " " + searchParams.get("SORT_order"));
        } else {
            sql.append("order by c.created_at desc");
        }

        PaginationHelper<Content> helper = new PaginationHelper<>();
        RowMapper<Content> rowMapper = new BeanPropertyRowMapper<Content>(Content.class);
        List<Content> contentList = jdbcTemplate.query(sql.toString(),args.toArray(),rowMapper);

        return contentList;
    }



    /**
     * 根据searchParams关联查询content信息
     * 关联表：xj_content，xj_disease_content，xj_therapy_content
     *
     * @param searchParams
     * @param pageBean
     * @return
     */
    public Page<Content> findBySearchParamsH5(Map<String, Object> searchParams, PageBean pageBean) {
        List<Object> args = new ArrayList<>();
        StringBuilder sql = new StringBuilder(" SELECT A.* FROM XJ_CONTENT A WHERE A.HIDDEN=0 AND A.status=2 ");
        if (isValidParam(searchParams, "EQ_diseaseId")) {
            sql.append(" AND A.`id` IN(select B.`content_id`  from xj_disease_content B WHERE B.`disease_id` IN "+searchParams.get("EQ_diseaseId")+") ");
        }
        if (isValidParam(searchParams, "EQ_therapyId")) {
            sql.append(" and A.`id` IN(select C.`content_id`  from xj_therapy_content C WHERE C.`therapy_id` IN "+searchParams.get("EQ_therapyId")+") ");
        }

        PaginationHelper<Content> helper = new PaginationHelper<>();

        return helper.fetchPage(jdbcTemplate, sql.toString(), args.toArray(), pageBean, (rs, i) -> {
            Content bean = new Content();
            bean.setId(rs.getString("ID"));
            bean.setName(rs.getString("NAME"));
            bean.setPrice(rs.getBigDecimal("PRICE"));
            bean.setRemark(rs.getString("REMARK"));
            bean.setCoverPic(rs.getString("COVER_PIC"));
            bean.setClicks(rs.getInt("CLICKS"));
            bean.setDuration(rs.getInt("DURATION"));
            return bean;
        });
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        contentDao.delete(id);
    }

    public Content getOne(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return contentDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Content save(Content content) {
        return contentDao.save(content);
    }

    /**
     * 保存内容及关联的疗法,病种
     * @param content
     * @param therapyIds
     * @param diseaseIds
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Content saveContentAndTherapyAndDisease(Content content,String therapyIds,String diseaseIds) {
        content =  contentDao.save(content);
        //处理关联的疗法
        if(StringUtils.isNotEmpty(therapyIds)){
            List<TherapyContent> therapyContentList = therapyContentService.findByContentId(content.getId());
            for(TherapyContent therapyContent : therapyContentList){
                therapyContentService.delete(therapyContent.getId());
            }
            String[] therapyIdArray = therapyIds.split(",");
            for(String id : therapyIdArray){
                TherapyContent therapyContent = new TherapyContent();
                therapyContent.setContentId(content.getId());
                therapyContent.setTherapyId(id);
                therapyContentService.save(therapyContent);
            }
        }
        //处理关联的病种
        if(StringUtils.isNotEmpty(diseaseIds)){
            List<DiseaseContent> diseaseContentList = diseaseContentService.findByContentId(content.getId());
            for(DiseaseContent diseaseContent : diseaseContentList){
                diseaseContentService.delete(diseaseContent.getId());
            }
            String[] diseaseIdArray = diseaseIds.split(",");
            for(String id : diseaseIdArray){
                DiseaseContent diseaseContent = new DiseaseContent();
                diseaseContent.setContentId(content.getId());
                diseaseContent.setDiseaseId(id);
                diseaseContentService.save(diseaseContent);
            }
        }else{
            List<DiseaseContent> diseaseContentList = diseaseContentService.findByContentId(content.getId());
            for(DiseaseContent diseaseContent : diseaseContentList){
                diseaseContentService.delete(diseaseContent.getId());
            }
        }
        return content;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void publish(Content content) {
        content.setStatus(Constants.CONTENT_STATUS_PUBLISH);
        contentDao.save(content);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void unPublish(Content content) {
        content.setStatus(Constants.CONTENT_STATUS_NO_PUBLISH);
        contentDao.save(content);
    }

    public ContentExtBean getExt(String contentId, int type) {
        ContentExtBean extbean = new ContentExtBean();

        if (type == Constants.CONTENT_TYPE_VIDEO) {
            ContentVideo contentVideo = contentVideoService.findByContentId(contentId);
            extbean.setContent(ossMtsService.getVodAccessUrl(contentVideo.getContent()));
            extbean.setId(contentVideo.getId());
            extbean.setVideosize(ossMtsService.getVodAccessSize(contentVideo.getContent()));
            return extbean;
        } else if (type == Constants.CONTENT_TYPE_AUDIO) {
            ContentAudio contentAudio = contentAudioService.findByContentId(contentId);
            extbean.setContent(ossMtsService.getVodAccessUrl(contentAudio.getContent()));
            extbean.setId(contentAudio.getId());
            return extbean;
        } else if (type == Constants.CONTENT_TYPE_PIC) {
            ContentPic contentPic = contentPicService.findByContentId(contentId);
            StringBuffer contentPicUrls = new StringBuffer("");
            if(contentPic != null) {
                String[] contentPics = contentPic.getContent().split(",");
                for (String pic : contentPics) {
                    contentPicUrls.append(ossMtsService.getImgAccessUrl(pic)).append(",");
                }
                if(contentPicUrls.length() > 1){
                    contentPic.setContent(contentPicUrls.toString().substring(0, contentPicUrls.toString().length() - 1));
                }else{
                    contentPic.setContent(contentPicUrls.toString());
                }
            }
            extbean.setContent(contentPic.getContent());
            extbean.setId(contentPic.getId());
            return extbean;

        } else if (type == Constants.CONTENT_TYPE_ARTICLE) {
            ContentArticle contentArticle = contentArticleService.findByContentId(contentId);
            extbean.setContent(contentArticle.getContent());
            extbean.setId(contentArticle.getId());
            return extbean;
        } else if (type == Constants.CONTENT_TYPE_GAME) {
            ContentGame contentGame = contentGameService.findByContentId(contentId);
            extbean.setContent(ossMtsService.getVodAccessUrl(contentGame.getContent()));
            extbean.setId(contentGame.getId());
            return extbean;
        } else if (type == Constants.CONTENT_TYPE_OUTSIDE_GAME) {
            ContentOutsideGame contentOutsideGame = contentOutsideGameService.findByContentId(contentId);
            extbean.setContent(contentOutsideGame.getContent());
            extbean.setId(contentOutsideGame.getId());
            return extbean;
        }
        return null;
    }

    public List<Content> findAll(List<String> idList) {
        return Lists.newArrayList(contentDao.findAll(idList));
    }
}


