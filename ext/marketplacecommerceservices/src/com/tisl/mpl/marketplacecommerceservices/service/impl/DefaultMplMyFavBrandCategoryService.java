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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplStyleProfileModel;
import com.tisl.mpl.core.model.MyRecommendationsConfigurationModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.data.MplFavBrandCategoryData;
import com.tisl.mpl.jalo.DefaultPromotionManager;
import com.tisl.mpl.marketplacecommerceservices.daos.MplMyFavBrandCategoryDao;
import com.tisl.mpl.marketplacecommerceservices.daos.MyStyleProfileDao;
import com.tisl.mpl.marketplacecommerceservices.service.ExtendedUserService;
import com.tisl.mpl.marketplacecommerceservices.service.MplMyFavBrandCategoryService;


/**
 * @author TCS
 *
 */
public class DefaultMplMyFavBrandCategoryService implements MplMyFavBrandCategoryService
{
	//private static final Logger LOG = Logger.getLogger(DefaultMplMyFavBrandCategoryService.class);
	@Autowired
	private ExtendedUserService extendedUserService;
	@Autowired
	private MplMyFavBrandCategoryDao mplMyFavBrandCategoryDao;
	@Autowired
	private ModelService modelService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private MyStyleProfileDao myStyleProfileDao;
	private static final String ANONYMOUS_USER = "anonymous";

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
	public boolean addFavCategories(final String emailId, final String deviceId, final List<String> codeList)
	{
		boolean result = false;
		try
		{
			final CustomerModel customer = getCurrentCustomerByEmail(emailId);
			final MplStyleProfileModel styleProfileModel = customer.getMyStyleProfile();
			List<CategoryModel> selectedCategory = new ArrayList<CategoryModel>();
			List<CategoryModel> newListCategory = new ArrayList<CategoryModel>();
			MplStyleProfileModel styleProfileModelUpdate = null;
			//MplStyleProfileModel styleProfileModelToSave = modelService.create(MplStyleProfileModel.class);

			if (emailId.equalsIgnoreCase(ANONYMOUS_USER))
			{
				if (deviceId != null)
				{
					//Without userLogin when device id is not null
					//for update if device id consists of categories
					final List<MplStyleProfileModel> myStyleProfileList = myStyleProfileDao.fetchCatBrandOfDevice(deviceId);
					if (CollectionUtils.isNotEmpty(myStyleProfileList))
					{
						//"Inside if, device id consists of categories
						styleProfileModelUpdate = myStyleProfileList.get(0);

						selectedCategory = new ArrayList(styleProfileModelUpdate.getPreferredCategory());
						final List<CategoryModel> newEntries = fetchCategoryData(codeList);
						for (final CategoryModel entry : newEntries)
						{
							selectedCategory.add(entry);
						}
						//Removing Duplicates;
						final Set<CategoryModel> s = new HashSet<CategoryModel>();
						s.addAll(selectedCategory);
						newListCategory = new ArrayList<CategoryModel>();
						newListCategory.addAll(s);
						//Now the List has only the identical Elements
						styleProfileModelUpdate.setPreferredCategory(newListCategory);
						modelService.save(styleProfileModelUpdate);
						result = true;
					}
					else
					{
						//for insert if device id does not consist of categories
						//Inside else, device id does not consist of categories
						final MplStyleProfileModel styleProfileModelToSave = modelService.create(MplStyleProfileModel.class);
						final List<CategoryModel> newEntries = fetchCategoryData(codeList);
						for (final CategoryModel entry : newEntries)
						{
							selectedCategory.add(entry);
						}
						styleProfileModelToSave.setPreferredCategory(selectedCategory);

						styleProfileModelToSave.setDeviceId(deviceId);
						modelService.save(styleProfileModelToSave);
						result = true;
					}
				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9219);
				}

			}
			else
			{
				//With using userLogin, when device id is neglected
				MplStyleProfileModel styleProfileModelToSave = modelService.create(MplStyleProfileModel.class);
				styleProfileModelToSave = styleProfileModel;
				selectedCategory = (List<CategoryModel>) styleProfileModelToSave.getPreferredCategory();
				final List<CategoryModel> newEntries = fetchCategoryData(codeList);

				if (CollectionUtils.isNotEmpty(selectedCategory))
				{
					for (final CategoryModel oldEntry : selectedCategory)
					{
						newListCategory.add(oldEntry);
					}
				}

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
				styleProfileModelToSave.setPreferredBrand(selectedBrands);
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
	public boolean addFavBrands(final String emailId, final String deviceId, final List<String> codeList)
	{
		boolean result = false;
		try
		{
			MplStyleProfileModel styleProfileModelToSave = null;
			final CustomerModel customer = getCurrentCustomerByEmail(emailId);
			final MplStyleProfileModel styleProfileModel = customer.getMyStyleProfile();
			List<CategoryModel> selectedBrands = new ArrayList<CategoryModel>();
			List<CategoryModel> newListBrand = new ArrayList<CategoryModel>();
			MplStyleProfileModel styleProfileModelUpdate = null;

			if (emailId.equalsIgnoreCase(ANONYMOUS_USER))
			{
				if (deviceId != null)
				{
					//Without userLogin when device id is not null
					//for update if device id consists of brands
					final List<MplStyleProfileModel> myStyleProfileList = myStyleProfileDao.fetchCatBrandOfDevice(deviceId);
					if (CollectionUtils.isNotEmpty(myStyleProfileList))
					{
						//Inside if, device id consists of brands
						styleProfileModelUpdate = myStyleProfileList.get(0);
						selectedBrands = new ArrayList(styleProfileModelUpdate.getPreferredBrand());
						final List<CategoryModel> newEntries = fetchCategoryData(codeList);
						for (final CategoryModel entry : newEntries)
						{
							selectedBrands.add(entry);
						}
						//Removing Duplicates;
						final Set<CategoryModel> s = new HashSet<CategoryModel>();
						s.addAll(selectedBrands);
						newListBrand = new ArrayList<CategoryModel>();
						newListBrand.addAll(s);
						//Now the List has only the identical Elements
						styleProfileModelUpdate.setPreferredBrand(newListBrand);
						modelService.save(styleProfileModelUpdate);
						result = true;
					}
					else
					{
						//for insert if device id does not consist of brands
						//Inside else, device id does not consist of brands
						styleProfileModelToSave = modelService.create(MplStyleProfileModel.class);
						final List<CategoryModel> newEntries = fetchCategoryData(codeList);
						for (final CategoryModel entry : newEntries)
						{
							selectedBrands.add(entry);
						}
						styleProfileModelToSave.setPreferredBrand(selectedBrands);
						styleProfileModelToSave.setDeviceId(deviceId);
						modelService.save(styleProfileModelToSave);
						result = true;
					}

				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9219);
				}
			}
			else
			{
				//With using userLogin, when device id is neglected
				if (null != styleProfileModel)
				{
					styleProfileModelToSave = styleProfileModel;
					selectedBrands = (List<CategoryModel>) styleProfileModelToSave.getPreferredBrand();
					final List<CategoryModel> newEntries = fetchCategoryData(codeList);
					if (CollectionUtils.isNotEmpty(selectedBrands))
					{
						for (final CategoryModel oldEntry : selectedBrands)
						{
							newListBrand.add(oldEntry);
						}
					}
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
					styleProfileModelToSave.setPreferredBrand(selectedBrands);
					modelService.save(styleProfileModelToSave);
					customer.setMyStyleProfile(styleProfileModelToSave);
					modelService.save(customer);
					result = true;
				}
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
	public boolean deleteFavCategories(final String emailId, final String deviceId, final String code)
	{
		boolean result = false;
		try
		{
			MplStyleProfileModel styleProfileModelToSave = null;
			final CustomerModel customer = getCurrentCustomerByEmail(emailId);
			final MplStyleProfileModel styleProfileModel = customer.getMyStyleProfile();
			List<CategoryModel> selectedCategory = new ArrayList<CategoryModel>();
			final List<CategoryModel> newCategory = new ArrayList<CategoryModel>();
			// anonymous user TISSAM-7
			if (StringUtils.equalsIgnoreCase(ANONYMOUS_USER, emailId))
			{
				if (deviceId != null)
				{
					//Without userLogin when device id is not null
					final List<MplStyleProfileModel> myStyleProfileList = myStyleProfileDao.fetchCatBrandOfDevice(deviceId);
					if (CollectionUtils.isNotEmpty(myStyleProfileList))
					{
						styleProfileModelToSave = myStyleProfileList.get(0);
						selectedCategory = new ArrayList(styleProfileModelToSave.getPreferredCategory());
						for (final CategoryModel entry : selectedCategory)
						{
							if (!entry.getCode().equalsIgnoreCase(code))
							{
								newCategory.add(entry);
							}
						}
						styleProfileModelToSave.setPreferredCategory(newCategory);
						modelService.save(styleProfileModelToSave);
						customer.setMyStyleProfile(styleProfileModelToSave);
						modelService.save(customer);
						result = true;
					}
				}
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9219);
				}
			}

			//  Logged in user
			else
			{
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
	public boolean deleteFavBrands(final String emailId, final String deviceId, final String code)
	{
		boolean result = false;
		try
		{
			MplStyleProfileModel styleProfileModelToSave = null;
			final CustomerModel customer = getCurrentCustomerByEmail(emailId);
			final MplStyleProfileModel styleProfileModel = customer.getMyStyleProfile();
			List<CategoryModel> selectedBrands = new ArrayList<CategoryModel>();
			final List<CategoryModel> newBrands = new ArrayList<CategoryModel>();
			// anonymous user TISSAM-7
			if (StringUtils.equalsIgnoreCase(ANONYMOUS_USER, emailId))
			{
				if (deviceId != null)
				{

					final List<MplStyleProfileModel> myStyleProfileList = myStyleProfileDao.fetchBrandOfDevice(deviceId);
					if (CollectionUtils.isNotEmpty(myStyleProfileList))
					{
						styleProfileModelToSave = myStyleProfileList.get(0);
						selectedBrands = new ArrayList(styleProfileModelToSave.getPreferredBrand());
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
				else
				{
					throw new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9219);
				}

			}
			//  Logged in user
			else
			{
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


	@Override
	public List<CategoryModel> fetchFavCategories(final String emailId, final String deviceId)
	{
		List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
		try
		{
			if (emailId.equalsIgnoreCase("anonymous"))
			{
				if (StringUtils.isNotEmpty(deviceId))
				{
					//Without userLogin when device id is not null
					//for update if device id consists of categories
					final List<MplStyleProfileModel> myStyleProfileList = myStyleProfileDao.fetchCatBrandOfDevice(deviceId);
					if (CollectionUtils.isNotEmpty(myStyleProfileList))
					{
						//LOG.info("Inside if, device id consists of categories");
						final MplStyleProfileModel styleProfileModelUpdate = myStyleProfileList.get(0);

						categoryList = new ArrayList(styleProfileModelUpdate.getPreferredCategory());

					}
				}
				/*else
				{
					//LOG.info("Email id is anonymous and device id is null");
				}*/
				return categoryList;
			}
			else
			{
				final CustomerModel customer = getCurrentCustomerByEmail(emailId);
				List<CategoryModel> newListCategory = null;

				if (null != customer && null != customer.getMyStyleProfile()
						&& null != customer.getMyStyleProfile().getPreferredCategory()
						&& !customer.getMyStyleProfile().getPreferredCategory().isEmpty())
				{
					categoryList = new ArrayList<CategoryModel>(customer.getMyStyleProfile().getPreferredCategory());
				}

				if (StringUtils.isNotEmpty(deviceId))
				{
					//Without userLogin when device id is not null
					newListCategory = new ArrayList<CategoryModel>();
					//for update if device id consists of categories
					final List<MplStyleProfileModel> myStyleProfileList = myStyleProfileDao.fetchCatBrandOfDevice(deviceId);
					if (CollectionUtils.isNotEmpty(myStyleProfileList))
					{
						//LOG.info("Inside if, device id consists of categories");
						final MplStyleProfileModel styleProfileModelUpdate = myStyleProfileList.get(0);

						newListCategory = new ArrayList(styleProfileModelUpdate.getPreferredCategory());
					}
					for (final CategoryModel entry : newListCategory)
					{
						categoryList.add(entry);
					}

				}

				//Removing Duplicates;

				final Set<CategoryModel> s = new HashSet<CategoryModel>();
				s.addAll(categoryList);
				newListCategory = new ArrayList<CategoryModel>();
				newListCategory.addAll(s);
				return newListCategory;
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}

	}


	@Override
	public List<CategoryModel> fetchFavBrands(final String emailId, final String deviceId)
	{
		List<CategoryModel> brandList = new ArrayList<CategoryModel>();
		try
		{
			if (emailId.equalsIgnoreCase("anonymous"))
			{
				if (StringUtils.isNotEmpty(deviceId))
				{
					//Without userLogin when device id is not null
					//for update if device id consists of brands
					final List<MplStyleProfileModel> myStyleProfileList = myStyleProfileDao.fetchCatBrandOfDevice(deviceId);
					if (CollectionUtils.isNotEmpty(myStyleProfileList))
					{
						//LOG.info("Inside if, device id consists of categories");
						final MplStyleProfileModel styleProfileModelUpdate = myStyleProfileList.get(0);

						brandList = new ArrayList(styleProfileModelUpdate.getPreferredBrand());
					}
				}
				/*else
				{
					//LOG.info("Email id is anonymous and device id is null");
				}*/
				return brandList;
			}
			else
			{
				List<CategoryModel> newListBrand = null;
				final CustomerModel customer = getCurrentCustomerByEmail(emailId);
				if (null != customer && null != customer.getMyStyleProfile()
						&& null != customer.getMyStyleProfile().getPreferredBrand()
						&& !customer.getMyStyleProfile().getPreferredBrand().isEmpty())
				{
					brandList = new ArrayList<CategoryModel>(customer.getMyStyleProfile().getPreferredBrand());
				}

				if (StringUtils.isNotEmpty(deviceId))
				{
					//Without userLogin when device id is not null
					newListBrand = new ArrayList<CategoryModel>();
					//for update if device id consists of categories
					final List<MplStyleProfileModel> myStyleProfileList = myStyleProfileDao.fetchCatBrandOfDevice(deviceId);
					if (CollectionUtils.isNotEmpty(myStyleProfileList))
					{
						//LOG.info("Inside if, device id consists of categories");
						final MplStyleProfileModel styleProfileModelUpdate = myStyleProfileList.get(0);

						newListBrand = new ArrayList(styleProfileModelUpdate.getPreferredBrand());
					}
					for (final CategoryModel entry : newListBrand)
					{
						brandList.add(entry);
					}

				}

				//Removing Duplicates;

				final Set<CategoryModel> s = new HashSet<CategoryModel>();
				s.addAll(brandList);
				newListBrand = new ArrayList<CategoryModel>();
				newListBrand.addAll(s);
				return newListBrand;
			}

		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}

	}




}
