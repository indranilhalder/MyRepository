/**
 *
 */
package com.tisl.mpl.interceptors;

import java.util.concurrent.ConcurrentHashMap;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;


/**
 * @author TCS
 *
 */
public class PerformanceMonitoring implements MethodInterceptor
{

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(PerformanceMonitoring.class);
	private static ConcurrentHashMap<String, MethodStats> methodStats = new ConcurrentHashMap<String, MethodStats>();
	private static long statLogFrequency = 10;
	private static long methodWarningThreshold = 1000;

	@Override
	public Object invoke(final MethodInvocation method) throws Throwable
	{
		final long start = System.currentTimeMillis();
		try
		{
			return method.proceed();
		}
		finally
		{
			updateStats(method.getMethod().getName(), (System.currentTimeMillis() - start));
		}
	}

	private void updateStats(final String methodName, final long elapsedTime)
	{
		MethodStats stats = methodStats.get(methodName);
		if (stats == null)
		{
			stats = new MethodStats(methodName);
			methodStats.put(methodName, stats);
		}
		stats.count++;
		stats.totalTime += elapsedTime;
		if (elapsedTime > stats.maxTime)
		{
			stats.maxTime = elapsedTime;
		}

		if (elapsedTime > methodWarningThreshold)
		{
			LOG.warn("method warning: " + methodName + "(), cnt = " + stats.count + ", lastTime = " + elapsedTime + ", maxTime = "
					+ stats.maxTime);
		}

		if (stats.count % statLogFrequency == 0)
		{
			final long avgTime = stats.totalTime / stats.count;
			final long runningAvg = (stats.totalTime - stats.lastTotalTime) / statLogFrequency;
			LOG.debug("method: " + methodName + "(), cnt = " + stats.count + ", lastTime = " + elapsedTime + ", avgTime = "
					+ avgTime + ", runningAvg = " + runningAvg + ", maxTime = " + stats.maxTime);

			//reset the last total time
			stats.lastTotalTime = stats.totalTime;
		}
	}

	class MethodStats
	{
		public String methodName;
		public long count;
		public long totalTime;
		public long lastTotalTime;
		public long maxTime;

		public MethodStats(final String methodName)
		{
			this.methodName = methodName;
		}
	}


}