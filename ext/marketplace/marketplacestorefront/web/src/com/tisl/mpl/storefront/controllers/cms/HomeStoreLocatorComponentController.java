/**
 *
 */
package com.tisl.mpl.storefront.controllers.cms;

import de.hybris.platform.acceleratorcms.model.components.HomeStoreLocatorComponentModel;
import de.hybris.platform.commercefacades.storefinder.StoreFinderFacade;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tisl.mpl.storefront.constants.RequestMappingUrlConstants;


/**
 * This HomeStoreLocatorComponentController gets all POS for current store and sets in model attributes.
 */
@Controller("HomeStoreLocatorComponentController")
@Scope(RequestMappingUrlConstants.TENANT)
@RequestMapping(value = "/view/HomeStoreLocatorComponentController")
public class HomeStoreLocatorComponentController extends AbstractCMSComponentController<HomeStoreLocatorComponentModel>
{

	/**
	 * Constants.
	 */
	private static final String LONGITUDE = "longitude";
	private static final String LATITUDE = "latitude";

	@Resource(name = "storeFinderFacade")
	private StoreFinderFacade storeFinderFacade;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.storefront.controllers.cms.AbstractCMSComponentController#fillModel(javax.servlet.http.
	 * HttpServletRequest, org.springframework.ui.Model,
	 * de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel)
	 */
	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final HomeStoreLocatorComponentModel component)
	{
		//Set default latitude.
		if (null != component.getLatitude())
		{
			model.addAttribute(LATITUDE, component.getLatitude());
		}
		else
		{
			model.addAttribute(LATITUDE, configurationService.getConfiguration().getString("default.latitude"));

		}
		//Set Longitude.
		if (null != component.getLongitude())
		{
			model.addAttribute(LONGITUDE, component.getLongitude());
		}
		else
		{
			model.addAttribute(LONGITUDE, configurationService.getConfiguration().getString("default.longtitude"));
		}
		if (null != component.getInitialZoom())
		{
			model.addAttribute("initialZoom", component.getInitialZoom());
		}
		else
		{
			model.addAttribute("initialZoom", configurationService.getConfiguration().getString("default.initialzoom"));
		}

		if (null != component.getMarkerZoom())
		{
			model.addAttribute("markerZoom", component.getMarkerZoom());
		}
		else
		{
			model.addAttribute("markerZoom", configurationService.getConfiguration().getString("default.markerZoom"));
		}

	}


	@RequestMapping(value = "/{latitude}/{longitude}", method = RequestMethod.GET)
	@ResponseBody
	public String getStoreFinderPage(@PathVariable(value = "latitude") final Double latitude,
			@PathVariable(value = "longitude") final Double longitude)
	{
		final GeoPoint geoPoint = new GeoPoint();
		geoPoint.setLatitude(latitude.doubleValue());
		geoPoint.setLongitude(longitude.doubleValue());

		double maxRadiusForPosSearch;
		final BaseStoreModel baseStore = baseStoreService.getCurrentBaseStore();
		if (null != baseStore & null != baseStore.getMaxRadiusForPoSSearch()
				&& baseStore.getMaxRadiusForPoSSearch().doubleValue() > 0)
		{
			maxRadiusForPosSearch = baseStore.getMaxRadiusForPoSSearch().doubleValue();
		}
		else
		{
			maxRadiusForPosSearch = configurationService.getConfiguration().getDouble("default.maxRadiusForPosSearch");
		}

		final StoreFinderSearchPageData<PointOfServiceData> searchResult = storeFinderFacade.positionSearch(geoPoint,
				createPageableData(0, 100, null), maxRadiusForPosSearch);

		final ObjectMapper objectMapper = new ObjectMapper();
		String jsonResponse = "";
		try
		{ //transform into json string.
			jsonResponse = objectMapper.writeValueAsString(searchResult);
		}
		catch (final JsonGenerationException e)
		{
			LOG.error("Error while generating JSON ", e);
		}
		catch (final JsonMappingException e)
		{
			LOG.error("Error while generating JSON ", e);
		}
		catch (final IOException e)
		{
			LOG.error("Error while generating JSON ", e);
		}


		return jsonResponse;
	}


	protected PageableData createPageableData(final int pageNumber, final int pageSize, final String sortCode)
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(pageNumber);
		pageableData.setSort(sortCode);
		pageableData.setPageSize(pageSize);
		return pageableData;
	}

}
