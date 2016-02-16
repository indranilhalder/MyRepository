/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.tisl.mpl.marketplacecommerceservices.daos.MplTicketDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplTicketService;
import com.tisl.mpl.model.CRMTicketDetailModel;


/**
 * @author 765463
 *
 */
public class MplTicketServiceImpl implements MplTicketService
{

	private MplTicketDao mplTicketDao;


	/**
	 * @return the mplTicketDao
	 */
	public MplTicketDao getMplTicketDao()
	{
		return mplTicketDao;
	}


	/**
	 * @param mplTicketDao
	 *           the mplTicketDao to set
	 */
	@Required
	public void setMplTicketDao(final MplTicketDao mplTicketDao)
	{
		this.mplTicketDao = mplTicketDao;
	}


	@Override
	public List<CRMTicketDetailModel> findCRMTicketDetail(final boolean isTicketCreatedInCRM)
	{
		// YTODO Auto-generated method stub
		return mplTicketDao.findCRMTicketDetail(isTicketCreatedInCRM);
	}

}
