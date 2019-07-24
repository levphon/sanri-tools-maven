package com.sanri.frame;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 作者:sanri <br/>
 * 时间:2017-7-26下午5:04:05<br/>
 * 功能:当容器销毁时，释放全局变量  <br/>
 */
@Documented
@Retention (RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PreDestroy {
	
}