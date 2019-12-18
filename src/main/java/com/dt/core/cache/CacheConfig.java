package com.dt.core.cache;

/**
 * @author: algernonking
 * @date: 2018年3月28日 下午7:34:24
 * @Description: TODO
 */
public class CacheConfig {

	// 刷新数据建议设置长些,短的话主动刷新轮不上
	// 2小时
	public static final String CACHE_PUBLIC = "public";

	public static final String CACHE_PUBLIC_45_10 = "public#45#10";
	
	public static final String CACHE_PUBLIC_80_10 = "public#80#10";
	// 非常频繁
	public static final String CACHE_PUBLIC_5_2 = "public#5#2";
	public static final String CACHE_PUBLIC_1d_1h = "public#86400#3600";
	// 非常频繁
	public static final String CACHE_PUBLIC_2h_20min = "public#7200#1200";
	// 不频繁
	public static final String CACHE_PUBLIC_3h_1h = "public#10800#3600";
	// 报表
	public static final String CACHE_PUBLIC_6h_30min = "public#21600#1800";

	// 用户数据
	public static final String CACHE_USER = "user";
	public static final String CACHE_USER_180_60 = "user#180#60";

	public static final String CACHE_WX_CONF = "wxconf";
	public static final String CACHE_WX_CONF_300_180 = "wxconf#300#180";

}
