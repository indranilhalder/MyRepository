/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
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
	@Resource
	private ConfigurationService configurationService;



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
		List<Double> distanceList = new ArrayList<Double>();
		List<PointOfServiceData> sortPOS = new ArrayList<PointOfServiceData>();
		try
		{
			final boolean distanceFlag = configurationService.getConfiguration().getBoolean("google.distance.enable");
			if (distanceFlag)
			{
				final DistanceMatrixUtility distance = new DistanceMatrixUtility();
				origins.append(latitude);
				origins.append(MarketplacecommerceservicesConstants.COMMA);
				origins.append(longitude);

				for (final PointOfServiceData pointOfServiceData : posData)
				{
					destinations.append(pointOfServiceData.getGeoPoint().getLatitude());
					destinations.append(MarketplacecommerceservicesConstants.COMMA);
					destinations.append(pointOfServiceData.getGeoPoint().getLongitude());
					destinations.append(MarketplacecommerceservicesConstants.CONCTASTRING);
				}

				distanceList = distance.calcDistance(origins, destinations);
				int count = 0;
				for (final PointOfServiceData pointOfServiceData : posData)
				{
					pointOfServiceData.setDistanceKm(distanceList.get(count));
					count++;
					//}
				}
			}
			else
			{
				for (final PointOfServiceData pointOfServiceData : posData)
				{
					pointOfServiceData.setDistanceKm(distFrom(latitude.doubleValue(), longitude.doubleValue(), pointOfServiceData
							.getGeoPoint().getLatitude(), pointOfServiceData.getGeoPoint().getLongitude()));
				}

			}
			sortPOS = getSortedSetData(posData);
		}
		catch (final Exception e)
		{
			LOG.debug(e);
		}
		return sortPOS;
	}

	public static Double distFrom(final double lat1, final double lng1, final double lat2, final double lng2)
	{
		double dist = 0;
		try
		{
			final double earthRadius = 6371000; //meters
			final double dLat = Math.toRadians(lat2 - lat1);
			final double dLng = Math.toRadians(lng2 - lng1);
			final double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
					* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
			final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			dist = ((earthRadius * c) / 1000);
			dist = (double) Math.round(dist * 100) / 100;
		}
		catch (final Exception e)
		{
			LOG.debug(e);
		}

		return Double.valueOf(dist);
	}

	private static List<PointOfServiceData> getSortedSetData(final List<PointOfServiceData> pointOfServiceData)
	{
		final List<PointOfServiceData> sortedList = new ArrayList<PointOfServiceData>();
		List<PointOfServiceData> listData = null;
		if (CollectionUtils.isNotEmpty(pointOfServiceData))
		{
			listData = new ArrayList<PointOfServiceData>(pointOfServiceData);
			Collections.sort(listData, new Comparator<PointOfServiceData>()
			{
				@Override
				public int compare(final PointOfServiceData pos1, final PointOfServiceData pos2)
				{
					return pos1.getDistanceKm().compareTo(pos2.getDistanceKm());
				}
			});
		}

		if (CollectionUtils.isNotEmpty(listData))
		{
			sortedList.addAll(listData);
		}
		return sortedList;
	}
}
