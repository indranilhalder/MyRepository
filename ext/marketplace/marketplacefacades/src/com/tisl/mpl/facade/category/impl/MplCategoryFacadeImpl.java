package com.tisl.mpl.facade.category.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.catalog.impl.DefaultCatalogFacade;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.facade.category.MplCategoryFacade;
import com.tisl.mpl.marketplacecommerceservices.service.MplCategoryService;
import com.tisl.mpl.marketplacecommerceservices.service.MplSellerInformationService;
import com.tisl.mpl.model.SellerInformationModel;
import com.tisl.mpl.model.SellerSalesCategoryModel;


/**
 * @author TCS
 *
 */
public class MplCategoryFacadeImpl extends DefaultCatalogFacade implements MplCategoryFacade
{

	private ConfigurationService configurationService;

	@Autowired
	private Converter<CategoryModel, CategoryData> categoryConverter;


	private CMSSiteService cmsSiteService;


	/**
	 * @return the cmsSiteService
	 */
	public CMSSiteService getCmsSiteService()
	{
		return cmsSiteService;
	}

	/**
	 * @param cmsSiteService
	 *           the cmsSiteService to set
	 */
	public void setCmsSiteService(final CMSSiteService cmsSiteService)
	{
		this.cmsSiteService = cmsSiteService;
	}

	/**
	 * @return the mplCategoryService
	 */
	public MplCategoryService getMplCategoryService()
	{
		return mplCategoryService;
	}

	/**
	 * @param mplCategoryService
	 *           the mplCategoryService to set
	 */
	public void setMplCategoryService(final MplCategoryService mplCategoryService)
	{
		this.mplCategoryService = mplCategoryService;
	}


	private MplCategoryService mplCategoryService;

	private MplSellerInformationService mplSellerInformationService;

	/**
	 * @return the mplSellerInformationService
	 */
	public MplSellerInformationService getMplSellerInformationService()
	{
		return mplSellerInformationService;
	}

	public void setMplSellerInformationService(final MplSellerInformationService mplSellerInformationService)
	{
		this.mplSellerInformationService = mplSellerInformationService;
	}

	@Override
	public String getSellerInformationBySellerName(final String sellerName)
	{
		final SellerInformationModel sellerInformationModel = mplSellerInformationService
				.getSellerInformationBySellerName(cmsSiteService.getCurrentCatalogVersion(), sellerName.toUpperCase());
		if (null != sellerInformationModel)
		{
			final String sellerID = sellerInformationModel.getSellerID();
			return sellerID;
		}
		return null;
	}

	@Override
	public String getActiveSellerRootCategoryBySellerId(final String sellerId)
	{
		final SellerSalesCategoryModel sellerStructureModel = mplSellerInformationService
				.getActiveSellerRootCategoryBySellerId(sellerId.toUpperCase());
		final String SellerRootCategoryId = sellerStructureModel.getSellerRootCategoryId();
		return SellerRootCategoryId;
	}

	/**
	 * It will fetch the sub category tree for the given category name. (non-Javadoc)
	 *
	 * @see com.tisl.mpl.facade.category.MplCategoryFacade#getShopBrandCategories(java.lang.String)
	 *
	 * @param sellerName
	 */
	@Override
	public CategoryData getShopBrandCategories(final String sellerName)

	{
		final String sellerId = getSellerInformationBySellerName(sellerName);
		if (sellerId != null)
		{
			final String sellerRootCategoryId = getActiveSellerRootCategoryBySellerId(sellerId);
			Preconditions.checkArgument(sellerName != null, "Category is required to perform this operation, null given");
			final String categoryCode = getConfigurationService().getConfiguration()
					.getString("marketplace.mplcatalog.seller.sales.category.root.id");

			final CategoryModel topCategoryDetails = mplCategoryService
					.getCategoryModelForCode(cmsSiteService.getCurrentCatalogVersion(), categoryCode);//SSH1

			boolean isCategoryIDAvailable = false;

			CategoryData secondLevelCategoryData = null;
			List<CategoryData> thirdLevelCategorydataList = null;
			CategoryData thirdLevelCategoryData = null;
			CategoryData fourthLevelCategoryData = null;
			List<CategoryData> fourthLevelCategorydataList = null;
			List<CategoryData> fifthLevelCategorydataList = null;
			CategoryData fifthLevelCategoryData = null;

			if (topCategoryDetails == null)
			{
				throw new UnknownIdentifierException("Category with code '" + categoryCode
						+ "' not found! (Active session catalogversions: " + cmsSiteService.getCurrentCatalogVersion() + ")");
			}

			for (final CategoryModel secondLevelCategoryModel : topCategoryDetails.getCategories())
			{
				if (secondLevelCategoryModel.getCode().equalsIgnoreCase(sellerRootCategoryId))
				{
					isCategoryIDAvailable = true;
					secondLevelCategoryData = categoryConverter.convert(secondLevelCategoryModel);

					thirdLevelCategorydataList = new ArrayList<CategoryData>();
					if (!secondLevelCategoryModel.getCategories().isEmpty())
					{
						for (final CategoryModel thirdLevelCategoryModel : secondLevelCategoryModel.getCategories())
						{
							thirdLevelCategoryData = categoryConverter.convert(thirdLevelCategoryModel);
							fourthLevelCategorydataList = new ArrayList<CategoryData>();
							if (!thirdLevelCategoryModel.getCategories().isEmpty())
							{
								for (final CategoryModel fourthLevelCategoryModel : thirdLevelCategoryModel.getCategories())
								{
									fourthLevelCategoryData = categoryConverter.convert(fourthLevelCategoryModel);
									fifthLevelCategorydataList = new ArrayList<CategoryData>();
									if (!fourthLevelCategoryModel.getCategories().isEmpty())
									{
										for (final CategoryModel fifthLevelCategoryModel : fourthLevelCategoryModel.getAllSubcategories())
										{
											fifthLevelCategoryData = categoryConverter.convert(fifthLevelCategoryModel);
											fifthLevelCategorydataList.add(fifthLevelCategoryData);
										}
										fourthLevelCategoryData.setSubCategories(fifthLevelCategorydataList);
									}
									fourthLevelCategorydataList.add(fourthLevelCategoryData);
								}
								thirdLevelCategoryData.setSubCategories(fourthLevelCategorydataList);
							}
							thirdLevelCategorydataList.add(thirdLevelCategoryData);
						}
					}
					secondLevelCategoryData.setSubCategories(thirdLevelCategorydataList);
				}
			}
			if (!isCategoryIDAvailable)
			{
				throw new EtailBusinessExceptions("Category with name '" + sellerName
						+ "' not found! (Active session catalogversions: " + cmsSiteService.getCurrentCatalogVersion() + ")");
			}
			return secondLevelCategoryData;
		}
		return null;
	}


	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}
}