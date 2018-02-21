/**
 * 
 */
package com.tisl.mpl.marketplacecommerceservices.egv.service.cart.impl;

import de.hybris.platform.core.model.order.CartModel;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.egv.dao.cart.MplEGVCartDao;
import com.tisl.mpl.marketplacecommerceservices.egv.service.cart.MplEGVCartService;

/**
 * @author PankajS
 *
 */
public class MplEGVCartServiceImpl implements MplEGVCartService
{
   @Autowired
	MplEGVCartDao mplEGVCartDao;
	
	@Override
	public void removeOldEGVCartCurrentCustomer()
	{
		mplEGVCartDao.removeOldEGVCartCurrentCustomer();
	}
	
	@Override
	public CartModel getEGVCartModel(final String guid)
	{
		return mplEGVCartDao.getEGVCartModel(guid);	
	}
}
