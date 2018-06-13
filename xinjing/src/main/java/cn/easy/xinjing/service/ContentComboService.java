package cn.easy.xinjing.service;

import cn.easy.base.bean.PageBean;
import cn.easy.base.service.BaseService;
import cn.easy.base.utils.ExtractUtil;
import cn.easy.xinjing.bean.ContentComboBean;
import cn.easy.xinjing.domain.Content;
import cn.easy.xinjing.domain.ContentCombo;
import cn.easy.xinjing.repository.ContentComboDao;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
public class ContentComboService extends BaseService<ContentCombo> {
    @Autowired
    private ContentComboDao	contentComboDao;
    @Autowired
    private ContentService contentService;

    public Page<ContentCombo> search(Map<String, Object> searchParams, PageBean pageBean) {
        return contentComboDao.findAll(spec(searchParams), pageBean.toPageRequest(new Sort(Direction.DESC, "createdAt")));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) {
        contentComboDao.delete(id);
    }

    public ContentCombo getOne(String id) {
        return contentComboDao.findOne(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ContentCombo save(ContentCombo contentCombo) {
        return contentComboDao.save(contentCombo);
    }

    /**
     * 获得关联套餐信息的优惠详情
     * @return
     */
    public ContentComboBean getContentComboBean(String id){
        List<ContentCombo> comboList = findByContentId(id);
        List<String> reContentIds = ExtractUtil.extractToList(comboList, "reContentId");

        Map<String, Object> searchParams = new HashedMap(){{
            put("IN_id", StringUtils.join(reContentIds,","));
        }};
        List<Content> reContentList = contentService.search(searchParams);
        List<String> reContentNames = ExtractUtil.extractToList(reContentList, "name");

        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal discountPrice = BigDecimal.ZERO;
        if(!comboList.isEmpty()){
            for(ContentCombo combo : comboList){
                totalPrice = totalPrice.add(combo.getUnitPrice());
                discountPrice = discountPrice.add(combo.getDiscountPrice());
            }
        }
        ContentComboBean comboBean = new ContentComboBean();
        comboBean.setContentId(id);
        comboBean.setReContentIds(searchParams.get("IN_id").toString());
        comboBean.setReContentNames(StringUtils.join(reContentNames, ","));
        comboBean.setTotalPrice(totalPrice);
        comboBean.setDiscountPrice(discountPrice);
        return comboBean;
    }

    /**
     * 保存套餐内容
     * @param contentComboBean
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(ContentComboBean contentComboBean) {
        //删除原有的关联内容
        List<ContentCombo> comboList = findByContentId(contentComboBean.getContentId());
        if(!comboList.isEmpty()){
           for(ContentCombo contentCombo : comboList){
               contentComboDao.delete(contentCombo.getId());
           }
        }
        String[] contentIdArray = contentComboBean.getReContentIds().split(",");
        for(String id : contentIdArray){
            ContentCombo combo = new ContentCombo();
            Content content = contentService.getOne(id);
            combo.setContentId(contentComboBean.getContentId());
            combo.setReContentId(id);
            combo.setUnitPrice(content.getPrice());
            combo.setDiscountPrice(content.getPrice().multiply(contentComboBean.getDiscountRate()));
            contentComboDao.save(combo);
        }
    }

    public List<ContentCombo> findByContentId(String contentId) {
        return contentComboDao.findByContentId(contentId);
    }
}


