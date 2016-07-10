/**
 *
 */
package com.tisl.mpl.service;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrFacetSearchKeywordRedirectModel;

import com.tisl.mpl.wsdto.ProductDetailMobileWsData;


/**
 * @author TCS
 *
 */
public interface MplProductWebService
{
	public ProductDetailMobileWsData getProductdetailsForProductCode(final String productCode, String baseUrl);

	public String getCategoryCodeOfProduct(final ProductData productData);

	public SolrFacetSearchKeywordRedirectModel getKeywordSearch(final String searchText);
}
