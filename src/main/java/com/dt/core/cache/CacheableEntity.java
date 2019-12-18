package com.dt.core.cache;

/**
 * @author: jinjie
 * @date: 2018年4月3日 下午1:14:45
 * @Description: TODO
 */
public class CacheableEntity {

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	private String value = "";
	private String key = "";

	private int expiredtime = -1;

	/**
	 * @return the expiredtime
	 */
	public int getExpiredtime() {
		return expiredtime;
	}

	/**
	 * @param expiredtime
	 *            the expiredtime to set
	 */
	public void setExpiredtime(int expiredtime) {
		this.expiredtime = expiredtime;
	}

	/**
	 * @return the refreshtime
	 */
	public int getRefreshtime() {
		return refreshtime;
	}

	/**
	 * @param refreshtime
	 *            the refreshtime to set
	 */
	public void setRefreshtime(int refreshtime) {
		this.refreshtime = refreshtime;
	}

	private int refreshtime = -1;

	public CacheableEntity(String value_in, String key_in) {
		value = value_in;
		key = key_in;
	}

	public String toString() {
		return "value:" + value + ",key:" + key + ",refreshtime:" + refreshtime + ",expiredtime:" + expiredtime;
	}
}
