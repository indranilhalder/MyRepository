/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

import java.util.List;

import com.tisl.mpl.core.model.SizeGuideModel;


/**
 * @author TCS
 *
 */
public interface SizeGuideService
{

	/**
	 * @description It will take the product code from the Product model
	 * @param productCode
	 * @return List<SizeGuideModel>
	 *
	 */
	public List<SizeGuideModel> getProductSizeGuideList(String productCode) throws CMSItemNotFoundException;



}