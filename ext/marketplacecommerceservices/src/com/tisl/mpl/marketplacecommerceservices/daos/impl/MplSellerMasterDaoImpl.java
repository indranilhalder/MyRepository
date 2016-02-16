/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplSellerMasterDao;
import com.tisl.mpl.model.SellerMasterModel;


/**
 * @author TCS
 *
 */
public class MplSellerMasterDaoImpl implements MplSellerMasterDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	private static final Logger LOG = Logger.getLogger(MplSellerMasterDaoImpl.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.MplSellerInformationDAO#getSellerInforationDetails(java.lang.String)
	 */
	@Override
	public SellerMasterModel getSellerMaster(final String sellerID)
	{
		try
		{
			LOG.info("seller id" + sellerID);
			final String queryString = //
			"SELECT {c:" + SellerMasterModel.PK + "}" //
					+ "FROM {" + SellerMasterModel._TYPECODE + " AS c} "//
					+ "WHERE " + "{c:" + SellerMasterModel.ID + "}=?sellerID";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("sellerID", sellerID);

			if (flexibleSearchService.<SellerMasterModel> search(query).getCount() > 0
					&& flexibleSearchService.<SellerMasterModel> search(query).getResult() != null)
			{
				return flexibleSearchService.<SellerMasterModel> search(query).getResult().get(0);
			}

			return null;

		}

		catch (final Exception ex)
		{
			LOG.debug("Exception is : " + ex);
			throw new EtailNonBusinessExceptions(ex);
		}
	}
}
