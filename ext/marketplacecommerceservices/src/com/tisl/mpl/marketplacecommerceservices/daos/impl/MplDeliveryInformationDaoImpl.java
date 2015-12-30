/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplDeliveryInformationDao;


/**
 * @author TCS
 *
 */
public class MplDeliveryInformationDaoImpl implements MplDeliveryInformationDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;


	private static final Logger LOG = Logger.getLogger(MplDeliveryInformationDaoImpl.class);


	/**
	 *
	 */
	@Override
	public List<DeliveryModeModel> getDeliveryInformation(final List<String> code) throws EtailNonBusinessExceptions
	{
		String codes = getIds(code);
		if (null != codes && codes.length() > 0)
		{
			codes = codes.substring(0, codes.length() - 1);
		}

		final String queryString = //
		"SELECT {c:" + DeliveryModeModel.PK + "} " + "FROM {" + DeliveryModeModel._TYPECODE + " AS c} "//
				+ "WHERE {c:" + DeliveryModeModel.CODE + "} IN (" + codes + ")";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		LOG.info("Query:::::::::::" + queryString);
		final List<DeliveryModeModel> deliveryInformationList = flexibleSearchService.<DeliveryModeModel> search(query).getResult();

		return deliveryInformationList;

	}

	/**
	 *
	 * @param code
	 */
	public static String getIds(final List<String> code)
	{
		String ids = null;
		if (null != code && !code.isEmpty())
		{
			final StringBuilder stringBuilder = new StringBuilder();
			for (final String id : code)
			{
				//stringBuilder.append("'").append(id).append("',"); Avoid appending characters as strings
				stringBuilder.append('\'').append(id).append('\'').append(',');
			}

			ids = stringBuilder.toString();
		}

		return ids;
	}

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}
}
