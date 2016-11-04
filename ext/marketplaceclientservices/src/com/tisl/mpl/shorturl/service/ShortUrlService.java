/**
 *
 */
package com.tisl.mpl.shorturl.service;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.OrderShortUrlInfoModel;


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
	String genearateShortURL(final String orderCode);

	/**
	 * @param orderid
	 * @return TULShortUrlReportModel
	 */
	OrderShortUrlInfoModel getShortUrlReportModelByOrderId(String orderid);

	/**
	 * @param fromDate
	 * @param toDate
	 * @return List<TULShortUrlReportModel>
	 */
	List<OrderShortUrlInfoModel> getShortUrlReportModels(Date fromDate, Date toDate);

}
