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
package com.tisl.mpl.v2.helper;

import de.hybris.platform.commercefacades.dto.storeats.StoreLocationResponseDataWsDTO;
import de.hybris.platform.commercefacades.storefinder.StoreFinderFacade;
import de.hybris.platform.commercefacades.storelocator.data.ListOfPointOfServiceData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.store.data.GeoPoint;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.commercewebservicescommons.dto.store.ListOfPointOfServiceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.StoreFinderSearchPageWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.location.impl.LocationDTO;
import de.hybris.platform.storelocator.location.impl.LocationDtoWrapper;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.dao.MplSlaveMasterDAO;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.facades.data.StoreLocationResponseData;
import com.tisl.mpl.marketplacecommerceservices.service.PincodeService;
import com.tisl.mpl.pincode.facade.PincodeServiceFacade;
import com.tisl.mpl.util.ExceptionUtil;



@Component
public class StoresHelper extends AbstractHelper
{

	private final static Logger LOG = Logger.getLogger(StoresHelper.class);

	private static final double EARTH_PERIMETER = 40075000.0;
	@Resource(name = "storeFinderFacade")
	private StoreFinderFacade storeFinderFacade;

	@Autowired
	private MplSlaveMasterDAO mplSlaveMasterDao;

	@Resource(name = "pointOfServiceConverter")
	private Converter<PointOfServiceModel, PointOfServiceData> pointOfServiceConverter;

	@Resource(name = "pincodeServiceFacade")
	private PincodeServiceFacade pincodeServiceFacade;

	@Resource(name = "pincodeService")
	private PincodeService pincodeService;

	@Cacheable(value = "storeCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'DTO',#query,#latitude,#longitude,#currentPage,#pageSize,#sort,#radius,#accuracy,#fields)")
	public StoreFinderSearchPageWsDTO locationSearch(final String query, final Double latitude, final Double longitude,
			final int currentPage, final int pageSize, final String sort, final double radius, final double accuracy,
			final String fields)
	{
		final StoreFinderSearchPageData<PointOfServiceData> result = locationSearch(query, latitude, longitude, currentPage,
				pageSize, sort, radius, accuracy);
		return dataMapper.map(result, StoreFinderSearchPageWsDTO.class, fields);
	}

	@Cacheable(value = "storeCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'Data',#query,#latitude,#longitude,#currentPage,#pageSize,#sort,#radius,#accuracy)")
	public StoreFinderSearchPageData<PointOfServiceData> locationSearch(final String query, final Double latitude,
			final Double longitude, final int currentPage, final int pageSize, final String sort, final double radius,
			final double accuracy)
	{
		if (radius > EARTH_PERIMETER)
		{
			throw new RequestParameterException("Radius cannot be greater than Earth's perimeter",
					RequestParameterException.INVALID, "radius");
		}

		final double radiusToSearch = getInKilometres(radius, accuracy);
		final PageableData pageableData = createPageableData(currentPage, pageSize, sort);
		StoreFinderSearchPageData<PointOfServiceData> result = null;
		if (StringUtils.isNotBlank(query))
		{
			result = storeFinderFacade.locationSearch(query, pageableData, radiusToSearch);
		}
		else if (latitude != null && longitude != null)
		{
			final GeoPoint geoPoint = new GeoPoint();
			geoPoint.setLatitude(latitude.doubleValue());
			geoPoint.setLongitude(longitude.doubleValue());
			result = storeFinderFacade.positionSearch(geoPoint, pageableData, radiusToSearch);
		}
		else
		{
			result = storeFinderFacade.getAllPointOfServices(pageableData);
		}
		return result;
	}

	/**
	 * Retrieves store for a given storedId.
	 *
	 * @param storeId
	 * @param fields
	 * @return Binds store in a dto and return it.
	 */
	//@Cacheable(value = "storeCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'storeDetails',#storeId,#fields)")
	public PointOfServiceWsDTO locationDetails(final String storeId, final String fields)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from locationDetails method");
		}
		PointOfServiceData pointOfServiceData = new PointOfServiceData();
		try
		{
			final PointOfServiceModel posModel = mplSlaveMasterDao.checkPOSForSlave(storeId);
			if (null != posModel)
			{
				pointOfServiceData = pointOfServiceConverter.convert(posModel);
				pointOfServiceData.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				LOG.debug(" no store found for storeId" + storeId);
				final EtailBusinessExceptions error = new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9518);
				ExceptionUtil.etailBusinessExceptionHandler(error, null);
				pointOfServiceData.setError(error.getErrorMessage());
				pointOfServiceData.setSlaveId(storeId);
				pointOfServiceData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}
		}
		catch (final Exception e)
		{
			LOG.error("Exception in storeLocationAts call");
			if (null != e.getMessage())
			{
				pointOfServiceData.setError(e.getMessage());
			}
			pointOfServiceData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return dataMapper.map(pointOfServiceData, PointOfServiceWsDTO.class, fields);
	}


	/**
	 * This method retrieves all the Stores for given pincode.
	 *
	 * @param latitude
	 * @param longitude
	 * @param pincode
	 * @param fields
	 * @return binds the response to ws dto and returns it.
	 */
	//@Cacheable(value = "storeCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'storeDetails',#storeId,#fields)")
	public ListOfPointOfServiceWsDTO getAllStoresForPincode(final String latitude, final String longitude, final String pincode,
			final String fields)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from getAllStoresForPincode method");
		}
		List<PointOfServiceData> posData = new ArrayList<PointOfServiceData>();
		final ListOfPointOfServiceData listOfPosData = new ListOfPointOfServiceData();
		final LocationDTO dto = new LocationDTO();
		String radius = Config.getParameter("marketplacestorefront.configure.radius");
		if (null == radius)
		{
			radius = "0";
		}
		Location myLocation = null;
		try
		{
			if (latitude != null && longitude != null)
			{
				dto.setLatitude(latitude);
				dto.setLongitude(longitude);
				myLocation = new LocationDtoWrapper(dto);
				posData = pincodeServiceFacade.getStoresForPincode(myLocation.getGPS(), radius);
			}
			else
			{
				//fetch latitude and longitude for a pincode from comm
				final PincodeModel pincodeModel = pincodeService.getLatAndLongForPincode(pincode);
				if (null != pincodeModel)
				{
					dto.setLatitude(pincodeModel.getLatitude().toString());
					dto.setLongitude(pincodeModel.getLongitude().toString());
					myLocation = new LocationDtoWrapper(dto);
					posData = pincodeServiceFacade.getStoresForPincode(myLocation.getGPS(), radius);
				}
				else
				{
					LOG.error(" pincode model not found for given pincode " + pincode);
					final EtailBusinessExceptions error = new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9516);
					ExceptionUtil.etailBusinessExceptionHandler(error, null);
					listOfPosData.setError(error.getErrorMessage());
					listOfPosData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);

				}
			}
		}
		catch (final Exception e)
		{
			LOG.error("Something went wrong in calling getAllStoresForPincode");

			if (null != e.getMessage())
			{
				listOfPosData.setError(e.getMessage());
			}
			listOfPosData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		listOfPosData.setStores(posData);
		return dataMapper.map(listOfPosData, ListOfPointOfServiceWsDTO.class, fields);
	}

	/**
	 * Gets StoreAts with ussid and Ats for given pincode and ussid.
	 *
	 * @param pincode
	 * @param ussId
	 * @param fields
	 * @return binds respone to ws dto and returns it.
	 */
	//@Cacheable(value = "storeCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'storeDetails',#storeId,#fields)")
	public StoreLocationResponseDataWsDTO storesAtCart(final String pincode, final String ussId, final String fields)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("from storesAtCart method");
		}
		List<StoreLocationResponseData> storesLocationResponse = null;
		StoreLocationResponseData storeLocationResData = new StoreLocationResponseData();
		try
		{
			storesLocationResponse = pincodeServiceFacade.getListofStoreLocationsforPincode(pincode, ussId, null);
			if (null != storesLocationResponse && storesLocationResponse.size() > 0)
			{
				storeLocationResData = storesLocationResponse.get(0);
				storeLocationResData.setStatus(MarketplacecommerceservicesConstants.SUCCESS_FLAG);
			}
			else
			{
				LOG.debug(" no store found for pincode and ussid" + pincode + "  " + ussId);
				final EtailBusinessExceptions error = new EtailBusinessExceptions(MarketplacecommerceservicesConstants.B9517);
				ExceptionUtil.etailBusinessExceptionHandler(error, null);
				storeLocationResData.setError(error.getErrorMessage());
				storeLocationResData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
			}
		}
		catch (final EtailBusinessExceptions e)
		{
			ExceptionUtil.etailBusinessExceptionHandler(e, null);
			if (null != e.getErrorMessage())
			{
				storeLocationResData.setError(e.getErrorMessage());
			}
			if (null != e.getErrorCode())
			{
				storeLocationResData.setErrorCode(e.getErrorCode());
			}
			storeLocationResData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		catch (final Exception e)
		{
			LOG.error("Exception in storeLocationAts call " + e.getMessage());
			if (null != e.getMessage())
			{
				storeLocationResData.setError(e.getMessage());
			}
			storeLocationResData.setStatus(MarketplacecommerceservicesConstants.ERROR_FLAG);
		}
		return dataMapper.map(storeLocationResData, StoreLocationResponseDataWsDTO.class, fields);
	}

	protected double getInKilometres(final double radius, final double accuracy)
	{
		return (radius + accuracy) / 1000.0;
	}
}
