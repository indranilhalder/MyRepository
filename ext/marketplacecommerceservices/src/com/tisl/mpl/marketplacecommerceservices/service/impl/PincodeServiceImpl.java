/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.exception.GeoLocatorException;
import de.hybris.platform.storelocator.exception.LocationInstantiationException;
import de.hybris.platform.storelocator.exception.LocationServiceException;
import de.hybris.platform.storelocator.exception.PointOfServiceDaoException;
import de.hybris.platform.storelocator.impl.DefaultGPS;
import de.hybris.platform.storelocator.impl.GeometryUtils;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.marketplacecommerceservices.daos.PincodeDao;
import com.tisl.mpl.marketplacecommerceservices.service.PincodeService;


/**
 * @author Techouts
 *
 */
public class PincodeServiceImpl implements PincodeService
{
	private static final Logger LOG = Logger.getLogger(PincodeServiceImpl.class);


	private PincodeDao pincodeDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tisl.mpl.marketplacecommerceservices.service.PincodeService#getSortedLocationsNearby(de.hybris.platform.
	 * storelocator.GPS, double)
	 */
	@Override
	public List<Location> getSortedLocationsNearby(final GPS gps, final double distance, final String sellerId)
	{
		try
		{
			final List result = new ArrayList();
			for (final PointOfServiceModel posModel : this.pincodeDao.getAllGeocodedPOS(gps, distance, sellerId))
			{
				final double dist = calculateDistance(gps, posModel);
				//result.add(new DefaultLocation(posModel, Double.valueOf(dist)));
				result.add(new CustomLocation(posModel, Double.valueOf(dist)));
			}
			Collections.sort(result);
			return result;
		}
		catch (final PointOfServiceDaoException e)
		{
			throw new LocationServiceException(e.getMessage(), e);
		}
		catch (final LocationInstantiationException e)
		{
			throw new LocationServiceException(e.getMessage(), e);
		}
		catch (final GeoLocatorException e)
		{
			throw new LocationServiceException(e.getMessage(), e);
		}
	}

	@Override
	public PincodeModel getLatAndLongForPincode(final String pincode)
	{
		PincodeModel pincodeModel = null;
		try
		{
			pincodeModel = this.pincodeDao.getLatAndLongForPincode(pincode);
			if (null != pincodeModel)
			{
				return pincodeModel;
			}
		}
		catch (final NullPointerException ex)
		{
			LOG.error(MarketplacecommerceservicesConstants.EXCEPTION_IS, ex);
			throw new EtailNonBusinessExceptions(ex);
		}
		return pincodeModel;
	}

	/**
	 * Fetch all the Stores for a Pincode and radius.
	 *
	 * @param gps
	 * @param distance
	 * @return Stores
	 */
	@Override
	public Collection<PointOfServiceModel> getStoresForPincode(final GPS gps, final double distance)
	{
		try
		{
			Collection<PointOfServiceModel> result = new ArrayList<PointOfServiceModel>();
			result = getPincodeDao().getAllGeocodedPOS(gps, distance);
			return result;
		}
		catch (final PointOfServiceDaoException e)
		{
			throw new LocationServiceException(e.getMessage(), e);
		}
		catch (final LocationInstantiationException e)
		{
			throw new LocationServiceException(e.getMessage(), e);
		}
		catch (final GeoLocatorException e)
		{
			throw new LocationServiceException(e.getMessage(), e);
		}
	}

	/**
	 * Fetch all the Stores for a Pincode and radius.
	 *
	 * @param gps
	 * @param distance
	 * @return Stores
	 */
	@Override
	public Collection<PointOfServiceModel> getAllReturnableStores(final GPS gps, final double distance, final String sellerId)
			throws LocationServiceException
	{
		try
		{
			Collection<PointOfServiceModel> result = new ArrayList<PointOfServiceModel>();
			result = getPincodeDao().getAllReturnablePOSforSeller(gps, distance, sellerId);
			return result;
		}
		catch (final PointOfServiceDaoException e)
		{
			throw new LocationServiceException(e.getMessage(), e);
		}
		catch (final LocationInstantiationException e)
		{
			throw new LocationServiceException(e.getMessage(), e);
		}
		catch (final GeoLocatorException e)
		{
			throw new LocationServiceException(e.getMessage(), e);
		}
	}

	public double calculateDistance(final GPS referenceGps, final PointOfServiceModel posModel) throws GeoLocatorException,
			LocationServiceException
	{
		if ((posModel.getLatitude() != null) && (posModel.getLongitude() != null))
		{
			final GPS positionGPS = new DefaultGPS().create(posModel.getLatitude().doubleValue(), posModel.getLongitude()
					.doubleValue());
			return GeometryUtils.getElipticalDistanceKM(referenceGps, positionGPS);
		}
		throw new LocationServiceException("Unable to calculate a distance for PointOfService(" + posModel
				+ ") due to missing  latitude, longitude value");
	}

	/**
	 * fetching all details about the given Pincode
	 *
	 * @param pincode
	 * @return PincodeModel
	 */
	@Override
	public PincodeModel getDetailsOfPincode(final String pincode)
	{
		final PincodeModel pincodeModel = null;
		try
		{
			final List<PincodeModel> pincodeModelList = pincodeDao.getAllDetailsOfPincode(pincode);
			if (!pincodeModelList.isEmpty())
			{
				return pincodeModelList.get(0);
			}
			else if (pincodeModelList.size() > 1)
			{
				LOG.error("More than one pincode model found for :" + pincode);
			}
			else
			{
				LOG.info("No pincode data found for :" + pincode);
			}
		}
		catch (final EtailBusinessExceptions businessException)
		{
			LOG.error("Exception while retrieving pincode Deatils");
			throw businessException;
		}
		catch (final EtailNonBusinessExceptions nonBusiness)
		{
			LOG.error(nonBusiness.getMessage());
			throw nonBusiness;
		}
		catch (final Exception exception)
		{
			LOG.error(exception.getCause().getMessage());
			throw exception;
		}
		return pincodeModel;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.tisl.mpl.marketplacecommerceservices.service.PincodeService#getSortedLocationsNearby(de.hybris.platform.
	 * storelocator.GPS, double)
	 */
	@Override
	public Map<PointOfServiceModel, Double> getSortedStoresNearby(final GPS gps, final double distance, final String sellerId)
	{
		final Map<PointOfServiceModel, Double> result = new HashMap<PointOfServiceModel, Double>();
		try
		{
			final Collection<PointOfServiceModel> posModels = pincodeDao.getAllGeocodedPOS(gps, distance, sellerId);
			if (CollectionUtils.isNotEmpty(posModels))
			{
				for (final PointOfServiceModel pos : posModels)
				{
					final double dist = calculateDistance(gps, pos);
					//result.add(new DefaultLocation(posModel, Double.valueOf(dist)));
					result.put(pos, Double.valueOf(dist));
				}
			}
			return result;
		}
		catch (final PointOfServiceDaoException e)
		{
			throw new LocationServiceException(e.getMessage(), e);
		}
		catch (final LocationInstantiationException e)
		{
			throw new LocationServiceException(e.getMessage(), e);
		}
		catch (final GeoLocatorException e)
		{
			throw new LocationServiceException(e.getMessage(), e);
		}
	}

	/**
	 * @return the pincodeDao
	 */
	public PincodeDao getPincodeDao()
	{
		return pincodeDao;
	}

	/**
	 * @param pincodeDao
	 *           the pincodeDao to set
	 */
	public void setPincodeDao(final PincodeDao pincodeDao)
	{
		this.pincodeDao = pincodeDao;
	}


}
