package com.dt.core.shiro;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;

/**
 * @author: algernonking
 * @date: 2017年11月9日 下午4:18:39
 * @Description: TODO
 */
public class RetryLimitSimpleCredentialsMatcher extends SimpleCredentialsMatcher {
	private int limitretryCount = 0;
	private Cache<Object, Object> passwordRetryCache;

	public RetryLimitSimpleCredentialsMatcher(EhCacheManager cacheManager) {
		passwordRetryCache = cacheManager.getCache("passwordRetryCache");
	}

	@Override
	public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {

		String username = (String) authcToken.getPrincipal();
		AtomicInteger retryCount = (AtomicInteger) passwordRetryCache.get(username);

		if (retryCount == null) {
			retryCount = new AtomicInteger(0);
			passwordRetryCache.put(username, retryCount);
		}
		// 自定义一个验证过程：当用户连续输入密码错误n次以上禁止用户登录一段时间,默认为3次
		if (limitretryCount >= 0) {
			if (limitretryCount <= 3) {
				limitretryCount = 3;
			}
			if (retryCount.incrementAndGet() >= limitretryCount) {
				throw new ExcessiveAttemptsException();
			}
		}

		boolean match = super.doCredentialsMatch(authcToken, info);
		if (match) {
			passwordRetryCache.remove(username);
		}
		return match;
	}

	/**
	 * @return the limitretryCount
	 */
	public int getLimitretryCount() {
		return limitretryCount;
	}

	/**
	 * @param limitretryCount
	 *            the limitretryCount to set
	 */
	public void setLimitretryCount(int limitretryCount) {

		this.limitretryCount = limitretryCount;
	}
}
