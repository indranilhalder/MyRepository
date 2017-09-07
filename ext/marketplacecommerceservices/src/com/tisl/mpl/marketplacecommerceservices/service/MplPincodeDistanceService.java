/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;

import java.util.List;


/**
 * @author TCS
 *
 */
public interface MplPincodeDistanceService
{
	public List<PointOfServiceData> pincodeDistance(final List<PointOfServiceData> posData, final Double latitude,
			final Double longitude, final String pincode);
}
