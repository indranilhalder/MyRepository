/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.promotions.model.AbstractPromotionModel;

import com.tisl.mpl.pojo.CampaignData;


/**
 * @author TCS
 *
 */
public interface CampaignPromoSubService
{

	/**
	 * @param promotion
	 * @return CampaignData
	 */
	CampaignData getPromoCampaignDetails(AbstractPromotionModel promotion);

}
