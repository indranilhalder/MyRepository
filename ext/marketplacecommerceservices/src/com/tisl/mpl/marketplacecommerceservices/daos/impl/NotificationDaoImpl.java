/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.voucher.model.VoucherModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OrderStatusNotificationModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.NotificationDao;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "PMD" })
public class NotificationDaoImpl implements NotificationDao
{

	@Autowired
	private FlexibleSearchService flexibleSearchService;
	private static final String SELECT_CLASS = "SELECT {c:";
	private static final String FROM_CLASS = "FROM {";
	private static final String WHERE_CLASS = "WHERE ";
	private static final String C_CLASS = "{c:";

	protected ConfigurationService getConfigurationService()
	{
		return Registry.getApplicationContext().getBean(MarketplacecommerceservicesConstants.CONFIGURATION_SER,
				ConfigurationService.class);
	}

	/*
	 * 
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.NotificationDao#getNotificationDetail(com.tisl.mpl.core.model.
	 * NotificationModel)
	 */

	@Override
	public List<OrderStatusNotificationModel> getNotificationDetail(final String customerUID, final boolean isDesktop)
	{
		List<OrderStatusNotificationModel> notificationList = null;
		try
		{

			String notificationCount;
			if (isDesktop)
			{
				notificationCount = getConfigurationService().getConfiguration().getString(
						MarketplacecommerceservicesConstants.NOTIFICATION_COUNT);
			}
			else
			{
				notificationCount = getConfigurationService().getConfiguration().getString(
						MarketplacecommerceservicesConstants.NOTIFICATION_COUNT_MOBILE);
			}

			if (notificationCount != null && customerUID != null)
			{
				final StringBuilder stringBuilder = new StringBuilder(100);
				stringBuilder.append(SELECT_CLASS).append(OrderStatusNotificationModel.PK).append("} ");
				stringBuilder.append(FROM_CLASS).append(OrderStatusNotificationModel._TYPECODE).append(" AS c} ");
				stringBuilder.append(WHERE_CLASS).append(C_CLASS);
				stringBuilder.append(OrderStatusNotificationModel.CUSTOMERUID).append("}=?code ");
				stringBuilder.append("ORDER BY").append(C_CLASS);
				stringBuilder.append(OrderStatusNotificationModel.CREATIONTIME).append("}DESC");


				final FlexibleSearchQuery query = new FlexibleSearchQuery(stringBuilder.toString());
				query.setCount(Integer.parseInt(notificationCount));
				query.addQueryParameter("code", customerUID);
				notificationList = flexibleSearchService.<OrderStatusNotificationModel> search(query).getResult();

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

		return notificationList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.NotificationDao#checkCustomerFacingEntry(com.tisl.mpl.core.model
	 * .OrderStatusNotificationModel)
	 */
	@Override
	public List<OrderStatusNotificationModel> checkCustomerFacingEntry(final String customerId, final String orderId,
			final String transactionId, final String customerStatus)
	{
		List<OrderStatusNotificationModel> orderList = new ArrayList<>();
		if (customerId != null && orderId != null && transactionId != null && customerStatus != null)
		{
			final String queryString = //
			SELECT_CLASS
					+ OrderStatusNotificationModel.PK
					+ "}" //
					+ FROM_CLASS + OrderStatusNotificationModel._TYPECODE + " AS c} " + WHERE_CLASS + C_CLASS
					+ OrderStatusNotificationModel.CUSTOMERUID + "}=?customerId AND" + C_CLASS
					+ OrderStatusNotificationModel.ORDERNUMBER + "}=?ordercode AND" + C_CLASS
					+ OrderStatusNotificationModel.TRANSACTIONID + "}=?transactionId AND" + C_CLASS
					+ OrderStatusNotificationModel.CUSTOMERSTATUS + "}=?customerStatus" + " ORDER BY" + C_CLASS
					+ OrderStatusNotificationModel.CREATIONTIME + "}DESC";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

			query.addQueryParameter(MarketplacecommerceservicesConstants.CUSTOMERID, customerId);
			query.addQueryParameter(MarketplacecommerceservicesConstants.ORDER_CODE, orderId);
			query.addQueryParameter("transactionId", transactionId);
			query.addQueryParameter("customerStatus", customerStatus);
			orderList = flexibleSearchService.<OrderStatusNotificationModel> search(query).getResult();
			return orderList;
		}

		return orderList;
	}


	@Override
	public List<OrderStatusNotificationModel> getModelforDetails(final String customerId, final String orderId,
			final String consignmentId, final String shopperStatus)
	{
		List<OrderStatusNotificationModel> orderList = new ArrayList<>();
		if (customerId != null && orderId != null && shopperStatus != null)
		{
			final String queryString = //
			SELECT_CLASS
					+ OrderStatusNotificationModel.PK
					+ "}" //
					+ FROM_CLASS + OrderStatusNotificationModel._TYPECODE + " AS c} " + WHERE_CLASS + C_CLASS
					+ OrderStatusNotificationModel.CUSTOMERUID + "}=?customerId AND " + C_CLASS
					+ OrderStatusNotificationModel.ORDERNUMBER + "}=?ordercode AND " + C_CLASS
					+ OrderStatusNotificationModel.TRANSACTIONID + "}=?consignmentId AND " + C_CLASS
					+ OrderStatusNotificationModel.CUSTOMERSTATUS + "}=?shopperStatus";

			final String queryString2 = //
			SELECT_CLASS
					+ OrderStatusNotificationModel.PK
					+ "}" //
					+ FROM_CLASS + OrderStatusNotificationModel._TYPECODE + " AS c} " + WHERE_CLASS + C_CLASS
					+ OrderStatusNotificationModel.CUSTOMERUID + "}=?customerId AND" + C_CLASS
					+ OrderStatusNotificationModel.ORDERNUMBER + "}=?ordercode AND" + C_CLASS
					+ OrderStatusNotificationModel.CUSTOMERSTATUS + "}=?shopperStatus";

			if (consignmentId != null && !StringUtils.isEmpty(consignmentId))
			{
				final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

				query.addQueryParameter(MarketplacecommerceservicesConstants.CUSTOMERID, customerId);
				query.addQueryParameter(MarketplacecommerceservicesConstants.ORDER_CODE, orderId);
				query.addQueryParameter("consignmentId", consignmentId);
				query.addQueryParameter("shopperStatus", shopperStatus);


				orderList = flexibleSearchService.<OrderStatusNotificationModel> search(query).getResult();
				return orderList;

			}

			else
			{
				final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString2);

				query.addQueryParameter(MarketplacecommerceservicesConstants.CUSTOMERID, customerId);
				query.addQueryParameter(MarketplacecommerceservicesConstants.ORDER_CODE, orderId);
				query.addQueryParameter("shopperStatus", shopperStatus);


				orderList = flexibleSearchService.<OrderStatusNotificationModel> search(query).getResult();
				return orderList;
			}
		}

		return orderList;
	}

	@Override
	public List<OrderStatusNotificationModel> getNotification(final String customerId, final String orderId,
			final String transactionId, final String orderStatus)
	{
		List<OrderStatusNotificationModel> orderList = new ArrayList<>();
		final String queryString = //
		SELECT_CLASS
				+ OrderStatusNotificationModel.PK
				+ "}" //
				+ FROM_CLASS + OrderStatusNotificationModel._TYPECODE + " AS c} " + WHERE_CLASS + C_CLASS
				+ OrderStatusNotificationModel.CUSTOMERUID + "}=?customerId AND " + C_CLASS
				+ OrderStatusNotificationModel.ORDERNUMBER + "}=?ordercode AND " + C_CLASS
				+ OrderStatusNotificationModel.TRANSACTIONID + "}=?transactionId AND " + C_CLASS
				+ OrderStatusNotificationModel.ORDERSTATUS + "}=?orderStatus";


		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

		query.addQueryParameter(MarketplacecommerceservicesConstants.CUSTOMERID, customerId);
		query.addQueryParameter(MarketplacecommerceservicesConstants.ORDER_CODE, orderId);
		query.addQueryParameter("transactionId", transactionId);
		query.addQueryParameter("orderStatus", orderStatus);


		orderList = flexibleSearchService.<OrderStatusNotificationModel> search(query).getResult();
		return orderList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.NotificationDao#findVoucher()
	 */
	@Override
	public List<VoucherModel> findVoucher()
	{
		final String queryString = MarketplacecommerceservicesConstants.VOUCHERWITHINDATEQUERY;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

		return flexibleSearchService.<VoucherModel> search(query).getResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.NotificationDao#getPromotion()
	 */
	@Override
	public List<AbstractPromotionModel> getPromotion()
	{
		List<AbstractPromotionModel> promotionList = new ArrayList<>();
		final String queryString = MarketplacecommerceservicesConstants.GETPROMOTIONS;
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		promotionList = flexibleSearchService.<AbstractPromotionModel> search(query).getResult();
		return promotionList;
	}



}
