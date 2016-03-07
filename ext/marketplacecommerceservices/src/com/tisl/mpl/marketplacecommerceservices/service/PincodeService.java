/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.location.Location;

import java.util.List;


/**
 * @author Techouts
 *
 */
public interface PincodeService
{
	/**
	 * @return List
	 */
	List<Location> getSortedLocationsNearby(final GPS gps, final double distance, String sellerId);

	/**
	 * @param model
	 * @return
	 */
	PincodeModel getLatAndLongForPincode(final String pincode);


}
