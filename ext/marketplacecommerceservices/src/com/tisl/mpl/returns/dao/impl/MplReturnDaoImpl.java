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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.MplCustomerBankAccountDetailsModel;
import com.tisl.mpl.core.model.MplReturnPickUpAddressInfoModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.returns.dao.MplReturnsDao;
import com.tisl.mpl.model.CRMTicketDetailModel;


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
	
	private static final String RETURN_REPORT_QUERY_BETWEEN_TWO_DATES = "SELECT {srm:" + MplReturnPickUpAddressInfoModel.PK + "}"
			+ " FROM {" + MplReturnPickUpAddressInfoModel._TYPECODE + " AS srm} " + "WHERE " + "{srm:"
			+ MplReturnPickUpAddressInfoModel.CREATIONTIME + "} between ?fromDate and ?toDate ";

	private static final String CRM_TICKET_MODEL_QUERY_BY_TRANSACTIONID = "SELECT {srm:" + CRMTicketDetailModel.PK + "}"
			+ " FROM {" + CRMTicketDetailModel._TYPECODE + " AS srm} " + "WHERE " + "{srm:" + CRMTicketDetailModel.TRANSACTIONID
			+ "}=?code " + " AND ({srm:" + CRMTicketDetailModel.TICKETID + "}) IS NOT NULL" + " ORDER BY {srm:"
			+ CRMTicketDetailModel.CREATIONTIME + "} DESC";



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

	@Override
	public List<MplReturnPickUpAddressInfoModel> getPickUpReturnReportByDates(final Date fromDate, final Date toDate)
	{
		try
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("In getPickUpAddressReturn - fromDate: =" + fromDate + "todate :=" + toDate);
			}
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(RETURN_REPORT_QUERY_BETWEEN_TWO_DATES);
			fQuery.addQueryParameter("fromDate", fromDate);
			fQuery.addQueryParameter("toDate", toDate);
			return flexibleSearchService.<MplReturnPickUpAddressInfoModel> search(fQuery).getResult();
		}
		catch (final Exception e)
		{
			LOG.error("Ërror while Sending ReturnPickUpAddressReturn models between From Date:" + fromDate + "toDate:" + toDate);
		}
		return null;
	}

	@Override
	public List<MplReturnPickUpAddressInfoModel> getPickUpReturnReportByParams(final String orderID, final String customerId,
			final String pincode)
	{


		String queryString = null;
		String queryPram = null;
		if (orderID != null&&StringUtils.isNotBlank(orderID)&&StringUtils.isNotEmpty(orderID))
		{
			queryString = "SELECT {srm:" + MplReturnPickUpAddressInfoModel.PK + "}" + " FROM {"
					+ MplReturnPickUpAddressInfoModel._TYPECODE + " AS srm} " + "WHERE " + "{srm:"
					+ MplReturnPickUpAddressInfoModel.ORDERID + "}=?code ";
			queryPram = orderID;
		}
		else if (customerId != null&&StringUtils.isNotBlank(customerId)&&StringUtils.isNotEmpty(customerId))
		{
			queryString = "SELECT {srm:" + MplReturnPickUpAddressInfoModel.PK + "}" + " FROM {"
					+ MplReturnPickUpAddressInfoModel._TYPECODE + " AS srm} " + "WHERE " + "{srm:"
					+ MplReturnPickUpAddressInfoModel.CUSTOMERID + "}=?code ";
			queryPram = customerId;
		}
		else
		{
			queryString = "SELECT {srm:" + MplReturnPickUpAddressInfoModel.PK + "}" + " FROM {"
					+ MplReturnPickUpAddressInfoModel._TYPECODE + " AS srm} " + "WHERE " + "{srm:"
					+ MplReturnPickUpAddressInfoModel.PINCODE + "}=?code ";
			queryPram = pincode;
		}
		try
		{
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryString);
			fQuery.addQueryParameter("code", queryPram);
			return flexibleSearchService.<MplReturnPickUpAddressInfoModel> search(fQuery).getResult();
		}
		catch (final Exception e)
		{
			LOG.error("MplReturnDaoImpl::getPickUpReturnReportByParams" + e.getMessage());
		}
		return null;
	}
	
	
	//get CRM Ticket Details
	@Override
	public CRMTicketDetailModel getCRMTicketDetail(String transactionId)
	{
		try
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("In getCRMTicketDetail - orderCode ***" + transactionId);
			}
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(CRM_TICKET_MODEL_QUERY_BY_TRANSACTIONID);
			fQuery.addQueryParameter("code", transactionId);

			final List<CRMTicketDetailModel> listOfData = flexibleSearchService.<CRMTicketDetailModel> search(fQuery).getResult();
			return !listOfData.isEmpty() ? listOfData.get(0) : null;
		}
		catch (final Exception e)
		{
			LOG.error("ï¿½rror while searching for  CRMTicketDetail   model for order  id" + transactionId);
		}
		return null;


 }

}
