/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tisl.mpl.marketplacecommerceservices.daos.FetchCustomerDetailsDao;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author TCS
 *
 */
@Component(value = "fetchCustomerDetailsDao")
public class FetchCustomerDetailsDaoImpl implements FetchCustomerDetailsDao
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(FetchCustomerDetailsDaoImpl.class.getName());

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
	 * @Description : Fetch Details corresponding to the code
	 * @param: code
	 * @return: MplConfigurationModel
	 */
	@Override
	public MplConfigurationModel fetchConfigDetails(final String code)
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
	 * @Description : Fetch Customer Data
	 * @param: void
	 * @return: List<CustomerModel>
	 */
	@Override
	public List<CustomerModel> fetchCustomerDetails()
	{
		final String queryString = //
		"SELECT {p:" + CustomerModel.PK + "} "//
				+ "FROM {" + CustomerModel._TYPECODE + " AS p} ";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		return flexibleSearchService.<CustomerModel> search(query).getResult();
	}

	/**
	 * @Description : Fetch Customer Data
	 * @param: void
	 * @return: List<CustomerModel>
	 */
	@Override
	public List<CustomerModel> specificCustomerDetails(final Date earlierDate, final Date presentDate)
	{
		/*
		 * final StringBuilder query = new StringBuilder("SELECT DISTINCT {cur:" + CustomerModel.PK + "} ");
		 * query.append(" FROM {" + CustomerModel._TYPECODE + " AS cur "); query.append("LEFT JOIN " +
		 * AddressModel._TYPECODE + "  AS adr  ON {cur:" + CustomerModel.PK + "}={adr:" + AddressModel.OWNER + "} ");
		 * 
		 * query.append("} WHERE {cur:" + CustomerModel.MODIFIEDTIME + "}  BETWEEN ?earlierDate and ?presentDate");
		 * query.append(" OR {adr:" + AddressModel.MODIFIEDTIME + "} BETWEEN ?earlierDate and ?presentDate");
		 */
		final StringBuilder query = new StringBuilder(500);
		query.append("SELECT DISTINCT {cur:" + CustomerModel.PK + "} ");
		query.append(" FROM {" + CustomerModel._TYPECODE + " AS cur ");
		query.append("LEFT JOIN " + AddressModel._TYPECODE + "  AS adr  ON {cur:" + CustomerModel.PK + "}={adr:"
				+ AddressModel.OWNER + "} ");
		query.append("} WHERE {cur:" + CustomerModel.MODIFIEDTIME + "}  BETWEEN ?earlierDate and ?presentDate");
		query.append(" OR {adr:" + AddressModel.MODIFIEDTIME + "} BETWEEN ?earlierDate and ?presentDate");

		final Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("earlierDate", earlierDate);
		params.put("presentDate", presentDate);

		final SearchResult<CustomerModel> searchRes = flexibleSearchService.search(query.toString(), params);
		if (searchRes != null && searchRes.getCount() > 0)
		{
			return searchRes.getResult();
		}
		return null;
	}


}
