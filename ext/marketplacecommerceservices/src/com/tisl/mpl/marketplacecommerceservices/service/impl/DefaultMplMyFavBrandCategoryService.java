/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplStyleProfileModel;
import com.tisl.mpl.core.model.MyRecommendationsConfigurationModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.data.MplFavBrandCategoryData;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.marketplacecommerceservices.daos.MplMyFavBrandCategoryDao;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.MplMyFavBrandCategoryService;


/**
 * @author TCS
 *
 */
public class DefaultMplMyFavBrandCategoryService implements MplMyFavBrandCategoryService
{
	@Autowired
	private ExtendedUserService extendedUserService;
	@Autowired
	private MplMyFavBrandCategoryDao mplMyFavBrandCategoryDao;
	@Autowired
	private ModelService modelService;
	@Autowired
	private CategoryService categoryService;

	//	private static final Logger LOG = Logger.getLogger(DefaultMplMyFavBrandCategoryService.class);

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


	/**
	 * @return the mplMyFavBrandCategoryDao
	 */
	public MplMyFavBrandCategoryDao getMplMyFavBrandCategoryDao()
	{
		return mplMyFavBrandCategoryDao;
	}


	/**
	 * @param mplMyFavBrandCategoryDao
	 *           the mplMyFavBrandCategoryDao to set
	 */
	public void setMplMyFavBrandCategoryDao(final MplMyFavBrandCategoryDao mplMyFavBrandCategoryDao)
	{
		this.mplMyFavBrandCategoryDao = mplMyFavBrandCategoryDao;
	}


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
	 * @return the categoryService
	 */
	public CategoryService getCategoryService()
	{
		return categoryService;
	}


	/**
	 * @param categoryService
	 *           the categoryService to set
	 */
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}



	public CustomerModel getCurrentCustomerByEmail(final String emailId)
	{
		try
		{
			CustomerModel customer = new CustomerModel();
			customer = extendedUserService.getUserForUid(emailId);
			return customer;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}


	@Override
	public List<CategoryModel> fetchFavCategories(final String emailId)
	{
		try
		{
			List<CategoryModel> category = new ArrayList<CategoryModel>();
			final CustomerModel customer = getCurrentCustomerByEmail(emailId);

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


	@Override
	public List<CategoryModel> fetchFavBrands(final String emailId)
	{
		try
		{
			List<CategoryModel> brands = new ArrayList<CategoryModel>();
			final CustomerModel customer = getCurrentCustomerByEmail(emailId);

			if (null != customer && null != customer.getMyStyleProfile() && null != customer.getMyStyleProfile().getPreferredBrand()
					&& !customer.getMyStyleProfile().getPreferredBrand().isEmpty())
			{
				brands = new ArrayList<CategoryModel>(customer.getMyStyleProfile().getPreferredBrand());
			}
			return brands;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}

	}


	@Override
	public Map<String, MplFavBrandCategoryData> fetchAllCategories()
	{
		try
		{
			final Map<String, MplFavBrandCategoryData> categoryDataMap = new HashMap<String, MplFavBrandCategoryData>();
			List<MyRecommendationsConfigurationModel> myRecommendedData = new ArrayList<MyRecommendationsConfigurationModel>();
			myRecommendedData = mplMyFavBrandCategoryDao.fetchRecommendedData();
			if (null != myRecommendedData && !myRecommendedData.isEmpty())
			{
				for (final MyRecommendationsConfigurationModel oModel : myRecommendedData)
				{
					final MplFavBrandCategoryData oData = new MplFavBrandCategoryData();
					oData.setCode(oModel.getConfiguredCategory().getCode());
					oData.setName(oModel.getConfiguredCategory().getName());
					if (null != oModel.getConfiguredCategory().getLogo())
					{
						oData.setLogo(new ArrayList<MediaModel>(oModel.getConfiguredCategory().getLogo()));
					}
					categoryDataMap.put(oModel.getConfiguredCategory().getCode(), oData);
				}
			}
			return categoryDataMap;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}


	@Override
	public Map<String, MplFavBrandCategoryData> fetchAllBrands()
	{
		try
		{
			final Map<String, MplFavBrandCategoryData> categoryDataMap = new HashMap<String, MplFavBrandCategoryData>();
			List<CategoryModel> brands = new ArrayList<CategoryModel>();
			List<MyRecommendationsConfigurationModel> myRecommendedCategoryData = new ArrayList<MyRecommendationsConfigurationModel>();
			myRecommendedCategoryData = mplMyFavBrandCategoryDao.fetchRecommendedData();
			if (null != myRecommendedCategoryData && !myRecommendedCategoryData.isEmpty())
			{
				for (final MyRecommendationsConfigurationModel oModel : myRecommendedCategoryData)
				{
					if (null != oModel.getConfiguredBrands() && oModel.getConfiguredBrands().size() > 0)
					{
						brands = new ArrayList<CategoryModel>(oModel.getConfiguredBrands());

						for (final CategoryModel oNewModel : brands)
						{
							final MplFavBrandCategoryData oData = new MplFavBrandCategoryData();
							oData.setCode(oNewModel.getCode());
							oData.setName(oNewModel.getName());
							oData.setLogo(new ArrayList<MediaModel>(oNewModel.getLogo()));
							categoryDataMap.put(oNewModel.getCode(), oData);
						}
					}
				}
			}
			return categoryDataMap;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	@Override
	public boolean addFavCategories(final String emailId, final List<String> codeList)
	{
		boolean result = false;
		try
		{
			MplStyleProfileModel styleProfileModelToSave = new MplStyleProfileModel();
			final CustomerModel customer = getCurrentCustomerByEmail(emailId);
			final MplStyleProfileModel styleProfileModel = customer.getMyStyleProfile();
			List<CategoryModel> selectedCategory = new ArrayList<CategoryModel>();
			final List<CategoryModel> newListCategory = new ArrayList<CategoryModel>();

			if (null != styleProfileModel)
			{
				styleProfileModelToSave = styleProfileModel;
				selectedCategory = (List<CategoryModel>) styleProfileModelToSave.getPreferredCategory();
				final List<CategoryModel> newEntries = fetchCategoryData(codeList);
				//if (null != selectedCategory && selectedCategory.size() > 0)
				if (CollectionUtils.isNotEmpty(selectedCategory))
				{
					for (final CategoryModel oldEntry : selectedCategory)
					{
						newListCategory.add(oldEntry);
					}
				}
				//if (null != newEntries && newEntries.size() > 0)
				if (CollectionUtils.isNotEmpty(newEntries))
				{
					for (final CategoryModel entry : newEntries)
					{
						newListCategory.add(entry);
					}
				}

				styleProfileModelToSave.setPreferredCategory(newListCategory);
				modelService.save(styleProfileModelToSave);
				customer.setMyStyleProfile(styleProfileModelToSave);
				modelService.save(customer);
				result = true;
			}
			else
			{
				styleProfileModelToSave = modelService.create(MplStyleProfileModel.class);
				final List<CategoryModel> newEntries = fetchCategoryData(codeList);
				for (final CategoryModel entry : newEntries)
				{
					selectedCategory.add(entry);
				}
				styleProfileModelToSave.setPreferredCategory(selectedCategory);
				modelService.save(styleProfileModelToSave);
				customer.setMyStyleProfile(styleProfileModelToSave);
				modelService.save(customer);
				result = true;
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		return result;
	}


	@Override
	public boolean addFavBrands(final String emailId, final List<String> codeList)
	{
		boolean result = false;
		try
		{
			MplStyleProfileModel styleProfileModelToSave = new MplStyleProfileModel();
			final CustomerModel customer = getCurrentCustomerByEmail(emailId);
			final MplStyleProfileModel styleProfileModel = customer.getMyStyleProfile();
			List<CategoryModel> selectedBrands = new ArrayList<CategoryModel>();
			final List<CategoryModel> newListBrand = new ArrayList<CategoryModel>();

			if (null != styleProfileModel)
			{
				styleProfileModelToSave = styleProfileModel;
				selectedBrands = (List<CategoryModel>) styleProfileModelToSave.getPreferredBrand();
				final List<CategoryModel> newEntries = fetchCategoryData(codeList);
				//if (null != selectedBrands && selectedBrands.size() > 0)
				if (CollectionUtils.isNotEmpty(selectedBrands))
				{
					for (final CategoryModel oldEntry : selectedBrands)
					{
						newListBrand.add(oldEntry);
					}
				}
				//if (null != newEntries && newEntries.size() > 0)
				if (CollectionUtils.isNotEmpty(newEntries))
				{
					for (final CategoryModel entry : newEntries)
					{
						newListBrand.add(entry);
					}
				}
				styleProfileModelToSave.setPreferredBrand(newListBrand);
				modelService.save(styleProfileModelToSave);
				customer.setMyStyleProfile(styleProfileModelToSave);
				modelService.save(customer);
				result = true;
			}
			else
			{
				styleProfileModelToSave = modelService.create(MplStyleProfileModel.class);
				final List<CategoryModel> newEntries = fetchCategoryData(codeList);
				for (final CategoryModel entry : newEntries)
				{
					selectedBrands.add(entry);
				}
				styleProfileModelToSave.setPreferredCategory(selectedBrands);
				modelService.save(styleProfileModelToSave);
				customer.setMyStyleProfile(styleProfileModelToSave);
				modelService.save(customer);
				result = true;
			}
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		return result;
	}


	@Override
	public boolean deleteFavCategories(final String emailId, final String code)
	{
		boolean result = false;
		try
		{
			MplStyleProfileModel styleProfileModelToSave = new MplStyleProfileModel();
			final CustomerModel customer = getCurrentCustomerByEmail(emailId);
			final MplStyleProfileModel styleProfileModel = customer.getMyStyleProfile();
			List<CategoryModel> selectedCategory = new ArrayList<CategoryModel>();
			final List<CategoryModel> newCategory = new ArrayList<CategoryModel>();

			if (null != styleProfileModel)
			{
				styleProfileModelToSave = styleProfileModel;
				selectedCategory = (List<CategoryModel>) styleProfileModelToSave.getPreferredCategory();
				//if (null != selectedCategory && selectedCategory.size() > 0)
				if (CollectionUtils.isNotEmpty(selectedCategory))
				{
					for (final CategoryModel entry : selectedCategory)
					{
						if (!entry.getCode().equalsIgnoreCase(code))
						{
							newCategory.add(entry);
						}
					}
				}
				styleProfileModelToSave.setPreferredCategory(newCategory);
				modelService.save(styleProfileModelToSave);
				customer.setMyStyleProfile(styleProfileModelToSave);
				modelService.save(customer);
				result = true;
			}
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		return result;
	}


	@Override
	public boolean deleteFavBrands(final String emailId, final String code)
	{
		boolean result = false;
		try
		{
			MplStyleProfileModel styleProfileModelToSave = new MplStyleProfileModel();
			final CustomerModel customer = getCurrentCustomerByEmail(emailId);
			final MplStyleProfileModel styleProfileModel = customer.getMyStyleProfile();
			List<CategoryModel> selectedBrands = new ArrayList<CategoryModel>();
			final List<CategoryModel> newBrands = new ArrayList<CategoryModel>();

			if (null != styleProfileModel)
			{
				styleProfileModelToSave = styleProfileModel;
				selectedBrands = (List<CategoryModel>) styleProfileModelToSave.getPreferredBrand();
				//if (null != selectedBrands && selectedBrands.size() > 0)
				if (CollectionUtils.isNotEmpty(selectedBrands))
				{
					for (final CategoryModel entry : selectedBrands)
					{
						if (!entry.getCode().equalsIgnoreCase(code))
						{
							newBrands.add(entry);
						}
					}
				}
				styleProfileModelToSave.setPreferredBrand(newBrands);
				modelService.save(styleProfileModelToSave);
				customer.setMyStyleProfile(styleProfileModelToSave);
				modelService.save(customer);
				result = true;
			}
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
		return result;
	}


	/**
	 * @Description : Returns Categories corresponding to code
	 * @param categoryCodeList
	 * @return categoryList
	 */
	@SuppressWarnings("deprecation")
	private List<CategoryModel> fetchCategoryData(final List<String> categoryCodeList)
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
			throw new EtailNonBusinessExceptions(exception, MarketplacecommerceservicesConstants.E0008);
		}

		return categoryList;
	}

	protected DefaultPromotionManager getDefaultPromotionsManager()
	{
		return Registry.getApplicationContext().getBean("defaultPromotionManager", DefaultPromotionManager.class);
	}
}
