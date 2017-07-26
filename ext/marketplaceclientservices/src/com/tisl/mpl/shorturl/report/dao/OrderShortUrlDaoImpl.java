/**
 *
 */
package com.tisl.mpl.shorturl.report.dao;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.OrderShortUrlInfoModel;


/**
 * @author Techouts
 *
 *         Class for managing the Short Url report model
 */
public class OrderShortUrlDaoImpl implements OrderShortUrlDao

{
	private static final Logger LOG = Logger.getLogger(OrderShortUrlDaoImpl.class);
	private static final String SHORT_URL_REPORT_QUERY_BY_ORDERID = "SELECT {srm:" + OrderShortUrlInfoModel.PK + "}" + " FROM {"
			+ OrderShortUrlInfoModel._TYPECODE + " AS srm} " + "WHERE " + "{srm:" + OrderShortUrlInfoModel.ORDERID + "}=?code ";

	private static final String SHORT_URL_REPORT_QUERY_BETWEEN_TWO_DATES = "SELECT {srm:" + OrderShortUrlInfoModel.PK + "}"
			+ " FROM {" + OrderShortUrlInfoModel._TYPECODE + " AS srm} " + "WHERE " + "{srm:" + OrderShortUrlInfoModel.CREATIONTIME
			+ "} between ?fromDate and ?toDate ";

	@Autowired
	private FlexibleSearchService flexibleSearchService;


	/**
	 * @Description Get the report model by order id
	 * @param orderCode
	 * @return TULShortUrlReportModel
	 */
	@Override
	public OrderShortUrlInfoModel getShortUrlReportModelByOrderId(final String orderCode)
	{
		try
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("In getShortUrlReportModelByOrderId - orderCode ***" + orderCode);
			}
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(SHORT_URL_REPORT_QUERY_BY_ORDERID);
			fQuery.addQueryParameter("code", orderCode);

			LOG.debug("**Query for orderCode " + orderCode + " is " + fQuery.toString());

			final List<OrderShortUrlInfoModel> listOfData = flexibleSearchService.<OrderShortUrlInfoModel> search(fQuery)
					.getResult();

			LOG.debug("Result-set ***" + listOfData + " with value**" + listOfData.isEmpty());

			return !listOfData.isEmpty() ? listOfData.get(0) : null;
		}
		catch (final Exception e)
		{
			LOG.error("Error while searching for Short Report model for order  id" + orderCode);
		}
		return null;
	}


	/**
	 * @Description Get the report models between two dates
	 * @param fromDate
	 * @param toDate
	 * @return TULShortUrlReportModel
	 */
	@Override
	public List<OrderShortUrlInfoModel> getShortUrlReportModels(final Date fromDate, final Date toDate)
	{
		try
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("In getShortUrlReportModels - fromDate: =" + fromDate + "todate :=" + toDate);
			}
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(SHORT_URL_REPORT_QUERY_BETWEEN_TWO_DATES);
			fQuery.addQueryParameter("fromDate", fromDate);
			fQuery.addQueryParameter("toDate", toDate);
			return flexibleSearchService.<OrderShortUrlInfoModel> search(fQuery).getResult();
		}
		catch (final Exception e)
		{
			LOG.error("Error while searching for Short Report models between From Date:"+fromDate +"toDate:"+toDate);
		}
		return null;
	}
}
