/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos.changeDeliveryAddress;

import de.hybris.platform.commercefacades.user.data.AddressData;


/**
 * @author pankajk
 *
 */

public interface ChangeDeliveryAddressDao
{

	public String saveAsTemproryAddressForCustomerDao(String orderCode, AddressData addressData);
}
