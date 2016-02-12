/**
 *
 */
package com.tisl.mpl.facade.helpmeshop.impl;

import de.hybris.platform.category.impl.DefaultCategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.data.GenderOrTitleData;
import com.tisl.mpl.data.HelpmeShopAgeData;
import com.tisl.mpl.data.HelpmeShopCategoryData;
import com.tisl.mpl.data.HelpmeShopData;
import com.tisl.mpl.data.HelpmeShopGenderData;
import com.tisl.mpl.data.ReasonOrEventData;
import com.tisl.mpl.data.TypeOfProductData;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.helpmeshop.HelpMeShopFacade;
import com.tisl.mpl.marketplacecommerceservices.service.helpmeshop.impl.DefaultHelpMeShopServiceImpl;


/**
 * @author TCS
 *
 */
public class DefaultHelpMeShopFacadeImpl implements HelpMeShopFacade
{
	@Resource(name = "helpMeShopService")
	private DefaultHelpMeShopServiceImpl helpMeShopService;

	@Autowired
	private DefaultCategoryService categoryService;

	private Converter<CategoryModel, CategoryData> categoryConverter;



	/**
	 * @return the categoryConverter
	 */
	public Converter<CategoryModel, CategoryData> getCategoryConverter()
	{
		return categoryConverter;
	}

	/**
	 * @param categoryConverter
	 *           the categoryConverter to set
	 */
	public void setCategoryConverter(final Converter<CategoryModel, CategoryData> categoryConverter)
	{
		this.categoryConverter = categoryConverter;
	}

	/**
	 * @Description: It is used for populating Gender Data in HelpMeShopSearchComponent Gender/Title drop down
	 * @return list of Gender Data from help me shop service
	 */
	@Override
	public List<GenderOrTitleData> getGenderOrTitle() throws EtailNonBusinessExceptions
	{
		return helpMeShopService.getGenderOrTitle();
	}

	/**
	 * @Description: It is used for populating Reason/Event Data in HelpMeShopSearchComponent Reason/Event drop down
	 * @return list of Reason/Event Data from help me shop service
	 */
	@Override
	public List<ReasonOrEventData> getReasonOrEvent() throws EtailNonBusinessExceptions
	{
		// YTODO Auto-generated method stub
		return helpMeShopService.getReasonOrEvent();
	}

	/**
	 * @Description: It is used for populating Type of Product Data in HelpMeShopSearchComponent Type of Product drop
	 *               down
	 * @return list of Type of product Data from help me shop service
	 */
	@Override
	public List<TypeOfProductData> getTypeOfProduct() throws EtailNonBusinessExceptions
	{
		// YTODO Auto-generated method stub
		return helpMeShopService.getTypeOfProduct();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.helpmeshop.HelpMeShopFacade#getCategoryForConceirgeSearch(java.lang.String)
	 */
	@Override
	public List<CategoryData> getCategoryForConceirgeSearch(final String categoryCode)
	{
		// YTODO Auto-generated method stub
		final CategoryModel categoryModel = categoryService.getCategoryForCode(categoryCode);
		final Collection<CategoryModel> categoryModels = categoryService.getAllSubcategoriesForCategory(categoryModel);
		final List<CategoryData> categoryData = Converters.convertAll(categoryModels, getCategoryConverter());
		return categoryData;

	}

	@Override
	public HelpmeShopData getWebServiceForConceirgeSearch()
	{
		final HelpmeShopData helpmeShopData = new HelpmeShopData();
		helpmeShopData.setOccasions(getReasonOrEvent());
		helpmeShopData.setGenders(getGenders());
		helpmeShopData.setAge(getAgeBand());
		return helpmeShopData;
	}

	public List<HelpmeShopGenderData> getGenders()
	{
		final List<HelpmeShopGenderData> genders = new ArrayList<HelpmeShopGenderData>();
		final HelpmeShopGenderData menGender = new HelpmeShopGenderData();
		menGender.setCode("MSH11");
		menGender.setGender("Man");
		genders.add(menGender);
		final HelpmeShopGenderData womenGender = new HelpmeShopGenderData();
		womenGender.setCode("MSH10");
		womenGender.setGender("Woman");
		genders.add(womenGender);
		final HelpmeShopGenderData boyGender = new HelpmeShopGenderData();
		boyGender.setCode("MPH1112100");
		boyGender.setGender("Boy");
		genders.add(boyGender);
		final HelpmeShopGenderData girlGender = new HelpmeShopGenderData();
		girlGender.setCode("MPH1112101");
		girlGender.setGender("Girl");
		genders.add(girlGender);
		final HelpmeShopGenderData infantGender = new HelpmeShopGenderData();
		infantGender.setCode("MPH1112102");
		infantGender.setGender("Infant");
		genders.add(infantGender);
		return genders;
	}

	public List<HelpmeShopAgeData> getAgeBand()
	{
		final List<HelpmeShopAgeData> ageList = new ArrayList<HelpmeShopAgeData>();
		final HelpmeShopAgeData helpmeShopAgeData1 = new HelpmeShopAgeData();
		helpmeShopAgeData1.setAge("0 - 1");
		helpmeShopAgeData1.setAgeBand("0-3m,3-6m,6-9m,9-12m");
		helpmeShopAgeData1.setGender(MarketplacecommerceservicesConstants.MAN_WOMAN);
		ageList.add(helpmeShopAgeData1);

		final HelpmeShopAgeData helpmeShopAgeData2 = new HelpmeShopAgeData();
		helpmeShopAgeData2.setAge("1 - 2");
		helpmeShopAgeData2.setAgeBand("12-18m,18-24m,1-2y");
		helpmeShopAgeData2.setGender(MarketplacecommerceservicesConstants.MAN_WOMAN);
		ageList.add(helpmeShopAgeData2);

		final HelpmeShopAgeData helpmeShopAgeData3 = new HelpmeShopAgeData();
		helpmeShopAgeData3.setAge("2 - 5");
		helpmeShopAgeData3.setAgeBand("24-48m,2-3y,3-4y,4-5y");
		helpmeShopAgeData3.setGender(MarketplacecommerceservicesConstants.MAN_WOMAN);
		ageList.add(helpmeShopAgeData3);

		final HelpmeShopAgeData helpmeShopAgeData4 = new HelpmeShopAgeData();
		helpmeShopAgeData4.setAge("5 - 9");
		helpmeShopAgeData4.setAgeBand("5-6y,6-7y,7-8y,8-9y");
		helpmeShopAgeData4.setGender(MarketplacecommerceservicesConstants.MAN_WOMAN);
		ageList.add(helpmeShopAgeData4);

		final HelpmeShopAgeData helpmeShopAgeData5 = new HelpmeShopAgeData();
		helpmeShopAgeData5.setAge("9 - 15");
		helpmeShopAgeData5.setAgeBand("9-10y,10-11y,11-12y,12-13y,13-14y");
		helpmeShopAgeData5.setGender(MarketplacecommerceservicesConstants.MAN_WOMAN);
		ageList.add(helpmeShopAgeData5);

		final HelpmeShopAgeData helpmeShopAgeData6 = new HelpmeShopAgeData();
		helpmeShopAgeData6.setAge("16 - 22");
		helpmeShopAgeData6.setAgeBand("16to22");
		helpmeShopAgeData6.setGender(MarketplacecommerceservicesConstants.MAN_WOMAN);
		ageList.add(helpmeShopAgeData6);

		final HelpmeShopAgeData helpmeShopAgeData7 = new HelpmeShopAgeData();
		helpmeShopAgeData7.setAge("22 - 35");
		helpmeShopAgeData7.setAgeBand("22to35");
		helpmeShopAgeData7.setGender(MarketplacecommerceservicesConstants.MAN_WOMAN);
		ageList.add(helpmeShopAgeData7);

		final HelpmeShopAgeData helpmeShopAgeData8 = new HelpmeShopAgeData();
		helpmeShopAgeData8.setAge("35 - 55");
		helpmeShopAgeData8.setAgeBand("35to55");
		helpmeShopAgeData8.setGender(MarketplacecommerceservicesConstants.MAN_WOMAN);
		ageList.add(helpmeShopAgeData8);

		final HelpmeShopAgeData helpmeShopAgeData9 = new HelpmeShopAgeData();
		helpmeShopAgeData9.setAge("55+");
		helpmeShopAgeData9.setAgeBand("55plus");
		helpmeShopAgeData9.setGender(MarketplacecommerceservicesConstants.MAN_WOMAN);
		ageList.add(helpmeShopAgeData9);

		return ageList;
	}


	@Override
	public HelpmeShopCategoryData getWebServicesForConceirgeCategory(final String categoryId)
	{
		final HelpmeShopCategoryData helpMeShopCategoryData = new HelpmeShopCategoryData();
		final List<CategoryData> categoryDatas = getCategoryForConceirgeSearch(categoryId);
		final List<String> categoryNames = new ArrayList<String>();
		for (final CategoryData category : categoryDatas)
		{
			categoryNames.add(category.getName());
		}
		helpMeShopCategoryData.setCategoryName(categoryNames);
		return helpMeShopCategoryData;
	}

}
