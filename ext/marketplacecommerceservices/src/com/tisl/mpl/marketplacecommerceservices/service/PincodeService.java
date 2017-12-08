/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.exception.GeoLocatorException;
import de.hybris.platform.storelocator.exception.LocationServiceException;
import de.hybris.platform.storelocator.location.Location;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.Collection;
import java.util.List;
import java.util.Map;


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
	 * @param pincode
	 * @return PincodeModel
	 */
	PincodeModel getLatAndLongForPincode(final String pincode);

	/**
	 *
	 * @param gps
	 * @param distance
	 * @return All the Stores for given gps and distance.
	 */
	public Collection<PointOfServiceModel> getStoresForPincode(final GPS gps, final double distance);

	/**
	 * Gets the Details of the pincode
	 *
	 * @param pincode
	 * @return PincodeModel
	 */
	public PincodeModel getDetailsOfPincode(final String pincode);

	/**
	 * @param gps
	 * @param distance
	 * @param sellerId
	 * @return Collection<PointOfServiceModel>
	 */
	public Collection<PointOfServiceModel> getAllReturnableStores(GPS gps, double distance, String sellerId);

	/**
	 * @param gps
	 * @param distance
	 * @param sellerId
	 * @return SortedMap<PointOfServiceModel, Double>
	 * */
	public Map<PointOfServiceModel, Double> getSortedStoresNearby(final GPS gps, final double distance, final String sellerId);

	public double calculateDistance(final GPS referenceGps, final PointOfServiceModel posModel) throws GeoLocatorException,
			LocationServiceException;

	public List<PointOfServiceModel> findPOSBySellerAndSlave(final List<String> slaveIds);
}
