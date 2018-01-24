/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.BulkCancellationProcessModel;
import de.hybris.platform.core.model.BulkReturnProcessModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.OrderModelDao;


/**
 * @author TCS
 *
 */
@Component(value = "orderModelDao")
public class OrderModelDaoImpl implements OrderModelDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private ModelService modelService;

	private static final Logger LOG = Logger.getLogger(OrderModelDaoImpl.class);


	/**
	 * It gets the list of all Orders
	 *
	 * @return List<OrderModel>
	 *
	 */
	@Override
	public List<OrderModel> getAllOrders() throws EtailNonBusinessExceptions
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(MarketplacecommerceservicesConstants.ORDERTYPE, MarketplacecommerceservicesConstants.PARENTORDER);
		try
		{
			final FlexibleSearchQuery query = new FlexibleSearchQuery(MarketplacecommerceservicesConstants.SALES_REPORT_QUERY,
					params);
			return flexibleSearchService.<OrderModel> search(query).getResult();
		}

		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
		}
	}

	/**
	 * It gets the list of Orders from creation date
	 *
	 * @return List<OrderModel>
	 *
	 */
	@Override
	public List<OrderModel> getAllOrders(final Date fromDate) throws EtailNonBusinessExceptions
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("fromDate", fromDate);
		params.put(MarketplacecommerceservicesConstants.ORDERTYPE, MarketplacecommerceservicesConstants.PARENTORDER);
		try
		{
			final FlexibleSearchQuery query = new FlexibleSearchQuery(MarketplacecommerceservicesConstants.SALES_REPORT_QUERY_START,
					params);

			return flexibleSearchService.<OrderModel> search(query).getResult();
		}

		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
		}
	}

	/**
	 * It gets the list of Parent Order No and Transaction Id
	 *
	 * @return List<BulkReturnProcessModel>
	 *
	 */
	@Override
	public List<BulkReturnProcessModel> getAllBulkReturnData() throws EtailNonBusinessExceptions
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.BULK_RETURN_DATA_QUERY_START;

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter(MarketplacecommerceservicesConstants.LOADSTATUS, "0");

			final List<BulkReturnProcessModel> listOfData = flexibleSearchService.<BulkReturnProcessModel> search(query).getResult();
			return listOfData;
		}

		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
		}
	}

	/**
	 * It updates the load status value
	 *
	 * @return
	 *
	 */



	/**
	 * It gets the list of Orders from start date to endDate
	 *
	 * @return List<OrderModel>
	 *
	 */
	@Override
	public List<OrderModel> getAllOrders(final Date startDate, final Date endDate) throws EtailNonBusinessExceptions
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put(MarketplacecommerceservicesConstants.ORDERTYPE, MarketplacecommerceservicesConstants.PARENTORDER);
		try
		{
			final FlexibleSearchQuery query = new FlexibleSearchQuery(
					MarketplacecommerceservicesConstants.SALES_REPORT_QUERY_START_END, params);

			return flexibleSearchService.<OrderModel> search(query).getResult();
		}

		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.OrderModelDao#getOrder(java.util.Date)
	 */
	@Override
	public OrderModel getOrder(final String code) throws EtailNonBusinessExceptions
	{
		OrderModel order = null;
		//List<OrderModel> orderlist = null;
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(MarketplacecommerceservicesConstants.ORDERCODE, code);
		params.put(MarketplacecommerceservicesConstants.ORDERTYPE, MarketplacecommerceservicesConstants.SUBORDER);

		try
		{
			//forming the flexible search query
			//final FlexibleSearchQuery orderQuery = new FlexibleSearchQuery(MarketplacecommerceservicesConstants.ORDER_QUERY, params);
			//CAR-301--
			//The following peice of code has been modified for performance tuning.
			final FlexibleSearchQuery orderQuery = new FlexibleSearchQuery(MarketplacecommerceservicesConstants.ORDER_QUERY_SUB,
					params);
			order = flexibleSearchService.<OrderModel> search(orderQuery).getResult().get(0);


			//			if (null != orderlist && !orderlist.isEmpty())
			//			{
			//				for (final OrderModel orderModel : orderlist)
			//				{
			//					if (orderModel.getVersionID() == null)
			//					{
			//						order = orderModel;
			//					}
			//				}
			//				//return order;
			//			}
			//			else
			//			{
			//				return null;
			//			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
		}
		return order;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.OrderModelDao#getOrder(java.util.Date)
	 */
	@Override
	public List<OrderModel> getOrders(final String code) throws EtailNonBusinessExceptions
	{
		List<OrderModel> orderlist = null;
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(MarketplacecommerceservicesConstants.ORDERCODE, code);
		params.put(MarketplacecommerceservicesConstants.ORDERTYPE, MarketplacecommerceservicesConstants.SUBORDER);
		try
		{
			//forming the flexible search query
			final FlexibleSearchQuery orderQuery = new FlexibleSearchQuery(MarketplacecommerceservicesConstants.ORDER_QUERY, params);
			orderlist = flexibleSearchService.<OrderModel> search(orderQuery).getResult();
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
		}
		return orderlist;
	}

	/**
	 * Get order for push notification --- versionId is not null
	 *
	 * @param code
	 * @return OrderModel
	 */
	@Override
	public OrderModel getOrderPushNotification(final String code)
	{
		OrderModel order = null;
		List<OrderModel> orderlist = null;
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(MarketplacecommerceservicesConstants.ORDERCODE, code);
		params.put(MarketplacecommerceservicesConstants.ORDERTYPE, "SubOrder");

		try
		{
			//forming the flexible search query
			final FlexibleSearchQuery orderQuery = new FlexibleSearchQuery(MarketplacecommerceservicesConstants.ORDER_QUERY, params);
			orderlist = flexibleSearchService.<OrderModel> search(orderQuery).getResult();
			if (null != orderlist && !orderlist.isEmpty())
			{
				for (final OrderModel orderModel : orderlist)
				{
					if (orderModel.getVersionID() != null)
					{
						order = orderModel;
					}
				}
				//return order;
			}
			//			else
			//			{
			//				return null;
			//			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
		}
		return order;
	}




	@Override
	public OrderModel updatePickUpDetailsDao(final String orderId, final String name, final String mobile)
	{

		OrderModel orderModel = new OrderModel();

		try
		{
			orderModel.setCode(orderId);
			orderModel = flexibleSearchService.getModelByExample(orderModel);
			LOG.debug(orderId + ":" + name + ":" + mobile);
			LOG.debug("*****************************");
			if (null != name)
			{
				LOG.info("update pickuUpPerson ");
				orderModel.setPickupPersonName(name);
			}
			if (null != mobile)
			{
				LOG.info("update pickuUpPersonMobileNo ");
				orderModel.setPickupPersonMobile(mobile);
			}

			LOG.info("UpdatePickUpDetails ChildOrder ");
			final List<OrderModel> childOrderList = orderModel.getChildOrders();
			for (final OrderModel childOrder : childOrderList)
			{
				childOrder.setPickupPersonMobile(mobile);
				childOrder.setPickupPersonName(name);
				modelService.save(childOrder);
			}

			modelService.save(orderModel);


		}
		catch (final ModelSavingException expection)
		{
			LOG.error("***********EditPickUpDetails" + expection.getMessage());
		}
		return orderModel;

	}

	@Override
	public OrderModel getOrderModel(final String orderId) throws EtailNonBusinessExceptions
	{
		OrderModel orderModel = new OrderModel();
		try
		{
			if (orderId != null)
			{
				orderModel.setCode(orderId);

				orderModel = flexibleSearchService.getModelByExample(orderModel);
			}
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
		}
		return orderModel;
	}





	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.OrderModelDao#getOrderByAgent(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<OrderModel> getOrderByAgent(final CustomerModel customer, final String agentId)
	{
		try
		{
			final String queryString = MarketplacecommerceservicesConstants.ORDER_BY_AGENT;

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter(MarketplacecommerceservicesConstants.USER, customer);
			query.addQueryParameter(MarketplacecommerceservicesConstants.AGENT_ID, agentId);

			return flexibleSearchService.<OrderModel> search(query).getResult();
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
		}
	}

	@Override
	public PointOfServiceModel getPointOfService(final String storeId)
	{

		try
		{
			final String MPL_DELIVERY_ADDRESS_REPORT_QUERY_BY_ORDERID = "SELECT {srm:" + PointOfServiceModel.PK + "}" + " FROM {"
					+ PointOfServiceModel._TYPECODE + " AS srm} " + "WHERE " + "{srm:" + PointOfServiceModel.SLAVEID + "}=?code ";
			if (LOG.isDebugEnabled())
			{
				LOG.debug("In getPointOfService - storeId ***" + storeId);
			}
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(MPL_DELIVERY_ADDRESS_REPORT_QUERY_BY_ORDERID);
			fQuery.addQueryParameter("code", storeId);

			final List<PointOfServiceModel> listOfData = flexibleSearchService.<PointOfServiceModel> search(fQuery).getResult();
			return !listOfData.isEmpty() ? listOfData.get(0) : null;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
		}
	}


	//Added For Bulk Return Cancellation Job For reducing Loops

	@Override
	public List<OrderEntryModel> getOrderCancelData()
	{
		try
		{
			final FlexibleSearchQuery query = new FlexibleSearchQuery(
					MarketplacecommerceservicesConstants.SUBORDER_DATA_FOR_BULK_CANCELLATION);

			return flexibleSearchService.<OrderEntryModel> search(query).getResult();

		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
		}
	}

	/**
	 * It gets the list of Parent Order No and Transaction Id
	 *
	 * @return List<BulkReturnProcessModel>
	 *
	 */
	@Override
	public BulkCancellationProcessModel getAllBulkCancelData(final String transactionID)
	{
		try
		{
			final FlexibleSearchQuery query = new FlexibleSearchQuery(
					MarketplacecommerceservicesConstants.BULK_CANCEL_DATA_QUERY_START);
			query.addQueryParameter(MarketplacecommerceservicesConstants.TRANSACTIONID, transactionID);
			final List<BulkCancellationProcessModel> listOfData = flexibleSearchService.<BulkCancellationProcessModel> search(query)
					.getResult();
			return CollectionUtils.isNotEmpty(listOfData) ? listOfData.get(0) : null;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
		}
	}


}
