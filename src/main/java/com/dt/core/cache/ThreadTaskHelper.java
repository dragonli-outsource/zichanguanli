package com.dt.core.cache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTaskHelper {

	private static ExecutorService executorService = Executors.newFixedThreadPool(20);

	public static void run(Runnable runnable) {
		executorService.execute(runnable);
	}
}