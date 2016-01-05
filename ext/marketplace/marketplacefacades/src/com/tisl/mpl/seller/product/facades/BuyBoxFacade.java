/**
 *
 */
package com.tisl.mpl.seller.product.facades;

import de.hybris.platform.commercefacades.product.data.SellerInformationData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;

import com.tisl.mpl.core.model.BuyBoxModel;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.facades.product.RichAttributeData;
import com.tisl.mpl.facades.product.data.BuyBoxData;


/**
 * @author TCS
 *
 */
public interface BuyBoxFacade
{
	public BuyBoxData buyboxPrice(String ProductCode) throws EtailNonBusinessExceptions;

	public List<SellerInformationData> getsellersDetails(String productCode) throws EtailNonBusinessExceptions;

	/**
	 * @param productModel
	 * @param buyboxid
	 */
	public RichAttributeData getRichAttributeDetails(ProductModel productModel, String buyboxid);

	public BuyBoxModel getpriceForUssid(String ussid);

	public BuyBoxData buyboxForSizeGuide(final String productCode, final String sellerId) throws EtailNonBusinessExceptions;

}