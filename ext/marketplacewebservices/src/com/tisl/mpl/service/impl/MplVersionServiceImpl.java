/**
 *
 */
package com.tisl.mpl.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.service.MplCustomBrandService;
import com.tisl.mpl.service.MplCustomCategoryService;
import com.tisl.mpl.service.MplVersionService;
import com.tisl.mpl.wsdto.BrandListHierarchyData;
import com.tisl.mpl.wsdto.DepartmentListHierarchyData;
import com.tisl.mpl.wsdto.VersionListResponseData;
import com.tisl.mpl.wsdto.VersionResponseData;


/**
 * @author TCS
 *
 */

public class MplVersionServiceImpl implements MplVersionService
{

	//Shop by Brand Mobile
	@Resource(name = "mplCustomBrandService")
	private MplCustomBrandService mplCustomBrandService;
	//Shop by dept
	@Resource(name = "mplCustomCategoryService")
	private MplCustomCategoryService mplCustomCategoryService;
	private static final String SHOP_BY_BRAND = "Shop by brand";
	private static final String SHOP_BY_DEPARTMENT = "Shop by Department";

	/**
	 * Mobile caching - sending modified time
	 *
	 * @return VersionResponseData
	 */
	@Override
	public VersionListResponseData getVersionResponseDetails()
	{
		final VersionListResponseData versionListResponse = new VersionListResponseData();
		final List<VersionResponseData> versionList = new ArrayList<VersionResponseData>();
		try
		{
			//get Shop by brand final modified time
			final BrandListHierarchyData brandData = mplCustomBrandService.getShopByBrand();
			if (null != brandData && null != brandData.getModifiedTime())
			{

				final VersionResponseData versionResponse = new VersionResponseData();
				versionResponse.setJsonName(SHOP_BY_BRAND);
				versionResponse.setJsonModifiedTime(brandData.getModifiedTime());
				versionList.add(versionResponse);
			}
			//get Shop by department final modified time
			final DepartmentListHierarchyData shopByDeptData = mplCustomCategoryService.getAllCategories();

			if (null != shopByDeptData && null != shopByDeptData.getModifiedTime())
			{
				final VersionResponseData versionResponse = new VersionResponseData();
				versionResponse.setJsonName(SHOP_BY_DEPARTMENT);
				versionResponse.setJsonModifiedTime(shopByDeptData.getModifiedTime());
				versionList.add(versionResponse);
			}
			if (!versionList.isEmpty())
			{
				versionListResponse.setVersionResponse(versionList);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			throw e;
		}
		catch (final Exception e)
		{
			new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}

		return versionListResponse;
	}
}
