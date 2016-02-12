/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import java.util.List;

import com.tisl.mpl.core.model.MplZoneDeliveryModeValueModel;


/**
 * @author TCS
 *
 */
public interface MplDeliveryCostService
{
	MplZoneDeliveryModeValueModel getDeliveryCost(String deliveryCode, String currencyIsoCode, String sellerArticleSku);

	List<MplZoneDeliveryModeValueModel> getDeliveryModesAndCost(String currencyIsoCode, String sellerArticleSku);
}
