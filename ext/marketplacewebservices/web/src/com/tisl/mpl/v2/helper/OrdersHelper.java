/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.v2.helper;

import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderHistoriesData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderHistoryListWsDTO;
import de.hybris.platform.commercewebservicescommons.mapping.DataMapper;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.tisl.mpl.constants.MarketplacewebservicesConstants;
import com.tisl.mpl.constants.YcommercewebservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.MplPaymentWebFacade;
import com.tisl.mpl.facades.account.register.MplOrderFacade;
import com.tisl.mpl.marketplacecommerceservices.daos.MplOrderDao;


@Component
public class OrdersHelper extends AbstractHelper
{
	@Resource(name = "orderFacade")
	private OrderFacade orderFacade;
	@Resource(name = "dataMapper")
	protected DataMapper dataMapper;

	@Resource(name = "mplOrderDao")
	MplOrderDao mplOrderDao;

	@Autowired
	private MplOrderFacade mplOrderFacade;

	@Autowired
	MplPaymentWebFacade mplPaymentWebFacade;
	@Autowired
	private ConfigurationService configurationService;

	public static final String MAX_PAGE_LIMIT_TOTAL_ORDER_COUNT_DISPLAY = "orderHistory.max.page.limit.count.display";

	//Sonar fixes
	//private static final String PAGINATION_NUMBER_OF_RESULTS_COUNT = "orderHistory.pagination.number.results.count";

	@Cacheable(value = MarketplacewebservicesConstants.ORDERCACHE, key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(true,true,'DTO',#statuses,#currentPage,#pageSize,#sort,#fields)")
	public OrderHistoryListWsDTO searchOrderHistory(final String statuses, final int currentPage, final int pageSize,
			final String sort, final String fields)
	{
		final OrderHistoriesData orderHistoriesData = searchOrderHistory(statuses, currentPage, pageSize, sort);
		final OrderHistoryListWsDTO dto = dataMapper.map(orderHistoriesData, OrderHistoryListWsDTO.class, fields);
		return dto;
	}

	@Cacheable(value = MarketplacewebservicesConstants.ORDERCACHE, key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(true,true,'Data',#statuses,#currentPage,#pageSize,#sort)")
	public OrderHistoriesData searchOrderHistory(final String statuses, final int currentPage, final int pageSize,
			final String sort)
	{
		final PageableData pageableData = createPageableData(currentPage, pageSize, sort);

		final OrderHistoriesData orderHistoriesData;
		if (statuses != null)
		{
			final Set<OrderStatus> statusSet = extractOrderStatuses(statuses);
			orderHistoriesData = createOrderHistoriesData(
					orderFacade.getPagedOrderHistoryForStatuses(pageableData, statusSet.toArray(new OrderStatus[statusSet.size()])));
		}
		else
		{
			orderHistoriesData = createOrderHistoriesData(orderFacade.getPagedOrderHistoryForStatuses(pageableData));
		}
		return orderHistoriesData;
	}

	/*
	 *
	 */

	/*
	 * @Cacheable(value = "orderCache", key =
	 * "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(true,true,'DTO',#statuses,#currentPage,#pageSize,#sort,#fields)"
	 * ) public OrderHistoryListWsDTO searchOrderHistoryDTO(final int currentPage, final int pageSize, final String sort,
	 * final String userId, final String fields) { final OrderHistoriesData orderHistoriesData =
	 * getParentOrders(currentPage, pageSize, sort, userId); final OrderHistoryListWsDTO dto =
	 * dataMapper.map(orderHistoriesData, OrderHistoryListWsDTO.class, fields); return dto; }
	 */

	public SearchPageData<OrderHistoryData> getParentOrders(final int currentPage, final int pageSize, final String sort)
	{
		//		final OrderHistoriesData orderHistoriesData;
		//TISEE-6323

		final PageableData pageableData = createPageableData(currentPage, pageSize, sort);
		//showing all orders
		final int MAX_PAGE_LIMIT = Integer
				.parseInt(configurationService.getConfiguration().getString(MAX_PAGE_LIMIT_TOTAL_ORDER_COUNT_DISPLAY, "500"));
		pageableData.setPageSize(MAX_PAGE_LIMIT);
		//	final CustomerModel customer = mplPaymentWebFacade.getCustomer(userId);

		//		final SearchPageData<OrderData> searchPageDataParentOrder = mplOrderFacade.getPagedParentOrderHistory(pageableData,
		//				customer);
		final SearchPageData<OrderHistoryData> searchPageDataParentOrder = mplOrderFacade
				.getPagedFilteredParentOrderHistory(pageableData);

		return searchPageDataParentOrder;

	}

	protected Set<OrderStatus> extractOrderStatuses(final String statuses)
	{
		final String statusesStrings[] = statuses.split(YcommercewebservicesConstants.OPTIONS_SEPARATOR);

		final Set<OrderStatus> statusesEnum = new HashSet<>();
		for (final String status : statusesStrings)
		{
			statusesEnum.add(OrderStatus.valueOf(status));
		}
		return statusesEnum;
	}

	protected OrderHistoriesData createOrderHistoriesData(final SearchPageData<OrderHistoryData> result)
	{
		final OrderHistoriesData orderHistoriesData = new OrderHistoriesData();

		orderHistoriesData.setOrders(result.getResults());
		orderHistoriesData.setSorts(result.getSorts());
		orderHistoriesData.setPagination(result.getPagination());

		return orderHistoriesData;
	}

	@Cacheable(value = MarketplacewebservicesConstants.ORDERCACHE, key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(true,true,'DTO',#statuses,#currentPage,#pageSize,#sort,#fields)")
	public OrderHistoryListWsDTO searchOrderHistoryforuser(final String statuses, final int currentPage, final int pageSize,
			final String sort, final String fields)
	{
		try
		{

			final OrderHistoriesData orderHistoriesData = searchOrderHistoryforuser(statuses, currentPage, pageSize, sort);
			final OrderHistoryListWsDTO dto = dataMapper.map(orderHistoriesData, OrderHistoryListWsDTO.class, fields);
			return dto;
		}
		catch (final EtailBusinessExceptions e)
		{
			return null;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			return null;
		}
	}

	@Cacheable(value = MarketplacewebservicesConstants.ORDERCACHE, key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(true,true,'Data',#statuses,#currentPage,#pageSize,#sort)")
	public OrderHistoriesData searchOrderHistoryforuser(final String statuses, final int currentPage, final int pageSize,
			final String sort)
	{
		final PageableData pageableData = createPageableData(currentPage, pageSize, sort);

		final OrderHistoriesData orderHistoriesData;
		try
		{
			if (statuses != null)
			{
				final Set<OrderStatus> statusSet = extractOrderStatusesforuser(statuses);
				orderHistoriesData = createOrderHistoriesDataforuser(orderFacade.getPagedOrderHistoryForStatuses(pageableData,
						statusSet.toArray(new OrderStatus[statusSet.size()])));
			}
			else
			{
				orderHistoriesData = createOrderHistoriesDataforuser(orderFacade.getPagedOrderHistoryForStatuses(pageableData));
			}
			return orderHistoriesData;
		}
		catch (final EtailBusinessExceptions e)
		{
			return null;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			return null;
		}
	}

	protected Set<OrderStatus> extractOrderStatusesforuser(final String statuses)
	{
		final String statusesStrings[] = statuses.split(YcommercewebservicesConstants.OPTIONS_SEPARATOR);

		final Set<OrderStatus> statusesEnum = new HashSet<>();
		try
		{
			for (final String status : statusesStrings)
			{
				statusesEnum.add(OrderStatus.valueOf(status));
			}
			return statusesEnum;
		}
		catch (final EtailBusinessExceptions e)
		{
			return null;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			return null;
		}
	}

	protected OrderHistoriesData createOrderHistoriesDataforuser(final SearchPageData<OrderHistoryData> result)
	{
		final OrderHistoriesData orderHistoriesData = new OrderHistoriesData();
		try
		{
			orderHistoriesData.setOrders(result.getResults());

			return orderHistoriesData;
		}
		catch (final EtailBusinessExceptions e)
		{
			return null;
		}
		catch (final EtailNonBusinessExceptions e)
		{
			return null;
		}
	}
}
