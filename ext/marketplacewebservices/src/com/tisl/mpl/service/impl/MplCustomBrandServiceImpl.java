/**
 *
 */
package com.tisl.mpl.service.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.enums.LayoutStructure;
import com.tisl.mpl.core.model.BrandCollectionComponentModel;
import com.tisl.mpl.core.model.BrandComponentModel;
import com.tisl.mpl.core.model.CMSSubbrandModel;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.service.brand.impl.DefaultBrandService;
import com.tisl.mpl.service.MplCustomBrandService;
import com.tisl.mpl.wsdto.BrandHierarchyData;
import com.tisl.mpl.wsdto.BrandListHierarchyData;
import com.tisl.mpl.wsdto.BrandListHierarchyDataAmp;
import com.tisl.mpl.wsdto.BrandSubHierarchyData;


/**
 * @author TCS
 *
 */
public class MplCustomBrandServiceImpl implements MplCustomBrandService
{
	@Autowired
	private DefaultBrandService defaultBrandService;
	@Resource(name = "cmsComponentService")
	private CMSComponentService cmsComponentService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	private static final String CODE = "MBH1";

	private static final String SELLER = "seller";
	private static final String BRAND = "brand";
	private static final String C = "/c-";

	//private static final String MICROSITE = "microsite";

	/*
	 * This method initially gets Shop by brand component for the component id and then fetches the corresponding fields
	 * Sets latest modified time for mobile caching
	 * 
	 * @see com.tisl.mpl.service.MplCustomBrandService#getShopByBrand()
	 */
	@Override
	public BrandListHierarchyData getShopByBrand() throws EtailNonBusinessExceptions
	{

		final BrandListHierarchyData shopByBrandData = new BrandListHierarchyData();
		final List<BrandHierarchyData> brandDataList = new ArrayList<BrandHierarchyData>();
		List<BrandSubHierarchyData> subBrandDataList = null;
		BrandHierarchyData brandData = null;
		Date modifiedTime = null;
		Collection<CategoryModel> sub_brand_list = null;
		try
		{
			final BrandCollectionComponentModel shopByBrandComponent = getShopByBrandComponent();
			if (null != shopByBrandComponent)
			{
				modifiedTime = getModifiedTime();
			}
			if (null != shopByBrandComponent.getBrandCollection() && !shopByBrandComponent.getBrandCollection().isEmpty())
			{

				for (final BrandComponentModel brand : shopByBrandComponent.getBrandCollection())
				{
					if (null != brand.getModifiedtime() && brand.getModifiedtime().compareTo(modifiedTime) > 0)
					{
						modifiedTime = brand.getModifiedtime();
					}
					subBrandDataList = new ArrayList<BrandSubHierarchyData>();
					brandData = new BrandHierarchyData();
					if (null != brand.getMasterBrandName() && StringUtils.isNotEmpty(brand.getMasterBrandName()))
					{
						brandData.setMenu_brand_name(brand.getMasterBrandName());
					}
					if (null != brand.getMasterBrandURL() && StringUtils.isNotEmpty(brand.getMasterBrandURL()))
					{
						brandData.setMenu_brand_logo(brand.getMasterBrandURL());
					}
					if (null != brand.getLayout())
					{
						brandData.setLayoutStructure(brand.getLayout().toString());
					}
					sub_brand_list = new ArrayList<CategoryModel>();


					Map<Character, List<CategoryModel>> sortedMap;

					if (null != brand.getUid() && brand.getUid().equalsIgnoreCase("AToZBrandsComponent"))
					{

						sortedMap = defaultBrandService.getAllBrandsInAplhabeticalOrder(CODE);
						for (final Entry<Character, List<CategoryModel>> entry : sortedMap.entrySet())
						{
							for (final CategoryModel subBrand : entry.getValue())
							{
								sub_brand_list.add(subBrand);

							}
						}


					}
					else if (null != brand.getLayout() && brand.getLayout().equals(LayoutStructure.FIVEBRANDIMAGES))
					{
						subBrandDataList.addAll(getMultiBrandStoreData(brand));
					}
					else
					{
						if (brand.getSubBrands() != null)
						{
							for (final CategoryModel subbrand : brand.getSubBrands())
							{
								sub_brand_list.add(subbrand);
							}
						}
					}
					if (null != brand.getLayout() && !brand.getLayout().equals(LayoutStructure.FIVEBRANDIMAGES))
					{
						for (final CategoryModel oModel : sub_brand_list)
						{
							if (null != oModel.getModifiedtime() && oModel.getModifiedtime().compareTo(modifiedTime) > 0)
							{
								modifiedTime = oModel.getModifiedtime();
							}

							final BrandSubHierarchyData subbranddata = new BrandSubHierarchyData();
							if (null != oModel.getCode() && StringUtils.isNotEmpty(oModel.getCode()))
							{
								subbranddata.setSub_brand_code(oModel.getCode());
							}

							if (null != oModel.getName() && StringUtils.isNotEmpty(oModel.getName()))
							{
								subbranddata.setSub_brand_name(oModel.getName());
							}
							//							final String subBrandUrl = "/Categories/" + oModel.getName() + "c/" + oModel.getCode();

							final String subBrandUrl = "/Categories/" + oModel.getName() + C + oModel.getCode();
							if (StringUtils.isNotEmpty(subBrandUrl))
							{

								subbranddata.setSub_brand_url(subBrandUrl);
							}
							subBrandDataList.add(subbranddata);


						}
					}
					if (!subBrandDataList.isEmpty())
					{
						brandData.setSubBrands(subBrandDataList);
					}
					brandDataList.add(brandData);
				}
			}
			if (null != modifiedTime)
			{
				shopByBrandData.setModifiedTime(modifiedTime.toString());
			}
			if (!brandDataList.isEmpty())
			{
				shopByBrandData.setShopbybrand(brandDataList);
			}
		}
		catch (final CMSItemNotFoundException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		catch (final EtailBusinessExceptions e)
		{
			throw e;

		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);

		}
		return shopByBrandData;
	}

	@Override
	public BrandListHierarchyDataAmp getShopByBrandAmp() throws EtailNonBusinessExceptions
	{

		final BrandListHierarchyDataAmp shopByBrandData = new BrandListHierarchyDataAmp();
		final List<BrandHierarchyData> brandDataList = new ArrayList<BrandHierarchyData>();
		List<BrandSubHierarchyData> subBrandDataList = null;
		BrandHierarchyData brandData = null;
		Date modifiedTime = null;
		Collection<CategoryModel> sub_brand_list = null;
		try
		{
			final BrandCollectionComponentModel shopByBrandComponent = getShopByBrandComponent();
			if (null != shopByBrandComponent)
			{
				modifiedTime = getModifiedTime();
			}
			if (null != shopByBrandComponent.getBrandCollection() && !shopByBrandComponent.getBrandCollection().isEmpty())
			{

				for (final BrandComponentModel brand : shopByBrandComponent.getBrandCollection())
				{
					if (null != brand.getModifiedtime() && brand.getModifiedtime().compareTo(modifiedTime) > 0)
					{
						modifiedTime = brand.getModifiedtime();
					}
					subBrandDataList = new ArrayList<BrandSubHierarchyData>();
					brandData = new BrandHierarchyData();
					if (null != brand.getMasterBrandName() && StringUtils.isNotEmpty(brand.getMasterBrandName()))
					{
						brandData.setMenu_brand_name(brand.getMasterBrandName());
					}
					if (null != brand.getMasterBrandURL() && StringUtils.isNotEmpty(brand.getMasterBrandURL()))
					{
						brandData.setMenu_brand_logo(brand.getMasterBrandURL());
					}
					if (null != brand.getLayout())
					{
						brandData.setLayoutStructure(brand.getLayout().toString());
					}
					sub_brand_list = new ArrayList<CategoryModel>();


					Map<Character, List<CategoryModel>> sortedMap;

					if (null != brand.getUid() && brand.getUid().equalsIgnoreCase("AToZBrandsComponent"))
					{

						sortedMap = defaultBrandService.getAllBrandsInAplhabeticalOrder(CODE);
						for (final Entry<Character, List<CategoryModel>> entry : sortedMap.entrySet())
						{
							for (final CategoryModel subBrand : entry.getValue())
							{
								sub_brand_list.add(subBrand);

							}
						}


					}
					else if (null != brand.getLayout() && brand.getLayout().equals(LayoutStructure.FIVEBRANDIMAGES))
					{
						subBrandDataList.addAll(getMultiBrandStoreData(brand));
					}
					else
					{
						if (brand.getSubBrands() != null)
						{
							for (final CategoryModel subbrand : brand.getSubBrands())
							{
								sub_brand_list.add(subbrand);
							}
						}
					}
					if (null != brand.getLayout() && !brand.getLayout().equals(LayoutStructure.FIVEBRANDIMAGES))
					{
						for (final CategoryModel oModel : sub_brand_list)
						{
							if (null != oModel.getModifiedtime() && oModel.getModifiedtime().compareTo(modifiedTime) > 0)
							{
								modifiedTime = oModel.getModifiedtime();
							}

							final BrandSubHierarchyData subbranddata = new BrandSubHierarchyData();
							if (null != oModel.getCode() && StringUtils.isNotEmpty(oModel.getCode()))
							{
								subbranddata.setSub_brand_code(oModel.getCode());
							}

							if (null != oModel.getName() && StringUtils.isNotEmpty(oModel.getName()))
							{
								subbranddata.setSub_brand_name(oModel.getName());
							}
							//							final String subBrandUrl = "/Categories/" + oModel.getName() + "c/" + oModel.getCode();

							final String subBrandUrl = "/Categories/" + oModel.getName() + C + oModel.getCode();
							if (StringUtils.isNotEmpty(subBrandUrl))
							{

								subbranddata.setSub_brand_url(subBrandUrl);
							}
							subBrandDataList.add(subbranddata);


						}
					}
					if (!subBrandDataList.isEmpty())
					{
						brandData.setSubBrands(subBrandDataList);
					}
					brandDataList.add(brandData);
				}
			}
			if (null != modifiedTime)
			{
				shopByBrandData.setModifiedTime(modifiedTime.toString());
			}
			if (!brandDataList.isEmpty())
			{
				shopByBrandData.setItems(brandDataList);
			}
		}
		catch (final CMSItemNotFoundException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);
		}
		catch (final EtailBusinessExceptions e)
		{
			throw e;

		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.B9004);

		}
		return shopByBrandData;
	}

	/**
	 * @param brand
	 */
	private List<BrandSubHierarchyData> getMultiBrandStoreData(final BrandComponentModel brand)
	{
		final List<BrandSubHierarchyData> subBrandDataList = new ArrayList<BrandSubHierarchyData>();
		final List<CMSSubbrandModel> multibrandList = brand.getSubBrandList();
		boolean micrositeId;
		if (multibrandList != null)
		{
			for (final CMSSubbrandModel subrand : multibrandList)
			{
				micrositeId = false;
				final BrandSubHierarchyData subbranddata = new BrandSubHierarchyData();


				if (null != subrand.getSubBrandUrl() && StringUtils.isNotEmpty(subrand.getSubBrandUrl()))
				{
					String subBrandCode = null;
					String subBrandurl = null;

					if (subrand.getSubBrandUrl().contains("/s/"))
					{
						subbranddata.setSub_brand_type(SELLER);
						subbranddata.setSub_brand_url(subrand.getSubBrandUrl().substring(3));


						subBrandurl = subrand.getSubBrandUrl();
						subBrandCode = subBrandurl.substring(subBrandurl.lastIndexOf('/') + 1);
						if (null != subBrandCode)
						{
							subbranddata.setSub_brand_code(subBrandCode);
						}
					}
					else if (subrand.getSubBrandUrl().contains("/c/") || subrand.getSubBrandUrl().contains(C))
					{
						subbranddata.setSub_brand_type(BRAND);
						subbranddata.setSub_brand_url(subrand.getSubBrandUrl().substring(3));

						subBrandurl = subrand.getSubBrandUrl();
						if (subrand.getSubBrandUrl().contains("/c/"))
						{
							subBrandCode = subBrandurl.substring(subBrandurl.lastIndexOf('/') + 1);
						}
						else if (subrand.getSubBrandUrl().contains(C))
						{
							subBrandCode = subBrandurl.substring(subBrandurl.lastIndexOf('-') + 1);
						}
						if (null != subBrandCode)
						{
							subbranddata.setSub_brand_code(subBrandCode);
						}
					}
					else
					{
						//subbranddata.setSub_brand_type(MICROSITE);
						micrositeId = true;
					}
					//subbranddata.setSub_brand_url(subrand.getSubBrandUrl()); }


				}

				if (null != subrand.getSubBrandName() && StringUtils.isNotEmpty(subrand.getSubBrandName()))
				{
					subbranddata.setSub_brand_name(subrand.getSubBrandName());
				}
				if (null != subrand.getSubBrandLogo() && StringUtils.isNotEmpty(subrand.getSubBrandLogo().getURL()))
				{
					subbranddata.setSub_brand_logo(subrand.getSubBrandLogo().getURL());
				}

				if (null != subrand.getSubBrandImage() && null != subrand.getSubBrandImage().getURL()
						&& !subrand.getSubBrandImage().getURL().isEmpty())
				{
					subbranddata.setSubBrandImageUrl(subrand.getSubBrandImage().getURL());

				}

				if (!micrositeId)
				{
					subBrandDataList.add(subbranddata);
				}
			}
		}
		return subBrandDataList;
	}

	/**
	 * This method initially gets Shop by brand component for the component id
	 *
	 * @return BrandCollectionComponentModel
	 * @throws CMSItemNotFoundException
	 */
	private BrandCollectionComponentModel getShopByBrandComponent() throws CMSItemNotFoundException
	{
		String componentUid = null;
		if (null != configurationService && null != configurationService.getConfiguration()
				&& null != MarketplacecommerceservicesConstants.SHOPBYBRANDCOMPONENT)
		{
			componentUid = configurationService.getConfiguration().getString(
					MarketplacecommerceservicesConstants.SHOPBYBRANDCOMPONENT, "");
		}
		final BrandCollectionComponentModel shopByBrandComponent = (BrandCollectionComponentModel) cmsComponentService
				.getSimpleCMSComponent(componentUid);
		return shopByBrandComponent;
	}

	/**
	 * Modified time
	 *
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	private Date getModifiedTime() throws CMSItemNotFoundException
	{
		final BrandCollectionComponentModel shopByBrandComponent = getShopByBrandComponent();
		Date modifiedTime = null;
		if (null != shopByBrandComponent && null != shopByBrandComponent.getModifiedtime())
		{
			modifiedTime = shopByBrandComponent.getModifiedtime();
		}
		return modifiedTime;
	}
}
