/**
 * 
 */
package com.tisl.mpl.returns.dao.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.MplCustomerBankAccountDetailsModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.returns.dao.MplReturnsDao;


/**
 * @author TECHOUTS
 *
 */
public class MplReturnDaoImpl implements MplReturnsDao
{

	private static final Logger LOG = Logger.getLogger(MplReturnDaoImpl.class);
	public static final String CUSTOMER_BANKDETAILS_QUERY = "select {pk} from {MplCustomerBankAccountDetails} where {customerid}=?customerid";
	public static final String CUSTOMER_BANKDETAILS_KEY ="customerid";
	
	public static final String RETURN_REQUEST_QUERY = "select {rr:pk} from {ReturnRequest as rr join Order as o on {o:pk}={rr:order} } where {o:code}=?orderid";
	public static final String RETURN_REQUEST_KEY ="orderid";
	
	@Autowired
	private FlexibleSearchService flexibleSearchService;
	
	/**
	 * Returns Customer Bank details by Id  
	 * 
	 * @param customerId
	 * @return  MplCustomerBankAccountDetailsModel
	 */ 
	@Override
	public MplCustomerBankAccountDetailsModel getCustomerBankDetailsById(String customerId)
	{
		
		ServicesUtil.validateParameterNotNull(customerId, "Id must not be null");
		final Map queryParams = new HashMap();
		final String query = CUSTOMER_BANKDETAILS_QUERY;
		queryParams.put(CUSTOMER_BANKDETAILS_KEY, customerId);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
      try
      {
      	if(LOG.isDebugEnabled())
      	{
      	LOG.debug("Query to fetch Customer bank account details with Customer ID :"+customerId +" Query : "+fQuery);
      	}
		final List<MplCustomerBankAccountDetailsModel> mplConfigModelList = flexibleSearchService.<MplCustomerBankAccountDetailsModel> search(fQuery).getResult();

		if (CollectionUtils.isNotEmpty(mplConfigModelList))
		{
			return mplConfigModelList.get(0);
		}
		else
		{
			return null;
		}
      }
      catch(FlexibleSearchException e)
      {
      	LOG.error("Flexible search Exception"+e);
      	throw new EtailNonBusinessExceptions(e,"Flexible search Exception");
      }
      catch (Exception e) {
      	LOG.error("Exception occured during fetching customer Bank account details with custmer Id :"+customerId+ "Error Trace:" +e);
      	throw new EtailNonBusinessExceptions(e);
		}
	}

	
	@Override
	public List<ReturnRequestModel> getListOfReturnRequest(String orderId)
	{
		
		ServicesUtil.validateParameterNotNull(orderId, "Id must not be null");
		final Map queryParams = new HashMap();
		final String query = RETURN_REQUEST_QUERY;
		queryParams.put(RETURN_REQUEST_KEY, orderId);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
      try
      {
      	if(LOG.isDebugEnabled())
      	{
      	LOG.debug("Query to fetch Customer bank account details with Order ID :"+orderId +" Query : "+fQuery);
      	}
		final List<ReturnRequestModel> returnRequestModelList = flexibleSearchService.<ReturnRequestModel> search(fQuery).getResult();

		if (CollectionUtils.isNotEmpty(returnRequestModelList))
		{
			return returnRequestModelList;
		}
		else
		{
			return null;
		}
      }
      catch(FlexibleSearchException e)
      {
      	LOG.error("Flexible search Exception"+e);
      	throw new EtailNonBusinessExceptions(e,"Flexible search Exception");
      }
      catch (Exception e) {
      	LOG.error("Exception occured during fetching customer Bank account details with custmer Id :"+orderId+ "Error Trace:" +e);
      	throw new EtailNonBusinessExceptions(e);
		}
	}
	
	@Override
	public List<OrderModel> getOrder(String orderId)
	{
		final String query = "SELECT {" + OrderModel.PK + "} FROM {" + OrderModel._TYPECODE + "} WHERE {" + OrderModel.CODE
				+ "}=?orderId";
		final FlexibleSearchQuery flexQuery = new FlexibleSearchQuery(query);
		flexQuery.addQueryParameter("orderId", orderId);
		try
		{
			final SearchResult<OrderModel> orderModel = flexibleSearchService.search(flexQuery);
			if (null != orderModel && null != orderModel.getResult())
			{
				return orderModel.getResult();
			}
			else
			{
				return null;
			}
		}
		catch (final FlexibleSearchException e)
		{
			LOG.error("Flexible search Exception" + e);
			throw new EtailNonBusinessExceptions(e, "Flexible search Exception");
		}
		catch (final Exception e)
		{
			LOG.error("Exception occured during fetching customer Bank account details with custmer Id :" + orderId + "Error Trace:"
					+ e);
			throw new EtailNonBusinessExceptions(e);
		}
	}

}
