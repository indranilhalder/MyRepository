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
			final String lineSeparator = "line.separator";

			if (task.getContext() instanceof OmsOrderSyncTaskContext)
			{
				final StringBuilder sb = new StringBuilder();
				sb.append(System.getProperty(lineSeparator));
				sb.append("===============================================");
				sb.append(System.getProperty(lineSeparator));
				sb.append("PK=");
                sb.append(task.getPk());
				sb.append(",executionDate=");
                sb.append(task.getExecutionDate());
				sb.append(",executionTimeMillis=");
                sb.append(task.getExecutionTimeMillis());
				sb.append(",expirationDate=");
				sb.append(task.getExpirationDate());
				sb.append(",failed="); 
				sb.append(task.getFailed());
				sb.append(",runningOnClusterNode=") ;
				sb.append(task.getRunningOnClusterNode());
				sb.append(",nodeId=");
                sb.append(task.getNodeId());
				sb.append(",retry="); 
				sb.append(task.getRetry());
				sb.append(",runnerBean="); 
				sb.append(task.getRunnerBean());
				sb.append(System.getProperty(lineSeparator));
				sb.append("===============================================");
				sb.append(System.getProperty(lineSeparator));

				LOG.info(sb);
			}
		}
	}

}