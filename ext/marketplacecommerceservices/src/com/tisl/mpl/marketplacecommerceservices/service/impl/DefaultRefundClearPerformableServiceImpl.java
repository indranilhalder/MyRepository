/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.marketplacecommerceservices.daos.RefundClearPerformableDao;
import com.tisl.mpl.marketplacecommerceservices.service.RefundClearPerformableService;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author 1079689
 *
 */
public class DefaultRefundClearPerformableServiceImpl implements RefundClearPerformableService
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DefaultRefundClearPerformableServiceImpl.class.getName());

	@Autowired
	private RefundClearPerformableDao refundClearPerformableDao;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;


	private ModelService modelService;

	final Double refundClearTATFinal = new Double(10);


	@Override
	public MplConfigurationModel getCronDetails(final String code)
	{
		// YTODO Auto-generated method stub
		return refundClearPerformableDao.getCronDetails(code);
	}

	@Override
	public void processRefundOrders(final Date lastStartDate)
	{
		// YTODO Auto-generated method stub

		List<OrderModel> orders = new ArrayList<OrderModel>();
		final String refundClearTAT = getConfigurationService().getConfiguration().getString(
				MarketplacecommerceservicesConstants.REFUNDCLEAR_SKIPTIME);
		final Double skipPendingOrdersTAT = (null != refundClearTAT ? Double.valueOf(refundClearTAT) : refundClearTATFinal);
		final Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, -skipPendingOrdersTAT.intValue());
		final Date queryTAT = cal.getTime();


		orders = refundClearPerformableDao.getRefundClearOrders(queryTAT);


	}




	@Override
	public void saveCronDetails(final Date startTime, final String code)
	{
		final MplConfigurationModel oModel = refundClearPerformableDao.getCronDetails(code);
		if (null != oModel && null != oModel.getMplConfigCode())
		{
			LOG.debug("Saving CronJob Run Time :" + startTime);
			oModel.setMplConfigDate(startTime);
			getModelService().save(oModel);
			LOG.debug("Cron Job Details Saved for Code :" + code);
		}
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}



}
