/**
 *
 */
package com.tisl.mpl.pincode.facade;

import de.hybris.platform.commercefacades.product.data.PincodeServiceData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.storelocator.GPS;
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
}
