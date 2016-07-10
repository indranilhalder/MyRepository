/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.storefront.controllers.pages;

import de.hybris.platform.acceleratorservices.customer.CustomerLocationService;
import de.hybris.platform.acceleratorservices.store.data.UserLocationData;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.StorefinderBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.StoreFinderForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.StorePositionForm;
import de.hybris.platform.acceleratorstorefrontcommons.util.XSSFilterUtil;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.storefinder.StoreFinderFacade;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.exception.GeoLocatorException;
import de.hybris.platform.storelocator.exception.MapServiceException;
import com.tisl.mpl.storefront.controllers.ControllerConstants;
import de.hybris.platform.acceleratorstorefrontcommons.util.MetaSanitizerUtil;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Controller for store locator search and detail pages.
 */

@Controller
@Scope("tenant")
@RequestMapping(value = "/store-finder")
public class StoreLocatorPageController extends AbstractSearchPageController
{
	protected static final Logger LOG = Logger.getLogger(StoreLocatorPageController.class);

	private static final String STORE_FINDER_CMS_PAGE_LABEL = "storefinder";
	private static final String GOOGLE_API_KEY_ID = "googleApiKey";
	private static final String GOOGLE_API_VERSION = "googleApiVersion";
	/**
	 * Constants.
	 */
	private static final String LONGITUDE = "longitude";
	private static final String LATITUDE = "latitude";

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "storefinderBreadcrumbBuilder")
	private StorefinderBreadcrumbBuilder storefinderBreadcrumbBuilder;

	@Resource(name = "storeFinderFacade")
	private StoreFinderFacade storeFinderFacade;

	@Resource(name = "customerLocationService")
	private CustomerLocationService customerLocationService;
	
	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;


	@ModelAttribute("googleApiVersion")
	public String getGoogleApiVersion()
	{
		return configurationService.getConfiguration().getString(GOOGLE_API_VERSION);
	}

	@ModelAttribute("googleApiKey")
	public String getGoogleApiKey(final HttpServletRequest request)
	{
		final String googleApiKey = getHostConfigService().getProperty(GOOGLE_API_KEY_ID, request.getServerName());
		if (StringUtils.isEmpty(googleApiKey))
		{
			LOG.warn("No Google API key found for server: " + request.getServerName());
		}
		return googleApiKey;
	}

	// Method to get the empty search form
	@RequestMapping(method = RequestMethod.GET)
	public String getStoreFinderPage(final Model model) throws CMSItemNotFoundException
	{
		setUpPageForms(model);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, storefinderBreadcrumbBuilder.getBreadcrumbs());
		storeCmsPageInModel(model, getStoreFinderPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getStoreFinderPage());
		//Set zoom label and default latitude and longitude.
		model.addAttribute(LATITUDE, configurationService.getConfiguration().getString("default.latitude"));
		model.addAttribute(LONGITUDE, configurationService.getConfiguration().getString("default.longtitude"));
		model.addAttribute("initialZoom", configurationService.getConfiguration().getString("default.initialzoom"));
		model.addAttribute("markerZoom", configurationService.getConfiguration().getString("default.markerZoom"));
		 
		return getViewForPage(model);
	}

	@RequestMapping(method = RequestMethod.GET, params = "q")
	public String findStores(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final AbstractSearchPageController.ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode,
			@RequestParam(value = "q") final String locationQuery,
			@RequestParam(value = "latitude", required = false) final Double latitude,
			@RequestParam(value = "longitude", required = false) final Double longitude, final StoreFinderForm storeFinderForm,
			final Model model, final BindingResult bindingResult) throws GeoLocatorException, MapServiceException,
			CMSItemNotFoundException
	{
		final String sanitizedSearchQuery = XSSFilterUtil.filter(locationQuery);

		if (latitude != null && longitude != null)
		{
			final GeoPoint geoPoint = new GeoPoint();
			geoPoint.setLatitude(latitude.doubleValue());
			geoPoint.setLongitude(longitude.doubleValue());

			setUpSearchResultsForPosition(geoPoint, createPageableData(page, getStoreLocatorPageSize(), sortCode, showMode), model);
		}
		else if (StringUtils.isNotBlank(sanitizedSearchQuery))
		{

			setUpSearchResultsForLocationQuery(sanitizedSearchQuery,
					createPageableData(page, getStoreLocatorPageSize(), sortCode, showMode), model);
			setUpMetaData(sanitizedSearchQuery, model);
			setUpPageForms(model);
			setUpPageTitle(sanitizedSearchQuery, model);

		}
		else
		{

			GlobalMessages.addErrorMessage(model, "storelocator.error.no.results.subtitle");
			model.addAttribute(WebConstants.BREADCRUMBS_KEY,
					storefinderBreadcrumbBuilder.getBreadcrumbsForLocationSearch(sanitizedSearchQuery));

		}

	 
		model.addAttribute("NoResultFound",Boolean.TRUE);
		GlobalMessages.addErrorMessage(model, "storelocator.error.no.results.subtitle"); 
		storeCmsPageInModel(model, getStoreFinderPage());

		return ControllerConstants.Views.Pages.StoreFinder.StoreFinderSearchPage;
	}

	@RequestMapping(value = "/position", method =
	{ RequestMethod.GET, RequestMethod.POST })
	public String searchByCurrentPosition(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final AbstractSearchPageController.ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode, final StorePositionForm storePositionForm,
			final Model model) throws GeoLocatorException, MapServiceException, CMSItemNotFoundException
	{
		final GeoPoint geoPoint = new GeoPoint();
		geoPoint.setLatitude(storePositionForm.getLatitude());
		geoPoint.setLongitude(storePositionForm.getLongitude());

		setUpSearchResultsForPosition(geoPoint, createPageableData(page, getStoreLocatorPageSize(), sortCode, showMode), model);
		setUpPageForms(model);
		storeCmsPageInModel(model, getStoreFinderPage());

		return ControllerConstants.Views.Pages.StoreFinder.StoreFinderSearchPage;
	}

	// setup methods to populate the model
	protected void setUpMetaData(final String locationQuery, final Model model)
	{
		model.addAttribute("metaRobots", "noindex,follow");
		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(locationQuery);
		final String metaDescription = MetaSanitizerUtil.sanitizeDescription(getSiteName()
				+ " "
				+ getMessageSource().getMessage("storeFinder.meta.description.results", null, "storeFinder.meta.description.results",
						getI18nService().getCurrentLocale()) + " " + locationQuery);
		super.setUpMetaData(model, metaKeywords, metaDescription);
	}

	protected void setUpNoResultsErrorMessage(final Model model, final StoreFinderSearchPageData<PointOfServiceData> searchResult)
	{
		if (searchResult.getResults().isEmpty())
		{
			GlobalMessages.addErrorMessage(model, "storelocator.error.no.results.subtitle");
		}
	}

	protected void setUpPageData(final Model model, final StoreFinderSearchPageData<PointOfServiceData> searchResult,
			final List<Breadcrumb> breadCrumbsList)
	{
		populateModel(model, searchResult, ShowMode.Page);
		model.addAttribute("locationQuery", StringEscapeUtils.escapeHtml(searchResult.getLocationText()));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, breadCrumbsList);
	}

	protected void setUpSearchResultsForPosition(final GeoPoint geoPoint, final PageableData pageableData, final Model model)
	{  
		double maxRadiusForPosSearch=getDefaultMaxRadius();
		// Run the location search & populate the model
		final StoreFinderSearchPageData<PointOfServiceData> searchResult = storeFinderFacade.positionSearch(geoPoint, pageableData,maxRadiusForPosSearch);

		final GeoPoint newGeoPoint = new GeoPoint();
		newGeoPoint.setLatitude(searchResult.getSourceLatitude());
		newGeoPoint.setLongitude(searchResult.getSourceLongitude());

		updateLocalUserPreferences(newGeoPoint, searchResult.getLocationText());
		setUpPageData(model, searchResult, storefinderBreadcrumbBuilder.getBreadcrumbsForCurrentPositionSearch());
		setUpPosition(model, newGeoPoint);
		setUpNoResultsErrorMessage(model, searchResult);
	}

	protected void setUpPosition(final Model model, final GeoPoint geoPoint)
	{
		model.addAttribute("geoPoint", geoPoint);
	}

	protected void setUpSearchResultsForLocationQuery(final String locationQuery, final PageableData pageableData,
			final Model model)
	{
		
		double maxRadiusForPosSearch=getDefaultMaxRadius();
		// Run the location search & populate the model
		final StoreFinderSearchPageData<PointOfServiceData> searchResult = storeFinderFacade.locationSearch(locationQuery,
				pageableData,maxRadiusForPosSearch);
		final GeoPoint geoPoint = new GeoPoint();
		geoPoint.setLatitude(searchResult.getSourceLatitude());
		geoPoint.setLongitude(searchResult.getSourceLongitude());

		updateLocalUserPreferences(geoPoint, searchResult.getLocationText());
		setUpPageData(model, searchResult, storefinderBreadcrumbBuilder.getBreadcrumbsForLocationSearch(locationQuery));
		setUpNoResultsErrorMessage(model, searchResult);
	}

	/**
	 * @return returns default max radius.
	 */
	private double getDefaultMaxRadius()
	{
		double maxRadiusForPosSearch;
		BaseStoreModel baseStore = baseStoreService.getCurrentBaseStore();
		if (null != baseStore & null != baseStore.getMaxRadiusForPoSSearch()
				&& baseStore.getMaxRadiusForPoSSearch().doubleValue() > 0)
		{
			maxRadiusForPosSearch = baseStore.getMaxRadiusForPoSSearch().doubleValue();
		}
		else
		{
			maxRadiusForPosSearch = configurationService.getConfiguration().getDouble("default.maxRadiusForPosSearch");
		}
		return maxRadiusForPosSearch;
	}


	protected void updateLocalUserPreferences(final GeoPoint geoPoint, final String location)
	{
		final UserLocationData userLocationData = new UserLocationData();
		userLocationData.setSearchTerm(location);
		userLocationData.setPoint(geoPoint);
		customerLocationService.setUserLocation(userLocationData);
	}


	protected void setUpPageForms(final Model model)
	{
		final StoreFinderForm storeFinderForm = new StoreFinderForm();
		final StorePositionForm storePositionForm = new StorePositionForm();
		model.addAttribute("storeFinderForm", storeFinderForm);
		model.addAttribute("storePositionForm", storePositionForm);
	}

	protected void setUpPageTitle(final String searchText, final Model model)
	{
		storeContentPageTitleInModel(
				model,
				getPageTitleResolver().resolveContentPageTitle(
						getMessageSource().getMessage("storeFinder.meta.title", null, "storeFinder.meta.title",
								getI18nService().getCurrentLocale())
								+ " " + searchText));
	}

	protected AbstractPageModel getStoreFinderPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId(STORE_FINDER_CMS_PAGE_LABEL);
	}

	/**
	 * Get the default search page size.
	 * 
	 * @return the number of results per page, <tt>0</tt> (zero) indicated 'default' size should be used
	 */
	protected int getStoreLocatorPageSize()
	{
		return getSiteConfigService().getInt("storefront.storelocator.pageSize", 0);
	}
}
