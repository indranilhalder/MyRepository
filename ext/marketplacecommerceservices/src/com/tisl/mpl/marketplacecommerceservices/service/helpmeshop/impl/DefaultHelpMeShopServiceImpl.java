/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.helpmeshop.impl;

import de.hybris.platform.core.model.enumeration.EnumerationValueModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.tisl.mpl.core.enums.HelpMeShopGenderOrTitleEnum;
import com.tisl.mpl.core.model.HelpMeShopOccasionModel;
import com.tisl.mpl.data.GenderOrTitleData;
import com.tisl.mpl.data.ReasonOrEventData;
import com.tisl.mpl.data.TypeOfProductData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.helper.MplEnumerationHelper;
import com.tisl.mpl.marketplacecommerceservices.daos.impl.DefaultHelpMeShopDaoImpl;
import com.tisl.mpl.marketplacecommerceservices.service.helpmeshop.HelpMeShopService;


/**
 * @author TCS
 *
 */
public class DefaultHelpMeShopServiceImpl implements HelpMeShopService
{
	@Resource(name = "mplEnumerationHelper")
	private MplEnumerationHelper mplEnumerationHelper;

	private DefaultHelpMeShopDaoImpl helpMeShopDao;

	/**
	 * @return the helpMeShopDao
	 */
	public DefaultHelpMeShopDaoImpl getHelpMeShopDao()
	{
		return helpMeShopDao;
	}

	/**
	 * @param helpMeShopDao
	 *           the helpMeShopDao to set
	 */
	public void setHelpMeShopDao(final DefaultHelpMeShopDaoImpl helpMeShopDao)
	{
		this.helpMeShopDao = helpMeShopDao;
	}

	/**
	 * @Description: It is used for populating Gender Data in HelpMeShopSearchComponent Gender/Title drop down
	 * @return list of Gender Data from HelpMeShopGenderOrTitleEnum
	 */
	@Override
	public java.util.List<GenderOrTitleData> getGenderOrTitle() throws EtailNonBusinessExceptions
	{
		final List<EnumerationValueModel> genderOrTitleEnumList = mplEnumerationHelper
				.getEnumerationValuesForCode(HelpMeShopGenderOrTitleEnum._TYPECODE);
		final List<GenderOrTitleData> genderOrTitleList = new ArrayList<GenderOrTitleData>();
		if (genderOrTitleEnumList != null)
		{
			for (final EnumerationValueModel enumModel : genderOrTitleEnumList)
			{
				final GenderOrTitleData genderOrTitleData = new GenderOrTitleData();
				genderOrTitleData.setCode(enumModel.getCode());
				genderOrTitleData.setName(enumModel.getName());
				genderOrTitleList.add(genderOrTitleData);

			}
		}
		return genderOrTitleList;
	}

	/**
	 * @Description: It is used for populating Reason/Event Data in HelpMeShopSearchComponent Reason/Event drop down
	 * @return list of Reason/Event Data from HelpMeShopReasonOrEventEnum
	 */
	@Override
	public List<ReasonOrEventData> getReasonOrEvent() throws EtailNonBusinessExceptions
	{
		final List<HelpMeShopOccasionModel> occasions = getHelpMeShopDao().getAllOccasions();
		final List<ReasonOrEventData> reasonOrEventList = new ArrayList<ReasonOrEventData>();
		if (occasions != null && !occasions.isEmpty())
		{
			for (final HelpMeShopOccasionModel helpMeShopOccasionModel : occasions)
			{
				final ReasonOrEventData reasonOrEventData = new ReasonOrEventData();
				reasonOrEventData.setCode(helpMeShopOccasionModel.getCode());
				reasonOrEventData.setDisplayName(helpMeShopOccasionModel.getDisplayName());
				reasonOrEventData.setFilterName(helpMeShopOccasionModel.getFilterName());
				reasonOrEventList.add(reasonOrEventData);
			}
		}
		return reasonOrEventList;
	}

	/**
	 * @Description: It is used for populating Type of Product Data in HelpMeShopSearchComponent Type of Product drop
	 *               down
	 * @return list of Type of Product Data from HelpMeShopTypeOfProductEnum
	 */
	@Override
	public List<TypeOfProductData> getTypeOfProduct() throws EtailNonBusinessExceptions
	{/*
	  * final List<HelpMeShopOccasionModel> occasions elpMeShopDao().getAllOccasions(); final List<TypeOfProductData>
	  * typeOfProductList = new ArrayList<TypeOfProductData>(); if (occasions != null && !occasions.isEmpty()) { for
	  * (final HelpMeShopOccasionModel occasionModel : occasions) { final TypeOfProductData typeOfProductData = new
	  * TypeOfProductData(); typeOfProductData.setCode(occasionModel.getCode());
	  * typeOfProductData.setName(occasionModel.getName()); typeOfProductList.add(typeOfProductData); } } return
	  * typeOfProductList;
	  */
		return java.util.Collections.EMPTY_LIST;
	}
}
