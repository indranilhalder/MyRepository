/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.tisl.mpl.core.model.MplWebCrmModel;
import com.tisl.mpl.core.model.MplWebCrmTicketModel;
import com.tisl.mpl.marketplacecommerceservices.daos.MplWebFormDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplWebFormService;



/**
 * @author TCS
 */
public class MplWebFormServiceImpl implements MplWebFormService
{
	@Resource
	private ModelService modelService;

	@Resource
	private BusinessProcessService businessProcessService;

	@Resource
	private MplWebFormDao mplWebFormDao;

	private static final Logger LOG = Logger.getLogger(MplWebFormServiceImpl.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplWebFormService#getWebCRMParentNodes()
	 */
	@Override
	public List<MplWebCrmModel> getWebCRMParentNodes()
	{
		// YTODO Auto-generated method stub
		return mplWebFormDao.getWebCRMParentNodes();
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplWebFormService#getWebCRMByNodes(java.lang.String)
	 */
	@Override
	public List<MplWebCrmModel> getWebCRMByNodes(final String nodeParent)
	{
		return mplWebFormDao.getWebCRMByNodes(nodeParent);
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplWebFormService#getWebCRMTicket(java.lang.String)
	 */
	@Override
	public MplWebCrmTicketModel getWebCRMTicket(final String commerceTicketId)
	{
		return mplWebFormDao.getWebCRMTicket(commerceTicketId);
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplWebFormService#checkDuplicateWebCRMTickets(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkDuplicateWebCRMTickets(final String ticketType, final String orderCode, final String subOrderCode,
			final String transactionId, final String L0code, final String L1code, final String L2code, final String L3code,
			final String L4code, final String customerId)
	{
		return mplWebFormDao.checkDuplicateWebCRMTickets(ticketType, orderCode, subOrderCode, transactionId, L0code, L1code,
				L2code, L3code, L4code, customerId);
	}


}
