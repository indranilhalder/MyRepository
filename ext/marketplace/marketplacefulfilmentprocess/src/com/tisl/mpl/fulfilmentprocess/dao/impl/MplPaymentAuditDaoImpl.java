/**
 *
 */
package com.tisl.mpl.fulfilmentprocess.dao.impl;

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.fulfilmentprocess.constants.MarketplaceFulfilmentProcessConstants;
import com.tisl.mpl.fulfilmentprocess.dao.MplPaymentAuditDao;


/**
 * @author TCS
 *
 */
public class MplPaymentAuditDaoImpl implements MplPaymentAuditDao
{

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	private static final Logger LOG = Logger.getLogger(MplPaymentAuditDaoImpl.class);


	/**
	 * This method fetches the audit list based on the cartGUID
	 *
	 * @param cartGUID
	 * @return MplPaymentAuditModel
	 */
	@Override
	public MplPaymentAuditModel getAuditList(final String cartGUID)
	{
		try
		{
			MplPaymentAuditModel mplPaymentAudit = new MplPaymentAuditModel();
			final String queryString = MarketplaceFulfilmentProcessConstants.AUDITWITHGUIDQUERY;

			//forming the flexible search query
			final FlexibleSearchQuery auditWithGUIDQuery = new FlexibleSearchQuery(queryString);
			auditWithGUIDQuery.addQueryParameter(MarketplaceFulfilmentProcessConstants.CARTGUID, cartGUID);

			//fetching list of Audit Entries from DB using flexible search query
			final List<MplPaymentAuditModel> mplPaymentAuditList = flexibleSearchService.<MplPaymentAuditModel> search(
					auditWithGUIDQuery).getResult();
			if (null != mplPaymentAuditList && !mplPaymentAuditList.isEmpty())
			{
				mplPaymentAudit = mplPaymentAuditList.get(0);
				return mplPaymentAudit;
			}
			else
			{
				return null;
			}
		}
		catch (final IllegalArgumentException ex)
		{
			LOG.error(MarketplaceFulfilmentProcessConstants.EXCEPTION_IS + ex);
			throw new IllegalArgumentException(ex);
		}
		catch (final UnknownIdentifierException ex)
		{
			LOG.error(MarketplaceFulfilmentProcessConstants.EXCEPTION_IS + ex);
			throw new UnknownIdentifierException(ex);
		}
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
