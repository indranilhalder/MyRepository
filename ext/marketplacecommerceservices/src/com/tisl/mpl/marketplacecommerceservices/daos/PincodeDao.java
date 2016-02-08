/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.Collection;


/**
 * @author Techouts
 *
 */
public interface PincodeDao
{

	/**
	 * @param gps
	 * @param distance
	 * @return
	 */
	Collection<PointOfServiceModel> getAllGeocodedPOS(GPS gps, double distance, String sellerId);

	/**
	 * @param model
	 * @return
	 */
	PincodeModel getLatAndLongForPincode(final String pincode);

}
