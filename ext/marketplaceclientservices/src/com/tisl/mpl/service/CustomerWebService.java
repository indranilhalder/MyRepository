/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;

import com.tisl.mpl.wsdto.CustomerCreateWSDTO;
import com.tisl.mpl.wsdto.CustomerUpdateAddressWSDTO;
import com.tisl.mpl.wsdto.CustomerUpdateTrialWSDTO;


/**
 * @author TCS
 *
 */
public interface CustomerWebService
{
	public void customerModeltoWsDTO(CustomerModel customerModel);

	public void customerCreateCRM(final CustomerCreateWSDTO customerTrialWSDTO);

	public void customerprofiledetailsupdate(final CustomerModel customerModel);

	public void customeraddressdetailsupdate(final AddressModel addressModel, final CustomerModel customerModel);

	public void customerUpdateCRM(final CustomerUpdateTrialWSDTO customerUpdateTrialWSDTO);

	public void customerAddressUpdateCRM(final CustomerUpdateAddressWSDTO customerUpdateAddressWSDTO);
}
