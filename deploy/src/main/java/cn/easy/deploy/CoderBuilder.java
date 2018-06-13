package cn.easy.deploy;

import cn.easy.base.utils.coder.Builder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by 000404 on 2015/10/9.
 */
public class CoderBuilder {


    public static void main(String[] args) throws Exception{
        buildCode(args);
    }

    public static void buildCode(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

//        new Builder("xj_user_vr_room", "xinjing", "cn.easy.xinjing", ctx.getBean(JdbcTemplate.class)).run();
//        new Builder("xj_content_combo", "xinjing", "cn.easy.xinjing", ctx.getBean(JdbcTemplate.class)).buildService().buildDomain().buildDao();
//        new Builder("xj_section_office", "xinjing", "cn.easy.xinjing", ctx.getBean(JdbcTemplate.class)).run();
//        new Builder("xj_section_office_disease", "xinjing", "cn.easy.xinjing", ctx.getBean(JdbcTemplate.class)).buildService().buildDomain().buildDao();
//        new Builder("xj_capital_flow", "xinjing", "cn.easy.xinjing", ctx.getBean(JdbcTemplate.class)).run();
//        new Builder("xj_doctor_patientcase", "xinjing", "cn.easy.xinjing", ctx.getBean(JdbcTemplate.class)).run();
        new Builder("xj_pc_version", "xinjing", "cn.easy.xinjing", ctx.getBean(JdbcTemplate.class)).run();

        SpringApplication.exit(ctx);
    }
}
