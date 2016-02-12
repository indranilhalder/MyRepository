package com.tisl.mpl.cockpits.cscockpit.widgets.helpers;

import java.util.List;

import com.tisl.mpl.exception.ClientEtailNonBusinessExceptions;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;

import de.hybris.platform.commercefacades.product.data.PinCodeResponseData;
import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.core.model.product.ProductModel;

public interface MarketplaceServiceabilityCheckHelper {
	
	/**
	 * Gets the seller details.
	 *
	 * @param productModel the product model
	 * @return the seller details
	 */
	List<String> getSellerDetails(ProductModel productModel);

	/**
	 * Gets the response for pin code.
	 *
	 * @param product the product
	 * @param pin the pin
	 * @param isDeliveryDateRequired the is delivery date required
	 * @param ussid 
	 * @return the response for pin code
	 * @throws EtailNonBusinessExceptions the etail non business exceptions
	 * @throws ClientEtailNonBusinessExceptions the client etail non business exceptions
	 */
	List<PinCodeResponseData> getResponseForPinCode(final ProductModel product,
			final String pin, final String isDeliveryDateRequired, String ussid) 
					throws EtailNonBusinessExceptions, ClientEtailNonBusinessExceptions;

	List<SellerInformationData> getSellerInformation(ProductModel productModel);

	

}
