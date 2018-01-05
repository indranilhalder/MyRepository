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

	MplZoneDeliveryModeValueModel getDeliveryCost(String deliveryCode, String currencyIsoCode, String sellerArticleSku,
			String fulfillmentType);

	List<MplZoneDeliveryModeValueModel> getDeliveryModesAndCost(String currencyIsoCode, String sellerArticleSku);

	//Added for CAR-266
	List<MplZoneDeliveryModeValueModel> getDeliveryCost(StringBuilder deliveryMode, String currencyIsoCode, String skuid);
}
