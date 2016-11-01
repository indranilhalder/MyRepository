/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.List;

import com.tisl.mpl.wsdto.GalleryImageData;
import com.tisl.mpl.wsdto.ProductDetailMobileWsData;


/**
 * @author TCS
 *
 */
public interface MplProductWebService
{
	public ProductDetailMobileWsData getProductdetailsForProductCode(String productCode, String baseUrl, String channel);

	public String getCategoryCodeOfProduct(final ProductData productData);

	public List<GalleryImageData> getGalleryImages(final ProductData productData);

	public String getKeywordSearch(String searchText);

}
