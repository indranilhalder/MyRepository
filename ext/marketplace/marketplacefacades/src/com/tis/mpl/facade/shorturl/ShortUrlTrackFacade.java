/**
 *
 */
package com.tis.mpl.facade.shorturl;

import java.util.Date;
import java.util.List;


/**
 * this class is used for short url report purpose
 *
 * @author prabhakar
 */
public interface ShortUrlTrackFacade
{

	/**
	 * @param fromDate
	 * @param toDate
	 * @return List<TULShortUrlReportModel>
	 */
	List<ShortUrlReportData> getShortUrlReportModels(Date fromDate, Date toDate);


}
