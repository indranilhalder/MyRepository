package com.tisl.mpl.facade.mystyleprofile.impl;

/**
 *
 */

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MyRecommendationsBrandsModel;
import com.tisl.mpl.core.model.MyRecommendationsConfigurationModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facade.mystyleprofile.MyStyleProfileFacade;
import com.tisl.mpl.facades.product.data.MyStyleProfileData;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.marketplacecommerceservices.service.MyStyleProfileService;


/**
 * @author TCS
 *
 */
public class DefaultMyStyleProfileFacadeImpl implements MyStyleProfileFacade
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultMyStyleProfileFacadeImpl.class);

	private MyStyleProfileService myStyleProfileService;

	@Autowired
	private CategoryService categoryService;


	/**
	 * @return the myStyleProfileService
	 */
	public MyStyleProfileService getMyStyleProfileService()
	{
		return myStyleProfileService;
	}

	/**
	 * @param myStyleProfileService
	 *           the myStyleProfileService to set
	 */
	public void setMyStyleProfileService(final MyStyleProfileService myStyleProfileService)
	{
		this.myStyleProfileService = myStyleProfileService;
	}

	/**
	 * @Description: To Save My Interest Category Data
	 * @param: styleProfileData
	 * @return: void
	 */
	@Override
	public void saveCategoryData(final MyStyleProfileData styleProfileData)
	{
		try
		{
			LOG.debug("Saving Category Data");
			List<CategoryModel> categoryList = new ArrayList<>();
			if (null != styleProfileData && null != styleProfileData.getCategoryCodeList()
					&& !styleProfileData.getCategoryCodeList().isEmpty())
			{
				categoryList = fetchCategoryData(styleProfileData.getCategoryCodeList());
				myStyleProfileService.saveCategoryData(categoryList);
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Description : Returns List of Categories corresponding to code
	 * @param catalogVersionModel
	 * @param categoryCodeList
	 * @return categoryList
	 */
	@SuppressWarnings("deprecation")
	private List<CategoryModel> fetchCategoryData(final List<String> categoryCodeList)
	{
		try
		{
			List<CategoryModel> categoryList = new ArrayList<>();
			try
			{
				if (null != categoryCodeList && !categoryCodeList.isEmpty())
				{
					for (final String categoryCode : categoryCodeList)
					{
						final CategoryModel category = categoryService.getCategory(getDefaultPromotionsManager().catalogData(),
								categoryCode);
						if (null != category)
						{
							categoryList.add(category);
						}
					}
				}
			}
			catch (final ModelNotFoundException exception)
			{
				categoryList = new ArrayList<>();
				throw new EtailNonBusinessExceptions(exception, MarketplacecommerceservicesConstants.E0000);
			}
			return categoryList;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}

	/**
	 * @Description :Fetched Interested Categories
	 * @return List<CategoryModel>
	 */
	@Override
	public List<CategoryModel> getInterstedCategories()
	{
		try
		{
			return myStyleProfileService.getInterstedCategories();
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Description :Check if Style Profile already created for User
	 * @return boolean
	 */
	@Override
	public boolean isStyleProfileCreated()
	{
		try
		{
			return myStyleProfileService.isStyleProfileCreated();
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Decsription : To fetch Data corresponding to gender
	 * @param: genderData
	 */
	@Override
	public List<MyRecommendationsConfigurationModel> fetchRecommendedData(final String genderData)
	{
		try
		{
			return myStyleProfileService.fetchRecommendedData(genderData);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Decsription : To fetch Brand Data corresponding to gender
	 * @param: genderData
	 * @param: catCode
	 */
	@Override
	public List<MyRecommendationsConfigurationModel> fetchBrands(final String genderData, final String catCode)
	{
		try
		{
			return myStyleProfileService.fetchBrands(genderData, catCode);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Description: To Save My Interest Brand Data
	 * @param: styleProfileData
	 * @return: void
	 */
	@Override
	public void saveBrandData(final MyStyleProfileData styleProfileData)
	{
		try
		{
			LOG.debug("Saving Brand Data");
			List<CategoryModel> categoryList = new ArrayList<>();
			if (null != styleProfileData && null != styleProfileData.getCategoryCodeList()
					&& !styleProfileData.getCategoryCodeList().isEmpty())
			{
				categoryList = fetchCategoryData(styleProfileData.getCategoryCodeList());
				myStyleProfileService.saveBrandData(categoryList);
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}

	}

	/**
	 * @Description: To Save Gender Data
	 * @param: genderData
	 * @return: void
	 */
	@Override
	public void saveGenderData(final String genderData)
	{
		try
		{
			if (null != genderData && !genderData.isEmpty())
			{
				myStyleProfileService.saveGenderData(genderData);
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Description: To Save Gender Data
	 * @return: String
	 */
	@Override
	public String fetchGenderData()
	{
		try
		{
			return myStyleProfileService.fetchGenderData();
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Description: Fetch Sub Category Data
	 * @return: List<CategoryModel>
	 */
	@Override
	public List<CategoryModel> fetchSubCategories(final String genderData, final String category)
	{
		try
		{
			return myStyleProfileService.fetchSubCategories(genderData, category);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Description :Fetched Interested Brands
	 * @return List<CategoryModel>
	 */
	@Override
	public List<CategoryModel> getInterstedBrands()
	{
		try
		{
			return myStyleProfileService.getInterstedBrands();
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Description :Save Sub Category Data
	 * @param : styleProfileData
	 */
	@Override
	public void saveSubCategoryData(final MyStyleProfileData styleProfileData)
	{
		try
		{
			LOG.debug("Saving Category Data");
			List<CategoryModel> categoryList = new ArrayList<>();
			if (null != styleProfileData && null != styleProfileData.getCategoryCodeList()
					&& !styleProfileData.getCategoryCodeList().isEmpty())
			{
				categoryList = fetchCategoryData(styleProfileData.getCategoryCodeList());
				myStyleProfileService.saveSubCategoryData(categoryList);
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Description :delete tagged style profile
	 *
	 */
	@Override
	public boolean removeMyStyleProfile()
	{
		try
		{
			return myStyleProfileService.removeMyStyleProfile();
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Description delete the sub brand
	 * @param styleProfileData
	 */
	@Override
	public void removeSingleBrand(final MyStyleProfileData styleProfileData)
	{
		try
		{
			List<CategoryModel> categoryList = new ArrayList<>();

			if (null != styleProfileData && null != styleProfileData.getCategoryCodeList()
					&& !styleProfileData.getCategoryCodeList().isEmpty())
			{
				categoryList = fetchCategoryData(styleProfileData.getCategoryCodeList());
				myStyleProfileService.removeSingleBrand(categoryList);
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Description delete category and tageed sub brands
	 * @param styleProfileDataCategory
	 *           , styleProfileDataBrand
	 */
	@Override
	public void removeSingleCategory(final MyStyleProfileData styleProfileDataCategory,
			final MyStyleProfileData styleProfileDataBrand)
	{
		try
		{
			List<CategoryModel> categoryList = new ArrayList<>();
			List<CategoryModel> brandList = new ArrayList<>();

			if (null != styleProfileDataCategory && null != styleProfileDataCategory.getCategoryCodeList()
					&& !styleProfileDataCategory.getCategoryCodeList().isEmpty() && null != styleProfileDataBrand
					&& null != styleProfileDataBrand.getCategoryCodeList() && !styleProfileDataBrand.getCategoryCodeList().isEmpty())
			{
				categoryList = fetchCategoryData(styleProfileDataCategory.getCategoryCodeList());
				brandList = fetchCategoryData(styleProfileDataBrand.getCategoryCodeList());

				myStyleProfileService.removeSingleCategory(categoryList, brandList);
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.facade.mystyleprofile.MyStyleProfileFacade#fetchSubCatdOfBrands(java.lang.String)
	 */
	@Override
	public List<MyRecommendationsBrandsModel> fetchSubCatdOfBrands(final String catCode)
	{
		return myStyleProfileService.fetchSubCatdOfBrands(catCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * Modify Brand
	 */
	@Override
	public void modifyBrand(final MyStyleProfileData styleProfileData)
	{
		try
		{
			List<CategoryModel> categoryList = new ArrayList<>();

			if (null != styleProfileData && null != styleProfileData.getCategoryCodeList()
					&& !styleProfileData.getCategoryCodeList().isEmpty())
			{
				categoryList = fetchCategoryData(styleProfileData.getCategoryCodeList());
				myStyleProfileService.modifyBrand(categoryList);
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * Modify Category
	 */
	@Override
	public void modifyCategory(final MyStyleProfileData styleProfileDataCategory)
	{
		try
		{
			List<CategoryModel> categoryList = new ArrayList<>();

			if (null != styleProfileDataCategory && null != styleProfileDataCategory.getCategoryCodeList()
					&& !styleProfileDataCategory.getCategoryCodeList().isEmpty())
			{
				categoryList = fetchCategoryData(styleProfileDataCategory.getCategoryCodeList());
				myStyleProfileService.modifyCategory(categoryList);
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}

	}



}
