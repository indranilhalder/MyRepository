package com.tisl.mpl.core.cronjob.passwordexpired.jobs;

import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.impl.DefaultUserService;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.tisl.mpl.marketplacecommerceservices.service.ExtendedEmployeeService;


public class PasswordExpiredCronJob extends AbstractJobPerformable<CronJobModel>
{
	private ExtendedEmployeeService extendedEmployeeService;

	private DefaultUserService defaultUserService;

	private ModelService modelService;

	private static final Logger LOG = Logger.getLogger(PasswordExpiredCronJob.class);

	@Override
	public PerformResult perform(final CronJobModel cronJobModel)
	{
		LOG.info("Starting Password Expiry Job");
		final Collection<EmployeeModel> empCol = extendedEmployeeService.getAllUsers();
		EmployeeModel emp = new EmployeeModel();
		final Calendar cal = Calendar.getInstance();
		final Date today = cal.getTime();
		final Calendar lastPasswordChangeCal = Calendar.getInstance();
		for (int i = 0; i < empCol.size(); i++)
		{
			emp = ((List<EmployeeModel>) empCol).get(i);
			if (emp != null && emp.getLastPasswordChanged() != null)
			{
				lastPasswordChangeCal.setTime(emp.getLastPasswordChanged());
				final int days = Days.daysBetween(new DateTime(lastPasswordChangeCal.getTime()), new DateTime(today)).getDays();
				if (days > 30)
				{
					if (!defaultUserService.isAdmin(emp))
					{
						emp.setLoginDisabled(true);
						modelService.save(emp);
					}
				}
			}
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	public DefaultUserService getDefaultUserService()
	{
		return defaultUserService;
	}

	public void setDefaultUserService(final DefaultUserService defaultUserService)
	{
		this.defaultUserService = defaultUserService;
	}

	public ExtendedEmployeeService getExtendedEmployeeService()
	{
		return extendedEmployeeService;
	}

	public void setExtendedEmployeeService(final ExtendedEmployeeService extendedEmployeeService)
	{
		this.extendedEmployeeService = extendedEmployeeService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public static Logger getLog()
	{
		return LOG;
	}

}