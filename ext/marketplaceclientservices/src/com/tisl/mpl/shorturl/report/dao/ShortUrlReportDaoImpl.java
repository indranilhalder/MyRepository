/**
 *
 */
package com.tisl.mpl.shorturl.report.dao;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.TULShortUrlReportModel;


/**
 * @author Techouts
 *
 *         Class for managing the Short Url report model
 */
public class ShortUrlReportDaoImpl implements ShortUrlReportDao
{
	private static final Logger LOG = Logger.getLogger(ShortUrlReportDaoImpl.class);
	private static final String SHORT_URL_REPORT_QUERY = "SELECT {srm:" + TULShortUrlReportModel.PK + "}" + "FROM {"
			+ TULShortUrlReportModel._TYPECODE + " AS srm} " + "WHERE " + "{srm:" + TULShortUrlReportModel.ORDERID + "}=?code ";

	@Autowired
	private FlexibleSearchService flexibleSearchService;


	/**
	 * @Description Get the report model by order id
	 * @param orderCode
	 * @return TULShortUrlReportModel
	 */
	@Override
	public TULShortUrlReportModel getShortUrlReportModelByOrderId(final String orderCode)
	{
		try
		{
			final FlexibleSearchQuery query = new FlexibleSearchQuery(SHORT_URL_REPORT_QUERY);
			query.addQueryParameter("code", orderCode);

			final List<TULShortUrlReportModel> listOfData = flexibleSearchService.<TULShortUrlReportModel> search(query).getResult();
			return !listOfData.isEmpty() ? listOfData.get(0) : null;
		}
		catch (final Exception e)
		{
			LOG.error("Ërror while searching for Short Report model for order  id" + orderCode);
		}
		return null;
	}
}
