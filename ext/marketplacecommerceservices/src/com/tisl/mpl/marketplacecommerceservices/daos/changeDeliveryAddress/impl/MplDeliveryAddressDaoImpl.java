/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress.MplDeliveryAddressDao;



/**
 * @author pankajk
 *
 */
public class MplDeliveryAddressDaoImpl implements MplDeliveryAddressDao
{

	private static final Logger LOG = Logger.getLogger(MplDeliveryAddressDaoImpl.class);


	@Autowired
	private FlexibleSearchService flexibleSearchService;
	@Autowired
	private ModelService modelService;

	@Override
	public List<OrderModel> getOrderModelList(Date fromDate,Date toDate)
	{
		List<OrderModel> orderModelList = null;
		try
		{
			ServicesUtil.validateParameterNotNull(fromDate, "dateFrom must not be null");
			ServicesUtil.validateParameterNotNull(toDate, "dateTo must not be null");
			
			final String SHORT_URL_REPORT_QUERY_BETWEEN_TWO_DATES = "SELECT {cdrm:" + OrderModel.PK + "}" + " FROM {"
					+ OrderModel._TYPECODE + " AS cdrm} " + "WHERE " + "{cdrm:" + OrderModel.MODIFIEDTIME
					+ "} between ?fromDate and ?toDate and" + "{cdrm:" + OrderModel.CHANGEDELIVERYTOTALREQUESTS + "}  >=1";

			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(SHORT_URL_REPORT_QUERY_BETWEEN_TWO_DATES);
			fQuery.addQueryParameter("fromDate", fromDate);
			fQuery.addQueryParameter("toDate", toDate);
			final SearchResult<OrderModel> searchResult = flexibleSearchService.search(fQuery);
			orderModelList = searchResult.getResult();
			return !orderModelList.isEmpty() ? orderModelList : null;

		}
		catch (final FlexibleSearchException e)
		{
			LOG.error(" FlexibleSearch Exception while geting the temprory Address  " + e.getMessage());
		}
		catch (final Exception e)
		{
			LOG.error("Exception occurred while  getting the temparory address " + e.getMessage());
		}
		return null;
	}

}
