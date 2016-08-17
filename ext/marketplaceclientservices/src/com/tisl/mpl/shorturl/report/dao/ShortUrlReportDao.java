/**
 *
 */
package com.tisl.mpl.shorturl.report.dao;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.TULShortUrlReportModel;


/**
 * @author Techouts
 *
 */
public interface ShortUrlReportDao
{
	/**
	 * @param orderid
	 * @return TULShortUrlReportModel
	 */
	TULShortUrlReportModel getShortUrlReportModelByOrderId(final String orderid);

	/**
	 * @param fromDate
	 * @param toDate
	 * @return List<TULShortUrlReportModel
	 */
	List<TULShortUrlReportModel> getShortUrlReportModels(Date fromDate, Date toDate);
}
