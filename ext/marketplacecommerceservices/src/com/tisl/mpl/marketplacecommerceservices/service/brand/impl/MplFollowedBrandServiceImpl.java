/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.brand.impl;

import java.util.List;

import javax.annotation.Resource;

import com.tisl.mpl.core.model.BrandMasterModel;
import com.tisl.mpl.marketplacecommerceservices.daos.brand.MplFollowedBrandDao;
import com.tisl.mpl.marketplacecommerceservices.service.brand.MplFollowedBrandService;


/**
 * @author TCS
 *
 */
public class MplFollowedBrandServiceImpl implements MplFollowedBrandService
{
	@Resource(name = "mplFollowedBrandDao")
	private MplFollowedBrandDao mplFollowedBrandDao;


	/**
	 * @return the mplFollowedBrandDao
	 */
	public MplFollowedBrandDao getMplFollowedBrandDao()
	{
		return mplFollowedBrandDao;
	}


	/**
	 * @param mplFollowedBrandDao
	 *           the mplFollowedBrandDao to set
	 */
	public void setMplFollowedBrandDao(final MplFollowedBrandDao mplFollowedBrandDao)
	{
		this.mplFollowedBrandDao = mplFollowedBrandDao;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.brand.MplFollowedBrandService#getFollowedBrands(java.lang.String)
	 */
	@Override
	public List<BrandMasterModel> getFollowedBrands(final String gender)
	{
		// YTODO Auto-generated method stub
		final List<BrandMasterModel> followedBrandList = mplFollowedBrandDao.getFollowedBrands(gender);
		return followedBrandList;
	}

}
