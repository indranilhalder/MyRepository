package com.tisl.mpl.facade.product;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.JewelleryInformationModel;

import java.util.List;


/**
 * @author TCS
 *
 */

public interface MplJewelleryFacade
{
	public List<JewelleryInformationModel> getJewelleryInfoByUssid(final String ussid);

	public List<String> getSellerMsgForRetRefTab(ProductData productData);
}
