package com.tisl.mpl.cockpits.services.search;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tisl.mpl.cockpits.constants.MarketplaceCockpitsConstants;
import com.tisl.mpl.cockpits.cscockpit.services.StoreAgentUserRole;
import com.tisl.mpl.constants.MarketplacecommerceservicesConstants;
import com.tisl.mpl.exception.EtailNonBusinessExceptions;

import com.tisl.mpl.marketplacecommerceservices.service.AgentIdForStore;

import de.hybris.platform.cscockpit.services.search.generic.query.AbstractCsFlexibleSearchQueryBuilder;
import de.hybris.platform.cscockpit.services.search.impl.DefaultCsTextFacetSearchCommand;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.exceptions.FlexibleSearchException;


public class DefaultMplProductSearchQueryBuilder extends
AbstractCsFlexibleSearchQueryBuilder<DefaultCsTextFacetSearchCommand> 
{
	private static final Logger LOG = Logger.getLogger(DefaultMplProductSearchQueryBuilder.class.getName());
	
	@Resource
	private ConfigurationService configurationService;
	
	@Resource
	StoreAgentUserRole storeAgentUserRole;
	
	@Resource
	private AgentIdForStore agentIdForStore;
	
	private final String PERCENTAGE = "%"; 
	
	protected FlexibleSearchQuery buildFlexibleSearchQuery(DefaultCsTextFacetSearchCommand command) 
	{
		String productText = command.getText();
		String sellerID = StringUtils.EMPTY;
		FlexibleSearchQuery searchQuery = null;
		//getting seller id from session in case csr agent for store order
		if(storeAgentUserRole.isUserInRole(MarketplaceCockpitsConstants.CSCOCKPIT_USER_GROUP_STOREMANAGERAGENTGROUP)){
			sellerID = agentIdForStore
					.getAgentIdForStore(MarketplaceCockpitsConstants.CSCOCKPIT_USER_GROUP_STOREMANAGERAGENTGROUP);
			LOG.debug("seller id ::: "+sellerID );
		}
		try
		{
			searchQuery = populateProductSearchQuery(productText, sellerID);
			return searchQuery;
		}
		catch (final FlexibleSearchException e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0002);
		}
		catch (final Exception e)
		{
			throw new EtailNonBusinessExceptions(e, MarketplacecommerceservicesConstants.E0000);
		}
	}

	private FlexibleSearchQuery populateProductSearchQuery(String productText, String sellerID) throws FlexibleSearchException
	{
		String ussid = StringUtils.EMPTY;
		if(StringUtils.isNotEmpty(sellerID) && StringUtils.isNotEmpty(productText))
		{
			ussid = sellerID.concat(productText);
		}
		final String firstOuterClause = "SELECT DISTINCT {p.pk} from {product as p JOIN Catalogversion as cv ON {p.catalogversion}={cv.pk} JOIN Buybox as bb ON {p.code}={bb.product}} where {cv.version} = 'Online' AND {bb.available} > 0 ";
		final String sellerClause = "AND {bb.SellerID} = ?sellerID ";
		final String secondOuterClause = "AND ({p.pk} in ({{ select {p.pk} from {PcmProductVariant as p} where {p.baseproduct} in	({{	SELECT {p.baseproduct} from {PcmProductVariant as p}";
		final String codeNnameSearchClause = "({p.name} LIKE ?productName OR {p.code} = ?productCode)";
		final String thirdOuterClause = " }}) }})";
		final String skuSearchClause = "OR {bb.sellerArticleSku}=?ussid";
		
		StringBuilder queryString = new StringBuilder();
		// For empty search
		/*if(StringUtils.isEmpty(productText)){
			queryString.append(firstOuterClause);
			if(StringUtils.isNotEmpty(sellerID)){
				queryString.append(sellerClause);
			}
		}*/
		// Non Empty search
		if(StringUtils.isNotEmpty(productText))
		{
			queryString.append(firstOuterClause);
			if(StringUtils.isNotEmpty(sellerID)){
				queryString.append(sellerClause);
			}
			queryString.append(secondOuterClause);
			if(StringUtils.isNotEmpty(productText)){
				queryString.append(" where "+codeNnameSearchClause);
			}
			queryString.append(thirdOuterClause);
			if(StringUtils.isNotEmpty(productText)){
				queryString.append(" OR "+codeNnameSearchClause);
			}
			if(StringUtils.isNotEmpty(ussid)){
				queryString.append(skuSearchClause);
			}
			queryString.append(" )");
		}
		
		FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(queryString.toString());
		
		if(StringUtils.isNotEmpty(sellerID)){
			searchQuery.addQueryParameter("sellerID", sellerID);
		}
		if(StringUtils.isNotEmpty(ussid)){
			searchQuery.addQueryParameter("ussid", ussid);
		}
		if(StringUtils.isNotEmpty(productText)){
			searchQuery.addQueryParameter("productName", PERCENTAGE+productText.trim()+PERCENTAGE);
			searchQuery.addQueryParameter("productCode", productText.trim());
		}
		return searchQuery;
	}
}
