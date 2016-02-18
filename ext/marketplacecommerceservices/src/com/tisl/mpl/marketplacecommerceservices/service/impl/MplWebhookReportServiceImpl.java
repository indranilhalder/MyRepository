/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.MplPaymentAuditStatusEnum;
import com.tisl.mpl.core.model.MplPaymentAuditEntryModel;
import com.tisl.mpl.core.model.MplPaymentAuditModel;
import com.tisl.mpl.core.model.RefundTransactionMappingModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplWebhookReportDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplWebhookReportService;


/**
 * @author TCS
 *
 */
public class MplWebhookReportServiceImpl implements MplWebhookReportService
{
	@Autowired
	private MplWebhookReportDao mplWebhookReportDao;

	/**
	 * @Desctription : This Method returns data within a date range
	 *
	 * @param startDate
	 * @param endDate
	 */
	@Override
	public List<MplPaymentAuditEntryModel> getSpecificAuditEntries(final Date startDate, final Date endDate)
			throws EtailNonBusinessExceptions
	{
		//Dao call to fetch all audit entries within the date range
		final List<MplPaymentAuditModel> audits = getMplWebhookReportDao().getSpecificAuditEntries(startDate, endDate);

		final List<MplPaymentAuditEntryModel> refundAudits = new ArrayList<MplPaymentAuditEntryModel>();
		try
		{
			for (final MplPaymentAuditModel mplPaymentAuditModel : audits)
			{
				Collection<MplPaymentAuditEntryModel> collection = mplPaymentAuditModel.getAuditEntries();
				final List<MplPaymentAuditEntryModel> entryList = new ArrayList<MplPaymentAuditEntryModel>();
				if (null == collection || collection.isEmpty())
				{
					collection = new ArrayList<MplPaymentAuditEntryModel>();
				}
				entryList.addAll(collection);
				final MplPaymentAuditEntryModel entry = entryList.get(entryList.size() - 1);
				if (entry.getStatus() != null
						&& ((entry.getStatus().equals(MplPaymentAuditStatusEnum.REFUND_INITIATED) || (entry.getStatus()
								.equals(MplPaymentAuditStatusEnum.REFUND_UNSUCCESSFUL)))))
				{
					refundAudits.add(entry);
				}
			}

			return refundAudits;
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
	 * @Desctription : This Method fetches the Refund Type from RefundTransactionMapping
	 * @param refundId
	 * @return List<MplPaymentAuditEntryModel>
	 */
	@Override
	public RefundTransactionMappingModel fetchRefundType(final String refundId) throws EtailNonBusinessExceptions
	{
		//Dao call to fetch RefundTransactionMappingModel against refund id
		final RefundTransactionMappingModel rtModel = getMplWebhookReportDao().getRefundType(refundId);

		return rtModel;
	}

	/**
	 * @return the mplWebhookReportDao
	 */
	public MplWebhookReportDao getMplWebhookReportDao()
	{
		return mplWebhookReportDao;
	}

	/**
	 * @param mplWebhookReportDao
	 *           the mplWebhookReportDao to set
	 */
	public void setMplWebhookReportDao(final MplWebhookReportDao mplWebhookReportDao)
	{
		this.mplWebhookReportDao = mplWebhookReportDao;
	}

}
