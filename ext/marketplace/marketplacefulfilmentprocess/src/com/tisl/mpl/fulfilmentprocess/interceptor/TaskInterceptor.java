package com.tisl.mpl.fulfilmentprocess.interceptor;

import de.hybris.platform.integration.oms.order.dataimport.cronjob.OmsOrderSyncTaskContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.task.TaskModel;

import org.apache.log4j.Logger;


/**
 *
 * @author TCS
 *
 */


public class TaskInterceptor implements RemoveInterceptor
{

	private static final Logger LOG = Logger.getLogger(TaskInterceptor.class);

	/**
	 * @Description : SAP Conveyed Steps SAP TICKET :128061/2016
	 */
	@Override
	public void onRemove(final Object model, final InterceptorContext ctx) throws InterceptorException
	{
		if (model instanceof TaskModel)
		{
			final TaskModel task = (TaskModel) model;

			if (task.getContext() instanceof OmsOrderSyncTaskContext)
			{
				final StringBuilder sb = new StringBuilder();
				sb.append(System.getProperty("line.separator"));
				sb.append("===============================================");
				sb.append(System.getProperty("line.separator"));
				sb.append("PK=" + task.getPk());
				sb.append(",executionDate=" + task.getExecutionDate());
				sb.append(",executionTimeMillis=" + task.getExecutionTimeMillis());
				sb.append(",expirationDate=" + task.getExpirationDate());
				sb.append(",failed=" + task.getFailed());
				sb.append(",runningOnClusterNode=" + task.getRunningOnClusterNode());
				sb.append(",nodeId=" + task.getNodeId());
				sb.append(",retry=" + task.getRetry());
				sb.append(",runnerBean=" + task.getRunnerBean());
				sb.append(System.getProperty("line.separator"));
				sb.append("===============================================");
				sb.append(System.getProperty("line.separator"));

				LOG.info(sb);
			}
		}
	}

}