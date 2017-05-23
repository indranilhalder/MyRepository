/**
 * 
 */
package com.tis.mpl.facade.address.validator;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.AddressModel;

/**
 * @author pankajk
 *
 */
public interface MplDeliveryAddressComparator
{
	public boolean compareAddress(AddressData oldAddress, AddressData newAddress);

	public boolean compareContactDetails(AddressData oldAddress, AddressData newAddress);
	
	public boolean compareAddressModel(AddressModel oldAddress, AddressModel newAddress);
	public boolean compareMobileNumber(AddressModel oldAddress,AddressModel newDeliveryAddress);
	public boolean compareNameDetails(AddressModel oldAddress,AddressModel newDeliveryAddress);
}
