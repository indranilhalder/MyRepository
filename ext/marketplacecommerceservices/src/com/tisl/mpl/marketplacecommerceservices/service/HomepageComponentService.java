/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;

import org.json.simple.JSONObject;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
public interface HomepageComponentService
{
	JSONObject getBestPicksJSON(ContentSlotModel contentSlot) throws EtailNonBusinessExceptions;

	JSONObject getBrandsYouLoveJSON(ContentSlotModel contentSlot) throws EtailNonBusinessExceptions;

	JSONObject getProductsYouCareJSON(ContentSlotModel contentSlot) throws EtailNonBusinessExceptions;


	JSONObject getNewandExclusiveJSON(ContentSlotModel contentSlot) throws EtailNonBusinessExceptions;

	JSONObject getShowCaseJSON(ContentSlotModel contentSlot) throws EtailNonBusinessExceptions;



	JSONObject getJsonBanner(ContentSlotModel contentSlot, String compType) throws EtailNonBusinessExceptions;

}
