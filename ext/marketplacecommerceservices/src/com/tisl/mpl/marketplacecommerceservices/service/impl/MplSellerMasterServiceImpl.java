/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.marketplacecommerceservices.daos.MplSellerMasterDao;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerMasterService;
import com.tisl.mpl.model.SellerMasterModel;


/**
 * @author TCS
 *
 */
public class MplSellerMasterServiceImpl implements MplSellerMasterService
{

	@Autowired
	private MplSellerMasterDao mplSellerMasterDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService#getSellerDetail(java.lang.String)
	 */
	@Override
	public SellerMasterModel getSellerMaster(final String sellerID)
	{
		if (sellerID != null)
		{
			//return mplSellerMasterDao.getSellerMaster(sellerID);
			return getMplSellerMasterDao().getSellerMaster(sellerID);
		}
		return null;
	}

	/**
	 * @return the mplSellerMasterDao
	 */
	public MplSellerMasterDao getMplSellerMasterDao()
	{
		return mplSellerMasterDao;
	}

	/**
	 * @param mplSellerMasterDao
	 *           the mplSellerMasterDao to set
	 */
	public void setMplSellerMasterDao(final MplSellerMasterDao mplSellerMasterDao)
	{
		this.mplSellerMasterDao = mplSellerMasterDao;
	}
}
