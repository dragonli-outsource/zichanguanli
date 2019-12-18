package com.dt.core.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: algernonking
 * @date: 2017年8月6日 下午3:22:48
 * @Description: Acl必须要设置,否则不能访问,allow:无需认证就可访问，common:登录后就可以访问,deny:不可以访问,
 *               需要赋权才能访问
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Acl {
	// 无权限限制
	public static String ACL_ALLOW = "allow";
	// 登录后就可以访问
	public static String ACL_USER = "user";
	// 登录后并且需要赋权才能访问
	public static String ACL_DENY = "deny";
	// 未使用
	public static String TYPE_API = "api";
	// 页面
	public static String TYPE_VIEW = "view";

	String type() default TYPE_API;

	String value() default ACL_DENY;

	String info() default "";

}
