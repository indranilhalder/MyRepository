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
import java.util.List;

import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
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
	 * @param gps
	 * @param distance
	 * @return Stores 
	 */
	@Override
	public Collection<PointOfServiceModel> getStoresForPincode(GPS gps, double distance)
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
	
	protected double calculateDistance(final GPS referenceGps, final PointOfServiceModel posModel) throws GeoLocatorException,
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
