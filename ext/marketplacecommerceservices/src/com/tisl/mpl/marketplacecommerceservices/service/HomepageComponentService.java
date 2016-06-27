/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;


/**
 * @author TCS
 *
 */
public interface HomepageComponentService
{
	JSONObject getBestPicksJSON(ContentSlotModel contentSlot, HttpServletRequest request) throws EtailNonBusinessExceptions;

	JSONObject getProductsYouCareJSON(ContentSlotModel contentSlot, HttpServletRequest request) throws EtailNonBusinessExceptions;

	JSONObject getJsonBanner(ContentSlotModel contentSlot, String compType) throws EtailNonBusinessExceptions;

	List<CategoryModel> getCategoryPath(CategoryModel categoryModel);
}
