/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplStyleProfileModel;
import com.tisl.mpl.core.model.MyRecommendationsBrandsModel;
import com.tisl.mpl.core.model.MyRecommendationsConfigurationModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.marketplacecommerceservices.daos.MyStyleProfileDao;
import com.tisl.mpl.marketplacecommerceservices.service.MyStyleProfileService;


/**
 * @author TCS
 *
 */
public class DefaultMyStyleProfileServiceImpl implements MyStyleProfileService
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultMyStyleProfileServiceImpl.class);

	private ModelService modelService;
	@Autowired
	private UserService userService;

	private MyStyleProfileDao myStyleProfileDao;

	@Autowired
	private CategoryService categoryService;

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	/**
	 * @Description : Save Category Data in Model
	 * @param: categoryList
	 */
	@Override
	public void saveCategoryData(final List<CategoryModel> categoryList)
	{
		try
		{
			if (null != categoryList && !categoryList.isEmpty())
			{
				final CustomerModel customer = getCurrentSessionCustomer();
				if (null != customer.getMyStyleProfile())
				{
					final MplStyleProfileModel oModel = customer.getMyStyleProfile();
					oModel.setPreferredCategory(categoryList);
					oModel.setIsStyleProfileCreated(Boolean.FALSE);
					customer.setMyStyleProfile(oModel);
					modelService.save(oModel);
					modelService.save(customer);
				}
			}

		}
		catch (final ModelNotFoundException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0008);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	private CustomerModel getCurrentSessionCustomer()
	{
		return (CustomerModel) userService.getCurrentUser();
	}


	/**
	 * @Description : Get Saved Categories
	 * @return category
	 */
	@Override
	public List<CategoryModel> getInterstedCategories()
	{
		try
		{
			List<CategoryModel> category = new ArrayList<CategoryModel>();
			final CustomerModel customer = getCurrentSessionCustomer();
			if (null != customer && null != customer.getMyStyleProfile()
					&& null != customer.getMyStyleProfile().getPreferredCategory()
					&& !customer.getMyStyleProfile().getPreferredCategory().isEmpty())
			{
				category = new ArrayList<CategoryModel>(customer.getMyStyleProfile().getPreferredCategory());
			}
			return category;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @return the myStyleProfileDao
	 */
	public MyStyleProfileDao getMyStyleProfileDao()
	{
		return myStyleProfileDao;
	}

	/**
	 * @param myStyleProfileDao
	 *           the myStyleProfileDao to set
	 */
	public void setMyStyleProfileDao(final MyStyleProfileDao myStyleProfileDao)
	{
		this.myStyleProfileDao = myStyleProfileDao;
	}

	/**
	 * @Description : Checks if Style Profile already created for User
	 * @return category
	 */
	@Override
	public boolean isStyleProfileCreated()
	{
		try
		{
			final CustomerModel customer = getCurrentSessionCustomer();
			if (null != customer && null != customer.getMyStyleProfile()
					&& customer.getMyStyleProfile().getIsStyleProfileCreated().booleanValue())
			{
				return true;
			}
			return false;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Decsription : To fetch Category Data corresponding to gender
	 * @param: genderData
	 */
	@Override
	public List<MyRecommendationsConfigurationModel> fetchRecommendedData(final String genderData)
	{
		try
		{
			return myStyleProfileDao.fetchRecommendedData(genderData);
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
			final List<MyRecommendationsConfigurationModel> brandDataList = new ArrayList<MyRecommendationsConfigurationModel>();
			final List<MyRecommendationsConfigurationModel> dataList = myStyleProfileDao.fetchRecommendedData(genderData);
			if (null != dataList && !dataList.isEmpty())
			{
				for (final MyRecommendationsConfigurationModel oModel : dataList)
				{
					if (null != oModel.getGenderData()
							&& null != oModel.getConfiguredCategory()
							&& null != oModel.getConfiguredCategory().getCode()
							&& ((oModel.getGenderData().toString().equalsIgnoreCase(genderData)) && (oModel.getConfiguredCategory()
									.getCode().equalsIgnoreCase(catCode))))
					{
						brandDataList.add(oModel);
					}
					else if (null != oModel.getGenderData() && null != oModel.getConfiguredCategory()
							&& null != oModel.getConfiguredCategory().getCode()
							&& ((oModel.getConfiguredCategory().getCode().equalsIgnoreCase(catCode))))
					{
						brandDataList.add(oModel);
					}
				}
			}
			return brandDataList;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Description : Save Brand Data in Model
	 * @param: categoryList
	 */
	@Override
	public void saveBrandData(final List<CategoryModel> categoryList)
	{
		try
		{
			if (null != categoryList && !categoryList.isEmpty())
			{
				final CustomerModel customer = getCurrentSessionCustomer();
				if (null != customer.getMyStyleProfile())
				{
					final MplStyleProfileModel oModel = customer.getMyStyleProfile();
					oModel.setPreferredBrand(categoryList);
					oModel.setIsStyleProfileCreated(Boolean.FALSE);
					customer.setMyStyleProfile(oModel);
					modelService.save(oModel);
					modelService.save(customer);
				}
			}

		}
		catch (final ModelNotFoundException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0008);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Description : Save Gender Data
	 * @param: genderData
	 */
	@Override
	public void saveGenderData(final String genderData)
	{

		final MplStyleProfileModel oModel = modelService.create(MplStyleProfileModel.class);
		try
		{
			if (null != genderData && !genderData.isEmpty())
			{
				oModel.setSelectedGender(genderData.toUpperCase());
				oModel.setIsStyleProfileCreated(Boolean.FALSE);
			}
			final CustomerModel customer = getCurrentSessionCustomer();
			customer.setMyStyleProfile(oModel);
			modelService.save(oModel);
			modelService.save(customer);
		}
		catch (final ModelNotFoundException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0008);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Description : Fetch Gender Data
	 * @param: String
	 */
	@Override
	public String fetchGenderData()
	{
		try
		{
			String gender = "";
			final CustomerModel customer = getCurrentSessionCustomer();
			if (null != customer && null != customer.getMyStyleProfile())
			{
				final MplStyleProfileModel oModel = customer.getMyStyleProfile();
				if (null != oModel.getSelectedGender())
				{
					gender = oModel.getSelectedGender();
				}
			}
			return gender;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Description : Fetch Sub Category Data
	 * @param: String
	 */
	@SuppressWarnings("deprecation")
	@Override
	public List<CategoryModel> fetchSubCategories(final String genderData, final String category)
	{
		List<CategoryModel> subCategoryData = new ArrayList<CategoryModel>();
		try
		{
			if (null != category && category.length() > 0)
			{
				final CategoryModel categoryModel = categoryService
						.getCategory(getDefaultPromotionsManager().catalogData(), category);
				if (null != categoryModel)
				{
					subCategoryData = new ArrayList<CategoryModel>(categoryService.getAllSubcategoriesForCategory(categoryModel));
				}
			}
		}
		catch (final ModelNotFoundException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		return subCategoryData;
	}


	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}

	/**
	 * @Description : Get Saved Brands
	 * @return category
	 */
	@Override
	public List<CategoryModel> getInterstedBrands()
	{
		try
		{
			List<CategoryModel> category = new ArrayList<CategoryModel>();
			final CustomerModel customer = getCurrentSessionCustomer();
			if (null != customer && null != customer.getMyStyleProfile() && null != customer.getMyStyleProfile().getPreferredBrand()
					&& !customer.getMyStyleProfile().getPreferredBrand().isEmpty())
			{
				category = new ArrayList<CategoryModel>(customer.getMyStyleProfile().getPreferredBrand());
			}
			return category;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @Description : Save the Sub Category Data
	 * @return category
	 */
	@SuppressWarnings("javadoc")
	@Override
	public void saveSubCategoryData(final List<CategoryModel> categoryList)
	{
		try
		{
			if (null != categoryList && !categoryList.isEmpty())
			{
				final CustomerModel customer = getCurrentSessionCustomer();
				if (null != customer.getMyStyleProfile())
				{
					final MplStyleProfileModel oModel = customer.getMyStyleProfile();
					oModel.setPreferredSubCtg(categoryList);
					oModel.setIsStyleProfileCreated(Boolean.TRUE);
					customer.setMyStyleProfile(oModel);
					modelService.save(oModel);
					modelService.save(customer);
				}
			}
		}
		catch (final ModelNotFoundException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0008);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * This removes all my style from current customer
	 *
	 * @return boolean
	 */
	@Override
	public boolean removeMyStyleProfile()
	{
		try
		{
			final CustomerModel customer = getCurrentSessionCustomer();
			if (null != customer.getMyStyleProfile())
			{
				final MplStyleProfileModel oModel = customer.getMyStyleProfile();
				try
				{
					modelService.remove(oModel);
					return true;
				}
				catch (final ModelRemovalException exception)
				{
					LOG.error(exception.getMessage());
					return false;
				}

			}
			return false;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * @desc This removes single brand based on brand id
	 * @param categoryList
	 */
	@Override
	public void removeSingleBrand(final List<CategoryModel> categoryList)
	{
		try
		{
			final List<CategoryModel> newBrandList = new ArrayList<CategoryModel>();
			if (null != categoryList)
			{
				final CategoryModel brandCategoryRemove = categoryList.get(0);
				final CustomerModel customer = getCurrentSessionCustomer();

				if (null != customer.getMyStyleProfile())
				{
					//private method created for sonar major fix------Logic moved to a private method
					createPreferredBrandList(newBrandList, customer, brandCategoryRemove);
				}
				/*
				 * if (null != customer.getMyStyleProfile()) { final MplStyleProfileModel oModel =
				 * customer.getMyStyleProfile(); final List<CategoryModel> preferredBrandList = (List<CategoryModel>)
				 * oModel.getPreferredBrand(); if (null != preferredBrandList && !preferredBrandList.isEmpty()) { for (final
				 * CategoryModel brand : preferredBrandList) { if (!brand.equals(brandCategoryRemove)) {
				 * newBrandList.add(brand); } } } oModel.setPreferredBrand(newBrandList);
				 * customer.setMyStyleProfile(oModel); modelService.save(oModel); modelService.save(customer); }
				 */
			}

		}

		catch (final ModelNotFoundException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0008);
		}

		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @param newBrandList
	 * @param customer
	 * @param brandCategoryRemove
	 */
	private void createPreferredBrandList(final List<CategoryModel> newBrandList, final CustomerModel customer,
			final CategoryModel brandCategoryRemove)
	{
		try
		{
			final MplStyleProfileModel oModel = customer.getMyStyleProfile();
			final List<CategoryModel> preferredBrandList = (List<CategoryModel>) oModel.getPreferredBrand();
			if (null != preferredBrandList && !preferredBrandList.isEmpty())
			{
				for (final CategoryModel brand : preferredBrandList)
				{
					if (!brand.equals(brandCategoryRemove))
					{
						newBrandList.add(brand);
					}
				}
			}
			oModel.setPreferredBrand(newBrandList);
			customer.setMyStyleProfile(oModel);
			modelService.save(oModel);
			modelService.save(customer);
		}
		catch (final ModelNotFoundException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0008);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}


	}

	/**
	 * @desc This removes single category and tagged brands
	 * @param List
	 *           <CategoryModel> removeCatList, List<CategoryModel> removeBrandList
	 */
	@SuppressWarnings("javadoc")
	@Override
	public void removeSingleCategory(final List<CategoryModel> removeCatList, final List<CategoryModel> removeBrandList)
	{
		try
		{
			final List<CategoryModel> newCatList = new ArrayList<CategoryModel>();
			final ArrayList<CategoryModel> preferredBrandListCopy = new ArrayList<CategoryModel>();

			if (null != removeCatList)
			{
				final CategoryModel categoryRemove = removeCatList.get(0);

				final CustomerModel customer = getCurrentSessionCustomer();
				//private method created for sonar major fix---------Logic moved to private method
				createPreferredCategoryList(newCatList, customer, categoryRemove, preferredBrandListCopy, removeBrandList);
				/*
				 * if (null != customer.getMyStyleProfile()) { final MplStyleProfileModel oModel =
				 * customer.getMyStyleProfile();
				 * 
				 * final List<CategoryModel> preferredCatList = (List<CategoryModel>) oModel.getPreferredCategory(); final
				 * List<CategoryModel> preferredBrandList = (List<CategoryModel>) oModel.getPreferredBrand(); for (final
				 * CategoryModel preferredBrand : preferredBrandList) { preferredBrandListCopy.add(preferredBrand); }
				 * 
				 * if (null != preferredCatList && !preferredCatList.isEmpty()) { for (final CategoryModel cat :
				 * preferredCatList) { if (!cat.equals(categoryRemove)) { newCatList.add(cat); } } }
				 * 
				 * oModel.setPreferredCategory(newCatList); preferredBrandListCopy.removeAll(removeBrandList);
				 * oModel.setPreferredBrand(preferredBrandListCopy); customer.setMyStyleProfile(oModel);
				 * modelService.save(oModel); modelService.save(customer); }
				 */
			}

		}
		catch (final ModelNotFoundException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0008);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @param newCatList
	 * @param customer
	 * @param categoryRemove
	 * @param preferredBrandListCopy
	 * @param removeBrandList
	 */
	private void createPreferredCategoryList(final List<CategoryModel> newCatList, final CustomerModel customer,
			final CategoryModel categoryRemove, final ArrayList<CategoryModel> preferredBrandListCopy,
			final List<CategoryModel> removeBrandList)
	{
		try
		{
			if (null != customer.getMyStyleProfile())
			{
				final MplStyleProfileModel oModel = customer.getMyStyleProfile();

				final List<CategoryModel> preferredCatList = (List<CategoryModel>) oModel.getPreferredCategory();
				final List<CategoryModel> preferredBrandList = (List<CategoryModel>) oModel.getPreferredBrand();
				for (final CategoryModel preferredBrand : preferredBrandList)
				{
					preferredBrandListCopy.add(preferredBrand);
				}

				if (null != preferredCatList && !preferredCatList.isEmpty())
				{
					for (final CategoryModel cat : preferredCatList)
					{
						if (!cat.equals(categoryRemove))
						{
							newCatList.add(cat);
						}
					}
				}

				oModel.setPreferredCategory(newCatList);
				preferredBrandListCopy.removeAll(removeBrandList);
				oModel.setPreferredBrand(preferredBrandListCopy);
				customer.setMyStyleProfile(oModel);
				modelService.save(oModel);
				modelService.save(customer);
			}
		}



		catch (final ModelNotFoundException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0008);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.MyStyleProfileService#fetchSubCatdOfBrands(java.lang.String)
	 */
	@Override
	public List<MyRecommendationsBrandsModel> fetchSubCatdOfBrands(final String catCode)
	{
		return myStyleProfileDao.fetchSubCatdOfBrands(catCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * Saves the updated Brand
	 */
	@Override
	public void modifyBrand(final List<CategoryModel> categoryList)
	{
		try
		{
			if (null != categoryList)
			{
				final CustomerModel customer = getCurrentSessionCustomer();
				if (null != customer.getMyStyleProfile())
				{
					final MplStyleProfileModel oModel = customer.getMyStyleProfile();
					oModel.setPreferredBrand(categoryList);
					customer.setMyStyleProfile(oModel);
					modelService.save(oModel);
					modelService.save(customer);
				}
			}

		}
		catch (final ModelNotFoundException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0008);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * Saves the updated Category L1
	 */
	@Override
	public void modifyCategory(final List<CategoryModel> categoryList)
	{
		try
		{
			if (null != categoryList)
			{
				final CustomerModel customer = getCurrentSessionCustomer();
				if (null != customer.getMyStyleProfile())
				{
					final MplStyleProfileModel oModel = customer.getMyStyleProfile();
					oModel.setPreferredCategory(categoryList);
					customer.setMyStyleProfile(oModel);
					modelService.save(oModel);
					modelService.save(customer);
				}
			}

		}
		catch (final ModelNotFoundException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0008);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}

	}
}
