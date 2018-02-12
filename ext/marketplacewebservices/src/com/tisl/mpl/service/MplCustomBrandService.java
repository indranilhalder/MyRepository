/**
 *
 */
package com.tisl.mpl.service;

import com.tisl.mpl.wsdto.BrandListHierarchyData;
import com.tisl.mpl.wsdto.BrandListHierarchyDataAmp;


/**
 * @author TCS
 *
 */
public interface MplCustomBrandService
{
	public BrandListHierarchyData getShopByBrand();

	public BrandListHierarchyDataAmp getShopByBrandAmp();

}
