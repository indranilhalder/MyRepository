/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.Collection;
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

	/**
	 * 
	 * @param gps
	 * @param distance
	 * @return All the Stores for given gps and distance.
	 */
	public Collection<PointOfServiceModel> getStoresForPincode(final GPS gps, final double distance);

}
