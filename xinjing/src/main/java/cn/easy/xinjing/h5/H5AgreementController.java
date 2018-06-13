package cn.easy.xinjing.h5;

import cn.easy.base.bean.PageBean;
import cn.easy.base.domain.Article;
import cn.easy.base.domain.Category;
import cn.easy.base.service.ArticleService;
import cn.easy.base.service.CategoryService;
import cn.easy.xinjing.domain.Hospital;
import cn.easy.xinjing.service.HospitalService;
import cn.easy.xinjing.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenzhongyi on 2017/5/8.
 */
@Controller
@RequestMapping("/h5/agreement")
public class H5AgreementController extends H5BaseController{

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleService articleService;




    @RequestMapping("/info")
    String info(String id, Model model, HttpServletRequest request) {
        Category category = categoryService.getByCode("01");
        Article article = new Article();
        if(category!=null){
            Map<String, Object> searchParams = new HashMap<>();
            searchParams.put("EQ_categoryId",category.getId());
            PageBean pageBean = new PageBean();
            pageBean.toPageRequest(new Sort(Sort.Direction.DESC, new String[]{"createdAt"}));
            Page<Article> articlePage = articleService.search(searchParams,pageBean);
            if( articlePage!=null&&articlePage.getContent().size()>=1){
                article = articlePage.getContent().get(0);
            }
        }
        model.addAttribute("article", article);
        return "h5/agreement/info";
    }
}
