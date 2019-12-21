package com.dt.core.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dt.core.annotion.Acl;
import com.dt.core.common.base.BaseConstants;
import com.dt.core.dao.sql.Insert;
import com.dt.core.shiro.ShiroKit;
import com.dt.core.tool.util.DbUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.module.db.DB;

/**
 * @author: jinjie
 * @date: 2018年4月2日 下午1:58:13
 * @Description: TODO
 */
@Aspect
@Component
@PropertySource(value = "classpath:application.properties")
public class AclAop {

	@Value("${app.recdb}")
	private String apprecdb;

	private static Logger _log = LoggerFactory.getLogger(AclAop.class);

	@Autowired
	public DB db = null;

	private <T extends Annotation> List<T> getMethodAnnotations(AnnotatedElement ae, Class<T> annotationType) {
		List<T> anns = new ArrayList<T>(2);
		// look for raw annotation
		T ann = ae.getAnnotation(annotationType);
		if (ann != null) {
			anns.add(ann);
		}
		// look for meta-annotations
		for (Annotation metaAnn : ae.getAnnotations()) {
			ann = metaAnn.annotationType().getAnnotation(annotationType);
			if (ann != null) {
				anns.add(ann);
			}
		}
		return (anns.isEmpty() ? null : anns);
	}

	private Method getSpecificmethod(ProceedingJoinPoint pjp) {
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
		Method method = methodSignature.getMethod();
		// The method may be on an interface, but we need attributes from the
		// target class. If the target class is null, the method will be
		// unchanged.
		Class<?> targetClass = AopProxyUtils.ultimateTargetClass(pjp.getTarget());
		if (targetClass == null && pjp.getTarget() != null) {
			targetClass = pjp.getTarget().getClass();
		}
		Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
		// If we are dealing with method with generic parameters, find the
		// original method.
		specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
		return specificMethod;
	}

	@Pointcut("@annotation(com.dt.core.annotion.Acl)")
	public void pointcut() {

	}

	public static String req2RawString(HttpServletRequest request) {
		String res = "";
		try {
			Map<String, String[]> map = request.getParameterMap();
			if (map != null) {
				for (String key : map.keySet()) {
					String values = "";
					for (int i = 0; i < map.get(key).length; i++) {
						values = map.get(key)[i] + "";
					}
					res = res + key + ":" + values + ",";
				}
			}
		}catch (Exception e){
			_log.info("record url post data failed");
		}
		return res;

	}

	@Around("pointcut()")
	public Object recAccessLog(ProceedingJoinPoint joinPoint) throws Throwable {

		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		HttpServletRequest request = sra.getRequest();
		Boolean is_auth = false;
		String user_id = "unknow";
		Boolean is_remember = false;
		if ("true".equals(BaseConstants.shiroenable) && ToolUtil.isNotEmpty(ShiroKit.getUser())) {
			user_id = ShiroKit.getUser().getId();
			is_auth = ShiroKit.isAuthenticated();
			is_remember = ShiroKit.isRemember();
		}
		String info = "";
		String url = request.getRequestURI().toString();
		String method_type = request.getMethod();

		String ip = HttpKit.getIpAddr(request);
		String aclpri = "";
		Method method = this.getSpecificmethod(joinPoint);
		List<Acl> annotations = this.getMethodAnnotations(method, Acl.class);
		for (Acl acls : annotations) {
			info = acls.info();
			aclpri = acls.value();
		}
		if (ToolUtil.isNotEmpty(apprecdb) && "true".equals(apprecdb.toLowerCase())) {
			if (!url.endsWith("checkLogin.do")) {
				Insert ins = new Insert("sys_log_access");
				ins.set("id", db.getUUID());
				ins.setIf("user_id", user_id);
				ins.setIf("ip", ip);
				ins.setIf("info", info);
				ins.setIf("url", url);
				ins.setIf("method_type", method_type);
				ins.setSE("rtime", DbUtil.getDbDateString(db.getDBType()));

				if ("POST".equals(method_type)) {
					String str = req2RawString(request);
					if (str.length() > 3500) {
						ins.setIf("postorget", str.substring(0, 3500));
					} else {
						ins.setIf("postorget", str);
					}
				} else {
					ins.setIf("postorget", request.getQueryString());
				}

				try {
					db.execute(ins);
				} catch (Exception e) {
					_log.info("Can't insert access log." + url);
				}
			}
		}

		_log.info("userId:" + user_id + ",url:" + url + ",isAuth=" + is_auth + ",isRemember:" + is_remember + ",aclpri:"
				+ aclpri);
		return joinPoint.proceed();

	}
}
