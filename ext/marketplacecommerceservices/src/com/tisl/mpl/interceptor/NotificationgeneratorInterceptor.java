package com.tisl.mpl.interceptor;

/**
 *@author TCS
 */

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.localization.Localization;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OrderStatusNotificationModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.NotificationService;
import com.tisl.mpl.marketplacecommerceservices.service.OrderModelService;
import com.tisl.mpl.util.ExceptionUtil;




public class NotificationgeneratorInterceptor implements PrepareInterceptor
{

	private static final Logger LOG = Logger.getLogger(NotificationgeneratorInterceptor.class);

	@Autowired
	private ConfigurationService configurationService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Autowired
	private OrderModelService orderService;
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
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @Javadoc
	 * @Description : The Method sets the Product Category Type based on the Supercatgeories
	 * @param: object
	 * @param: arg1
	 * @return: void
	 */
	@Override
	public void onPrepare(final Object object, final InterceptorContext arg1) throws InterceptorException
	{
		LOG.debug(Localization.getLocalizedString("Notification.Interceptor.Starting"));
		try
		{
			final String useNotification = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.USE_NOTIFICATION);

			final String fireStatusAll = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.FIRE_NOTIFICATION);

			if (object instanceof ConsignmentModel && useNotification.equalsIgnoreCase(MarketplacecommerceservicesConstants.Y))
			{

				final ConsignmentModel consignment = (ConsignmentModel) object;

				String status = "";
				if (null != consignment.getStatus() && null != consignment.getStatus().getCode())
				{
					status = consignment.getStatus().getCode();

				}

				else
				{
					LOG.debug("Consignment Status is Null");
				}

				String transactionId = "";
				if (null != consignment.getCode())
				{
					transactionId = consignment.getCode();
				}

				else
				{
					LOG.debug("Consignment Code is Null");
				}
				final Boolean isRead = Boolean.FALSE;



				String orderId = "";
				if (null != consignment.getOrder() && null != consignment.getOrder().getCode())
				{
					orderId = consignment.getOrder().getCode();
					try
					{
						final OrderModel om = orderService.getOrder(orderId);

						if (om != null & om.getParentReference() != null)
						{
							final OrderModel parentOrder = om.getParentReference();
							orderId = parentOrder.getCode();
						}
					}
					catch (final Exception e)
					{
						LOG.debug("Problem In Order Service");
					}


				}

				else
				{
					LOG.debug("Order Code is Null");
				}

				String customerUID = "";
				if (null != consignment.getOrder() && null != consignment.getOrder().getUser()
						&& null != consignment.getOrder().getUser().getUid())
				{
					customerUID = consignment.getOrder().getUser().getUid();
				}
				else
				{
					LOG.debug("customerUID is Null");
				}


				String orderDetails = "";
				if (null != consignment.getStatusDisplay())
				{
					orderDetails = consignment.getStatusDisplay();
				}

				else
				{
					LOG.debug("orderDetails is Null");
				}

				final OrderStatusNotificationModel osn = new OrderStatusNotificationModel();
				if (null != status && null != transactionId && null != orderId && null != customerUID)
				{
					osn.setOrderStatus(status);
					osn.setOrderDetails(orderDetails);
					osn.setTransactionId(transactionId);
					osn.setOrderNumber(orderId);
					osn.setCustomerUID(customerUID);
					osn.setIsRead(isRead);
				}

				final List<OrderStatusNotificationModel> notificationList = notificationService.isAlreadyNotified(customerUID,
						orderId, transactionId, status);

				final String[] fireStatusList = fireStatusAll.split(",");
				boolean isFireStatus = false;
				for (final String fireStatus : fireStatusList)
				{
					if (status.equalsIgnoreCase(fireStatus))
					{
						isFireStatus = true;
						break;
					}
				}

				if (isFireStatus && notificationList.size() <= 0)
				{
					LOG.debug("Calling Customer Facing Interceptor");
					modelService.save(osn);
					LOG.debug("Notification Successfully Saved");

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
