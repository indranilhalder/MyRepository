/**
 *
 */
package com.tisl.mpl.services;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.FeedbackArea;
import com.tisl.mpl.core.enums.Frequency;
import com.tisl.mpl.core.model.MarketplacePreferenceModel;
import com.tisl.mpl.facades.data.MplPreferenceData;
import com.tisl.mpl.helper.MplEnumerationHelper;


/**
 * @author TCS
 *
 */
@UnitTest
public class DefaultMplPreferenceServiceUnitTest
{
	@Autowired
	private MplEnumerationHelper mplEnumerationHelper;
	@Autowired
	private BaseSiteService baseSiteService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private UserService userService;

	//	private DefaultMplPreferenceService defaultMplPreferenceService;

	protected static final Logger LOG = Logger.getLogger(DefaultMplPreferenceServiceUnitTest.class);


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		//		this.defaultMplPreferenceService = new DefaultMplPreferenceService();
		this.mplEnumerationHelper = Mockito.mock(MplEnumerationHelper.class);
		this.modelService = Mockito.mock(ModelService.class);
		this.baseSiteService = Mockito.mock(BaseSiteService.class);
		this.userService = Mockito.mock(UserService.class);
	}

	@Test
	public void testFetchPresavedFavouritePreferences()
	{
		final CustomerModel currentCustomer = new CustomerModel();
		Mockito.when(userService.getCurrentUser()).thenReturn(currentCustomer);
		final MarketplacePreferenceModel mplPreferenceModel = new MarketplacePreferenceModel();
		Mockito.when(currentCustomer.getMarketplacepreference()).thenReturn(mplPreferenceModel);

		final MplPreferenceData mplPreferenceData = new MplPreferenceData();
		final List<CategoryData> categoryDataList = new ArrayList<CategoryData>();
		final List<CategoryData> categoryDataBrandList = new ArrayList<CategoryData>();

		mplPreferenceData.setSelectedCategory(categoryDataList);
		mplPreferenceData.setSelectedBrand(categoryDataBrandList);

		Mockito.when(mplPreferenceModel.getIsInterestedInEmail()).thenReturn(Boolean.TRUE);
		mplPreferenceData.setMyInterest(MarketplacecommerceservicesConstants.INTERESTED_IN_EMAIL);

		Mockito.when(mplPreferenceModel.getIsInterestedInEmail()).thenReturn(Boolean.FALSE);
		mplPreferenceData.setMyInterest(MarketplacecommerceservicesConstants.NOT_INTERESTED_IN_EMAIL);

		Mockito.when(mplPreferenceModel.getCustomerSurveys()).thenReturn(Boolean.FALSE);
		Mockito.when(mplPreferenceModel.getUserReview()).thenReturn(Boolean.FALSE);
		LOG.info("Method : testFetchPresavedFavouritePreferences >>>>>>>");
	}

	@Test
	public void testGetFrequency()
	{
		final List<EnumerationValueModel> enumList = new ArrayList<EnumerationValueModel>();
		Mockito.when(mplEnumerationHelper.getEnumerationValuesForCode(Frequency._TYPECODE)).thenReturn(enumList);
		final List<EnumerationValueModel> enumListAc = mplEnumerationHelper.getEnumerationValuesForCode(Frequency._TYPECODE);
		assertEquals("And should equals what the mock returned", enumList, enumListAc);
		LOG.info("Method : testGetFrequency >>>>>>>");
	}

	@Test
	public void testGetFeedbackArea()
	{
		final List<EnumerationValueModel> enumList = new ArrayList<EnumerationValueModel>();
		Mockito.when(mplEnumerationHelper.getEnumerationValuesForCode(FeedbackArea._TYPECODE)).thenReturn(enumList);
		final List<EnumerationValueModel> enumListAc = mplEnumerationHelper.getEnumerationValuesForCode(FeedbackArea._TYPECODE);
		assertEquals("And should equals what the mock returned", enumList, enumListAc);
		LOG.info("Method : testGetFeedbackArea >>>>>>>");
	}

	@Test
	public void testGetInterestPreference()
	{
		final List<String> interestPreferenceList = new ArrayList<String>();
		interestPreferenceList.add(0, MarketplacecommerceservicesConstants.INTERESTED_IN_EMAIL);
		interestPreferenceList.add(1, MarketplacecommerceservicesConstants.NOT_INTERESTED_IN_EMAIL);
		LOG.info("Method : testGetInterestPreference >>>>>>>");
	}

	@Test
	public void testGetBaseSitePreferredCategories()
	{
		final CategoryModel categoryModel = new CategoryModel();
		final List<CategoryModel> categoryList = Arrays.asList(categoryModel);
		Mockito.when(baseSiteService.getCurrentBaseSite().getMplPrefferedCategories()).thenReturn(categoryList);
		final Collection<CategoryModel> categoryListAc = baseSiteService.getCurrentBaseSite().getMplPrefferedCategories();
		assertEquals("And should equals what the mock returned", categoryList, categoryListAc);
		LOG.info("Method : testGetBaseSitePreferredCategories >>>>>>>");
	}

	@Test
	public void testSavePreferencesAndSubscription()
	{
		final CustomerModel currentCustomer = new CustomerModel();
		Mockito.when(userService.getCurrentUser()).thenReturn(currentCustomer);
		final List<CategoryModel> selectedCategoryList = new ArrayList<CategoryModel>();
		final List<CategoryModel> selectedBrandList = new ArrayList<CategoryModel>();
		final MarketplacePreferenceModel mplPreferenceModelToSave = new MarketplacePreferenceModel();

		mplPreferenceModelToSave.setIsInterestedInEmail(Boolean.TRUE);

		mplPreferenceModelToSave.setPreferredCategory(selectedCategoryList);
		mplPreferenceModelToSave.setPreferredBrand(selectedBrandList);

		mplPreferenceModelToSave.setEmailFrequency(Frequency.WEEKLY);

		mplPreferenceModelToSave.setUserReview(Boolean.TRUE);
		mplPreferenceModelToSave.setCustomerSurveys(Boolean.TRUE);


		final CustomerModel customerModel = new CustomerModel();
		Mockito.when(userService.getCurrentUser()).thenReturn(customerModel);
		customerModel.setMarketplacepreference(mplPreferenceModelToSave);
		Mockito.doNothing().when(modelService).save(mplPreferenceModelToSave);
		Mockito.doNothing().when(modelService).save(customerModel);
		LOG.info("Method : testSavePreferencesAndSubscription >>>>>>>");
	}

	@Test
	public void testUnsubscribeAllPreferences()
	{
		final CustomerModel currentCustomer = new CustomerModel();
		Mockito.when(userService.getCurrentUser()).thenReturn(currentCustomer);
		final List<CategoryModel> selectedCategoryList = new ArrayList<CategoryModel>();
		final List<CategoryModel> selectedBrandList = new ArrayList<CategoryModel>();
		final MarketplacePreferenceModel mplPreferenceModelToSave = new MarketplacePreferenceModel();

		mplPreferenceModelToSave.setIsInterestedInEmail(Boolean.FALSE);
		mplPreferenceModelToSave.setPreferredCategory(selectedCategoryList);
		mplPreferenceModelToSave.setPreferredBrand(selectedBrandList);
		mplPreferenceModelToSave.setEmailFrequency(Frequency.WEEKLY);

		mplPreferenceModelToSave.setUserReview(Boolean.TRUE);
		mplPreferenceModelToSave.setCustomerSurveys(Boolean.TRUE);


		final CustomerModel customerModel = new CustomerModel();
		Mockito.when(userService.getCurrentUser()).thenReturn(customerModel);
		customerModel.setMarketplacepreference(mplPreferenceModelToSave);
		Mockito.doNothing().when(modelService).save(mplPreferenceModelToSave);
		Mockito.doNothing().when(modelService).save(customerModel);
		LOG.info("Method : testUnsubscribeAllPreferences >>>>>>>");
	}
}
