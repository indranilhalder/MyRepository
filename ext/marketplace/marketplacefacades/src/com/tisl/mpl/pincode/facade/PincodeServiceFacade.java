/**
 *
 */
package com.tisl.mpl.pincode.facade;

import de.hybris.platform.commercefacades.product.data.PincodeServiceData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.core.model.product.PincodeModel;
import de.hybris.platform.storelocator.GPS;
import de.hybris.platform.storelocator.location.Location;

import java.util.List;

import com.tisl.mpl.facades.data.StoreLocationResponseData;


/**
 * @author Techouts
 * 
 */
public interface PincodeServiceFacade
{
	/**
	 * @param pincodeValue
	 */
	public List<StoreLocationResponseData> getListofStoreLocationsforPincode(final String pincode, final String sellerUssId,
			String productCode);

	/**
	 * 
	 * @param pincode
	 * @param productCode
	 * @return
	 */
	public boolean checkPincodeServiceble(final String pincode, String productCode);

	/**
	 * @param productCode
	 * @return requestData
	 */
	public List<PincodeServiceData> populatePinCodeServiceData(final String productCode, final GPS gps,
			final Double configurableRadius);
	
	/**
	 * Get all the PointOfService(stores) for given gps and radius.
	 * @param gps
	 * @param radius
	 * @return all the stores for given pincode and radius.
	 */
	public List<PointOfServiceData> getStoresForPincode(final GPS gps, final String radius);
	
	/**
	 * To get PincodeModel for a given pincode.
	 * 
	 * @param pincode
	 * @return Pincode model
	 */
	PincodeModel getLatAndLongForPincode(final String pincode);
	
	/**
	 * Gets List of Location object for given gps, distance and sellerId
	 * @author TECH
	 * @param gps
	 * @param distance
	 * @param sellerId
	 */
	List<Location> getSortedLocationsNearby(final GPS gps, final double distance, String sellerId);
}
