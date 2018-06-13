package cn.easy.xinjing.aop;

import java.lang.annotation.*;

/**
 * Created by chenzhongyi on 2016/10/10.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiAuth {
}
