package com.dt.core.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerUtils;
import com.dt.core.tool.util.ToolUtil;

public class CustomizedEhCacheCacheManager extends EhCacheCacheManager {

	private static final Logger logger = LoggerFactory.getLogger(CustomizedEhCacheCacheManager.class);

	private String separator = "#";

	public static ArrayList<String> cachenames = new ArrayList<String>();

	/**
	 * Create a new EhCacheCacheManager, setting the target EhCache CacheManager
	 * through the {@link #setCacheManager} bean property.
	 */
	public CustomizedEhCacheCacheManager() {
	}

	/**
	 * Create a new EhCacheCacheManager for the given backing EhCache CacheManager.
	 * 
	 * @param cacheManager
	 *            the backing EhCache {@link net.sf.ehcache.CacheManager}
	 */

	/**
	 * Set the backing EhCache {@link net.sf.ehcache.CacheManager}.
	 */

	/**
	 * Return the backing EhCache {@link net.sf.ehcache.CacheManager}.
	 */

	@Override
	public void afterPropertiesSet() {
		if (getCacheManager() == null) {
			setCacheManager(EhCacheManagerUtils.buildCacheManager());
		}
		super.afterPropertiesSet();

	}

	@Override
	protected Collection<Cache> loadCaches() {
		logger.info("loadCaches");
		Status status = getCacheManager().getStatus();
		if (!Status.STATUS_ALIVE.equals(status)) {
			throw new IllegalStateException(
					"An 'alive' EhCache CacheManager is required - current cache is " + status.toString());
		}

		String[] names = getCacheManager().getCacheNames();
		Collection<Cache> caches = new LinkedHashSet<Cache>(names.length);
		for (String name : names) {
			logger.info("name:" + name);
			cachenames.add(name);
			caches.add(new CustomizedEhCacheCache(getCacheManager().getEhcache(name)));
		}
		return caches;
	}

	@Override
	protected Cache getMissingCache(String name) {
		// Check the EhCache cache again (in case the cache was added at
		// runtime)
		int expiredtime = 0;
		int refreshtime = 0;
		String[] cacheParams = name.split(separator);
		String cacheName = cacheParams[0];

		if (ToolUtil.isEmpty(cacheName)) {
			return null;
		}

		if (cacheParams.length > 1) {
			expiredtime = ToolUtil.toInt(cacheParams[1], 2);
		}
		if (cacheParams.length > 2) {
			refreshtime = ToolUtil.toInt(cacheParams[2], 0);
		}
		if (cacheParams.length == 1) {
			expiredtime = -2;
		}

		Ehcache ehcache = getCacheManager().getEhcache(cacheName);
		if (ehcache != null) {
			return new CustomizedEhCacheCache(ehcache, expiredtime, refreshtime);
		}
		return null;
	}

}
