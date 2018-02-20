/**
 *
 */
package com.tisl.mpl.facade.brand.impl;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.tisl.mpl.core.model.BrandMasterModel;
import com.tisl.mpl.facade.brand.MplFollowedBrandFacade;
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

}
