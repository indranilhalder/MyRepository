/**
 *
 */
package com.tisl.mpl.facades.account.address;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.CustomerModel;

import java.util.List;

import com.tisl.mpl.facades.product.data.StateData;


/**
 * @author TCS
 *
 */
public interface MplAccountAddressFacade
{

	/**
	 *
	 * @param newAddress
	 */
	void addaddress(AddressData newAddress);

	/**
	 *
	 * @param addressData
	 */
	void editAddress(AddressData addressData);

	/**
	 *
	 * @return List<AddressData>
	 */
	List<AddressData> getAddressBook();

	/**
	 *
	 * @return AddressData
	 */
	AddressData getDefaultAddress();

	/**
	 *
	 * @return List<StateData>
	 */
	List<StateData> getStates();

	/**
	 *
	 * @param newAddress
	 * @param customer
	 */
	void addaddress(AddressData newAddress, CustomerModel customer);

}
