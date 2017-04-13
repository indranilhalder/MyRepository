/**
 *
 */
package com.tisl.mpl.shorturl.report.dao;

import java.util.Date;
import java.util.List;

import com.tisl.mpl.core.model.OrderShortUrlInfoModel;


/**
 * @author Techouts
 *
 */
public interface OrderShortUrlDao

{
	/**
	 * @param orderid
	 * @return TULShortUrlReportModel
	 */
	OrderShortUrlInfoModel getShortUrlReportModelByOrderId(final String orderid);

	/**
	 * @param fromDate
	 * @param toDate
	 * @return List<TULShortUrlReportModel
	 */
	List<OrderShortUrlInfoModel> getShortUrlReportModels(Date fromDate, Date toDate);
}
