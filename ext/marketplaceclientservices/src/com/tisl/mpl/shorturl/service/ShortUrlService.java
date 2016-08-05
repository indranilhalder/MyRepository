/**
 *
 */
package com.tisl.mpl.shorturl.service;

import com.tisl.mpl.core.model.TULShortUrlReportModel;


/**
 * @author techouts
 *
 *         Interface for short url service
 */
public interface ShortUrlService
{

	public abstract String genearateShorterURL(final String orderCode);

	public abstract TULShortUrlReportModel getShortUrlReportModelByOrderId(String orderid);

}
