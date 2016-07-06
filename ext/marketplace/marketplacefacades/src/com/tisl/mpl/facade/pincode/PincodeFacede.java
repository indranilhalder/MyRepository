/**
 * 
 */
package com.tisl.mpl.facade.pincode;

import com.tisl.mpl.facades.data.PincodeData;

/**
 * @author Dileep
 *
 */

public interface PincodeFacede
{
	
	/**
	 * @Description Converting PincodeModel to PincodeData using the Populators and Converters
	 * @param pincode
	 * @return PincodeData
	 */
	public PincodeData getAllDetails(final String pincode);
}
