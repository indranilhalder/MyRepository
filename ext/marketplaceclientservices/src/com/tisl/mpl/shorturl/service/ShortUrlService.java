/**
 *
 */
package com.tisl.mpl.shorturl.service;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.TULShortUrlReportModel;


/**
 * @author Techouts
 *
 *         Interface for short URL service
 */
public interface ShortUrlService
{

	/**
	 * @param orderCode
	 * @return String
	 */
	String genearateShorterURL(final String orderCode);

	/**
	 * @param orderid
	 * @return TULShortUrlReportModel
	 */
	TULShortUrlReportModel getShortUrlReportModelByOrderId(String orderid);

	/**
	 * @param fromDate
	 * @param toDate
	 * @return List<TULShortUrlReportModel>
	 */
	List<TULShortUrlReportModel> getShortUrlReportModels(Date fromDate, Date toDate);

}
