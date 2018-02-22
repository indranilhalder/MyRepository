/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.brand.impl;

import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.BrandMasterModel;
import com.tisl.mpl.core.model.FollowedBrandMcvidModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.brand.MplFollowedBrandDao;
import com.tisl.mpl.marketplacecommerceservices.service.brand.MplFollowedBrandService;


/**
 * @author TCS
 *
 */
public class MplFollowedBrandServiceImpl implements MplFollowedBrandService
{
	private static final Logger LOG = Logger.getLogger(MplFollowedBrandServiceImpl.class);

	@Autowired
	private ModelService modelService;

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


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.brand.MplFollowedBrandService#updateFollowedBrands(java.lang.
	 * String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean updateFollowedBrands(final String mcvId, final String brands, final boolean follow)
	{
		// YTODO Auto-generated method stub
		List<BrandMasterModel> brandList = null;
		Set<BrandMasterModel> brandSetModify = null;
		FollowedBrandMcvidModel followedBrandModel = new FollowedBrandMcvidModel();
		boolean status = false;

		try
		{
			if (StringUtils.isNotEmpty(brands))
			{
				brandList = mplFollowedBrandDao.getBrands(brands);
			}

			if (CollectionUtils.isNotEmpty(brandList) && StringUtils.isNotEmpty(mcvId))
			{
				final List<FollowedBrandMcvidModel> listOfMcvIDbrands = mplFollowedBrandDao.getMcvIdBrands(mcvId);

				if (CollectionUtils.isNotEmpty(listOfMcvIDbrands))
				{
					followedBrandModel = listOfMcvIDbrands.get(0);

					if (null != followedBrandModel && CollectionUtils.isNotEmpty(followedBrandModel.getBrandList()))
					{

						brandSetModify = new HashSet<BrandMasterModel>(followedBrandModel.getBrandList());

					}
					if (CollectionUtils.isNotEmpty(brandSetModify))
					{
						//follow brands
						if (follow)
						{

							brandSetModify.addAll(brandList.stream().collect(Collectors.toSet()));

							followedBrandModel.setBrandList(brandSetModify);

							modelService.save(followedBrandModel);
							status = true;

						}

						//unfollow brands
						else
						{
							brandSetModify.removeAll(brandList.stream().collect(Collectors.toSet()));

							followedBrandModel.setBrandList(brandSetModify);
							modelService.save(followedBrandModel);
							status = true;
						}
					}
					//if mcvId with no brands in bd
					else
					{
						followedBrandModel.setBrandList(brandList.stream().collect(Collectors.toSet()));
						modelService.save(followedBrandModel);
						status = true;

					}

				}

				//New mcvId entry
				else
				{

					followedBrandModel = modelService.create(FollowedBrandMcvidModel.class);

					followedBrandModel.setMcvid(mcvId);

					followedBrandModel.setBrandList(brandList.stream().collect(Collectors.toSet()));

					modelService.save(followedBrandModel);
					status = true;

				}
			}
		}
		catch (final ModelSavingException ex)
		{
			LOG.error("Model saving exception " + ex.getMessage());
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0008);
		}
		catch (final Exception e)
		{
			LOG.error(" exception in updateFollowedBrands:MplFollowedBrandServiceImpl " + e.getMessage());
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
		return status;

	}
}
