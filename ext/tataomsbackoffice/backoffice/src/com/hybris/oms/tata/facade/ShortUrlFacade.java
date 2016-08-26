/**
 *
 */
package com.hybris.oms.tata.facade;

import java.util.Date;
import java.util.List;

import com.techouts.backoffice.ShortUrlReportData;


/**
 * this class is used for short url report purpose
 * 
 * @author prabhakar
 */
public interface ShortUrlFacade
{

	/**
	 * @param fromDate
	 * @param toDate
	 * @return List<TULShortUrlReportModel>
	 */
	List<ShortUrlReportData> getShortUrlReportModels(Date fromDate, Date toDate);


}
