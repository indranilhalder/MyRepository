/**
 *
 */
package com.hybris.oms.tata.services;

import java.util.List;

import javax.annotation.Resource;

import com.hybris.oms.tata.daos.SellerMasterModelDao;


/**
 * @author techouts
 *
 */
public class DefaultSellerInfoService implements SellerInfoService
{
	@Resource(name = "sellerMasterModelDao")
	private SellerMasterModelDao sellerMasterModelDao;

	/**
	 * @return sellers
	 */
	@Override
	public List<String> getAllSellers()
	{
		return sellerMasterModelDao.getAllSellers();
	}

	/**
	 * @param sellerMasterModelDao
	 *           the sellerMasterModelDao to set
	 */
	public void setSellerMasterModelDao(final SellerMasterModelDao sellerMasterModelDao)
	{
		this.sellerMasterModelDao = sellerMasterModelDao;
	}
}
