package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.core.model.MplShopByLookModel;
import com.tisl.mpl.facade.shopbylook.ShopByLookFacade;
import com.tisl.mpl.solrfacet.search.impl.DefaultMplProductSearchFacade;
import com.tisl.mpl.storefront.constants.ModelAttributetConstants;
import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;
import com.tisl.mpl.storefront.controllers.pages.SearchPageController.UserPreferencesData;


/**
 * @author TCS
 *
 */
@Controller
@Scope(RequestMappingUrlConstants.TENANT)
@RequestMapping(value = "/collection")
public class ShopByLookController extends AbstractSearchPageController
{
	private static final Logger LOG = Logger.getLogger(ShopByLookController.class);

	@Resource(name = "shopByLookFacade")
	private ShopByLookFacade shopbyLookFacade;
	@Resource(name = "defaultMplProductSearchFacade")
	private DefaultMplProductSearchFacade searchFacade;
	@Resource(name = "sessionService")
	private SessionService sessionService;

	/**
	 * @desc this method fetches the shop by collection based on look-id provided.
	 * @param lookId
	 * @param model
	 * @return String
	 * @throws CMSItemNotFoundException
	 */
	@SuppressWarnings("boxing")
	@RequestMapping(method = RequestMethod.GET, value = "{look-id}")
	public String loadShopTheLooks(@PathVariable("look-id") final String lookId,
			@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = "sort", required = false) final String sortCode,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "page", required = false, defaultValue = "0") final Integer page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "24") final Integer pageSize, final Model model)
			throws CMSItemNotFoundException
	{
		String formedPaginationUrl = null;

		if (null != searchQuery || null != sortCode)
		{
			final UserPreferencesData preferencesData = updateUserPreferences(pageSize);
			int count = getSearchPageSize();
			if (preferencesData != null && preferencesData.getPageSize() != null)
			{
				count = preferencesData.getPageSize().intValue();
			}
			final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = (ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData>) performSearch(
					lookId, searchQuery, page, showMode, sortCode, count);
			final String url = searchPageData.getCurrentQuery().getUrl();
			formedPaginationUrl = url.replace("/search", "");
			searchPageData.getCurrentQuery().setUrl(formedPaginationUrl);
			populateModel(model, searchPageData, showMode);
		}
		else
		{
			ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = null;
			final SearchStateData searchState = new SearchStateData();
			final SearchQueryData searchQueryData = new SearchQueryData();
			final PageableData pageableData = createPageableData(page, pageSize, null, ShowMode.Page);
			searchState.setQuery(searchQueryData);
			searchPageData = searchFacade.collectionSearch(lookId, searchState, pageableData);
			final String url = searchPageData.getCurrentQuery().getUrl();
			formedPaginationUrl = url.replace("/search", "");
			searchPageData.getCurrentQuery().setUrl(formedPaginationUrl);
			populateModel(model, searchPageData, ShowMode.Page);
		}

		if (null != lookId)
		{
			final String LAST_LINK_CLASS = "active";
			final Date date = new Date();
			Date dateStart = null;
			Date endDate = null;

			final List<MplShopByLookModel> shopByLookProducts = shopbyLookFacade.getAllShopByLookProducts(lookId);
			/*
			 * check for validity of the shop the look page
			 */

			for (final MplShopByLookModel shopByLook : shopByLookProducts)
			{
				dateStart = shopByLook.getStartDate();
				endDate = shopByLook.getEndDate();
				final List<Breadcrumb> shopTheLookBreadCrub = new ArrayList<Breadcrumb>();
				final Breadcrumb crub = new Breadcrumb("#", shopByLook.getCollectionName(), LAST_LINK_CLASS);
				shopTheLookBreadCrub.add(crub);
				model.addAttribute(WebConstants.BREADCRUMBS_KEY, shopTheLookBreadCrub);

				if (null != dateStart && null != endDate && date.after(dateStart) && date.before(endDate))
				{
					//SONAR Fix
					LOG.debug("Date falls between dateStart and endDate");
				}
				else
				{
					model.addAttribute(ModelAttributetConstants.PRODUCT, null);
					model.addAttribute(ModelAttributetConstants.SHOP_THE_LOOK_PAGE_EXPIRED, "yes");
				}
			}
		}
		/*
		 * common page model loading
		 */
		ContentPageModel shopByLookPage = null;
		try
		{
			shopByLookPage = getContentPageForLabelOrId(lookId);
		}
		catch (final CMSItemNotFoundException e)
		{
			return FORWARD_PREFIX + "/404";
			//storeCmsPageInModel(model, getCmsPageService().getDefaultCategoryPage());
		}
		if (null != shopByLookPage)
		{
			storeCmsPageInModel(model, shopByLookPage);
			setUpMetaDataForContentPage(model, shopByLookPage);
		}
		return getViewForPage(model);

	}

	/**
	 * @description method is called to create PageableData
	 * @param pageNumber
	 * @param pageSize
	 * @param sortCode
	 * @param showMode
	 * @return PageableData
	 */
	@Override
	protected PageableData createPageableData(final int pageNumber, final int pageSize, final String sortCode,
			final ShowMode showMode)
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(pageNumber);
		pageableData.setSort(sortCode);

		if (ShowMode.All == showMode)
		{
			pageableData.setPageSize(MarketplacecommerceservicesConstants.MAX_PAGE_LIMIT);
		}
		else
		{
			pageableData.setPageSize(pageSize);
		}
		return pageableData;
	}

	protected UserPreferencesData updateUserPreferences(final Integer pageSize)
	{
		final UserPreferencesData preferencesData = getUserPreferences();

		if (pageSize != null)
		{
			preferencesData.setPageSize(pageSize);
		}

		return preferencesData;
	}

	protected void setUserPreferences(final UserPreferencesData userPreferencesData)
	{
		final Session session = sessionService.getCurrentSession();
		session.setAttribute("preferredSearchPreferences", userPreferencesData);
	}

	protected UserPreferencesData getUserPreferences()
	{
		final Session session = sessionService.getCurrentSession();
		UserPreferencesData userPreferencesData = session.getAttribute("preferredSearchPreferences");
		if (userPreferencesData == null)
		{
			userPreferencesData = new UserPreferencesData();
			setUserPreferences(userPreferencesData);
		}
		return userPreferencesData;
	}

	/**
	 *
	 * @param searchText
	 * @param model
	 */
	protected void updatePageTitle(final String searchText, final Model model)
	{
		storeContentPageTitleInModel(
				model,
				getPageTitleResolver().resolveContentPageTitle(
						getMessageSource().getMessage("search.meta.title", null, "search.meta.title",
								getI18nService().getCurrentLocale())
								+ " " + searchText));
	}

	/**
	 *
	 * @param searchQuery
	 * @param page
	 * @param showMode
	 * @param sortCode
	 * @param pageSize
	 * @return ProductSearchPageData
	 */
	protected ProductSearchPageData<SearchStateData, ProductData> performSearch(final String lookId, final String searchQuery,
			final int page, final ShowMode showMode, final String sortCode, final int pageSize)
	{
		final PageableData pageableData = createPageableData(page, pageSize, sortCode, showMode);

		final SearchStateData searchState = new SearchStateData();
		final SearchQueryData searchQueryData = new SearchQueryData();
		searchQueryData.setValue(searchQuery);
		searchState.setQuery(searchQueryData);
		return searchFacade.collectionSearch(lookId, searchState, pageableData);
	}

	/**
	 * @return the shopbyLookFacade
	 */
	public ShopByLookFacade getShopbyLookFacade()
	{
		return shopbyLookFacade;
	}

	/**
	 * @param shopbyLookFacade
	 *           the shopbyLookFacade to set
	 */
	public void setShopbyLookFacade(final ShopByLookFacade shopbyLookFacade)
	{
		this.shopbyLookFacade = shopbyLookFacade;
	}

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
	 * @return the sessionService
	 */
	@Override
	public SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}
