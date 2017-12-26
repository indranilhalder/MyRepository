/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.impl;

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.RefundTransactionEntryModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.MplUtrNoArnNoDao;


/**
 * @author TCS
 *
 */
public class MplUtrNoArnNoDaoImpl implements MplUtrNoArnNoDao
{

	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.daos.MplUtrNoArnNoDao#getUtrNoArnNo(java.lang.String)
	 */
	@Override
	public String getUtrNoArnNo(final String orderLineId)
	{

		try
		{
			String utrNoArnNo = StringUtils.EMPTY;
			List<RefundTransactionEntryModel> UtrNoArnNopk = null;
			final String query = "select {pk} from {RefundTransactionEntry} where {transactionid}=?orderLineId";
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameter("orderLineId", orderLineId);
			UtrNoArnNopk = flexibleSearchService.<RefundTransactionEntryModel> search(fQuery).getResult();
			if (CollectionUtils.isNotEmpty(UtrNoArnNopk))
			{
				final RefundTransactionEntryModel reftrentry = UtrNoArnNopk.get(0);
				if (null != reftrentry)
				{

					if (StringUtils.isNotEmpty(reftrentry.getUtrNumber()))
					{
						utrNoArnNo = reftrentry.getUtrNumber();
					}
					if (StringUtils.isNotEmpty(reftrentry.getArnNumber()))
					{
						utrNoArnNo = reftrentry.getArnNumber();
					}
				}
			}
			return utrNoArnNo;
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0006);
		}
		catch (final EtailNonBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}


	}

}
