/**
 *
 */
package com.tisl.mpl.shorturl.report.dao;

import com.tisl.mpl.core.model.TULShortUrlReportModel;


/**
 * @author Techouts
 *
 */
public interface ShortUrlReportDao
{
	TULShortUrlReportModel getShortUrlReportModelByOrderId(final String orderid);
}
