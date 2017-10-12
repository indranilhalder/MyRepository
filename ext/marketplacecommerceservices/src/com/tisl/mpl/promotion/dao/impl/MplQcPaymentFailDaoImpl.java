/**
 *
 */
package com.tisl.mpl.promotion.dao.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.core.model.WalletApportionReturnInfoModel;
import com.tisl.mpl.promotion.dao.MplQcPaymentFailDao;


/**
 * @author Techouts
 *
 */
public class MplQcPaymentFailDaoImpl implements MplQcPaymentFailDao
{

	private static final Logger LOG = Logger.getLogger(MplBinErrorDaoImpl.class);
	public static final String QC_PAYMENT_QUERY = "select {pk} from {WalletApportionReturnInfo as w} where {w.status}=?status and  {w.type}=?type";
	public static final String QC_ORDER_QUERY = "select {pk} from {OrderModel as o} where {w.code}=?code";

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<WalletApportionReturnInfoModel> getPendingQcPayments()
	{
		LOG.debug("Fetching Bin Error Details");
		final String queryString = QC_PAYMENT_QUERY;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		return flexibleSearchService.<WalletApportionReturnInfoModel> search(query).getResult();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.promotion.dao.MplQcPaymentFailDao#getOrder(java.lang.String)
	 */
	@Override
	public OrderModel getOrder(final String orderCode)
	{
		LOG.debug("Fetching order Details");
		final String queryString = QC_ORDER_QUERY;

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
		if (!flexibleSearchService.<OrderModel> search(query).getResult().isEmpty())
		{
			return flexibleSearchService.<OrderModel> search(query).getResult().get(0);
		}
		else
		{
			return null;
		}

	}

}
