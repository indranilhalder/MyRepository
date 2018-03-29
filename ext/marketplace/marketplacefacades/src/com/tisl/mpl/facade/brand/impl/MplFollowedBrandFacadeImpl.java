/**
 *
 */
package com.tisl.mpl.facade.brand.impl;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.tisl.mpl.core.model.BrandMasterModel;
import com.tisl.mpl.core.model.FollowedBrandMcvidModel;
import com.tisl.mpl.facade.brand.MplFollowedBrandFacade;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.brand.MplFollowedBrandService;
import com.tisl.mpl.wsdto.FollowedBrandWsDto;


/**
 * @author TCS
 *
 */
public class MplFollowedBrandFacadeImpl implements MplFollowedBrandFacade
{
	@Resource(name = "mplFollowedBrandService")
	private MplFollowedBrandService mplFollowedBrandService;

	private Converter<BrandMasterModel, FollowedBrandWsDto> mplFollowedBrandConverter;

	@Resource(name = "extendedUserService")
	private ExtendedUserService extendedUserService;

	/**
	 * @return the mplFollowedBrandService
	 */
	public MplFollowedBrandService getMplFollowedBrandService()
	{
		return mplFollowedBrandService;
	}

	/**
	 * @param mplFollowedBrandService
	 *           the mplFollowedBrandService to set
	 */
	public void setMplFollowedBrandService(final MplFollowedBrandService mplFollowedBrandService)
	{
		this.mplFollowedBrandService = mplFollowedBrandService;
	}

	/**
	 * @return the mplFollowedBrandConverter
	 */
	public Converter<BrandMasterModel, FollowedBrandWsDto> getMplFollowedBrandConverter()
	{
		return mplFollowedBrandConverter;
	}

	/**
	 * @param mplFollowedBrandConverter
	 *           the mplFollowedBrandConverter to set
	 */
	public void setMplFollowedBrandConverter(final Converter<BrandMasterModel, FollowedBrandWsDto> mplFollowedBrandConverter)
	{
		this.mplFollowedBrandConverter = mplFollowedBrandConverter;
	}



	/**
	 * @return the extendedUserService
	 */
	public ExtendedUserService getExtendedUserService()
	{
		return extendedUserService;
	}

	/**
	 * @param extendedUserService
	 *           the extendedUserService to set
	 */
	public void setExtendedUserService(final ExtendedUserService extendedUserService)
	{
		this.extendedUserService = extendedUserService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.brand.MplFollowedBrandFacade#getFollowedBrands(java.lang.String)
	 */
	@Override
	public List<FollowedBrandWsDto> getFollowedBrands(final String gender)
	{
		// YTODO Auto-generated method stub
		List<FollowedBrandWsDto> followedBrandWsDtoList = new ArrayList<FollowedBrandWsDto>();

		final List<BrandMasterModel> followedBrandList = mplFollowedBrandService.getFollowedBrands(gender);

		followedBrandWsDtoList = Converters.convertAll(followedBrandList, getMplFollowedBrandConverter());

		return followedBrandWsDtoList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.brand.MplFollowedBrandFacade#updateFollowedBrands(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean updateFollowedBrands(final String mcvId, final String brands, final boolean follow)
	{

		// YTODO Auto-generated method stub
		return mplFollowedBrandService.updateFollowedBrands(mcvId, brands, follow);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.brand.MplFollowedBrandFacade#getUserFollowedMcvIds(java.lang.String)
	 */
	@Override
	public List<FollowedBrandMcvidModel> getUserFollowedMcvIds(final String mcvId)
	{
		// YTODO Auto-generated method stub
		return mplFollowedBrandService.getUserFollowedMcvIds(mcvId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.brand.MplFollowedBrandFacade#getCustomerFollowedBrands(java.lang.String)
	 */
	@Override
	public List<FollowedBrandWsDto> getCustomerFollowedBrands(final String userId)
	{
		// YTODO Auto-generated method stub
		CustomerModel customerModel = null;
		//final List<BrandMasterModel> allFollowedBrandList = new ArrayList<BrandMasterModel>();
		final Set<BrandMasterModel> allFollowedBrandSet = new HashSet<BrandMasterModel>();
		List<FollowedBrandWsDto> followedBrandWsDtoList = new ArrayList<FollowedBrandWsDto>();

		if (StringUtils.isNotEmpty(userId))
		{
			customerModel = extendedUserService.getUserForUid(userId);
		}
		//IQA code Review fix
		//if (null != customerModel && CollectionUtils.isNotEmpty(customerModel.getFollowedBrandMcvid()))
		if (null != customerModel)
		{

			Set<FollowedBrandMcvidModel> followedBrandMcvidModelSet = new HashSet<FollowedBrandMcvidModel>();
			followedBrandMcvidModelSet = customerModel.getFollowedBrandMcvid();

			if (CollectionUtils.isNotEmpty(followedBrandMcvidModelSet))
			{

				//for (final FollowedBrandMcvidModel followedBrandMcvidModel : customerModel.getFollowedBrandMcvid())
				for (final FollowedBrandMcvidModel followedBrandMcvidModel : followedBrandMcvidModelSet)
				{

					Set<BrandMasterModel> brandMasterModelSet = new HashSet<BrandMasterModel>();
					brandMasterModelSet = followedBrandMcvidModel.getBrandList();

					//if (CollectionUtils.isNotEmpty(followedBrandMcvidModel.getBrandList()))
					if (CollectionUtils.isNotEmpty(brandMasterModelSet))
					{
						//allFollowedBrandSet.addAll(new HashSet<BrandMasterModel>(followedBrandMcvidModel.getBrandList()));
						allFollowedBrandSet.addAll(new HashSet<BrandMasterModel>(brandMasterModelSet));
					}
				}

			}
		}

		if (CollectionUtils.isNotEmpty(allFollowedBrandSet))
		{
			followedBrandWsDtoList = Converters.convertAll(new ArrayList<BrandMasterModel>(allFollowedBrandSet),
					getMplFollowedBrandConverter());
		}

		return followedBrandWsDtoList;
	}
}
