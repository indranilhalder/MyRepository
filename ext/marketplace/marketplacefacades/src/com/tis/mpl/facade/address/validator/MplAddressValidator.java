/**
 * 
 */
package com.tis.mpl.facade.address.validator;

import de.hybris.platform.core.model.user.AddressModel;

import com.tisl.mpl.core.model.TemproryAddressModel;

/**
 * @author pankajk
 *
 */
public interface MplAddressValidator
{
	public boolean compareAddress(AddressModel addressModel, TemproryAddressModel temproryAddressModel);

	public boolean compareContactDetails(AddressModel addressModel, TemproryAddressModel temproryAddressModel);
}
