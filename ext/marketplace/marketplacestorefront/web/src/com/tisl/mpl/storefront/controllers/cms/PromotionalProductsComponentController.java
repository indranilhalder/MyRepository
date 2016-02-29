/*
 * (non-Javadoc)
 *
 * @see
 * com.tisl.mpl.storefront.controllers.cms.AbstractCMSComponentController#fillModel(javax.servlet.http.HttpServletRequest
 * , org.springframework.ui.Model, de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel)
 */


package com.tisl.mpl.storefront.controllers.cms;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController.ShowMode;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tisl.mpl.facades.product.data.CategoryData;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.mpl.model.cms.components.PromotionalProductsComponentModel;
import com.tisl.mpl.solrfacet.search.impl.DefaultMplProductSearchFacade;
import com.tisl.mpl.storefront.controllers.ControllerConstants;


/**
 * @author TCS
 *
 */
@SuppressWarnings(
{ "PMD" })
@Controller("PromotionalProductsComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.PromotionalProductsComponent)
public class PromotionalProductsComponentController extends AbstractCMSComponentController<PromotionalProductsComponentModel>
{


	private static final Logger LOG = Logger.getLogger(PromotionalProductsComponentController.class);

	protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE,
			ProductOption.VARIANT_FULL);

	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;

	@Autowired
	private ConfigurationService configurationService;

	@Resource(name = "siteConfigService")
	private SiteConfigService siteConfigService;

	public static final int MAX_PAGE_LIMIT = 100;

	@Resource(name = "defaultMplProductSearchFacade")
	private DefaultMplProductSearchFacade searchFacade;






	/**
	 * @return the searchFacade
	 */
	public DefaultMplProductSearchFacade getSearchFacade()
	{
		return searchFacade;
	}

	/**
	 * @param searchFacade
	 *           the searchFacade to set
	 */
	public void setSearchFacade(final DefaultMplProductSearchFacade searchFacade)
	{
		this.searchFacade = searchFacade;
	}

	/**
	 * @return the siteConfigService
	 */
	public SiteConfigService getSiteConfigService()
	{
		return siteConfigService;
	}

	/**
	 * @param siteConfigService
	 *           the siteConfigService to set
	 */
	public void setSiteConfigService(final SiteConfigService siteConfigService)
	{
		this.siteConfigService = siteConfigService;
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
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	/**
	 * @description It is used for populating Promotional Products in Brand landing page
	 * @param model
	 *
	 *
	 *
	 */

	@SuppressWarnings(
	{ "boxing", "deprecation" })
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final PromotionalProductsComponentModel component)
	{

		final SellerMasterModel seller = component.getSeller();
		final CategoryModel brand = component.getBrand();

		String brandId = null;
		String sellerId = null;
		String offerId = null;
		if (seller != null)
		{
			sellerId = seller.getId();
		}
		if (brand != null)
		{
			brandId = brand.getCode();
		}

		final String identifier = component.getPromotion().getCode();

		try
		{
			offerId = URLEncoder.encode(identifier, "UTF-8");
		}
		catch (final UnsupportedEncodingException e)
		{
			LOG.error("Unable to find the offer ID", e);
		}



		//change
		final int page = 0;
		String categoryID = null;
		if (sellerId != null)
		{
			categoryID = sellerId;
		}
		if (brandId != null)
		{
			categoryID = brandId;
		}

		LOG.debug(
				"**************************" + categoryID + "***********************************************************" + offerId);

		final PageableData pageableData = createPageableData(page, getSearchPageSize(), null, ShowMode.All);
		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchState.setQuery(searchQueryData);
		final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = searchFacade
				.dropDownSearchForOffer(searchState, offerId, pageableData, categoryID, "Web");


		final List<ProductData> promoProductDataList = searchPageData.getResults();


		LOG.debug("productData ::::::::::::::::" + promoProductDataList);
		model.addAttribute("productData", promoProductDataList);
		model.addAttribute("offerId", offerId);
		model.addAttribute("offerLinkId", categoryID);

	}







	protected int getSearchPageSize()
	{
		return getSiteConfigService().getInt("storefront.search.pageSize", 0);
	}

	protected PageableData createPageableData(final int pageNumber, final int pageSize, final String sortCode,
			final ShowMode showMode)
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(pageNumber);
		pageableData.setSort(sortCode);

		if (ShowMode.All == showMode)
		{
			pageableData.setPageSize(MAX_PAGE_LIMIT);
		}
		else
		{
			pageableData.setPageSize(pageSize);
		}
		return pageableData;
	}











}