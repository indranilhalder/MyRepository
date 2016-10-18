/**
 *
 */
package com.tisl.mpl.promotion.dao.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tisl.mpl.core.model.BinErrorModel;
import com.tisl.mpl.promotion.dao.MplBinErrorDao;


/**
 * @author TCS
 *
 */
@Component(value = "MplBinErrorDao")
public class MplBinErrorDaoImpl implements MplBinErrorDao
{
	private static final Logger LOG = Logger.getLogger(MplBinErrorDaoImpl.class);

	//private static final String Err = "{err."; // Blocked for SONAR FIX
	private static final String QUERY_FROM = "FROM {";

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<BinErrorModel> getBinErrorDetails()
	{
		LOG.debug("Fetching Bin Error Details");
		final String queryString = //
		"SELECT {err:" + BinErrorModel.PK + "} "//
				+ QUERY_FROM + BinErrorModel._TYPECODE + " AS err }";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		return flexibleSearchService.<BinErrorModel> search(query).getResult();
	}
}
