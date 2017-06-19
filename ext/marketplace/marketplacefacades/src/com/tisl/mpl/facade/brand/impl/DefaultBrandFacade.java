/**
 *
 */
package com.tisl.mpl.facade.brand.impl;

import de.hybris.platform.category.model.CategoryModel;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.tisl.mpl.facade.brand.BrandFacade;
import com.tisl.mpl.marketplacecommerceservices.service.brand.impl.DefaultBrandService;
import com.tisl.mpl.model.cms.components.MplNewsLetterSubscriptionModel;


/**
 * @author TCS
 *
 */
public class DefaultBrandFacade implements BrandFacade
{
	@Resource(name = "brandService")
	private DefaultBrandService brandService;


	/**
	 * This method segregates the brands in alphabetical order like A having Addidas,Allensolly B having BOSE,Bosch
	 *
	 * @param rootBrandCode
	 *
	 * @return Map<Character, List<CategoryModel>>
	 *
	 **/
	@Override
	public Map<Character, List<CategoryModel>> getAllBrandsInAplhabeticalOrder(final String rootBrandCode)
	{

		if (brandService.getAllBrandsInAplhabeticalOrder(rootBrandCode) != null)
		{
			return brandService.getAllBrandsInAplhabeticalOrder(rootBrandCode);
		}
		return null;
	}


	/**
	 * This method checks whether the email id exists or not
	 *
	 * @param emailId
	 *
	 * @return boolean value
	 *
	 **/
	@Override
	public boolean checkEmailId(final String emailId)
	{

		return brandService.checkEmailId(emailId);


	}

	@Override
	public List<MplNewsLetterSubscriptionModel> checkEmailIdForluxury(final String emailId)
	{

		return brandService.checkEmailIdForluxury(emailId);


	}

	@Override
	public List<MplNewsLetterSubscriptionModel> checkEmailIdForMarketplace(final String emailId)
	{
		return brandService.checkEmailIdForMarketplace(emailId);
	}

	@Override
	public List<MplNewsLetterSubscriptionModel> checkEmailIdForluxury(final String emailId, final String isLuxury)
	{

		return brandService.checkEmailIdForluxury(emailId, isLuxury);


	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.brand.BrandFacade#getAllBrandsFromCmsCockpit(java.lang.String)
	 */
	@Override
	public Map<Character, List<CategoryModel>> getAllBrandsFromCmsCockpit(final String componentUid)
	{
		if (brandService.getAllBrandsFromCmsCockpit(componentUid) != null)
		{
			return brandService.getAllBrandsFromCmsCockpit(componentUid);
		}
		return Collections.EMPTY_MAP;
	}



}
