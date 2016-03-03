/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.FeedbackArea;
import com.tisl.mpl.core.enums.Frequency;
import com.tisl.mpl.core.model.MarketplacePreferenceModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.data.MplPreferenceData;
import com.tisl.mpl.facades.data.MplPreferencePopulationData;
import com.tisl.mpl.helper.MplEnumerationHelper;
import com.tisl.mpl.marketplacecommerceservices.service.MplPreferenceService;


/**
 * @author TCS
 *
 */
public class DefaultMplPreferenceService implements MplPreferenceService
{
	@Autowired
	private MplEnumerationHelper mplEnumerationHelper;
	@Autowired
	private BaseSiteService baseSiteService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private UserService userService;
	protected static final Logger LOG = Logger.getLogger(DefaultMplPreferenceService.class);

	/**
	 * @return the mplEnumerationHelper
	 */
	public MplEnumerationHelper getMplEnumerationHelper()
	{
		return mplEnumerationHelper;
	}

	/**
	 * @param mplEnumerationHelper
	 *           the mplEnumerationHelper to set
	 */
	public void setMplEnumerationHelper(final MplEnumerationHelper mplEnumerationHelper)
	{
		this.mplEnumerationHelper = mplEnumerationHelper;
	}

	/**
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	/**
	 * @param baseSiteService
	 *           the baseSiteService to set
	 */
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
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
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}





	/**
	 * @description method is called to fetch all marketplace preference contents
	 * @return MplPreferencePopulationData
	 */
	@Override
	public MplPreferencePopulationData fetchAllMplPreferenceContents()
	{
		try
		{
			final MplPreferencePopulationData mplPreferenceDataForAllDetail = new MplPreferencePopulationData();

			//		final List<String> preferredCategoryList = new ArrayList<String>();
			//		final List<String> preferredBrandList = new ArrayList<String>();

			final List<CategoryModel> preferredCategoryList1 = new ArrayList<CategoryModel>();
			final List<CategoryModel> preferredBrandList1 = new ArrayList<CategoryModel>();

			final List<String> interestList = getInterestPreference();
			final List<String> frequencyList = getFrequency();
			final List<String> feedbackAreaList = getFeedbackArea();
			Collection<CategoryModel> categoryList = new ArrayList<CategoryModel>();
			categoryList = getBaseSitePreferredCategories();

			//if (null != categoryList && categoryList.size() > 0)
			if (CollectionUtils.isNotEmpty(categoryList))
			{
				for (final CategoryModel categoryLineItem : categoryList)
				{
					if (!categoryLineItem.getCode().startsWith(MarketplacecommerceservicesConstants.BRAND_NAME_PREFIX))
					{
						preferredCategoryList1.add(categoryLineItem);
					}
					else
					{
						preferredBrandList1.add(categoryLineItem);
					}
				}
			}

			mplPreferenceDataForAllDetail.setInterestList(interestList);
			mplPreferenceDataForAllDetail.setPreferredCategory(preferredCategoryList1);
			mplPreferenceDataForAllDetail.setPreferredBrand(preferredBrandList1);
			mplPreferenceDataForAllDetail.setFrequencyList(frequencyList);
			mplPreferenceDataForAllDetail.setFeedbackAreaList(feedbackAreaList);

			return mplPreferenceDataForAllDetail;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description method is called to fetch all pre saved data
	 * @return MplPreferenceData
	 */
	@Override
	public MplPreferenceData fetchPresavedFavouritePreferences()
	{
		try
		{
			final CustomerModel currentCustomer = getCurrentSessionCustomer();
			final MarketplacePreferenceModel mplPreferenceModel = currentCustomer.getMarketplacepreference();
			final MplPreferenceData mplPreferenceData = new MplPreferenceData();
			final List<CategoryData> categoryDataList = new ArrayList<CategoryData>();
			final List<CategoryData> categoryDataBrandList = new ArrayList<CategoryData>();
			if (null != mplPreferenceModel)
			{
				if (null != mplPreferenceModel.getPreferredCategory() && mplPreferenceModel.getPreferredCategory().size() > 0)
				{
					for (final CategoryModel category : mplPreferenceModel.getPreferredCategory())
					{
						final CategoryData categoryData = new CategoryData();
						categoryData.setCode(category.getCode());
						categoryData.setName(category.getName());
						categoryDataList.add(categoryData);
					}
				}
				if (null != mplPreferenceModel.getPreferredBrand() && mplPreferenceModel.getPreferredBrand().size() > 0)
				{
					for (final CategoryModel categoryBrand : mplPreferenceModel.getPreferredBrand())
					{
						final CategoryData categoryData = new CategoryData();
						categoryData.setCode(categoryBrand.getCode());
						categoryData.setName(categoryBrand.getName());
						categoryDataBrandList.add(categoryData);
					}
				}

				if (null != mplPreferenceModel.getIsInterestedInEmail())
				{
					if (mplPreferenceModel.getIsInterestedInEmail().booleanValue())
					{
						mplPreferenceData.setMyInterest(MarketplacecommerceservicesConstants.INTERESTED_IN_EMAIL);
					}
					else
					{
						mplPreferenceData.setMyInterest(MarketplacecommerceservicesConstants.NOT_INTERESTED_IN_EMAIL);
					}
				}
				mplPreferenceData.setSelectedCategory(categoryDataList);
				mplPreferenceData.setSelectedBrand(categoryDataBrandList);

				if (null != mplPreferenceModel.getCustomerSurveys())
				{
					mplPreferenceData.setFeedbackCustomerSurveys(mplPreferenceModel.getCustomerSurveys().booleanValue());
				}
				if (null != mplPreferenceModel.getUserReview())
				{
					mplPreferenceData.setFeedbackUserReview(mplPreferenceModel.getUserReview().booleanValue());
				}
				if (null != mplPreferenceModel.getEmailFrequency())
				{
					mplPreferenceData.setSelectedFrequency(mplPreferenceModel.getEmailFrequency().toString());
				}
			}
			return mplPreferenceData;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}


	/**
	 * @description method is called to get the type of Frequency
	 * @return Collection
	 */
	@Override
	public List<String> getFrequency()
	{
		try
		{
			final List<EnumerationValueModel> enumList = mplEnumerationHelper.getEnumerationValuesForCode(Frequency._TYPECODE);
			final List<String> frequency = new ArrayList<String>();
			for (final EnumerationValueModel enumerationValueModel : enumList)
			{
				if (enumerationValueModel != null)
				{
					frequency.add(enumerationValueModel.getCode());
				}
			}
			return frequency;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @description method is called to get the type of Feedback
	 * @return Collection
	 */
	@Override
	public List<String> getFeedbackArea()
	{
		try
		{
			final List<EnumerationValueModel> enumList = mplEnumerationHelper.getEnumerationValuesForCode(FeedbackArea._TYPECODE);
			final List<String> feedbackAreaList = new ArrayList<String>();
			for (final EnumerationValueModel enumerationValueModel : enumList)
			{
				if (enumerationValueModel != null)
				{
					final String code = enumerationValueModel.getCode().replace(MarketplacecommerceservicesConstants.UNDER_SCORE,
							MarketplacecommerceservicesConstants.SPACE);
					feedbackAreaList.add(code);
				}
			}
			return feedbackAreaList;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}



	/**
	 * @description method is called to get the type of Interest Preference
	 * @return Collection
	 */
	@Override
	public List<String> getInterestPreference()
	{
		try
		{
			final List<String> interestPreferenceList = new ArrayList<String>();
			interestPreferenceList.add(0, MarketplacecommerceservicesConstants.INTERESTED_IN_EMAIL);
			interestPreferenceList.add(1, MarketplacecommerceservicesConstants.NOT_INTERESTED_IN_EMAIL);
			return interestPreferenceList;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 *
	 */
	@Override
	public Collection<CategoryModel> getBaseSitePreferredCategories()
	{
		try
		{
			final Collection<CategoryModel> categoryList = baseSiteService.getCurrentBaseSite().getMplPrefferedCategories();
			return categoryList;
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 *
	 */
	@Override
	public void saveMarketplacePreference(final MplPreferenceData mplPreferenceData)
	{
		try
		{
			if (null != mplPreferenceData)
			{
				savePreferencesAndSubscription(mplPreferenceData);
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @param mplPreferenceData
	 */
	private void savePreferencesAndSubscription(final MplPreferenceData mplPreferenceData)
	{
		try
		{
			final List<CategoryModel> selectedCategoryList = new ArrayList<CategoryModel>();
			final List<CategoryModel> selectedBrandList = new ArrayList<CategoryModel>();
			final CustomerModel currentCustomer = getCurrentSessionCustomer();
			final MarketplacePreferenceModel mplPreferenceModel = currentCustomer.getMarketplacepreference();
			MarketplacePreferenceModel mplPreferenceModelToSave = new MarketplacePreferenceModel();
			if (null != mplPreferenceModel)
			{
				mplPreferenceModelToSave = mplPreferenceModel;
			}
			else
			{
				mplPreferenceModelToSave = modelService.create(MarketplacePreferenceModel.class);
			}
			Collection<CategoryModel> categoryList = new ArrayList<CategoryModel>();
			categoryList = getBaseSitePreferredCategories();
			final List<String> categoryCode = mplPreferenceData.getSelectedCategory();
			final List<String> brandCode = mplPreferenceData.getSelectedBrand();
			//if (null != categoryCode && categoryCode.size() > 0)
			if (CollectionUtils.isNotEmpty(categoryCode))
			{
				for (final String code : categoryCode)
				{
					for (final CategoryModel categoryModel : categoryList)
					{
						if (code.equalsIgnoreCase(categoryModel.getCode()))
						{
							selectedCategoryList.add(categoryModel);
						}
					}
				}
			}
			//if (null != brandCode && brandCode.size() > 0)
			if (CollectionUtils.isNotEmpty(brandCode))
			{
				for (final String code : brandCode)
				{
					for (final CategoryModel categoryModel : categoryList)
					{
						if (code.equalsIgnoreCase(categoryModel.getCode()))
						{
							selectedBrandList.add(categoryModel);
						}
					}
				}
			}

			if (mplPreferenceData.getMyInterest().equalsIgnoreCase(MarketplacecommerceservicesConstants.INTERESTED_IN_EMAIL))
			{
				mplPreferenceModelToSave.setIsInterestedInEmail(Boolean.TRUE);

				mplPreferenceModelToSave.setPreferredCategory(selectedCategoryList);
				mplPreferenceModelToSave.setPreferredBrand(selectedBrandList);
				if (mplPreferenceData.getSelectedFrequency().equalsIgnoreCase(Frequency.DAILY.toString()))
				{
					mplPreferenceModelToSave.setEmailFrequency(Frequency.DAILY);
				}
				else if (mplPreferenceData.getSelectedFrequency().equalsIgnoreCase(Frequency.WEEKLY.toString()))
				{
					mplPreferenceModelToSave.setEmailFrequency(Frequency.WEEKLY);
				}
				else if (mplPreferenceData.getSelectedFrequency().equalsIgnoreCase(Frequency.BI_WEEKLY.toString()))
				{
					mplPreferenceModelToSave.setEmailFrequency(Frequency.BI_WEEKLY);
				}
				mplPreferenceModelToSave.setUserReview(Boolean.valueOf(mplPreferenceData.isFeedbackUserReview()));
				mplPreferenceModelToSave.setCustomerSurveys(Boolean.valueOf(mplPreferenceData.isFeedbackCustomerSurveys()));
			}
			else
			{
				mplPreferenceModelToSave.setIsInterestedInEmail(Boolean.FALSE);
			}
			//	saving marketplace preference model for current customer

			final CustomerModel customerModel = getCurrentSessionCustomer();
			customerModel.setMarketplacepreference(mplPreferenceModelToSave);
			modelService.save(mplPreferenceModelToSave);
			customerModel.setModifiedtime(new Date());
			modelService.save(customerModel);
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}


	//TISPRO-181-capturing mpl preference data to customer model during registration
	/**
	 * Changes made for saving mpl pref data in during registration
	 */
	@Override
	public void saveUserSpecificMplPrefDataInitially(final CustomerModel customerModel)
	{
		try
		{
			final List<CategoryModel> preferredCategoryList = new ArrayList<CategoryModel>();
			final List<CategoryModel> preferredBrandList = new ArrayList<CategoryModel>();
			final MarketplacePreferenceModel mplPreferenceModel = customerModel.getMarketplacepreference();
			MarketplacePreferenceModel mplPreferenceModelToSave = new MarketplacePreferenceModel();
			if (null != mplPreferenceModel)
			{
				mplPreferenceModelToSave = mplPreferenceModel;
			}
			else
			{
				mplPreferenceModelToSave = modelService.create(MarketplacePreferenceModel.class);
			}
			Collection<CategoryModel> categoryList = new ArrayList<CategoryModel>();
			categoryList = getBaseSitePreferredCategories();

			if (CollectionUtils.isNotEmpty(categoryList))
			{
				for (final CategoryModel categoryLineItem : categoryList)
				{
					if (!categoryLineItem.getCode().startsWith(MarketplacecommerceservicesConstants.BRAND_NAME_PREFIX))
					{
						preferredCategoryList.add(categoryLineItem);
					}
					else
					{
						preferredBrandList.add(categoryLineItem);
					}
				}
			}

			mplPreferenceModelToSave.setIsInterestedInEmail(Boolean.TRUE);
			mplPreferenceModelToSave.setPreferredCategory(preferredCategoryList);
			mplPreferenceModelToSave.setPreferredBrand(preferredBrandList);
			mplPreferenceModelToSave.setEmailFrequency(Frequency.WEEKLY);
			mplPreferenceModelToSave.setUserReview(Boolean.TRUE);
			mplPreferenceModelToSave.setCustomerSurveys(Boolean.TRUE);

			customerModel.setMarketplacepreference(mplPreferenceModelToSave);
			modelService.save(mplPreferenceModelToSave);
			customerModel.setModifiedtime(new Date());
			modelService.save(customerModel);
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	/**
	 * @param mplPreferenceData
	 */
	@Override
	public void unsubscribeAllPreferences(final MplPreferenceData mplPreferenceData)
	{
		try
		{
			final CustomerModel currentCustomer = getCurrentSessionCustomer();
			final MarketplacePreferenceModel mplPreferenceModel = currentCustomer.getMarketplacepreference();
			MarketplacePreferenceModel mplPreferenceModelToSave = new MarketplacePreferenceModel();
			if (null != mplPreferenceModel)
			{
				mplPreferenceModelToSave = mplPreferenceModel;
			}
			else
			{
				mplPreferenceModelToSave = modelService.create(MarketplacePreferenceModel.class);
			}
			final List<CategoryModel> selectedCategoryList = new ArrayList<CategoryModel>();
			final List<CategoryModel> selectedBrandList = new ArrayList<CategoryModel>();
			mplPreferenceModelToSave.setIsInterestedInEmail(Boolean.FALSE);
			mplPreferenceModelToSave.setPreferredCategory(selectedCategoryList);
			mplPreferenceModelToSave.setPreferredBrand(selectedBrandList);
			mplPreferenceModelToSave.setEmailFrequency(Frequency.WEEKLY);
			mplPreferenceModelToSave.setUserReview(Boolean.valueOf(mplPreferenceData.isFeedbackUserReview()));
			mplPreferenceModelToSave.setCustomerSurveys(Boolean.valueOf(mplPreferenceData.isFeedbackCustomerSurveys()));

			//	saving marketplace preference model for current customer

			modelService.save(mplPreferenceModelToSave);
			final CustomerModel customerModel = getCurrentSessionCustomer();
			customerModel.setMarketplacepreference(mplPreferenceModelToSave);
			customerModel.setModifiedtime(new Date());
			modelService.save(customerModel);
		}
		catch (final ModelSavingException ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0007);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}
	}

	protected CustomerModel getCurrentSessionCustomer()
	{
		return (CustomerModel) userService.getCurrentUser();
	}


}
