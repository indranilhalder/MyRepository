/**
 *
 */
package com.tisl.mpl.facades.account.preference;

import de.hybris.platform.category.model.CategoryModel;

import java.util.Collection;
import java.util.List;

import com.tisl.mpl.facades.data.MplPreferenceData;
import com.tisl.mpl.facades.data.MplPreferencePopulationData;


/**
 * @author TCS
 *
 */
public interface MplPreferenceFacade
{
	/**
	 * @return MplPreferenceData
	 */
	MplPreferencePopulationData fetchAllMplPreferenceContents();

	/**
	 * @return MplPreferenceData
	 */
	MplPreferenceData fetchPresavedFavouritePreferences();

	/**
	 * @return List
	 */
	List<String> getFrequency();

	/**
	 * @return List
	 */
	List<String> getFeedbackArea();

	/**
	 * @return List
	 */
	List<String> getInterestPreference();

	/**
	 * @return Collection
	 */
	Collection<CategoryModel> getBaseSitePreferredCategories();

	/**
	 * @param mplPreferenceData
	 */
	void saveMarketplacePreference(MplPreferenceData mplPreferenceData);

	/**
	 * @param mplPreferenceData
	 */
	void unsubscribeAllPreferences(MplPreferenceData mplPreferenceData);

	/**
	 * @param mplPreferenceData
	 */
	void savePreferencesAndSubscription(MplPreferenceData mplPreferenceData);
}
