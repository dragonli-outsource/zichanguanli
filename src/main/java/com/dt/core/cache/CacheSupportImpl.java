package com.dt.core.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.util.MethodInvoker;
import com.dt.core.tool.util.ToolUtil;
import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 手动刷新缓存实现类
 */
@Component
public class CacheSupportImpl implements CacheSupport, InvocationRegistry {

	private static final Logger logger = LoggerFactory.getLogger(CacheSupportImpl.class);

	/**
	 * 记录容器与所有执行方法信息
	 */
	public static Map<String, ConcurrentHashMap<String, CachedInvocation>> cacheInvocationsMap;

	@Autowired
	private CacheManager cacheManager;

	private void refreshCache(CachedInvocation invocation, String cacheName) {

		boolean invocationSuccess;
		Object computed = null;
		try {
			computed = invoke(invocation);
			invocationSuccess = true;
		} catch (Exception ex) {
			invocationSuccess = false;
		}
		if (invocationSuccess) {
			if (cacheInvocationsMap.get(cacheName) != null) {
				cacheManager.getCache(cacheName).put(invocation.getKey(), computed);

			}
		}
	}

	private Object invoke(CachedInvocation invocation)
			throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		final MethodInvoker invoker = new MethodInvoker();
		invoker.setTargetObject(invocation.getTargetBean());
		invoker.setArguments(invocation.getArguments());
		invoker.setTargetMethod(invocation.getTargetMethod().getName());
		invoker.prepare();
		logger.info("Action invoke key:" + invocation.getKey());
		return invoker.invoke();
	}

	@PostConstruct
	public void initialize() {

		cacheInvocationsMap = new ConcurrentHashMap<String, ConcurrentHashMap<String, CachedInvocation>>(10);
		for (final String cacheName : cacheManager.getCacheNames()) {
			cacheInvocationsMap.put(cacheName, new ConcurrentHashMap<String, CachedInvocation>());

		}
	}

	@Override
	public void registerInvocation(CachedInvocation invocation) {
		String key = invocation.getcacheableEntity().getKey();
		String realCacheName = invocation.getcacheableEntity().getValue();

		if (!cacheInvocationsMap.containsKey(realCacheName)) {
			this.initialize();
		}
		logger.info("保存执行信息,realCacheName:" + realCacheName + ",key:" + key);
		cacheInvocationsMap.get(realCacheName).put(key, invocation);

	}

	@Override
	public void removeCacheByKey(String cacheName, String cachekey) {
		logger.info("removeCacheByKey:" + cacheName + ",cacheKey:" + cachekey);
		if (cacheInvocationsMap.get(cacheName) != null && ToolUtil.isNotEmpty(cachekey)) {
			cacheInvocationsMap.get(cacheName).remove(cachekey);
		} else {
			logger.info("Cache name:" + cacheName + " not exists");
		}
	}

	@Override
	public void refreshCache(String cacheName) {
		logger.info("refreshCache" + cacheName);
		this.refreshCacheByKey(cacheName, null);
	}

	@Override
	public void refreshCacheByKey(String cacheName, String cacheKey) {
		logger.info("refreshCacheName:" + cacheName + ",cacheKey:" + cacheKey);
		if (cacheInvocationsMap.get(cacheName) != null && ToolUtil.isNotEmpty(cacheKey)) {
			refreshCache(cacheInvocationsMap.get(cacheName).get(cacheKey), cacheName);
		} else {
			logger.info("Cache name:" + cacheName + " not exists");
		}
	}

}