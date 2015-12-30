/**
 *
 */
package com.tisl.mpl.facade.brand.impl;

import de.hybris.platform.category.model.CategoryModel;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.tisl.mpl.facade.brand.BrandFacade;
import com.tisl.mpl.marketplacecommerceservices.service.brand.impl.DefaultBrandService;


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
	 * **/
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
	 * **/
	@Override
	public boolean checkEmailId(final String emailId)
	{

		return brandService.checkEmailId(emailId);


	}



}
