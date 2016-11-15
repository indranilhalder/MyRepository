/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.List;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.wsdto.GalleryImageData;
import com.tisl.mpl.wsdto.ProductAPlusWsData;
import com.tisl.mpl.wsdto.ProductDetailMobileWsData;


/**
 * @author TCS
 *
 */
public interface MplProductWebService
{
	public ProductDetailMobileWsData getProductdetailsForProductCode(final String productCode, String baseUrl);

	public ProductDetailMobileWsData getProductInfoForProductCode(final String productCode, String baseUrl);

	public String getCategoryCodeOfProduct(final ProductData productData);

	public List<GalleryImageData> getGalleryImages(final ProductData productData);

	public String getKeywordSearch(String searchText);

	public ProductAPlusWsData getAPluscontentForProductCode(String productCode) throws EtailNonBusinessExceptions,
			CMSItemNotFoundException;
}
