/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.strategy;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.jobs.maintenance.MaintenanceCleanupStrategy;
import de.hybris.platform.servicelayer.internal.model.MaintenanceCleanupJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * @author TCS
 *
 */
public class MplCleanupCronJobStrategy implements MaintenanceCleanupStrategy<CronJobModel, CronJobModel>
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MplCleanupCronJobStrategy.class.getName());

	//static bean properties
	private ModelService modelService;
	private TypeService typeService;
	private Set<CronJobResult> result;
	private Set<CronJobStatus> status;
	private Set<String> excludedCronJobCodes;
	//private String expiryDays;


	//dynamic job properties
	private int daysOld = 14;
	private String cronJobType = CronJobModel._TYPECODE;
	private static final String ADD_QUERY_STATEMENT = "AND {";

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Override
	public FlexibleSearchQuery createFetchQuery(final CronJobModel cjm)
	{
		LOG.error("**Inside Cron Job Strategy**");
		try
		{
			daysOld = Integer.parseInt(configurationService.getConfiguration().getString("cronjob.dayOld.count"));
		}
		catch (final Exception exceptn)
		{
			daysOld = 14;
		}


		LOG.error("*Days Old Evaluated*" + daysOld);
		LOG.error("**Evaluating CronJob**" + cjm.getCode());

		checkJobParameters(cjm);

		final Map<String, Object> params = new HashMap<String, Object>();
		final StringBuilder builder = new StringBuilder(1000);

		builder.append("SELECT {" + CronJobModel.PK + "} FROM {" + this.cronJobType + " AS c} ");
		builder.append("WHERE {c." + CronJobModel.PK + "} NOT IN (");
		builder.append(" {{SELECT {" + TriggerModel.CRONJOB + "} FROM {" + TriggerModel._TYPECODE + "} ");
		builder.append("WHERE {" + TriggerModel.CRONJOB + "} IS NOT NULL}}");
		builder.append(") ");

		if (!excludedCronJobCodes.isEmpty())
		{
			builder.append(ADD_QUERY_STATEMENT + CronJobModel.CODE + "} NOT IN ( ?excludedCronJobCodes ) ");
			params.put("excludedCronJobCodes", excludedCronJobCodes);
		}

		builder.append(ADD_QUERY_STATEMENT + CronJobModel.STATUS + "} IN ( ?status ) ");
		builder.append(ADD_QUERY_STATEMENT + CronJobModel.RESULT + "} IN ( ?result ) ");
		builder.append(ADD_QUERY_STATEMENT + CronJobModel.ENDTIME + "} < ?date");

		params.put("date", new Date(System.currentTimeMillis() - 1000L * 3600L * 24 * daysOld));
		params.put("result", this.result);
		params.put("status", this.status);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(builder.toString(), params);
		query.setResultClassList(Arrays.asList(CronJobModel.class));

		return query;
	}

	@Override
	public void process(final List<CronJobModel> elements)
	{
		LOG.info("Removing " + elements.size() + " items from type " + this.cronJobType);

		if (CollectionUtils.isNotEmpty(elements))
		{
			LOG.error("**Removing the Jobs*: Purged Data Count*" + elements.size());
		}
		modelService.removeAll(elements);
	}

	private void checkJobParameters(final CronJobModel cjm)
	{
		if (cjm.getJob() instanceof MaintenanceCleanupJobModel)
		{
			final MaintenanceCleanupJobModel job = (MaintenanceCleanupJobModel) cjm.getJob();
			if (job.getThreshold() != null)
			{
				LOG.info("MaintenanceCleanupJobModel contains a threeshold value, using " //
						+ job.getThreshold() + " instead of " + daysOld);
				setDaysOld(job.getThreshold());
			}
			if (job.getSearchType() != null)
			{
				LOG.info("MaintenanceCleanupJobModel contains a composedtype value, using: " + job.getSearchType().getCode());
				setCronJobType(job.getSearchType().getCode());
			}
		}
	}

	private void setCronJobType(final String cronjobtype)
	{
		if (typeService.isAssignableFrom(this.cronJobType, cronjobtype))
		{
			//cronjobtype is not null and is a subtype, doesn't matter if it is the same.
			this.cronJobType = cronjobtype;
		}
		else
		{
			throw new IllegalArgumentException(cronjobtype + " must be a subtype of " + CronJobModel._TYPECODE);
		}
	}

	private void setDaysOld(final Integer daysold)
	{
		if (daysold.intValue() < 0)
		{
			throw new IllegalArgumentException("Cannot set negative value for daysold");
		}
		this.daysOld = daysold.intValue();
	}

	@Required
	public void setExcludedCronJobCodes(final Set<String> excludedCronjobCodes)
	{
		this.excludedCronJobCodes = excludedCronjobCodes;
	}

	@Required
	public void setResult(final Set<CronJobResult> result)
	{
		if (result == null || result.isEmpty())
		{
			throw new IllegalArgumentException("The CronJob result set must contains at least one value!");
		}
		this.result = result;
	}

	@Required
	public void setStatus(final Set<CronJobStatus> status)
	{
		if (status == null || status.isEmpty())
		{
			throw new IllegalArgumentException("The CronJob status set must contains at least one value!");
		}
		this.status = status;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	/**
	 * @return the expiryDays
	 */
	//public String getExpiryDays()
	//{
	//	return expiryDays;
	//}

	/**
	 * @param expiryDays
	 *           the expiryDays to set
	 */
	//public void setExpiryDays(final String expiryDays)
	//{
	//this.expiryDays = expiryDays;
	//}


}
