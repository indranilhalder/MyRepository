/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tisl.mpl.marketplacecommerceservices.service.MplPincodeDistanceService;
import com.tisl.mpl.util.DistanceMatrixUtility;


/**
 * @author TCS
 *
 */
public class MplPincodeDistanceServiceImpl implements MplPincodeDistanceService
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(MplPincodeDistanceServiceImpl.class);
	private static final String API_KEY = "YOUR_API_KEY";


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.tisl.mpl.marketplacecommerceservices.service.impl.MplPincodeDistanceService#pincodeDistance(java.util.List)
	 */
	@Override
	public List<PointOfServiceData> pincodeDistance(final List<PointOfServiceData> posData, final Double latitude,
			final Double longitude, final String pincode)
	{
		final StringBuffer origins = new StringBuffer();
		final StringBuffer destinations = new StringBuffer();
		List<String> distanceList = new ArrayList<>();
		try
		{
			final DistanceMatrixUtility distance = new DistanceMatrixUtility();
			origins.append(latitude);
			origins.append(",");
			origins.append(longitude);

			for (final PointOfServiceData pointOfServiceData : posData)
			{
				destinations.append(pointOfServiceData.getGeoPoint().getLatitude());
				destinations.append(",");
				destinations.append(pointOfServiceData.getGeoPoint().getLongitude());
				destinations.append("|");
			}

			distanceList = distance.calcDistance(origins, destinations);
			for (final PointOfServiceData pointOfServiceData : posData)
			{
				for (final String distanceKM : distanceList)
				{
					pointOfServiceData.setDistanceKm(Double.valueOf(distanceKM));
				}
			}

		}
		catch (final Exception e)
		{
			LOG.debug(e);
		}
		return posData;
	}
}
