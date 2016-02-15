/**
 *
 */
package com.tisl.mpl.facade.account.test;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.facades.data.MplPreferenceData;
import com.tisl.mpl.facades.data.MplPreferencePopulationData;
import com.tisl.mpl.marketplacecommerceservices.service.MplPreferenceService;


/**
 * @author TCS
 *
 */


@UnitTest
public class DefaultMplPreferenceFacadeUnitTest
{
	@Autowired
	private MplPreferenceService mplPreferenceService;
	@Autowired
	private ModelService modelService;
	//private DefaultMplPreferenceFacade defaultMplPreferenceFacade;
	protected static final Logger LOG = Logger.getLogger(DefaultMplPreferenceFacadeUnitTest.class);

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		//this.defaultMplPreferenceFacade = new DefaultMplPreferenceFacade();
		this.mplPreferenceService = Mockito.mock(MplPreferenceService.class);
		this.modelService = Mockito.mock(ModelService.class);
	}

	@Test
	public void testFetchAllMplPreferenceContents()
	{
		final MplPreferencePopulationData mplPreferenceData = modelService.create(MplPreferencePopulationData.class);
		Mockito.when(mplPreferenceService.fetchAllMplPreferenceContents()).thenReturn(mplPreferenceData);
		final MplPreferencePopulationData mplPreferenceDataAc = mplPreferenceService.fetchAllMplPreferenceContents();
		assertEquals("And should equals what the mock returned", mplPreferenceData, mplPreferenceDataAc);
		LOG.info("Method : testFetchAllMplPreferenceContents >>>>>>>");
	}

	@Test
	public void testFetchPresavedFavouritePreferences()
	{
		final MplPreferenceData mplPreferenceData = modelService.create(MplPreferenceData.class);
		Mockito.when(mplPreferenceService.fetchPresavedFavouritePreferences()).thenReturn(mplPreferenceData);
		final MplPreferenceData mplPreferenceDataAc = mplPreferenceService.fetchPresavedFavouritePreferences();
		assertEquals("And should equals what the mock returned", mplPreferenceData, mplPreferenceDataAc);
		LOG.info("Method : testFetchPresavedFavouritePreferences >>>>>>>");
	}

	@Test
	public void testGetFrequency()
	{
		final String frequency = "";
		final List<String> frequencyList = Arrays.asList(frequency);
		Mockito.when(mplPreferenceService.getFrequency()).thenReturn(frequencyList);
		final List<String> frequencyListAc = mplPreferenceService.getFrequency();
		assertEquals("And should equals what the mock returned", frequencyList, frequencyListAc);
		LOG.info("Method : testGetFrequency >>>>>>>");
	}

	@Test
	public void testGetFeedbackArea()
	{
		final String feedBack = "";
		final List<String> feedBackList = Arrays.asList(feedBack);
		Mockito.when(mplPreferenceService.getFeedbackArea()).thenReturn(feedBackList);
		final List<String> feedBackListAc = mplPreferenceService.getFeedbackArea();
		assertEquals("And should equals what the mock returned", feedBackList, feedBackListAc);
		LOG.info("Method : testGetFeedbackArea >>>>>>>");
	}

	@Test
	public void testGetInterestPreference()
	{
		final String interest = "";
		final List<String> interestList = Arrays.asList(interest);
		Mockito.when(mplPreferenceService.getInterestPreference()).thenReturn(interestList);
		final List<String> interestListAc = mplPreferenceService.getInterestPreference();
		assertEquals("And should equals what the mock returned", interestList, interestListAc);
		LOG.info("Method : testGetInterestPreference >>>>>>>");
	}

	@Test
	public void testGetBaseSitePreferredCategories()
	{
		final CategoryModel categoryModel = new CategoryModel();
		final List<CategoryModel> categoryList = Arrays.asList(categoryModel);
		Mockito.when(mplPreferenceService.getBaseSitePreferredCategories()).thenReturn(categoryList);
		final Collection<CategoryModel> categoryListAc = mplPreferenceService.getBaseSitePreferredCategories();
		assertEquals("And should equals what the mock returned", categoryList, categoryListAc);
		LOG.info("Method : testGetBaseSitePreferredCategories >>>>>>>");
	}

	@Test
	public void testSaveMarketplacePreference()
	{
		final MplPreferenceData mplPreferenceData = modelService.create(MplPreferencePopulationData.class);
		Mockito.doNothing().when(mplPreferenceService).saveMarketplacePreference(mplPreferenceData);
		mplPreferenceService.saveMarketplacePreference(mplPreferenceData);
		LOG.info("Method : testSaveMarketplacePreference >>>>>>>");
	}


	@Test
	public void testSavePreferencesAndSubscription()
	{
		final MplPreferenceData mplPreferenceData = modelService.create(MplPreferencePopulationData.class);
		Mockito.doNothing().when(mplPreferenceService).saveMarketplacePreference(mplPreferenceData);
		mplPreferenceService.saveMarketplacePreference(mplPreferenceData);
		LOG.info("Method : testSavePreferencesAndSubscription >>>>>>>");
	}

	@Test
	public void testUnsubscribeAllPreferences()
	{
		final MplPreferenceData mplPreferenceData = modelService.create(MplPreferencePopulationData.class);
		Mockito.doNothing().when(mplPreferenceService).unsubscribeAllPreferences(mplPreferenceData);
		mplPreferenceService.unsubscribeAllPreferences(mplPreferenceData);
		LOG.info("Method : testSavePreferencesAndSubscription >>>>>>>");
	}

}
