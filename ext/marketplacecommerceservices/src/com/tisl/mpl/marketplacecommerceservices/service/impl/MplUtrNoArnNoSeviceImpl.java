/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import javax.annotation.Resource;

import com.tisl.mpl.marketplacecommerceservices.daos.MplUtrNoArnNoDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplUtrNoArnNoService;


/**
 * @author TCS
 *
 */


public class MplUtrNoArnNoSeviceImpl implements MplUtrNoArnNoService
{
	@Resource(name = "mplUtrNoArnNoDao")
	private MplUtrNoArnNoDao mplUtrNoArnNoDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MplUtrNoArnNoService#getUtrNoArnNo(java.lang.String)
	 */
	@Override
	public String getUtrNoArnNo(final String orderLineId)
	{
		// YTODO Auto-generated method stub
		return mplUtrNoArnNoDao.getUtrNoArnNo(orderLineId);


	}


}
