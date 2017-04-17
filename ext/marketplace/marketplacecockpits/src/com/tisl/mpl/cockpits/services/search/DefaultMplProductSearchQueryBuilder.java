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
	String brand ="'MBH11A00184','MBH13F00003'";
	StringBuilder query = new StringBuilder(500);
	
	query.append("SELECT DISTINCT {pk}, {name}, {code} ");
	
	query.append("FROM {Product AS p JOIN Catalogversion as cv ON {p.catalogversion}={cv.pk}} ");
	query.append(" WHERE ");
	/*if (searchProductText) {
		query.append("({p.name} LIKE ?productNameText OR {p.code} = ?productCodeText)");
		query.append(" "+"AND"+" ");
	}*/
	
	query.append("{p.code} in");
	query.append(" (");
	
		query.append("{{");
		query.append(" select {p.code} "
				+ "from {PcmProductVariant as p} "
				+ "where {p.baseproduct} in ({{"
					+ "SELECT {p.baseproduct} "
					+ "from {PcmProductVariant as p JOIN Catalogversion as cv ON {p.catalogversion}={cv.pk}} "
					+ "where {p.pk} in ({{"
						+ "select {target} "
						+ "from {CategoryProductRelation} "
						+ "where {source} in ({{"
							+ "select {c.pk} "
							+ "from {Category as c JOIN Catalogversion as cv ON {c.catalogversion}={cv.pk}} "
							+ "where {c.code} in ("
							+ brand
							+ ") AND {cv.version} = 'Online'"
						+ "}})"
					+ "}}) AND {cv.version} = 'Online' ");
					if (searchProductText) {
						query.append(" "+"AND"+" ");
						query.append("({p.name} LIKE ?productNameText OR {p.code} = ?productCodeText)");
					}
					query.append("}}) ");
		
		query.append("}}");
	
		query.append(")");
		
		query.append(" "+"AND"+" ");
		
		query.append("{cv.version} = 'Online'");
		
		query.append(" "+"OR"+" ");
		
		query.append("{p.code} in");
		
		query.append(" (");
		
			query.append("{{");
			
			query.append("SELECT {p.code} "
					+ "from {product as p JOIN Catalogversion as cv ON {p.catalogversion}={cv.pk}} "
					+ "where pk in "
						+ "({{select {target} "
						+ "from {CategoryProductRelation} "
						+ "where {source} in "
							+ "({{select {c.pk} "
							+ "from {Category as c JOIN Catalogversion as cv ON {c.catalogversion}={cv.pk}} "
							+ "where {c.code} in ("
							+ brand
							+ ") AND {cv.version} = 'Online'}})"
						+ "}}) "
					+ "AND {cv.version} = 'Online' ");
			if (searchProductText) {
				query.append(" "+"AND"+" ");
				query.append("({p.name} LIKE ?productNameText OR {p.code} = ?productCodeText)");
			}
			
			query.append("}}");
			
			query.append(")");
			
			query.append(" "+"AND"+" ");
			
			query.append("{cv.version} = 'Online'");
			
			query.append(" "+"OR"+" ");
			
			query.append("{p.code} in");
			
			query.append(" (");
			
				query.append("{{");
				
				query.append("SELECT {p.code} "
						+ "from {product as p JOIN Catalogversion as cv ON {p.catalogversion}={cv.pk}} "
						+ "where pk in ({{"
							+ "select {si.productsource} "
							+ "from {SellerInformation as si JOIN Catalogversion as cv ON {si.catalogversion}={cv.pk}} "
							+ "where {cv.version} = 'Online' AND {SellerSKU} = ?SKUText AND {si.productsource} in ({{"
								+ "select {target} "
								+ "from {CategoryProductRelation} where {source} in ({{"
									+ "select {c.pk} "
									+ "from {Category as c JOIN Catalogversion as cv ON {c.catalogversion}={cv.pk}} "
									+ "where {c.code} in ("
									+ brand
									+ ") AND {cv.version} = 'Online'"
								+ "}})"
							+ "}})"
						+ "}}) AND {cv.version} = 'Online'");
				query.append("}}");
				
				query.append(")");
				
				query.append(" "+"AND"+" ");
				
				query.append("{cv.version} = 'Online'");
			
	query.append(" ORDER BY {name}, {code} ASC ");
	
	FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(
			query.toString());
	
	if (searchProductText) {
		searchQuery.addQueryParameter("productNameText",
				"%" + productText.trim() + "%");
		searchQuery.addQueryParameter("productCodeText",
				productText.trim());
		searchQuery.addQueryParameter("SKUText",
				productText.trim());
	}
	
	
	return searchQuery;
	}
}
