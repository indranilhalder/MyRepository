/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplWebhookReportDao;


/**
 * @author TCS
 *
 */
public class MplWebhookReportDaoImpl implements MplWebhookReportDao
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MplWebhookReportDaoImpl.class.getName());

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/**
	 * @Description : Returns data within a date range
	 * @param startDate
	 * @param endDate
	 * @return List<MplPaymentAuditEntryModel>
	 */
	@Override
	public List<MplPaymentAuditModel> getSpecificAuditEntries(final Date startDate, final Date endDate)
	{
		final String queryString = //
		"SELECT {p:" + MplPaymentAuditModel.PK
				+ "} "//
				+ "FROM {" + MplPaymentAuditModel._TYPECODE + " AS p} where " + "{p." + MplPaymentAuditModel.CREATIONTIME
				+ "} BETWEEN ?startDate and ?endDate  ";

		LOG.debug("Fetching Audit Entries within Range");
		LOG.debug("STARTDATE" + startDate);
		LOG.debug("ENDDATE" + endDate);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		query.addQueryParameter("startDate", startDate);
		query.addQueryParameter("endDate", endDate);
		return getFlexibleSearchService().<MplPaymentAuditModel> search(query).getResult();
	}

	/**
	 * @Description : Returns refund type for a refund request
	 * @param refundId
	 * @return RefundTransactionMappingModel
	 */
	@Override
	public RefundTransactionMappingModel getRefundType(final String refundId)
	{
		try
		{
			final String queryString = //
			"SELECT {rtm:" + RefundTransactionMappingModel.PK
					+ "} "//
					+ "FROM {" + RefundTransactionMappingModel._TYPECODE + " AS rtm} where " + "{rtm."
					+ RefundTransactionMappingModel.JUSPAYREFUNDID + "} = ?refundId ";

			final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
			query.addQueryParameter("refundId", refundId);
			return getFlexibleSearchService().<RefundTransactionMappingModel> searchUnique(query);
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final ModelNotFoundException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0008);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final NullPointerException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0008);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
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
