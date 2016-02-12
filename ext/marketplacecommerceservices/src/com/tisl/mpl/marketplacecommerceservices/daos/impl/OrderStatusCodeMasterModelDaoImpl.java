/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.OrderStatusCodeMasterModelDao;
import com.tisl.mpl.model.OrderStatusCodeMasterModel;


/**
 * @author TCS
 *
 */
@Component(value = "orderStatusCodeMasterModelDao")
public class OrderStatusCodeMasterModelDaoImpl implements OrderStatusCodeMasterModelDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.OrderStatusCodeMasterModelDao#getOrderStatusCodeMaster(java.lang
	 * .String)
	 */
	@Override
	public OrderStatusCodeMasterModel getOrderStatusCodeMaster(final String statusCode) throws EtailNonBusinessExceptions
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("code", statusCode);
		try
		{
			final FlexibleSearchQuery query = new FlexibleSearchQuery(MarketplacecommerceservicesConstants.ORDER_STATUS_QUERY,
					params);
			if (flexibleSearchService.<OrderStatusCodeMasterModel> search(query).getResult().size() > 0)
			{
				return flexibleSearchService.<OrderStatusCodeMasterModel> search(query).getResult().get(0);
			}
			else
			{
				return null;
			}
		}
		/*
		 * catch (final EtailNonBusinessExceptions e) { throw new EtailNonBusinessExceptions(e,
		 * MarketplacecommerceservicesConstants.E0000); }
		 */
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.OrderStatusCodeMasterModelDao#getOrderStatusCodeMasterList()
	 */
	@Override
	public List<OrderStatusCodeMasterModel> getOrderStatusCodeMasterList() throws EtailNonBusinessExceptions
	{
		try
		{
			final FlexibleSearchQuery query = new FlexibleSearchQuery(MarketplacecommerceservicesConstants.ORDER_STATUS_QUERY_ALL);

			return flexibleSearchService.<OrderStatusCodeMasterModel> search(query).getResult();
		}
		/*
		 * catch (final EtailNonBusinessExceptions e) { throw new EtailNonBusinessExceptions(e); }
		 */
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e);
		}
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