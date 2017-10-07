/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.Collection;
import java.util.List;


/**
 * @author Techouts
 *
 */
public interface PincodeDao
{

	/**
	 * @param gps
	 * @param distance
	 * @param sellerId
	 * @return Collection<PointOfServiceModel>
	 */
	Collection<PointOfServiceModel> getAllGeocodedPOS(GPS gps, double distance, String sellerId);

	/**
	 * @param gps
	 * @param distance
	 * @return List of PointOfService
	 */
	Collection<PointOfServiceModel> getAllGeocodedPOS(GPS gps, double distance);

	/**
	 * @param pincode
	 * @return PincodeModel
	 */
	PincodeModel getLatAndLongForPincode(final String pincode);

	/**
	 * Get the Available Details of the give pincode
	 *
	 * @param pincode
	 * @return PincodeModel
	 */
	public List<PincodeModel> getAllDetailsOfPincode(final String pincode);

	/**
	 *
	 * @param center
	 * @param radius
	 * @param sellerId
	 * @return Collection<PointOfServiceModel>
	 */
	public Collection<PointOfServiceModel> getAllReturnablePOSforSeller(final GPS center, final double radius,
			final String sellerId);

	public List<PointOfServiceModel> findPOSForSellerAndSlave(final List<String> slaveIds);
}
