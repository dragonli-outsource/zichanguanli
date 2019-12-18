package com.dt.module.base.service.impl;

import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.dt.core.cache.CacheSupportImpl;
import com.dt.core.cache.CacheableEntity;
import com.dt.core.cache.CachedInvocation;
import com.dt.core.cache.CustomizedEhCacheCache;
import com.dt.core.cache.ThreadTaskHelper;
import com.dt.core.common.base.R;
import com.dt.core.tool.lang.SpringContextUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.core.tool.util.support.DateTimeKit;

import net.sf.ehcache.Element;
import net.sf.json.JSONObject;

/**
 * @author: algernonking
 * @date: 2018年3月30日 下午6:06:12
 * @Description: TODO
 */
@Service
public class CacheService {

	@Autowired
	private CacheSupportImpl cacheSupportImpl;

	@Autowired
	private CacheManager cacheManager;

	public static CacheService me() {
		return SpringContextUtil.getBean(CacheService.class);
	}

	private static Logger _log = LoggerFactory.getLogger(CacheService.class);

	public CacheManager initCacheManager() {
		try {
			if (cacheManager == null) {

			}
			// cacheManager.
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cacheManager;
	}

	public R refreshCaches() {
		Collection<String> col = initCacheManager().getCacheNames();
		for (String cache : col) {
			if (cache.indexOf("#") == -1) {
				refreshCache(cache);
			}
		}
		return R.SUCCESS_OPER();
	}

	public R refreshCache(String cache) {
		if (ToolUtil.isEmpty(cache)) {
			return R.FAILURE_NO_DATA();
		}

		CustomizedEhCacheCache c = ((CustomizedEhCacheCache) (initCacheManager().getCache(cache)));
		for (int i = 0; i < c.getAllKeys().size(); i++) {
			// 捕捉瞬间key失效报错问题
			try {
				// 判断是否需要刷新
				String key = c.getAllKeys().get(i).toString();
				Element el = c.getKey(key);
				long hit = el.getHitCount();
				Long expired = (el.getExpirationTime() - System.currentTimeMillis()) / 1000;
				CachedInvocation inv = CacheSupportImpl.cacheInvocationsMap.get(cache).get(key);
				if (inv == null) {
					continue;
				}
				CacheableEntity ce = inv.getcacheableEntity();
				int refreshtime = ce.getRefreshtime();
				// 主动刷新时间太低,并且命中率不高,则不去主动刷新
				if (refreshtime < 600 && hit < 5) {
					_log.info("too low to refresh,too low to hit.cache:" + cache + ",key:" + key + ",refreshtime:"
							+ refreshtime + ",hit:" + hit);
					continue;
				}
				if (refreshtime > 0 && expired != null && expired > 0 && expired <= refreshtime) {
					ThreadTaskHelper.run(new Runnable() {
						@Override
						public void run() {
							_log.info("refresh by CacheService");
							cacheSupportImpl.refreshCacheByKey(cache, key);
						}
					});
				}
			} catch (Exception e) {
				_log.info(e.getMessage());
			}
		}
		return R.SUCCESS_OPER();
	}

	public R removeCacheKey(String cache, String key) {
		if (ToolUtil.isOneEmpty(cache, key)) {
			return R.FAILURE_NO_DATA();
		}
		initCacheManager().getCache(cache).evict(key);
		return R.SUCCESS_OPER();
	}

	public static String API_CACHE = "public";

	public R putCacheKeyForApi(String key, String ct, long timeout) {
		if (ToolUtil.isOneEmpty(key, ct)) {
			return R.FAILURE_NO_DATA();
		}
		CustomizedEhCacheCache c = ((CustomizedEhCacheCache) (initCacheManager().getCache(API_CACHE)));
		Element e = new Element(key, ct);
		e.setTimeToIdle((int) timeout);
		e.setTimeToLive((int) timeout);
		c.put(e);

		return R.SUCCESS_OPER();
	}

	public String queryCacheKeyForApi(String key) {
		if (ToolUtil.isOneEmpty(key)) {
			return null;
		}
		//CustomizedEhCacheCache c = ((CustomizedEhCacheCache) (initCacheManager().getCache(API_CACHE)));

		String value = null;
		// System.out.println(c.g);

		return value;

	}

	public R queryCustomizedEhCacheCacheManagerCacheKeys(String cache) {
		if (ToolUtil.isEmpty(cache)) {
			return R.FAILURE_NO_DATA();
		}
		JSONArray res = new JSONArray();
		CustomizedEhCacheCache c = ((CustomizedEhCacheCache) (initCacheManager().getCache(cache)));
		for (int i = 0; i < c.getAllKeys().size(); i++) {
			// 捕捉瞬间key失效报错问题
			try {
				Element el = c.getKey(c.getAllKeys().get(i).toString());
				if (!ToolUtil.isEmpty(el)) {
					JSONObject e = new JSONObject();
					e.put("cache", cache);
					e.put("key", c.getAllKeys().get(i));
					e.put("hit", el.getHitCount());
					e.put("ctime",
							DateTimeKit.format(new Date(el.getCreationTime()), DateTimeKit.NORM_DATETIME_PATTERN));
					e.put("accesstime",
							DateTimeKit.format(new Date(el.getLastAccessTime()), DateTimeKit.NORM_DATETIME_PATTERN));
					e.put("expiretime",
							DateTimeKit.format(new Date(el.getExpirationTime()), DateTimeKit.NORM_DATETIME_PATTERN));
					e.put("ttl", el.getTimeToLive());
					e.put("tti", el.getTimeToIdle());
					res.add(e);
				}
			} catch (Exception e) {
				_log.info(e.getMessage());
			}
		}
		return R.SUCCESS_OPER(res);
	}

	public R queryCacheCacheManagerCaches() {

		JSONArray res = new JSONArray();
		Collection<String> col = initCacheManager().getCacheNames();
		for (String cache : col) {
			if (cache.indexOf("#") == -1) {
				JSONObject e = new JSONObject();
				e.put("id", cache);
				e.put("name", cache);
				res.add(e);
			}
		}
		return R.SUCCESS_OPER(res);
	}

}
