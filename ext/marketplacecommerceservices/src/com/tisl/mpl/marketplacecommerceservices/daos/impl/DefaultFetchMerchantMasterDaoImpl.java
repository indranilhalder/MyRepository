/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.model.MerchantMasterTableModel;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */

public class DefaultFetchMerchantMasterDaoImpl
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(DefaultFetchMerchantMasterDaoImpl.class.getName());

	@Autowired
	private FlexibleSearchService flexibleSearchService;


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


	/**
	 * @Description : Fetch Cron Configuration Details
	 * @param: code
	 * @return : MplConfigurationModel
	 */

	public MplConfigurationModel getCronDetails(final String code)
	{
		final String queryString = //
		"SELECT {p:" + MplConfigurationModel.PK
				+ "} "//
				+ "FROM {" + MplConfigurationModel._TYPECODE + " AS p } where" + "{p." + MplConfigurationModel.MPLCONFIGCODE
				+ "} = ?code";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("code", code);
		return flexibleSearchService.<MplConfigurationModel> searchUnique(query);
	}


	/**
	 * @Description : Fetch Merchant Master Table details
	 * @return : List<MerchantMasterTableModel>
	 */

	public List<MerchantMasterTableModel> fetchDetails()
	{

		final String queryString = //
		"SELECT {p:" + MerchantMasterTableModel.PK + "} "//
				+ "FROM {" + MerchantMasterTableModel._TYPECODE + " AS p }";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		return flexibleSearchService.<MerchantMasterTableModel> search(query).getResult();
	}


	/*	*//**
	 * @Description : Fetch Parent Order Details within stipulated Date Range
	 * @return : List<OrderModel>
	 */
	/*
	 *
	 * public List<OrderModel> fetchSpecifiedData(final Date startTime, final Date endTime) { final String queryString =
	 * // "SELECT {p:" + OrderModel.PK + "} "// + "FROM {" + OrderModel._TYPECODE + " AS p} where " + "{p." +
	 * OrderModel.MODIFIEDTIME + "} BETWEEN ?earlierDate and ?presentDate and " + "{p." + OrderModel.TYPE + "} = ?type";
	 *
	 * LOG.debug("earlierDate" + startTime); LOG.debug("presentDate" + endTime); final FlexibleSearchQuery query = new
	 * FlexibleSearchQuery(queryString); query.addQueryParameter("earlierDate", startTime);
	 * query.addQueryParameter("presentDate", endTime); query.addQueryParameter("type", "Parent"); return
	 * flexibleSearchService.<OrderModel> search(query).getResult(); }
	 */

}
