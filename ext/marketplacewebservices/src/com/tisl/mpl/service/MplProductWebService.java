/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.commercefacades.product.data.ProductContentData;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.List;
import java.util.Map;

import com.tisl.mpl.exception.EtailNonBusinessExceptions;
import com.tisl.mpl.wsdto.GalleryImageData;
import com.tisl.mpl.wsdto.ProductDetailMobileWsData;


/**
 * @author TCS
 *
 */
public interface MplProductWebService
{
	public ProductDetailMobileWsData getProductdetailsForProductCode(final String productCode, String baseUrl);

	public String getCategoryCodeOfProduct(final ProductData productData);

	public List<GalleryImageData> getGalleryImages(final ProductData productData);

	public String getKeywordSearch(String searchText);



	public Map<String, ProductContentData> getProductcontentForProductCode(String productCode,
			ProductContentData productContentData) throws EtailNonBusinessExceptions;


}
