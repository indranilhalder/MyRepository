package com.tisl.mpl.cockpits.services.search;

import java.util.Set;

import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.cscockpit.services.search.generic.query.AbstractCsFlexibleSearchQueryBuilder;
import de.hybris.platform.cscockpit.services.search.impl.DefaultCsTextFacetSearchCommand;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.services.StoreAgentUserRole;


public class DefaultMplProductSearchQueryBuilder extends
AbstractCsFlexibleSearchQueryBuilder<DefaultCsTextFacetSearchCommand> 
{
	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	StoreAgentUserRole storeAgentUserRole;
	
	protected FlexibleSearchQuery buildFlexibleSearchQuery(
		DefaultCsTextFacetSearchCommand command) {
	String productText = command.getText();
	boolean searchProductText = StringUtils.isNotEmpty(productText);
	//String brand ="'MBH11A00184','MBH13F00003'";
	//String sellerID = "'500112'";
	StringBuilder query = new StringBuilder(500);
	
	//getiing seller id from session in case csr agent for store order
	final String sellerID = "'"+(String) JaloSession.getCurrentSession().getAttribute("sellerId")+"'";
	
	query.append("SELECT DISTINCT {p.pk}, {p.name}, {p.code} "
	+"from {product as p JOIN Catalogversion as cv ON {p.catalogversion}={cv.pk} JOIN Buybox as bb ON {p.code}={bb.product}");
	if (storeAgentUserRole.isUserInRole((configurationService
			.getConfiguration()
			.getString(MarketplaceCockpitsConstants.CSCOCKPIT_USER_GROUP_STOREMANAGERGROUP)))) {
		query.append(" join SellerInformation as si on {si.productsource}={p.pk}");
	}
	query.append("} where");
	if (storeAgentUserRole.isUserInRole((configurationService
			.getConfiguration()
			.getString(MarketplaceCockpitsConstants.CSCOCKPIT_USER_GROUP_STOREMANAGERGROUP)))) {
		query.append(" {si.SellerID} = "+sellerID+" AND ");
	}
	query.append(" {p.pk} in ({{"
		+"select {p.pk}" 
		+"from {PcmProductVariant as p} "
		+"where {p.baseproduct} in ({{"
			+"SELECT {p.baseproduct}" 
			+"from {PcmProductVariant as p JOIN Catalogversion as cv ON {p.catalogversion}={cv.pk}} "
			+"where {cv.version} = 'Online'");
			if (searchProductText) {
				query.append(" AND ({p.name} LIKE ?productNameText OR {p.code} = ?productCodeText)");
			}
		query.append("}})"
	+"}})"
	+"OR");
		if (storeAgentUserRole.isUserInRole((configurationService
				.getConfiguration()
				.getString(MarketplaceCockpitsConstants.CSCOCKPIT_USER_GROUP_STOREMANAGERGROUP)))) {
			query.append(" {si.SellerID} = "+sellerID+" AND ");
		}
		query.append( " {p.pk} in ({{"
		+"SELECT {p.pk}" 
		+"from {product as p JOIN Catalogversion as cv ON {p.catalogversion}={cv.pk}} "
		+"where {cv.version} = 'Online'");
		if (searchProductText) {
			query.append(" AND ({p.name} LIKE ?productNameText OR {p.code} = ?productCodeText)");
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
