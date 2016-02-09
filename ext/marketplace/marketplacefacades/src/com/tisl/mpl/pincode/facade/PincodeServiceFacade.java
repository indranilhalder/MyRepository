/**
 *
 */
package com.tisl.mpl.pincode.facade;

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
}
