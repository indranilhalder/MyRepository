package com.tisl.mpl.interceptor;

/**
 *@author TCS
 */

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.util.localization.Localization;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OrderStatusNotificationModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.NotificationService;
import com.tisl.mpl.util.ExceptionUtil;




public class CustomerFacingStatusInterceptor implements ValidateInterceptor
{
	private static final Logger LOG = Logger.getLogger(CustomerFacingStatusInterceptor.class);
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private NotificationService notificationService;

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
	 * @return the notificationService
	 */
	public NotificationService getNotificationService()
	{
		return notificationService;
	}

	/**
	 * @param notificationService
	 *           the notificationService to set
	 */
	public void setNotificationService(final NotificationService notificationService)
	{
		this.notificationService = notificationService;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.servicelayer.interceptor.ValidateInterceptor#onValidate(java.lang.Object,
	 * de.hybris.platform.servicelayer.interceptor.InterceptorContext)
	 */
	@Override
	public void onValidate(final Object paramMODEL, final InterceptorContext paramInterceptorContext) throws InterceptorException
	{
		LOG.debug(Localization.getLocalizedString("Customer_Status.Interceptor.Starting"));
		try
		{
			final String useNotification = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.USE_NOTIFICATION);
			if (paramMODEL instanceof OrderStatusNotificationModel
					&& useNotification.equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
			{
				final OrderStatusNotificationModel orderNotification = (OrderStatusNotificationModel) paramMODEL;

				final String input = configurationService.getConfiguration().getString(
						MarketplacecommerceservicesConstants.NOTIFICATION_STATUS);

				if (input != null && !input.isEmpty())
				{
					final Map<String, String> statusMap = new HashMap<String, String>();
					for (final String actualElement : input.split(MarketplacecommerceservicesConstants.COMMA))
					{
						statusMap.put(actualElement.split(MarketplacecommerceservicesConstants.HYPHEN)[Integer
								.parseInt(MarketplacecommerceservicesConstants.KEY)], actualElement
								.split(MarketplacecommerceservicesConstants.HYPHEN)[Integer
								.parseInt(MarketplacecommerceservicesConstants.VALUE)]);
					}

					LOG.debug("Status Mapping Completed from Properties File");

					final String status = orderNotification.getOrderStatus();
					if (status != null)
					{
						for (final Map.Entry<String, String> entry : statusMap.entrySet())
						{
							if (status.equalsIgnoreCase(entry.getKey()))
							{
								orderNotification.setCustomerStatus(entry.getValue());
							}
						}

						if (StringUtils.isEmpty(orderNotification.getCustomerStatus()))
						{
							//orderNotification.setCustomerUID(MarketplacecommerceservicesConstants.STATUS_NOT_PRESENT_ERROR);
							LOG.error(MarketplacecommerceservicesConstants.STATUS_NOT_PRESENT_ERROR);
						}
					}

					else
					{
						LOG.debug("Order Status Not Present");
					}

					/*
					 * if (notificationService.checkCustomerFacingEntry(orderNotification)) {
					 * orderNotification.setCustomerUID(MarketplacecommerceservicesConstants.STATUS_ALREADY_PRESENT); }
					 */
				}
				else
				{
					LOG.debug("NOTIFICATION_STATUS not present in properties");
				}
			}

			else
			{
				LOG.debug("Notification is turned OFF");
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			ExceptionUtil.etailNonBusinessExceptionHandler(e);
		}
		catch (final Exception e)
		{
			LOG.debug(e.getStackTrace());
		}

	}
}
