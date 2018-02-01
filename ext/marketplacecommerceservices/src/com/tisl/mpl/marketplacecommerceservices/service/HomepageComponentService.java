/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;

import java.util.List;

import org.json.simple.JSONObject;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
public interface HomepageComponentService
{
	JSONObject getBestPicksJSON(ContentSlotModel contentSlot) throws EtailNonBusinessExceptions;

	JSONObject getProductsYouCareJSON(ContentSlotModel contentSlot) throws EtailNonBusinessExceptions;

	JSONObject getJsonBanner(ContentSlotModel contentSlot, String compType) throws EtailNonBusinessExceptions;

	JSONObject getJsonBannerAmp(ContentSlotModel contentSlot, String compType) throws EtailNonBusinessExceptions;

	List<CategoryModel> getCategoryPath(CategoryModel categoryModel);

	//TPR-558 Scheduling of banners
	boolean showOnTimeRestriction(final AbstractCMSComponentModel component);

	//TPR-1672
	JSONObject getBestOffersJSON(ContentSlotModel contentSlot) throws EtailNonBusinessExceptions;
}
