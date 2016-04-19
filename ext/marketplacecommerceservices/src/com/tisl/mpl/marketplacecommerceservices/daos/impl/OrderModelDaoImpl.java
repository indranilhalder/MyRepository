/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		List<OrderModel> orderlist = null;
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put(MarketplacecommerceservicesConstants.ORDERCODE, code);
		params.put(MarketplacecommerceservicesConstants.ORDERTYPE, MarketplacecommerceservicesConstants.SUBORDER);

		try
		{
			//forming the flexible search query
			final FlexibleSearchQuery orderQuery = new FlexibleSearchQuery(MarketplacecommerceservicesConstants.ORDER_QUERY, params);
			orderlist = flexibleSearchService.<OrderModel> search(orderQuery).getResult();
			if (null != orderlist && !orderlist.isEmpty())
			{
				for (final OrderModel orderModel : orderlist)
				{
					if (orderModel.getVersionID() == null)
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
}