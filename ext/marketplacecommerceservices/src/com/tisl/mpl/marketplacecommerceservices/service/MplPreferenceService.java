/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.category.model.CategoryModel;

import java.util.Collection;
import java.util.List;

import com.tisl.mpl.facades.data.MplPreferenceData;
import com.tisl.mpl.facades.data.MplPreferencePopulationData;


/**
 * @author TCS
 *
 */
public interface MplPreferenceService
{

	/**
	 * @return MplPreferenceData
	 */
	public MplPreferencePopulationData fetchAllMplPreferenceContents();

	/**
	 * @return MplPreferenceData
	 */
	public MplPreferenceData fetchPresavedFavouritePreferences();

	/**
	 * @return List
	 */
	public List<String> getFrequency();

	/**
	 * @return List
	 */
	public List<String> getFeedbackArea();

	/**
	 * @return List
	 */
	public List<String> getInterestPreference();

	/**
	 * @return Collection
	 */
	public Collection<CategoryModel> getBaseSitePreferredCategories();

	/**
	 * @param mplPreferenceData
	 */
	public void saveMarketplacePreference(MplPreferenceData mplPreferenceData);

	/**
	 * @param mplPreferenceData
	 */
	public void unsubscribeAllPreferences(MplPreferenceData mplPreferenceData);

}
