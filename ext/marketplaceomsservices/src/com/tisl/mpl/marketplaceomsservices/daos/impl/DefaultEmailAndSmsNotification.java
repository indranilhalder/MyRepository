/**
 *
 */
package com.tisl.mpl.marketplaceomsservices.daos.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplaceomsservicesConstants;
import com.tisl.mpl.core.model.OrderUpdateProcessModel;
import com.tisl.mpl.core.model.OrderUpdateSmsProcessModel;
import com.tisl.mpl.marketplaceomsservices.daos.EmailAndSmsNotification;


/**
 * @author TCS
 *
 */
public class DefaultEmailAndSmsNotification implements EmailAndSmsNotification
{
	private static final Logger log = Logger.getLogger(DefaultEmailAndSmsNotification.class.getName());

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/**
	 * @return the flexibleSearchService
	 */


	/**
	 * @param awbNumber
	 * @param shipmentStatus
	 * @return OrderUpdateProcessModel
	 * @description This method is used to check whether an email is sent corresponding to the awbNumber taken as
	 *              parameter
	 */

	@Override
	public List<OrderUpdateProcessModel> checkEmailSent(final String awbNumber, final ConsignmentStatus shipmentStatus)
	{

		if (null != awbNumber && !awbNumber.isEmpty() && null != shipmentStatus)
		{
			final String queryString = //
			"SELECT" + MarketplaceomsservicesConstants.QUERYPART
					+ OrderUpdateProcessModel.PK
					+ "}" //
					+ "FROM {"
					+ OrderUpdateProcessModel._TYPECODE
					+ " AS p} "//
					+ "WHERE " + MarketplaceomsservicesConstants.QUERYPART + OrderUpdateProcessModel.AWBNUMBER + "}=?awbNumber"
					+ " AND " + MarketplaceomsservicesConstants.QUERYPART + OrderUpdateProcessModel.SHIPMENTSTATUS
					+ "}=?shipmentStatus";

			log.debug("checkEmailSent >> queryString:" + queryString);
			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("awbNumber", awbNumber);
			query.addQueryParameter("shipmentStatus", shipmentStatus);

			return flexibleSearchService.<OrderUpdateProcessModel> search(query).getResult();
		}
		else
		{
			return null;
		}

	}

	/**
	 * @param awbNumber
	 * @param shipmentStatus
	 * @return OrderUpdateProcessModel
	 * @description This method is used to check whether an SMS is sent corresponding to the awbNumber taken as parameter
	 */
	@Override
	public List<OrderUpdateSmsProcessModel> checkSmsSent(final String awbNumber, final ConsignmentStatus shipmentStatus)
	{
		if (null != awbNumber && !awbNumber.isEmpty())
		{
			final String queryString = //
			"SELECT" + MarketplaceomsservicesConstants.QUERYPART
					+ OrderUpdateSmsProcessModel.PK
					+ "}" //
					+ "FROM {"
					+ OrderUpdateSmsProcessModel._TYPECODE
					+ " AS p} "//
					+ "WHERE " + MarketplaceomsservicesConstants.QUERYPART + OrderUpdateSmsProcessModel.AWBNUMBER + "}=?awbNumber"
					+ " AND " + MarketplaceomsservicesConstants.QUERYPART + OrderUpdateSmsProcessModel.SHIPMENTSTATUS
					+ "}=?shipmentStatus";

			log.debug("checkSmsSent >> queryString:" + queryString);

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("awbNumber", awbNumber);
			query.addQueryParameter("shipmentStatus", shipmentStatus);
			return flexibleSearchService.<OrderUpdateSmsProcessModel> search(query).getResult();
		}
		else
		{
			return null;
		}

	}
}
