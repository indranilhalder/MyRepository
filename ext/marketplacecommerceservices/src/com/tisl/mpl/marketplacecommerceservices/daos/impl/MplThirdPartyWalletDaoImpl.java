/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.core.model.ThirdPartyAuditModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplThirdPartyWalletDao;
import com.tisl.mpl.model.MplConfigurationModel;


/**
 * @author 682160
 *
 */
public class MplThirdPartyWalletDaoImpl implements MplThirdPartyWalletDao
{

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MplThirdPartyWalletDaoImpl.class.getName());

	@Autowired
	private FlexibleSearchService flexibleSearchService;



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplThirdPartyWalletDao#getCronDetails(java.lang.String)
	 */
	@Override
	public MplConfigurationModel getCronDetails(final String code)
	{
		final String queryString = //
		"SELECT {cm:" + MplConfigurationModel.PK
				+ "} "//
				+ MarketplacecommerceservicesConstants.QUERYFROM + MplConfigurationModel._TYPECODE + " AS cm } where" + "{cm."
				+ MplConfigurationModel.MPLCONFIGCODE + "} = ?code";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(MarketplacecommerceservicesConstants.CODE, code);
		return getFlexibleSearchService().<MplConfigurationModel> searchUnique(query);
	}

	@Override
	public BaseStoreModel getJobTAT()
	{
		final String queryString = //
		"SELECT {bs:" + BaseStoreModel.PK
				+ "} "//
				+ MarketplacecommerceservicesConstants.QUERYFROM + BaseStoreModel._TYPECODE + " AS bs } where" + "{bs."
				+ BaseStoreModel.UID + "} = ?code";

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter(MarketplacecommerceservicesConstants.CODE, MarketplacecommerceservicesConstants.BASESTORE_UID);
		return getFlexibleSearchService().<BaseStoreModel> searchUnique(query);
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

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.daos.MplThirdPartyWalletDao#fetchSpecificAuditTableData(java.util.Date,
	 * java.util.Date)
	 */
	@Override
	public List<MplPaymentAuditModel> fetchSpecificAuditTableData(final Date mplConfigDate, final Date startTime)
	{
		final String queryString = //
		"SELECT {j:"
				+ MplPaymentAuditModel.PK
				+ "} "//
				+ MarketplacecommerceservicesConstants.QUERYFROM + MplPaymentAuditModel._TYPECODE + " AS j} where " + "{j."
				+ ThirdPartyAuditModel.MODIFIEDTIME + "} <= ?earlierDate  and " + "{j." + MplPaymentAuditModel.ISEXPIRED
				+ "} = ?expiredData";

		LOG.debug("earlierDate" + mplConfigDate);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("earlierDate", mplConfigDate);
		query.addQueryParameter("expiredData", MarketplacecommerceservicesConstants.THIRDPARTYWALLET_ENTRY_EXPIRED);
		return getFlexibleSearchService().<MplPaymentAuditModel> search(query).getResult();
	}
}
