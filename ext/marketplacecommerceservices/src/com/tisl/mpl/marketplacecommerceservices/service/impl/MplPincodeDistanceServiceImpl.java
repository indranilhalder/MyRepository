/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service.impl;

import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
		Double dist = null;
		double distanceCal = 0;
		double distRound = 0;
		try
		{
			final boolean distanceFlag = configurationService.getConfiguration().getBoolean("google.distance.enable");
			final boolean distanceCalculationFlag = configurationService.getConfiguration().getBoolean("show.distance.calculation");
			if (distanceFlag)
			{
				if (distanceCalculationFlag)
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
						if (distanceList != null && distanceList.get(count).doubleValue() > 1000)
						{
							distanceCal = distanceList.get(count).doubleValue() / 1000;
							distRound = new BigDecimal(distanceCal).setScale(2, RoundingMode.HALF_UP).doubleValue();
							pointOfServiceData.setDistanceKm(new Double(distRound));
							pointOfServiceData.setStatus("Km");
							count++;
						}
						else
						{
							if (distanceList != null)
							{
								distanceCal = distanceList.get(count).doubleValue();
								distRound = new BigDecimal(distanceCal).setScale(2, RoundingMode.HALF_UP).doubleValue();
								pointOfServiceData.setDistanceKm(new Double(distRound));
								pointOfServiceData.setStatus("m");
								count++;
							}

						}
					}
				}
			}
			else
			{
				if (latitude != null && longitude != null && distanceCalculationFlag)
				{
					for (final PointOfServiceData pointOfServiceData : posData)
					{
						dist = distFrom(latitude.doubleValue(), longitude.doubleValue(),
								pointOfServiceData.getGeoPoint().getLatitude(), pointOfServiceData.getGeoPoint().getLongitude());
						if (dist != null && dist.doubleValue() > 1000)
						{
							dist = Double.valueOf(dist.doubleValue() / 1000);
							dist = Double.valueOf(new BigDecimal(dist.doubleValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());
							pointOfServiceData.setDistanceKm(dist);
							pointOfServiceData.setStatus("Kms");
						}
						else
						{
							dist = Double.valueOf(new BigDecimal(dist.doubleValue()).setScale(2, RoundingMode.HALF_UP).doubleValue());
							pointOfServiceData.setDistanceKm(dist);
							pointOfServiceData.setStatus("m");
						}
					}
				}
			}
			if (distanceCalculationFlag)
			{
				sortPOS = getSortedSetData(posData);
			}
			else
			{
				sortPOS = posData;
			}
		}
		catch (final Exception e)
		{
			LOG.debug(e);
		}
		return sortPOS;
	}

	public static Double distFrom(final double lat1, final double long1, final double lat2, final double long2)
	{
		double dist = 0;
		final double el1 = 0, el2 = 0;
		try
		{
			final int R = 6371; // Radius of the earth

			final double latDistance = Math.toRadians(lat2 - lat1);
			final double lonDistance = Math.toRadians(long2 - long1);
			final double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
					* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
			final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			double distance = R * c * 1000; // convert to meters

			final double height = el1 - el2;

			distance = Math.pow(distance, 2) + Math.pow(height, 2);

			dist = Math.sqrt(distance);
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
