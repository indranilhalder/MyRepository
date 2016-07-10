/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.OrderStatusNotificationModel;
import com.tisl.mpl.core.model.VoucherStatusNotificationModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.NotificationDao;
import com.tisl.mpl.util.ExceptionUtil;


/**
 * @author TCS
 *
 */
public class NotificationDaoImpl implements NotificationDao
{

	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	private static final String SELECT_CLASS = "SELECT {c:";
	private static final String FROM_CLASS = "FROM {";
	private static final String WHERE_CLASS = "WHERE ";
	private static final String C_CLASS = "{c:";
	private static final String AS_C = " AS c} ";

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
			if (customerUID != null)
			{
				final StringBuilder stringBuilder = new StringBuilder(100);
				stringBuilder.append(SELECT_CLASS).append(OrderStatusNotificationModel.PK).append("} ");
				stringBuilder.append(FROM_CLASS).append(OrderStatusNotificationModel._TYPECODE).append(AS_C);
				stringBuilder.append(WHERE_CLASS).append(C_CLASS);
				stringBuilder.append(OrderStatusNotificationModel.CUSTOMERUID).append("}=?code ");
				stringBuilder.append("ORDER BY").append(C_CLASS);
				stringBuilder.append(OrderStatusNotificationModel.CREATIONTIME).append("}DESC");

				final FlexibleSearchQuery query = new FlexibleSearchQuery(stringBuilder.toString());
				query.addQueryParameter("code", customerUID);
				notificationList = getFlexibleSearchService().<OrderStatusNotificationModel> search(query).getResult();

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
					+ FROM_CLASS + OrderStatusNotificationModel._TYPECODE + AS_C + WHERE_CLASS + C_CLASS
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
			orderList = getFlexibleSearchService().<OrderStatusNotificationModel> search(query).getResult();
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
					+ FROM_CLASS + OrderStatusNotificationModel._TYPECODE + AS_C + WHERE_CLASS + C_CLASS
					+ OrderStatusNotificationModel.CUSTOMERUID + "}=?customerId AND " + C_CLASS
					+ OrderStatusNotificationModel.ORDERNUMBER + "}=?ordercode AND " + C_CLASS
					+ OrderStatusNotificationModel.TRANSACTIONID + "}=?consignmentId AND " + C_CLASS
					+ OrderStatusNotificationModel.CUSTOMERSTATUS + "}=?shopperStatus";

			final String queryString2 = //
			SELECT_CLASS
					+ OrderStatusNotificationModel.PK
					+ "}" //
					+ FROM_CLASS + OrderStatusNotificationModel._TYPECODE + AS_C + WHERE_CLASS + C_CLASS
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


				orderList = getFlexibleSearchService().<OrderStatusNotificationModel> search(query).getResult();
				return orderList;

			}

			else
			{
				final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString2);

				query.addQueryParameter(MarketplacecommerceservicesConstants.CUSTOMERID, customerId);
				query.addQueryParameter(MarketplacecommerceservicesConstants.ORDER_CODE, orderId);
				query.addQueryParameter("shopperStatus", shopperStatus);


				orderList = getFlexibleSearchService().<OrderStatusNotificationModel> search(query).getResult();
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
				+ FROM_CLASS + OrderStatusNotificationModel._TYPECODE + AS_C + WHERE_CLASS + C_CLASS
				+ OrderStatusNotificationModel.CUSTOMERUID + "}=?customerId AND " + C_CLASS
				+ OrderStatusNotificationModel.ORDERNUMBER + "}=?ordercode AND " + C_CLASS
				+ OrderStatusNotificationModel.TRANSACTIONID + "}=?transactionId AND " + C_CLASS
				+ OrderStatusNotificationModel.ORDERSTATUS + "}=?orderStatus";


		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

		query.addQueryParameter(MarketplacecommerceservicesConstants.CUSTOMERID, customerId);
		query.addQueryParameter(MarketplacecommerceservicesConstants.ORDER_CODE, orderId);
		query.addQueryParameter("transactionId", transactionId);
		query.addQueryParameter("orderStatus", orderStatus);


		orderList = getFlexibleSearchService().<OrderStatusNotificationModel> search(query).getResult();
		return orderList;
	}



	/**
	 * This method returns list of VoucherStatusNotificationModel where the date is active
	 *
	 * @return List<VoucherStatusNotificationModel>
	 *
	 */
	@Override
	public List<VoucherStatusNotificationModel> findVoucher()
	{
		List<VoucherStatusNotificationModel> voucherList = new ArrayList<>();

		try
		{
			final String queryString = MarketplacecommerceservicesConstants.VOUCHERWITHINDATEQUERYFROMCOUPONMODEL;

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter(MarketplacecommerceservicesConstants.SYSDATE, new Date());

			voucherList = getFlexibleSearchService().<VoucherStatusNotificationModel> search(query).getResult();
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return voucherList;
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
		promotionList = getFlexibleSearchService().<AbstractPromotionModel> search(query).getResult();
		return promotionList;
	}

	//	@Override
	//	public boolean checkIsUpdated(final String voucherCode)
	//	{
	//
	//		if (null != voucherCode && !voucherCode.isEmpty())
	//		{
	//			final String queryString = //
	//			"SELECT {p:" + VoucherStatusNotificationModel.PK + "}" //
	//					+ "FROM {" + VoucherStatusNotificationModel._TYPECODE + " AS p} "//
	//					+ "WHERE " + "{p:" + VoucherStatusNotificationModel.VOUCHERCODE + "}=?voucherCode ";
	//
	//			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
	//			query.addQueryParameter(MarketplacecommerceservicesConstants.VOUCHERCODE, voucherCode);
	//			if ((getFlexibleSearchService().<VoucherStatusNotificationModel> search(query).getResult().isEmpty()))
	//			{
	//				return true;
	//			}
	//
	//		}
	//		return false;
	//	}


	/**
	 * This method returns list of voucher status notification model based on voucher identifier
	 *
	 * @param voucherIndentifier
	 * @return List<VoucherStatusNotificationModel>
	 *
	 */
	@Override
	public List<VoucherStatusNotificationModel> getModelForVoucher(final String voucherIndentifier)
	{
		List<VoucherStatusNotificationModel> voucherList = new ArrayList<VoucherStatusNotificationModel>();

		try
		{
			if (StringUtils.isNotEmpty(voucherIndentifier))
			{
				final StringBuilder queryBuilder = new StringBuilder(50);
				final String queryString1 = //
				queryBuilder.append("SELECT {p:").append(VoucherStatusNotificationModel.PK).append("}").append("FROM {")
						.append(VoucherStatusNotificationModel._TYPECODE).append(" AS p} ").append("WHERE ").append("{p:")
						.append(VoucherStatusNotificationModel.VOUCHERIDENTIFIER).append("}=?voucherIndentifier").toString();


				final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString1);
				query.addQueryParameter(MarketplacecommerceservicesConstants.VOUCHERIDENTIFIER, voucherIndentifier);
				getFlexibleSearchService().<VoucherStatusNotificationModel> search(query).getResult();
				voucherList = getFlexibleSearchService().<VoucherStatusNotificationModel> search(query).getResult();

			}
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return voucherList;
	}



	/**
	 * This method returns list of voucher status notification model based on voucher code
	 *
	 * @param voucherCode
	 * @return List<VoucherStatusNotificationModel>
	 *
	 */
	@Override
	public List<VoucherStatusNotificationModel> getModelForVoucherIdentifier(final String voucherCode)
	{
		List<VoucherStatusNotificationModel> voucherList = new ArrayList<VoucherStatusNotificationModel>();
		try
		{
			if (StringUtils.isNotEmpty(voucherCode))
			{
				final StringBuilder queryBuilder = new StringBuilder(50);
				final String queryString1 = queryBuilder.append("SELECT {p:").append(VoucherStatusNotificationModel.PK).append("}")
						.append("FROM {").append(VoucherStatusNotificationModel._TYPECODE).append(" AS p} ").append("WHERE ")
						.append("{p:").append(VoucherStatusNotificationModel.VOUCHERCODE).append("}=?voucherCode").toString();

				final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString1);

				query.addQueryParameter(MarketplacecommerceservicesConstants.VOUCHERCODE, voucherCode);
				getFlexibleSearchService().<VoucherStatusNotificationModel> search(query).getResult();
				voucherList = getFlexibleSearchService().<VoucherStatusNotificationModel> search(query).getResult();

			}
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}

		return voucherList;

	}

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}







}
