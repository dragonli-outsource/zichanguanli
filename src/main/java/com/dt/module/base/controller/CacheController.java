package com.dt.module.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.dt.core.annotion.Acl;
import com.dt.core.cache.CacheSupportImpl;
import com.dt.core.cache.ThreadTaskHelper;
import com.dt.core.common.base.R;
import com.dt.module.base.service.impl.CacheService;

/**
 * @author: algernonking
 * @date: 2018年7月28日 下午2:42:22
 * @Description: TODO
 */
@Controller
@RequestMapping("/api/sysApi")
public class CacheController {
	@Autowired
	CacheService cacheService;

	@Autowired
	private CacheSupportImpl cacheSupportImpl;

	@RequestMapping(value = "/system/queryCacheName.do")
	@ResponseBody
	@Acl(info = "查询CacheName", value = Acl.ACL_DENY)
	public R queryCustomizedEhCacheCacheManagerCaches() {
		return cacheService.queryCacheCacheManagerCaches();
	}

	@RequestMapping(value = "/system/queryCacheKeys.do")
	@ResponseBody
	@Acl(info = "查询CacheName", value = Acl.ACL_DENY)
	public R queryCacheKeys(String cache) {
		return cacheService.queryCustomizedEhCacheCacheManagerCacheKeys(cache);
	}

	@RequestMapping(value = "/system/removeCacheKey.do")
	@ResponseBody
	@Acl(info = "删除CacheKey", value = Acl.ACL_DENY)
	public R removeCacheKey(String cache, String key) {
		return cacheService.removeCacheKey(cache, key);
	}

	@RequestMapping(value = "/system/refreshCache.do")
	@ResponseBody
	@Acl(info = "删除CacheKey", value = Acl.ACL_DENY)
	public R refreshCache(String key, String cache) {
		ThreadTaskHelper.run(new Runnable() {
			@Override
			public void run() {
				cacheSupportImpl.refreshCacheByKey(cache, key);
			}
		});
		return R.SUCCESS_OPER();
	}

}
