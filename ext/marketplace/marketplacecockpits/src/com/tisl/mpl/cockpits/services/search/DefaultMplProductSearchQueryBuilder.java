package com.tisl.mpl.cockpits.services.search;

import de.hybris.platform.cscockpit.services.search.generic.query.AbstractCsFlexibleSearchQueryBuilder;
import de.hybris.platform.cscockpit.services.search.impl.DefaultCsTextFacetSearchCommand;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import org.apache.commons.lang.StringUtils;


public class DefaultMplProductSearchQueryBuilder extends
AbstractCsFlexibleSearchQueryBuilder<DefaultCsTextFacetSearchCommand> 
{
	protected FlexibleSearchQuery buildFlexibleSearchQuery(
		DefaultCsTextFacetSearchCommand command) {
	String productText = command.getText();
	boolean searchProductText = StringUtils.isNotEmpty(productText);
	//String brand ="'MBH11A00184','MBH13F00003'";
	String sellerID = "'500112'";
	StringBuilder query = new StringBuilder(500);
	
	query.append("SELECT {p.pk}, {p.name}, {p.code} , {si.ussid}"
	+"from {product as p JOIN Catalogversion as cv ON {p.catalogversion}={cv.pk} join SellerInformation as si on {si.productsource}={p.pk}}" 
	+"where {p.pk} in ({{"
		+"select {p.pk}" 
		+"from {PcmProductVariant as p} "
		+"where {p.baseproduct} in ({{"
			+"SELECT {p.baseproduct}" 
			+"from {PcmProductVariant as p JOIN Catalogversion as cv ON {p.catalogversion}={cv.pk}} "
			+"where {cv.version} = 'Online'");
			if (searchProductText) {
				query.append(" AND {p.code} =?productCodeText");
			}
		query.append("}})"
	+"}})"
	+"OR {p.pk} in ({{"
		+"SELECT {p.pk}" 
		+"from {product as p JOIN Catalogversion as cv ON {p.catalogversion}={cv.pk}} "
		+"where {cv.version} = 'Online'");
		if (searchProductText) {
			query.append(" AND {p.code} =?productCodeText");
		}
	query.append("}})"
	+"OR {p.pk} in ({{"
		+"select {si.productsource}" 
		+"from {SellerInformation as si JOIN Catalogversion as cv ON {si.catalogversion}={cv.pk}}"
		+"where {cv.version} = 'Online'");
		if (searchProductText) {
			query.append(" AND {SellerSKU} = ?SKUText");
		}
		query.append( " AND {SellerID} = "+sellerID
	+"}})");
	
	query.append(" ORDER BY {p.code}, {p.name}  ASC ");
	
	FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(
			query.toString());
	
	if (searchProductText) {
		/*searchQuery.addQueryParameter("productNameText",
				"%" + productText.trim() + "%");*/
		searchQuery.addQueryParameter("productCodeText",
				productText.trim());
		searchQuery.addQueryParameter("SKUText",
				productText.trim());
	}
	
	
	return searchQuery;
	}
}
