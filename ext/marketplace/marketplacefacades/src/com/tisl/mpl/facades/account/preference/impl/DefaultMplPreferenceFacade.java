/**
 *
 */
package com.tisl.mpl.facades.account.preference.impl;

import de.hybris.platform.category.model.CategoryModel;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.account.preference.MplPreferenceFacade;
import com.tisl.mpl.facades.data.MplPreferenceData;
import com.tisl.mpl.facades.data.MplPreferencePopulationData;
import com.tisl.mpl.marketplacecommerceservices.service.MplPreferenceService;


/**
 * @author TCS
 *
 */
public class DefaultMplPreferenceFacade implements MplPreferenceFacade
{
	@Autowired
	private MplPreferenceService mplPreferenceService;

	/**
	 * @return the mplPreferenceService
	 */
	public MplPreferenceService getMplPreferenceService()
	{
		return mplPreferenceService;
	}

	/**
	 * @param mplPreferenceService
	 *           the mplPreferenceService to set
	 */
	public void setMplPreferenceService(final MplPreferenceService mplPreferenceService)
	{
		this.mplPreferenceService = mplPreferenceService;
	}

	protected static final Logger LOG = Logger.getLogger(DefaultMplPreferenceFacade.class);


	/**
	 *
	 */
	@Override
	public MplPreferencePopulationData fetchAllMplPreferenceContents()
	{
		try
		{
			final MplPreferencePopulationData mplPreferenceData = mplPreferenceService.fetchAllMplPreferenceContents();
			return mplPreferenceData;
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
	public MplPreferenceData fetchPresavedFavouritePreferences()
	{
		try
		{
			final MplPreferenceData mplPreferenceData = mplPreferenceService.fetchPresavedFavouritePreferences();
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
			final List<String> frequencyList = mplPreferenceService.getFrequency();
			return frequencyList;
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
			final List<String> feedbackAreaList = mplPreferenceService.getFeedbackArea();
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
			final List<String> interestList = mplPreferenceService.getInterestPreference();
			return interestList;
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
			final Collection<CategoryModel> categoryList = mplPreferenceService.getBaseSitePreferredCategories();
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
				mplPreferenceService.saveMarketplacePreference(mplPreferenceData);
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
	 * @see
	 * com.tisl.mpl.facades.account.preference.MplPreferenceFacade#savePreferencesAndSubscription(com.tisl.mpl.facades
	 * .data.MplPreferenceData)
	 */
	@Override
	public void savePreferencesAndSubscription(final MplPreferenceData mplPreferenceData)
	{
		try
		{
			if (null != mplPreferenceData)
			{
				mplPreferenceService.saveMarketplacePreference(mplPreferenceData);
			}
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}

	}

	/*
	 *
	 * @param (MplPreferenceData)
	 */
	@Override
	public void unsubscribeAllPreferences(final MplPreferenceData mplPreferenceData)
	{

		try
		{
			mplPreferenceService.unsubscribeAllPreferences(mplPreferenceData);
		}
		catch (final Exception ex)
		{
			throw new EtailNonBusinessExceptions(ex, MarketplacecommerceservicesConstants.E0000);
		}

	}

}
